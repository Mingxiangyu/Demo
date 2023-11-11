package com.iglens.http.okhttp;

import cn.hutool.core.util.BooleanUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.io.File;
import java.util.ArrayList;

public class OkHttpTest {

  public static void main(String[] args) {
    // getTest();
    // test();
    // postFile();
    // putText();
    // 查看该图层是否发布成功
    String isPublishFlag =
        OkHttpUtils.builder()
            .url("http://36.41.73.198:10001/geo/" + "/getGeoLayerInfo")
            .addParam("geoServerWorkspace", "test")
            .addParam("layerName", "test")
            .post()
            .async();
    System.out.println("创建图层组：" + isPublishFlag);
    /*
     * {
     *   "success": true,
     *   "code": 20000,
     *   "message": "成功",
     *   "data": {
     *     "item": {
     *       "success": false,
     *       "code": 20001,
     *       "message": "无该图层信息",
     *       "data": {}
     *     }
     *   }
     * }
     */
    JSONObject publishResp = JSONObject.parseObject(isPublishFlag);
    JSONObject publishData = publishResp.getJSONObject("data");
    JSONObject publishItem = publishData.getJSONObject("item");
    String success = publishItem.getString("success");
    if (BooleanUtil.isTrue(BooleanUtil.toBoolean(success))) {
    }
  }

  private static void putText() {
    String workspace = "daoda";
    String saveSpace = "testHeishan";
    String spaceUrl = "http://36.41.73.198:28080/geoserver/rest/workspaces/" + workspace
        + "/coveragestores/" + saveSpace + "/external.geotiff";
    String text =
        "/data/53/filedata/hjzb_cleaned/pop/常态化任务/39/pop_人口分布_关岛_2000_8_20230606045920_&#人口分布_关岛_2000.tif";
    String putSync =
        OkHttpUtils.builder().url(spaceUrl).addHeader("Content-Type", "text/plain")
            .putText(text).sync();
    System.out.println(putSync);
  }

  private static void postFile() {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("isPublishFlag", true);
    String json = JSON.toJSONString(jsonObject);

    ArrayList<File> fileList = new ArrayList<>();
    fileList.add(
        new File(
            "C:\\Users\\zhouhuilin\\Desktop\\众包测试\\地名\\普通任务\\582\\placename_地名_中国_&_21_20230724175631_&#中国.geojson"));
    String isPublishFlag =
        OkHttpUtils.builder()
            .url("http://36.41.73.198:10001/pgData/upload_geojson")
            .addParam("isPublishFlag", true)
            .addParam("layerName", "oosm")
            .postFile(fileList)
            .sync();
    System.out.println(isPublishFlag);
  }

  private static void test() {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("start", 1557244800000L);
    jsonObject.put("end", 1687535999000L);
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
            .addParam("shootingTime", jsonObject.toJSONString())
            .addParam(
                "geo",
                "POLYGON((115.3861736806782 73.24360117159506,"
                    + "115.3861736806782 53.52731033127831,"
                    + "110.64370969728694 73.24360117159506,"
                    + "110.64370969728694 53.52731033127831,"
                    + "115.3861736806782 73.24360117159506))")
            .post(true)
            .sync();
    System.out.println(sync);
  }

  private static void getTest() {
    String sync =
        OkHttpUtils.builder().url("http://47.243.130.178:18081/spider/" + "软件文档").get().sync();
    System.out.println(sync);
  }
}
