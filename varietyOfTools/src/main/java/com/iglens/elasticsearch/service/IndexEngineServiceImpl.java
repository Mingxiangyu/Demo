package com.iglens.elasticsearch.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.iglens.elasticsearch.convert.IndexDocumentConverter;
import com.iglens.elasticsearch.dto.IndexDocumentDTO;
import com.iglens.elasticsearch.support.IndexUtils;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsRequest;
import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** @author fli */
@Service
@Transactional(rollbackFor = Exception.class)
public class IndexEngineServiceImpl implements IndexEngineService {
  private static final Logger log = LoggerFactory.getLogger(IndexEngineServiceImpl.class);

  private final RestHighLevelClient client;

  private final IndexDocumentConverter converter;

  private final AdministrationService administrationService;

  @Autowired
  public IndexEngineServiceImpl(
      RestHighLevelClient client,
      IndexDocumentConverter converter,
      AdministrationService administrationService) {
    this.client = client;
    this.converter = converter;
    this.administrationService = administrationService;
  }

  @Override
  public boolean processDeleteRequest(String objectType, String objectId) {
    return deleteDocumentById(objectId);
  }

  @Override
  public boolean processIndexRequest(String objectType, String objectId) {
    log.info(">>> processIndexRequest objectType={},objectId={}", objectType, objectId);
    // 基于类型获取dto对象
    //    BusinessObjectDTO objectDTO = getSourceDTO(objectType, objectId);
    //    if (objectDTO == null) {
    //       throw new EspException("获取数据属性信息失败:objectType=" + objectType + ",objectId=" +
    // objectId);
    //      log.error("获取数据属性信息失败:objectType=" + objectType + ",objectId=" + objectId);
    //      return false;
    //    }

    IndexDocumentDTO indexDocumentDTO = new IndexDocumentDTO();
    //        = converter.convert(objectDTO);
    if (indexDocumentDTO == null) {
      log.info(">>> processIndexRequest indexDocumentDTO={}", indexDocumentDTO);
      return false;
    }
    String content = getIndexDocumentJson(indexDocumentDTO);
    log.info(">>> processIndexRequest documentDTO={}", content);

    String tenantIdentifier = indexDocumentDTO.getTenantId();
    if (StringUtils.isBlank(tenantIdentifier)) {
      // throw new EspException("获取数据所属租户失败:objectType=" + objectType + ",objectId=" + objectId);
      log.error("获取数据所属租户失败:objectType=" + objectType + ",objectId=" + objectId);
      return false;
    }
    String index = IndexUtils.formatIndexName(tenantIdentifier);

    // 创建索引
    administrationService.createIndex(index);

    return indexDocument(index, objectId, content);
  }

  //  private BusinessObjectDTO getSourceDTO(String objectType, String objectId) {
  //    BusinessObjectDTO sourceDTO = null;
  //    switch (BusinessObjectType.toBusinessObjectType(objectType)) {
  //      case DOCUMENT:
  //        // 文档
  //        sourceDTO = documentService.getDocument(objectId);
  //        break;
  //      case PART:
  //        // 部件
  //        sourceDTO = partService.getPart(objectId);
  //        break;
  //      case DRAWING:
  //        // CAD文档
  //        sourceDTO = drawingService.getDrawing(objectId);
  //        break;
  //      case CHANGEREQUESTORDER:
  //        // 更改请求
  //        sourceDTO = changeRequestService.getChangeRequest(objectId);
  //        break;
  //      case CHANGEREQUEST:
  //        // 更改请求
  //        sourceDTO = changeRequestService.getChangeRequest(objectId);
  //        break;
  //      case CHANGEORDER:
  //        // 更改单
  //        sourceDTO = changeOrderService.getChangeOrder(objectId);
  //        break;
  //      case CHANGEPHASEORDER:
  //        // 转阶段更改单
  //        sourceDTO = changeOrderService.getChangeOrder(objectId);
  //        break;
  //      case DEVIATION:
  //        // 偏离单
  //        sourceDTO = changeOrderService.getChangeOrder(objectId);
  //        break;
  //      case QUESTIONORDER:
  //        // 质疑单
  //        break;
  //      case UNQUALIFIEDORDER:
  //        // 不合格审理单
  //        break;
  //      case BASELINE:
  //        // 基线
  //        sourceDTO = baselineService.getBaseline(objectId);
  //        break;
  //      case DATASENDORDER:
  //        // 数据发送单
  //        sourceDTO = dataSendOrderService.getDataSendOrder(objectId);
  //        break;
  //      default:
  //        break;
  //    }
  //    return sourceDTO;
  //  }

