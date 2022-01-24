package com.iglens.elasticsearch;

import com.alibaba.fastjson.JSONObject;
import java.io.IOException;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;

public class es查询语句 {
  @Autowired private RestHighLevelClient client;

  public void search() throws IOException {
    /*1 词条查询  */
    //    1.1 等值查询-term
    //    SQL：select * from person where name = '张无忌';
    /*es:
        GET /person/_search
    {
     "query": {
      "term": {
       "name.keyword": {
        "value": "张无忌",
        "boost": 1.0
       }
      }
     }
    }
         */
    // 根据索引创建查询请求
    SearchRequest searchRequest = new SearchRequest("person");
    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
    // 构建查询语句
    searchSourceBuilder.query(QueryBuilders.termQuery("name.keyword", "张无忌"));
    System.out.println("searchSourceBuilder=====================" + searchSourceBuilder);
    searchRequest.source(searchSourceBuilder);
    SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
    System.out.println(JSONObject.toJSON(response));
  }
}
