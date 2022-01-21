package com.iglens.elasticsearch.service;

import com.iglens.elasticsearch.dto.SearchFormDTO;
import com.iglens.elasticsearch.dto.SearchResultDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * The interface Search engine service.
 *
 * @author Li Feixiang
 */
public interface SearchEngineService {
  /**
   * 搜索数据
   *
   * @param indexName the index name
   * @param form the form
   * @param pageable the pageable
   * @return page
   */
  Page<SearchResultDTO> search(String indexName, SearchFormDTO form, Pageable pageable);

  /**
   * Export.
   *
   * @param path the path
   * @param name the name
   * @param offset the offset
   * @param size the size
   */
  void export(String path, String name, int offset, int size);

}