  private boolean indexDocument(String index, String id, String source) {
    try {

      IndexRequest indexRequest =
          new IndexRequest(index, IndexUtils.DEFAULT_TYPE_NAME, id)
              .source(source, XContentType.JSON);
      IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);
      RestStatus status = indexResponse.status();

      if ((status == RestStatus.OK) || (status == RestStatus.CREATED)) {
        return true;
      }
    } catch (ElasticsearchException | IOException e) {
      log.error(e.getLocalizedMessage(), e);
    }
    return false;
  }

  private boolean deleteDocumentById(String objectId) {
    SearchRequest searchRequest = new SearchRequest();
    try {
      // 查询条件
      SearchSourceBuilder sourceBuilder = SearchSourceBuilder.searchSource();
      BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
      queryBuilder.must(QueryBuilders.termQuery("_id", objectId));
      sourceBuilder.query(queryBuilder);
      searchRequest.source(sourceBuilder);

      // 搜索类型
      searchRequest.searchType(SearchType.QUERY_THEN_FETCH);
      SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
      SearchHits hits = response.getHits();

      for (SearchHit hit : hits.getHits()) {
        String index = hit.getIndex();
        String type = hit.getType();
        String id = hit.getId();
        DeleteRequest deleteRequest = new DeleteRequest(index, type, id);
        DeleteResponse deleteResponse = client.delete(deleteRequest, RequestOptions.DEFAULT);
        RestStatus status = deleteResponse.status();
        log.info(">>> deleteDocumentById status={}", status.getStatus());
      }
      return true;
    } catch (IOException e) {
      log.error(e.getLocalizedMessage(), e);
    }
    return false;
  }

  private String getIndexDocumentJson(Object source) {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

    String json = null;
    try {
      // 将对象转成字符串
      json = objectMapper.writeValueAsString(source);
    } catch (JsonProcessingException e) {
      log.error(e.getMessage(), e);
    }
    return json;
  }

  @Override
  public Map<String, Object> getMappings(String indexName) {
    GetMappingsResponse response = getMappingsResponse(indexName);
    ImmutableOpenMap<String, ImmutableOpenMap<String, MappingMetaData>> allMappings =
        response.mappings();
    MappingMetaData typeMapping = allMappings.get(indexName).get(IndexUtils.DEFAULT_TYPE_NAME);
    if (typeMapping == null) {
      return Collections.emptyMap();
    }
    Map<String, Object> mapping = typeMapping.sourceAsMap();
    if (mapping == null) {
      return Collections.emptyMap();
    }
    Object p = mapping.get("properties");
    if (p == null) {
      return Collections.emptyMap();
    }
    return (Map<String, Object>) p;
  }

  public GetMappingsResponse getMappingsResponse(String... indices) {
    try {
      log.info(">>> getMappingsResponse indices={}", indices);
      GetMappingsRequest request = new GetMappingsRequest();
      request.indices(indices);
      request.types(IndexUtils.DEFAULT_TYPE_NAME);
      log.info(">>> getMappingsResponse request={}", request);
      return client.indices().getMapping(request, RequestOptions.DEFAULT);
    } catch (IOException e) {
      throw new RuntimeException("获取索引映射失败，" + e.getMessage());
    }
  }
}
