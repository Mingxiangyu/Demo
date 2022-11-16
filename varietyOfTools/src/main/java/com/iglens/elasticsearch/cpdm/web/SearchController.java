package com.iglens.elasticsearch.cpdm.web;

import com.iglens.elasticsearch.cpdm.dto.SearchFormDTO;
import com.iglens.elasticsearch.cpdm.dto.SearchResultDTO;
import com.iglens.elasticsearch.cpdm.service.SearchEngineService;
import com.iglens.elasticsearch.cpdm.support.IndexUtils;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SearchController {


  private final SearchEngineService searchEngineService;


  @Autowired(required = false)
  public SearchController(
      SearchEngineService searchEngineService) {
    this.searchEngineService = searchEngineService;
  }

  @ApiImplicitParams({
      @ApiImplicitParam(name = "name",value = "姓名",required = true,paramType = "query"),
      @ApiImplicitParam(name = "age",value = "年龄",required = true,paramType = "query",dataType = "Integer")
  })
  //原文链接： https://blog.51cto.com/u_9177933/4064396
  @RequestMapping(path = "/search", method = RequestMethod.GET)
  public ResponseEntity<Page<SearchResultDTO>> search(SearchFormDTO searchForm, Pageable pageable) {
    Long tenantId = 0L;
//        即indexId
//        UserContext.getTenantId();
    String indexName = null;
    if (tenantId > 0) {
      indexName = IndexUtils.formatIndexName(String.valueOf(tenantId));
    }

    Page<SearchResultDTO> page = searchEngineService.search(indexName, searchForm, pageable);
    if (page == null) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
    return ResponseEntity.status(HttpStatus.OK).body(page);
  }

}
