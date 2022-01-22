package com.iglens.elasticsearch.cpdm.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.lucene.util.IOUtils;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.io.Streams;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** @author Li Feixiang */
@Service
@Transactional(rollbackFor = Exception.class)
public class AdministrationServiceImpl implements AdministrationService {
  private static final Logger log = LoggerFactory.getLogger(AdministrationServiceImpl.class);

  private static final String INDEX_MAPPING_FILE = "classpath:es-mapping.json";

  private static final String INDEX_SETTING_FILE = "classpath:es-setting.json";

  private final RestHighLevelClient client;

  @Autowired
  public AdministrationServiceImpl(RestHighLevelClient client) {
    this.client = client;
  }

  @Override
  public boolean createIndex(IndexCreationDTO creationDTO) {
    if (creationDTO == null) {
      return false;
    }
    String name = creationDTO.getName();
    Integer numberOfShards = creationDTO.getNumberOfShards();
    Integer numberOfReplicas = creationDTO.getNumberOfReplicas();
    Integer autoExpandReplicas = creationDTO.getAutoExpandReplicas();
    if (numberOfShards == null || numberOfShards == 0) {
      numberOfShards = 1;
    }
    if (numberOfReplicas == null || numberOfReplicas == 0) {
      numberOfReplicas = 1;
    }
    if (autoExpandReplicas == null || autoExpandReplicas == 0) {
      numberOfReplicas = 1;
    }

    try {
      // 如果索引已存在，不处理
      if (existsIndex(name)) {
        return true;
      }
      // 这个请求是创建一个索引，在es中的索引相当于mysql 中创建一个数据库
      CreateIndexRequest request = new CreateIndexRequest(name);
      request.settings(indexSetting(), XContentType.JSON);
      request.mapping(IndexUtils.DEFAULT_TYPE_NAME, indexMapping(), XContentType.JSON);

      CreateIndexResponse response = client.indices().create(request, RequestOptions.DEFAULT);
      return response.isAcknowledged();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void createIndex(String name) {
    IndexCreationDTO indexCreationDTO = new IndexCreationDTO();
    indexCreationDTO.setName(name);
    createIndex(indexCreationDTO);
  }

  @Override
  public boolean existsIndex(String name) {
    GetIndexRequest request = new GetIndexRequest();
    request.indices(name);

    boolean exists = false;
    try {
      exists = client.indices().exists(request, RequestOptions.DEFAULT);
    } catch (IOException e) {
      log.error(e.getMessage(), e);
    }
    return exists;
  }

  private String indexMapping() {

    ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
    Resource mappingResource = resolver.getResource(INDEX_MAPPING_FILE);
    try (InputStream is = mappingResource.getInputStream()) {
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      Streams.copy(is, out);
      return out.toString(IOUtils.UTF_8);
    } catch (Exception e) {
      throw new IllegalStateException(
          "failed to create tasks results index template [" + INDEX_MAPPING_FILE + "]", e);
    }
  }

  private String indexSetting() {
    ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
    Resource mappingResource = resolver.getResource(INDEX_SETTING_FILE);
    try (InputStream is = mappingResource.getInputStream()) {
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      Streams.copy(is, out);
      return out.toString(IOUtils.UTF_8);
    } catch (Exception e) {
      throw new IllegalStateException(
          "failed to create tasks results index template [" + INDEX_MAPPING_FILE + "]", e);
    }
  }
}
