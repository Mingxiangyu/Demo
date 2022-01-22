package com.iglens.elasticsearch.cpdm.service;

import com.iglens.elasticsearch.cpdm.dto.IndexCreationDTO;

/** @author T480S */
public interface AdministrationService {

  /**
   * 判断是否存在指定索引
   *
   * @param name 索引名称
   * @return true：存在索引；false：不存在索引
   */
  boolean existsIndex(String name);

  /**
   * 创建索引
   *
   * @param name 索引名称
   */
  void createIndex(String name);

  /**
   * 创建索引
   *
   * @param creationDTO 创建信息
   * @return true：创建成功；false：创建失败
   */
  boolean createIndex(IndexCreationDTO creationDTO);
}
