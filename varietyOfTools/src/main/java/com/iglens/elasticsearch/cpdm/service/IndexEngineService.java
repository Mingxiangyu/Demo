package com.iglens.elasticsearch.cpdm.service;

import java.util.Map;

/**
 * The interface Index engine service.
 *
 */
public interface IndexEngineService {

  /**
   * Process delete request boolean.
   *
   * @param objectType the object type
   * @param objectId the object id
   * @return the boolean
   */
  boolean processDeleteRequest(String objectType, String objectId);

  /**
   * Process index request boolean.
   *
   * @param objectType the object type
   * @param objectId the object id
   * @return the boolean
   */
  boolean processIndexRequest(String objectType, String objectId);

  /**
   * Gets mappings.
   *
   * @param indexName the index name
   * @return the mappings
   */
  Map<String, Object> getMappings(String indexName);
}
