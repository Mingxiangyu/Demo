package com.iglens.elasticsearch.service;

import com.iglens.elasticsearch.dto.SearchFormDTO;
import com.iglens.elasticsearch.dto.SearchResultDTO;
import com.iglens.elasticsearch.support.IndexAttributeConstants;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.TotalHits;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.elasticsearch.ElasticsearchStatusException;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchPhraseQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** @author Li Feixiang */
@Service
@Transactional(rollbackFor = Exception.class)
public class SearchEngineServiceImpl implements SearchEngineService {

  private static final Logger log = LoggerFactory.getLogger(SearchEngineServiceImpl.class);

  private final RestHighLevelClient client;

  private final IndexEngineService indexEngineService;

  @Autowired
  public SearchEngineServiceImpl(
      RestHighLevelClient client,
      IndexEngineService indexEngineService) {
    this.client = client;
    this.indexEngineService = indexEngineService;
  }

  private static String extractStringValue(Map<String, Object> source, String key) {
    Object ret = source.get(key);
    if (ret == null) {
      return null;
    }
    if (ret instanceof List) {
      return ((List<?>) ret).get(0).toString();
    } else {
      return ret.toString();
    }
  }

  @SuppressWarnings("deprecation")
  @Override
  public Page<SearchResultDTO> search(String index, SearchFormDTO searchForm, Pageable pageable) {
    List<String> typeIds = searchForm.getTypeId();
    String categoryId = searchForm.getCategoryId();
    if (StringUtils.isNotBlank(categoryId)) {
      typeIds.add(categoryId);
    }
    String keyword = StringUtils.trimToEmpty(searchForm.getQ());
    keyword = QueryParser.escape(keyword);

    String code = StringUtils.trimToEmpty(searchForm.getCodeKeyword());
    code = QueryParser.escape(code);

    String name = StringUtils.trimToEmpty(searchForm.getNameKeyword());
    name = QueryParser.escape(name);

    Page<SearchResultDTO> page = new PageImpl<>(new ArrayList<>());

    // 关键词、编号、名称、类型全为空时，不执行搜索
    if (StringUtils.isBlank(keyword)
        && StringUtils.isBlank(code)
        && StringUtils.isBlank(name)
        && CollectionUtils.isEmpty(typeIds)) {
      // return page;
    }
    //    判断用户密级
    //    Long userId = UserContext.getUserId();
    //    UserEntity user = userServiceSvr.getUserByIdSvr(userId);
    //        String userSecretLevel = "重要";
    //    if (user != null) {
    //      userSecretLevel = user.getSecretLevel();
    //    }
    List<String> userSecretLevels = new ArrayList<>();
    //    if (StringUtils.equals(userSecretLevel, "核心") || StringUtils.equals(userSecretLevel,
    // "重要")) {
    //
    //    } else if (StringUtils.equals(userSecretLevel, "一般")) {
    //      userSecretLevels.add("机密");
    //    } else {
    //      userSecretLevels.add("秘密");
    //      userSecretLevels.add("机密");
    //    }

    try {
      SearchSourceBuilder searchSourceBuilder = SearchSourceBuilder.searchSource();

      BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();

      // 按最新修订版本过滤（不查询非最新修订版本）
      queryBuilder.mustNot(
          QueryBuilders.queryStringQuery("false").field(IndexAttributeConstants.LATESTREVISION));

      // 按最新小版本过滤（不查询非最新小版本）
      if (BooleanUtils.isTrue(searchForm.getLatestIteration())) {
        queryBuilder.must(
            QueryBuilders.queryStringQuery("true")
                .field(IndexAttributeConstants.LATEST_ITERATION_KEY));
      }

      // 密级过滤
      if (CollectionUtils.isNotEmpty(userSecretLevels)) {
        BoolQueryBuilder secretQuery = QueryBuilders.boolQuery();
        for (String objectSecretLevel : userSecretLevels) {
          secretQuery.should(
              QueryBuilders.termQuery(IndexAttributeConstants.SECRETLEVEL_KEY, objectSecretLevel));
          secretQuery.should(
              QueryBuilders.queryStringQuery(objectSecretLevel)
                  .field("details.SecretLevel.keyword"));
        }
        queryBuilder.mustNot(secretQuery);
      }

      // 关键字查询QueryParser
      if (StringUtils.isNotBlank(keyword)) {
        StringBuilder queryString = new StringBuilder();
        StringBuilder queryStringMatch = new StringBuilder();
        String[] words = StringUtils.split(keyword, " ");
        BoolQueryBuilder shouldBuilder = QueryBuilders.boolQuery();
        for (String word : words) {
          if (StringUtils.isBlank(word)) {
            continue;
          }
          shouldBuilder.should(QueryBuilders.matchQuery(IndexAttributeConstants.CODE_KEY, word));
          shouldBuilder.should(QueryBuilders.matchQuery(IndexAttributeConstants.NAME_KEY, word));
          if (!StringUtils.startsWith(word, "*")) {
            word = "*" + word;
          }
          if (!StringUtils.endsWith(word, "*")) {
            word = word + "*";
          }
          if (StringUtils.isNotBlank(queryString)) {
            queryString.append(" AND ");
            queryStringMatch.append(" AND ");
          }
          queryString.append(word);
          queryStringMatch.append(word);
        }
        Map<String, Float> fields = new HashMap<>(16);
        fields.put(IndexAttributeConstants.CODE_KEY + ".keyword", 1.0F);
        fields.put(IndexAttributeConstants.NAME_KEY + ".keyword", 1.0F);
        QueryStringQueryBuilder keywordQueryBuilder =
            QueryBuilders.queryStringQuery(queryString.toString()).fields(fields);
        shouldBuilder.should(keywordQueryBuilder);
        queryBuilder.must(shouldBuilder);
        queryBuilder.must(keywordQueryBuilder);
      }

      // 编号查询
      if (StringUtils.isNotBlank(code)) {
        BoolQueryBuilder shouldBuilder = QueryBuilders.boolQuery();
        shouldBuilder.should(QueryBuilders.matchQuery(IndexAttributeConstants.CODE_KEY, code));
        if (!StringUtils.startsWith(code, "*")) {
          code = "*" + code;
        }
        if (!StringUtils.endsWith(code, "*")) {
          code = code + "*";
        }
        QueryStringQueryBuilder queryCode =
            QueryBuilders.queryStringQuery(code)
                .field(IndexAttributeConstants.CODE_KEY + ".keyword");
        shouldBuilder.should(queryCode);
        queryBuilder.must(shouldBuilder);
        queryBuilder.must(queryCode);
      }

      // 名称查询
      if (StringUtils.isNotBlank(name)) {
        BoolQueryBuilder shouldBuilder = QueryBuilders.boolQuery();
        shouldBuilder.should(QueryBuilders.matchQuery(IndexAttributeConstants.NAME_KEY, name));
        if (!StringUtils.startsWith(name, "*")) {
          name = "*" + name;
        }
        if (!StringUtils.endsWith(name, "*")) {
          name = name + "*";
        }
        QueryStringQueryBuilder queryName =
            QueryBuilders.queryStringQuery(name)
                .field(IndexAttributeConstants.NAME_KEY + ".keyword");
        shouldBuilder.should(queryName);
        queryBuilder.must(shouldBuilder);
        queryBuilder.must(queryName);
      }

      // 型号代号查询
      String modelCode = searchForm.getModelCode();
      if (StringUtils.isNotBlank(modelCode)) {
        if (!StringUtils.startsWith(modelCode, "*")) {
          modelCode = "*" + modelCode;
        }
        if (!StringUtils.endsWith(modelCode, "*")) {
          modelCode = modelCode + "*";
        }
        queryBuilder.must(
            QueryBuilders.queryStringQuery(modelCode)
                .field(IndexAttributeConstants.MODELCODE_KEY + ".keyword"));
      }

      // 说明查询
      String description = searchForm.getDescription();
      if (StringUtils.isNotBlank(description)) {
        queryBuilder.must(
            QueryBuilders.wildcardQuery(IndexAttributeConstants.DESCRIPTION_KEY, description));
      }

      // 数据分类
      List<String> types = searchForm.getType();
      if (CollectionUtils.isNotEmpty(types)) {
        BoolQueryBuilder typeQuery = QueryBuilders.boolQuery();
        for (String type : types) {
          if (StringUtils.isBlank(type) || StringUtils.equalsIgnoreCase(type, "all")) {
            continue;
          }
          typeQuery.should(
              QueryBuilders.termQuery(IndexAttributeConstants.BUSINESS_TYPE_KEY, type));
        }
        queryBuilder.filter(typeQuery);
      }

      // 对象类型过滤
      if (CollectionUtils.isNotEmpty(typeIds)) {
        BoolQueryBuilder typeIdQuery = QueryBuilders.boolQuery();
        for (String typeId : typeIds) {
          if (StringUtils.isBlank(typeId) || StringUtils.equalsIgnoreCase(typeId, "all")) {
            continue;
          }
          typeIdQuery.should(
              QueryBuilders.termQuery(IndexAttributeConstants.TYPEDEFINITION_ID_KEY, typeId));
        }
        queryBuilder.filter(typeIdQuery);
      }

      // 所属分类
      List<String> classifications = searchForm.getClassification();
      if (CollectionUtils.isNotEmpty(classifications)) {
        BoolQueryBuilder classificationQuery = QueryBuilders.boolQuery();
        for (String classification : classifications) {
          classificationQuery.should(
              QueryBuilders.termQuery(IndexAttributeConstants.CLASSIFICATION_KEY, classification));
        }
        queryBuilder.filter(classificationQuery);
      }

      // 生命周期状态过滤
      String lifecycleState = searchForm.getLifecycleState();
      List<String> states = searchForm.getLifecycleStates();
      if (StringUtils.isNotBlank(lifecycleState)) {
        states.add(lifecycleState);
      }
      if (CollectionUtils.isNotEmpty(states)) {
        BoolQueryBuilder stateQuery = QueryBuilders.boolQuery();
        for (String state : states) {
          stateQuery.should(QueryBuilders.termQuery(IndexAttributeConstants.STATE_KEY, state));
        }
        queryBuilder.filter(stateQuery);
      }

      // 创建者过滤
      Long creatorId = searchForm.getCreatorId();
      if (creatorId != null) {
        TermQueryBuilder creatorQueryBuilder =
            QueryBuilders.termQuery(IndexAttributeConstants.CREATOR_ID_KEY, creatorId);
        queryBuilder.filter(creatorQueryBuilder);
      }

      // 修改者过滤
      Long modifierId = searchForm.getModifierId();
      if (modifierId != null) {
        TermQueryBuilder modifierQueryBuilder =
            QueryBuilders.termQuery(IndexAttributeConstants.MODIFIER_ID_KEY, modifierId);
        queryBuilder.filter(modifierQueryBuilder);
      }

      // 标准类别
      String standardType = searchForm.getStandardType();
      if (StringUtils.isNotBlank(standardType)) {
        TermQueryBuilder standardTypeQueryBuilder =
            QueryBuilders.termQuery(IndexAttributeConstants.STANDARDTYPE, standardType);
        queryBuilder.filter(standardTypeQueryBuilder);
      }

      // 创建时间过滤
      String[] creationDate = searchForm.getCreationDate();
      if (ArrayUtils.isNotEmpty(creationDate)) {
        String creationDateFrom = creationDate[0];
        String creationDateTo = creationDate[1];
        RangeQueryBuilder creatioinDateRangeQueryBuilder =
            QueryBuilders.rangeQuery(IndexAttributeConstants.CREATION_DATE_KEY);
        if (StringUtils.isNotBlank(creationDateFrom)) {
          creationDateFrom = creationDateFrom + "T00:00:00";
          LocalDateTime localCreationDateFrom =
              LocalDateTime.parse(creationDateFrom, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
          Instant utcCreateDateFrom =
              localCreationDateFrom.atZone(ZoneId.systemDefault()).toInstant();
          creatioinDateRangeQueryBuilder.from(utcCreateDateFrom.toString());
        }
        if (StringUtils.isNotBlank(creationDateTo)) {
          creationDateTo = creationDateTo + "T23:59:59";
          LocalDateTime localCreationDateTo =
              LocalDateTime.parse(creationDateTo, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
          Instant utcCreateDateTo = localCreationDateTo.atZone(ZoneId.systemDefault()).toInstant();
          creatioinDateRangeQueryBuilder.to(utcCreateDateTo.toString());
        }
        queryBuilder.filter(creatioinDateRangeQueryBuilder);
      }

      // 修改时间过滤
      String[] modificationDate = searchForm.getModificationDate();
      if (ArrayUtils.isNotEmpty(modificationDate)) {

        String modificationDateFrom = modificationDate[0];
        String modificationDateTo = modificationDate[1];
        RangeQueryBuilder modificationDateRangeQueryBuilder =
            QueryBuilders.rangeQuery(IndexAttributeConstants.MODIFICATION_DATE_KEY);

        if (modificationDateFrom != null) {
          modificationDateFrom = modificationDateFrom + "T00:00:00";
          LocalDateTime localModificationDateFrom =
              LocalDateTime.parse(modificationDateFrom, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
          Instant utcModificationDateFrom =
              localModificationDateFrom.atZone(ZoneId.systemDefault()).toInstant();

          modificationDateRangeQueryBuilder.from(utcModificationDateFrom.toString());
        }
        if (modificationDateTo != null) {
          modificationDateTo = modificationDateTo + "T23:59:59";
          LocalDateTime localModificationDateTo =
              LocalDateTime.parse(modificationDateTo, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
          Instant utcModificationDateTo =
              localModificationDateTo.atZone(ZoneId.systemDefault()).toInstant();
          modificationDateRangeQueryBuilder.to(utcModificationDateTo.toString());
        }
        queryBuilder.filter(modificationDateRangeQueryBuilder);
      }

      // 容器过滤
      String repositoryId = searchForm.getRepositoryId();
      if (repositoryId != null) {
        MatchPhraseQueryBuilder repositoryQueryBuilder =
            QueryBuilders.matchPhraseQuery(IndexAttributeConstants.REPOSITORY_ID_KEY, repositoryId);
        queryBuilder.filter(repositoryQueryBuilder);
      }

      // 部门过滤
      String[] departmentIds = searchForm.getDepartmentIds();
      if (departmentIds != null && departmentIds.length > 0) {
        String departmentId = departmentIds[departmentIds.length - 1];
        if (NumberUtils.isCreatable(departmentId)) {
          List<String> documentIds =new ArrayList<>();
//              departmentService.getSubDocumentIds(UserContext.getTenantId(), departmentId);
          if (CollectionUtils.isNotEmpty(documentIds)) {
            BoolQueryBuilder documentQuery = QueryBuilders.boolQuery();
            for (String id : documentIds) {
              documentQuery.should(
                  QueryBuilders.termQuery(IndexAttributeConstants.DEPARTMENT_ID_KEY, id));
            }
            queryBuilder.filter(documentQuery);
          }
        }
      }

      // 阶段过滤
      String phaseMark = searchForm.getPhaseMark();
      List<String> phaseMarks = searchForm.getPhaseMarks();
      if (StringUtils.isNotBlank(phaseMark)) {
        phaseMarks.add(phaseMark);
      }
      if (CollectionUtils.isNotEmpty(phaseMarks)) {
        BoolQueryBuilder phaseQuery = QueryBuilders.boolQuery();
        for (String phase : phaseMarks) {
          phaseQuery.should(QueryBuilders.termQuery(IndexAttributeConstants.PHASE, phase));
        }
        queryBuilder.filter(phaseQuery);
      }

      // IBA查询
      Map<String, String> customAttributes = searchForm.getCustomAttributes();
      if (customAttributes != null) {
        for (Map.Entry<String, String> entry : customAttributes.entrySet()) {
          String key = entry.getKey();
          String value = entry.getValue();
          if (StringUtils.isBlank(key) || StringUtils.isBlank(value)) {
            continue;
          }
          // 去空、转义
          value = handleAttribute(value);
          // 前后增加通配符
          if (!StringUtils.startsWith(value, "*")) {
            value = "*" + value;
          }
          if (!StringUtils.endsWith(value, "*")) {
            value = value + "*";
          }
          queryBuilder.must(
              QueryBuilders.queryStringQuery(value).field("details." + key + ".keyword"));
        }
      }

      // 过滤物资编码是否存在
      Boolean codeOfMaterialExist = searchForm.getCodeOfMaterialExist();
      if (codeOfMaterialExist != null) {
        if (codeOfMaterialExist) {
          //SoftAttributeConstants.CODE_OF_MATERIAL 为字段key值
//          queryBuilder.must(
//              QueryBuilders.wildcardQuery(SoftAttributeConstants.CODE_OF_MATERIAL, "*"));
        } else {
          queryBuilder.must(
              QueryBuilders.boolQuery()
//                  .should(QueryBuilders.termQuery(SoftAttributeConstants.CODE_OF_MATERIAL, ""))
                  .should(
                      QueryBuilders.boolQuery()
                          .mustNot(
                              QueryBuilders.existsQuery("xxx")
                          )));
        }
      }

      // 过滤物资编码_A是否存在
      Boolean codeOfMaterialAExist = searchForm.getCodeOfMaterialAExist();
      if (codeOfMaterialAExist != null) {
        if (codeOfMaterialAExist) {
          queryBuilder.must(
              QueryBuilders.wildcardQuery("SoftAttributeConstants.CODE_OF_MATERIAL_A", "*"));
        } else {
          queryBuilder.must(
              QueryBuilders.boolQuery()
                  .should(QueryBuilders.termQuery("SoftAttributeConstants.CODE_OF_MATERIAL_A", ""))
                  .should(
                      QueryBuilders.boolQuery()
                          .mustNot(
                              QueryBuilders.existsQuery(
                                 " SoftAttributeConstants.CODE_OF_MATERIAL_A"))));
        }
      }

      /* 物资编码_A(CODE_OF_MATERIAL_A) */
      String codeOfMaterialA = StringUtils.trimToEmpty(searchForm.getCodeOfMaterialA());
      codeOfMaterialA = QueryParser.escape(codeOfMaterialA);
      if (StringUtils.isNotBlank(codeOfMaterialA)) {
        if (!StringUtils.startsWith(codeOfMaterialA, "*")) {
          codeOfMaterialA = "*" + codeOfMaterialA;
        }
        if (!StringUtils.endsWith(codeOfMaterialA, "*")) {
          codeOfMaterialA = codeOfMaterialA + "*";
        }
        queryBuilder.must(
            QueryBuilders.queryStringQuery(codeOfMaterialA)
                .field("SoftAttributeConstants.CODE_OF_MATERIAL_A" + ".keyword"));
      }

      /* 物资编码(CODE_OF_MATERIAL) */
      String codeOfMaterial = StringUtils.trimToEmpty(searchForm.getCodeOfMaterial());
      codeOfMaterial = QueryParser.escape(codeOfMaterial);
      if (StringUtils.isNotBlank(codeOfMaterial)) {
        if (!StringUtils.startsWith(codeOfMaterial, "*")) {
          codeOfMaterial = "*" + codeOfMaterial;
        }
        if (!StringUtils.endsWith(codeOfMaterial, "*")) {
          codeOfMaterial = codeOfMaterial + "*";
        }
        queryBuilder.must(
            QueryBuilders.queryStringQuery(codeOfMaterial)
                .field("SoftAttributeConstants.CODE_OF_MATERIAL" + ".keyword"));
      }

      // 过滤型号是否在用
      Boolean standardPartUsing = searchForm.getStandardPartUsing();
      if (standardPartUsing != null) {
        if (standardPartUsing) {
          queryBuilder.mustNot(
              QueryBuilders.boolQuery()
                  .should(
                      QueryBuilders.matchPhraseQuery(
                          IndexAttributeConstants.STANDARDPARTUSAGECOUNT, "0"))
                  .should(
                      QueryBuilders.boolQuery()
                          .mustNot(
                              QueryBuilders.existsQuery(
                                  IndexAttributeConstants.STANDARDPARTUSAGECOUNT))));
        } else {
          queryBuilder.must(
              QueryBuilders.boolQuery()
                  .should(
                      QueryBuilders.matchPhraseQuery(
                          IndexAttributeConstants.STANDARDPARTUSAGECOUNT, "0"))
                  .should(
                      QueryBuilders.boolQuery()
                          .mustNot(
                              QueryBuilders.existsQuery(
                                  IndexAttributeConstants.STANDARDPARTUSAGECOUNT))));
        }
      }

      searchSourceBuilder.query(queryBuilder);

      // 分页参数
      searchSourceBuilder.from((int) pageable.getOffset());
      searchSourceBuilder.size(pageable.getPageSize());
      searchSourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
      searchSourceBuilder.trackScores(true);

      // 排序
      Sort sort = pageable.getSort();
      if (sort.isUnsorted()) {
        sort = Sort.by(Direction.DESC, "modifyStamp");
      }
      searchSourceBuilder.sort(new FieldSortBuilder("_score").order(SortOrder.DESC));
      for (Order order : sort) {
        String sortFieldName = getSortFiledName(order.getProperty());
        SortOrder sortOrder;
        if (order.isAscending()) {
          sortOrder = SortOrder.ASC;
        } else {
          sortOrder = SortOrder.DESC;
        }
        searchSourceBuilder.sort(new FieldSortBuilder(sortFieldName).order(sortOrder));
      }

      SearchRequest searchRequest = new SearchRequest();
      // 指定索引库
      if (StringUtils.isNotBlank(index)) {
        searchRequest.indices(index);
      }

      // 查询条件
      searchRequest.source(searchSourceBuilder);

      log.info(">>> search index={} searchSourceBuilder={}", index, searchSourceBuilder);

      SearchResponse response = null;
      try {
        response = client.search(searchRequest, RequestOptions.DEFAULT);
      } catch (ElasticsearchStatusException e) {
        log.error(e.getLocalizedMessage());
        e.printStackTrace();
      }
      if (response == null) {
        return page;
      }
      SearchHits hits = response.getHits();
      TotalHits totalHits = hits.getTotalHits();
      long totalResults = (long) totalHits.value;
      float maxScore = hits.getMaxScore();

      log.info(">>> search maxScore={},totalResults={}", maxScore, totalResults);

      List<SearchResultDTO> results = new ArrayList<>();
      hits = response.getHits();
      totalResults = hits.getTotalHits().value;

      page = new PageImpl<>(results, pageable, totalResults);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return page;
  }

  /**
   * 根据前端传递的属性判断排序字段
   *
   * @param propertyName 属性名称
   * @return 转义后的属性名称
   */
  private String getSortFiledName(String propertyName) {
    if (StringUtils.equals(propertyName, "name")) {
      propertyName = propertyName + ".keyword";
    } else if (StringUtils.equals(propertyName, "code")) {
      propertyName = propertyName + ".keyword";
    } else if (StringUtils.equals(propertyName, "stateDisplay")) {
      propertyName = "state" + ".keyword";
    } else if (StringUtils.equals(propertyName, "phaseMark")) {
      propertyName = "phase" + ".keyword";
    } else if (StringUtils.equals(propertyName, "repositoryName")) {
      propertyName = "repositoryId" + ".keyword";
    } else if (StringUtils.equals(propertyName, "modifierFullName")) {
      propertyName = "modifierId" + ".keyword";
    } else if (StringUtils.equals(propertyName, "departmentName")) {
      propertyName = "departmentId" + ".keyword";
    } else if (StringUtils.equals(propertyName, "secretLevel")) {
      propertyName = "secretLevel" + ".keyword";
    } else if (StringUtils.equals(propertyName, "version")) {
      propertyName = "version" + ".keyword";
    }
    return propertyName;
  }

  private SearchResultDTO prepareResults(Map<String, Object> source, Map<String, String> stateMap) {
    SearchResultDTO searchResult = new SearchResultDTO();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

    try {
      String idStr = extractStringValue(source, IndexAttributeConstants.ID_KEY);
      if (StringUtils.isNotBlank(idStr)) {
        searchResult.setId(Long.parseLong(idStr));
      }
    } catch (NumberFormatException e) {
      log.error(e.getMessage(), e);
    }
    String businessType = extractStringValue(source, IndexAttributeConstants.BUSINESS_TYPE_KEY);
//    searchResult.setObjectType(BusinessObjectType.toBusinessObjectType(businessType));
    searchResult.setLocation(extractStringValue(source, IndexAttributeConstants.LOCATION_KEY));
    searchResult.setCodeOfMaterialA(
        extractStringValue(source, IndexAttributeConstants.CODE_OF_MATERIAL_A));
    searchResult.setCodeOfMaterial(
        extractStringValue(source, IndexAttributeConstants.CODE_OF_MATERIAL));
    searchResult.setOid(extractStringValue(source, IndexAttributeConstants.OID_KEY));
    searchResult.setWtVid(extractStringValue(source, IndexAttributeConstants.REVISIONOID_KEY));
    searchResult.setCode(extractStringValue(source, IndexAttributeConstants.CODE_KEY));
    searchResult.setName(extractStringValue(source, IndexAttributeConstants.NAME_KEY));
    searchResult.setDescription(
        extractStringValue(source, IndexAttributeConstants.DESCRIPTION_KEY));
    searchResult.setVersion(extractStringValue(source, IndexAttributeConstants.VERSION_KEY));
    searchResult.setModelCode(extractStringValue(source, IndexAttributeConstants.MODELCODE_KEY));
    searchResult.setProductCode(
        extractStringValue(source, IndexAttributeConstants.PRODUCTCODE_KEY));

    String departmentId = extractStringValue(source, IndexAttributeConstants.DEPARTMENT_ID_KEY);

    String icon = extractStringValue(source, IndexAttributeConstants.ICON_KEY);
    searchResult.setSecretLevel(
        extractStringValue(source, IndexAttributeConstants.SECRETLEVEL_KEY));
    Object details = source.get("details");
    if (details != null && searchResult.getSecretLevel() == null) {
      if (details instanceof Map) {
        Map<String, Object> detailsMap = (Map<String, Object>) details;
        searchResult.setSecretLevel(extractStringValue(detailsMap, "SecretLevel"));
      }
    }
    icon = "/static/image/type/" + icon;
    searchResult.setIcon(icon);
    searchResult.setPhaseMark(
        StringUtils.isBlank(extractStringValue(source, IndexAttributeConstants.PHASE))
            ? ""
            : extractStringValue(source, IndexAttributeConstants.PHASE));

    // 生命周期状态
    try {
      String stateKey = extractStringValue(source, IndexAttributeConstants.STATE_KEY);
      searchResult.setStateKey(stateKey);
      searchResult.setStateDisplay(stateMap.get(stateKey));
    } catch (NumberFormatException e) {
      log.error(e.getMessage(), e);
    }

    String createStampValue = extractStringValue(source, IndexAttributeConstants.CREATION_DATE_KEY);
    if (StringUtils.isNotBlank(createStampValue)) {
      createStampValue = StringUtils.replace(createStampValue, "+00:00", "+0000");
      try {
        Date createStamp = sdf.parse(createStampValue);
        searchResult.setCreateStamp(createStamp);
      } catch (ParseException e) {
        log.error(e.getMessage(), e);
      }
    }

    String modifyStampValue =
        extractStringValue(source, IndexAttributeConstants.MODIFICATION_DATE_KEY);
    if (StringUtils.isNotBlank(modifyStampValue)) {
      modifyStampValue = StringUtils.replace(modifyStampValue, "+00:00", "+0000");
      try {
        Date modifyStamp = sdf.parse(modifyStampValue);
        searchResult.setModifyStamp(modifyStamp);
      } catch (ParseException e) {
        log.error(e.getMessage(), e);
      }
    }

    try {
      Map<String, Object> customAttributes =
          (Map<String, Object>) source.get(IndexAttributeConstants.DETAILS_KEY);
      searchResult.setCustomAttributes(customAttributes);
    } catch (NumberFormatException e) {
      log.error(e.getMessage(), e);
    }
    return searchResult;
  }

  @Override
  public void export(String path, String name, int offset, int size) {
    List<Map<String, Object>> results = new ArrayList<>();
    for (int i = 0; i < offset; i++) {
      SearchSourceBuilder searchSourceBuilder = SearchSourceBuilder.searchSource();
      searchSourceBuilder.from(i);
      searchSourceBuilder.size(size);
      searchSourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
      searchSourceBuilder.trackScores(true);
      // 排序
      Sort sort = Sort.by(Direction.ASC, "modifyStamp");
      for (Order order : sort) {
        String sortFieldName = getSortFiledName(order.getProperty());
        SortOrder sortOrder;
        if (order.isAscending()) {
          sortOrder = SortOrder.ASC;
        } else {
          sortOrder = SortOrder.DESC;
        }
        searchSourceBuilder.sort(new FieldSortBuilder(sortFieldName).order(sortOrder));
      }
      SearchRequest searchRequest = new SearchRequest();
      // 查询条件
      searchRequest.source(searchSourceBuilder);
      SearchResponse response = null;
      try {
        response = client.search(searchRequest, RequestOptions.DEFAULT);
      } catch (Exception e) {
        log.error(e.getLocalizedMessage());
        e.printStackTrace();
      }
      if (response == null) {
        break;
      }
      SearchHits hits = response.getHits();
      if (hits == null) {
        break;
      }
      long totalResults = hits.getTotalHits().value;
      float maxScore = hits.getMaxScore();
      log.debug("maxScore={},totalResults={},i={}", maxScore, totalResults, i);
      totalResults = hits.getTotalHits().value;
      SearchHit[] hits2 = hits.getHits();
      if (hits2 != null && hits2.length > 0) {
        for (SearchHit hit : hits.getHits()) {
          Map<String, Object> source = hit.getSourceAsMap();
          if (source != null) {
            results.add(source);
          }
        }
      } else {
        break;
      }
    }
    try {
      exportExcel(getHeaders(), path, name, results);
    } catch (IOException e) {
      log.info(e.getMessage());
      e.printStackTrace();
    }
  }

  private static String[] getHeaders() {
    return new String[] {
      "数据类型(businessType)",
      "租户id(tenantId)",
      "ID(id)",
      "OID(oid)",
      "编号(code)",
      "名称(name)",
      "状态(state)",
      "版本号(version)",
      "版本OID(revisionOid)",
      "主版本OID(masterOid)",
      "类型id集合(categoryId)",
      "部门id(departmentId)",
      "创建者id(creatorId)",
      "修改者id(modifierId)",
      "创建时间(createStamp)",
      "修改时间(modifyStamp)",
      "阶段标记(phase)",
      "密级(secretLevel)",
      "图标(icon)"
    };
  }

  private static String[] getHeaderKeys() {
    return new String[] {
      "businessType",
      "tenantId",
      "id",
      "oid",
      "code",
      "name",
      "state",
      "version",
      "revisionOid",
      "masterOid",
      "categoryId",
      "departmentId",
      "creatorId",
      "modifierId",
      "createStamp",
      "modifyStamp",
      "phase",
      "secretLevel",
      "icon"
    };
  }

  @SuppressWarnings("deprecation")
  public static void exportExcel(
      String[] headers, String path, String name, List<Map<String, Object>> source)
      throws IOException {
    log.debug(
        "====> 生成excel表格的数据为空,headers={},path={},name={},source={}", headers, path, name, source);
    // 声明一个工作薄
    HashMap<String, Integer> mapRow = new HashMap<>(16);
    HashMap<String, HSSFSheet> mapSheet = new HashMap<>(16);
    @SuppressWarnings("resource")
    HSSFWorkbook workbook = new HSSFWorkbook();
    // 生成一个样式
    HSSFCellStyle style = workbook.createCellStyle();
    // 设置这些样式
//    style.setFillForegroundColor(HSSFColor.HSSFColorPredefined.SKY_BLUE.getIndex());
    style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    style.setBorderBottom(BorderStyle.THIN);
    style.setBorderLeft(BorderStyle.THIN);
    style.setBorderRight(BorderStyle.THIN);
    style.setBorderTop(BorderStyle.THIN);
    style.setAlignment(HorizontalAlignment.CENTER);
    // 生成一个字体
    HSSFFont font = workbook.createFont();
//    font.setColor(HSSFColor.HSSFColorPredefined.VIOLET.getIndex());
    font.setFontHeightInPoints((short) 12);
    font.setBold(true);
    // 把字体应用到当前的样式
    style.setFont(font);
    // 生成并设置另一个样式
    HSSFCellStyle style2 = workbook.createCellStyle();
//    style2.setFillForegroundColor(HSSFColor.HSSFColorPredefined.LIGHT_YELLOW.getIndex());
    style2.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    style2.setBorderBottom(BorderStyle.THIN);
    style2.setBorderLeft(BorderStyle.THIN);
    style2.setBorderRight(BorderStyle.THIN);
    style2.setBorderTop(BorderStyle.THIN);
    style2.setAlignment(HorizontalAlignment.CENTER);
    style2.setVerticalAlignment(VerticalAlignment.CENTER);
    // 生成另一个字体
    HSSFFont font2 = workbook.createFont();
    font2.setBold(false);
    // 把字体应用到当前的样式
    style2.setFont(font2);
    if (CollectionUtils.isNotEmpty(source)) {
      for (int i = 0; i < source.size(); i++) {
        Map<String, Object> data = source.get(i);
        String businessType = extractStringValue(data, "businessType");
        if (StringUtils.isBlank(businessType)) {
          log.info("businessType为null,source={}", data);
          continue;
        }
        HSSFSheet sheet = mapSheet.get(businessType);

        int index = 1;
        if (sheet != null) {
          index = mapRow.get(businessType) + index;
        } else {
          // 生成一个表格
          sheet = workbook.createSheet();
          // 设置表格默认列宽度为15个字节
          sheet.setDefaultColumnWidth((short) 15);
          // 产生表格标题行
          HSSFRow row = sheet.createRow(0);
          for (short j = 0; j < headers.length; j++) {
            HSSFCell cell = row.createCell(j);
            cell.setCellStyle(style);
            HSSFRichTextString text = new HSSFRichTextString(headers[j]);
            cell.setCellValue(text);
          }
          mapRow.put(businessType, 0);
        }
        HSSFRow row = sheet.createRow(index);
        String[] headerKeys = getHeaderKeys();
        for (int j = 0; j < headerKeys.length; j++) {
          String extractStringValue = extractStringValue(data, headerKeys[j]);
          HSSFCell cell0 = row.createCell(j);
          cell0.setCellStyle(style2);
          cell0.setCellValue(extractStringValue);
        }
        mapSheet.put(businessType, sheet);
        mapRow.put(businessType, index);
      }

      if (log.isDebugEnabled()) {
        log.debug("====>name={}", name);
      }
      OutputStream out = new FileOutputStream(path + File.separator + name + ".xls");

      workbook.write(out);
      out.close();
    }
  }

  /**
   * 功能描述: 处理字符串
   *
   * @return java.lang.String
   * @author tzhang
   * @date 2019/2/28 9:33
   */
  public static String handleAttribute(String str) {
    if (StringUtils.isBlank(str)) {
      return null;
    }
    // 去空
    String result = StringUtils.trimToEmpty(str);
    // 转义
    result = QueryParser.escape(result);
    return result;
  }

  private boolean nameIsText(Map<String, Object> mapping, String name) {
    if (MapUtils.isEmpty(mapping) || StringUtils.isBlank(name)) {
      return false;
    }
    String type = null;
    Object k = mapping.get(name);
    if (k != null) {
      Map<String, Object> key = (Map<String, Object>) k;
      Object t = key.get("type");
      if (t != null) {
        type = (String) t;
      }
    }
    if (StringUtils.equals(type, "keyword")
        || StringUtils.equals(type, "date")
        || StringUtils.equals(type, "boolean")) {
      return false;
    } else {
      return true;
    }
  }

  private String getQueryString(String value) {
    if (StringUtils.isBlank(value)) {
      return value;
    }
    if (!StringUtils.startsWithIgnoreCase(value, "*")) {
      value = "*" + value;
    }
    if (!StringUtils.endsWithIgnoreCase(value, "*")) {
      value = value + "*";
    }
    return value;
  }
}
