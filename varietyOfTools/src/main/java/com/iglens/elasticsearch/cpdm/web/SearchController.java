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
  public SearchController(SearchEngineService searchEngineService) {
    this.searchEngineService = searchEngineService;
  }

  @ApiImplicitParams({ // 用在请求的方法上，表示一组参数说明
    @ApiImplicitParam( // 指定一个请求参数的各个方面
        name = "name", // name：参数名
        value = "姓名", // value：参数的汉字说明、解释
        required = true, // required：参数是否必须传
        /*
        paramType：参数放在哪个地方
            · header --> 请求参数的获取：@RequestHeader
            · query --> 请求参数的获取：@RequestParam
            · path（用于restful接口）--> 请求参数的获取：@PathVariable
            · div（不常用）
            · form（不常用）
         */
        paramType = "query",
        dataType = "Integer" // dataType：参数类型，默认String，其它值dataType="Integer"
        ),
    @ApiImplicitParam(
        name = "age",
        value = "年龄",
        required = true,
        paramType = "query",
        dataType = "Integer")
  })
  // 原文链接： https://blog.51cto.com/u_9177933/4064396
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
