package com.iglens.http.okhttp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class OkHttpTest {

  public static void main(String[] args) {
    // getTest();
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("start",1557244800000L);
    jsonObject.put("end",1687535999000L);
    String json = JSON.toJSONString(jsonObject);

    // String value = jsonObject.toString();
    // System.out.println(value);

    String sync =
        OkHttpUtils.builder()
            .url("http://36.41.73.198:16097/api/datamanager/api/v1/advancedSearch")
            // .addHeader("Content-Length", "<calculated when request is sent>")
            .addHeader(
                "Authorization",
                "eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInVzZXJJZCI6I"
                    + "jAiLCJuYW1lIjoi566h55CG5ZGYIiwicm9sZXMiOiIxIn0."
                    + "o3LrcgLRsi1BaAfg6T7MH6Z6Lqy54KHt0JVf6TorDuO46pi"
                    + "RRRm8QJTD8B-FWjo4tq2gjaR1vEbESM8RLnIYbyioyXQceBooJk5Upp1"
                    + "TflDbS4ixbH_LPrat7hTCXMS2XNc9-gXXNY6LJ3W900rM8v1FYr5RaIz-zZTri5Qlwpw")
            // .addParam("keyWordName", "osm")
            // .addParam("pageSize", "50")
            // .addParam("currentPage", "1")
            .addObjectParam("shootingTime", jsonObject.toJSONString())
            .addObjectParam(
                "geo",
                "POLYGON((115.3861736806782 73.24360117159506,"
                    + "115.3861736806782 53.52731033127831,"
                    + "110.64370969728694 73.24360117159506,"
                    + "110.64370969728694 53.52731033127831,"
                    + "115.3861736806782 73.24360117159506))")
            .jsonObjectPost()
            .sync();
    System.out.println(sync);
  }

  private static void getTest() {
    String sync =
        OkHttpUtils.builder().url("http://47.243.130.178:18081/spider/" + "软件文档").get().sync();
    System.out.println(sync);
  }
}
