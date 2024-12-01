package com.iglens.地理;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class 高德实现路径规划 {

  private static final String API_URL = "https://restapi.amap.com/v3/direction/transit/integrated";
  private static final String key = "1736f136af56af6a0be7239e2a936f63";

  /**
   * 成功标识
   */
  private static String SUCCESS_FLAG = "1";


  public static void main(String[] args) throws Exception {
    // 构建请求URL
    String city = "北京";
    String origin = "116.481028,39.989643";
    String destination = "116.482086,39.990496";
    extracted(origin, destination, city);
  }

  private static Double extracted(String origin, String destination, String city) throws IOException {
    String url = API_URL + "?origin=" + origin +
        "&" + "destination=" + destination +
        "&city=" + city +
        "&key=" + key;

    //高德接口返回的是JSON格式的字符串
    String queryResult = getResponse(url);
    JSONObject obj = JSONObject.parseObject(queryResult);
    double requiredMinutes;
    if (String.valueOf(obj.get("status")).equals(SUCCESS_FLAG)) {
      JSONObject route = obj.getJSONObject("route");
      JSONArray transits = route.getJSONArray("transits");
      if (transits.isEmpty()) {
        String distance = route.getString("distance");
        throw new RuntimeException("公交地铁路径规划失败，步行距离为：" + distance + " 米");
      }
      JSONObject jsonObject1 = transits.getJSONObject(0);
      Integer duration = jsonObject1.getInteger("duration");
      requiredMinutes = (double) (duration / 60);
    } else {
      throw new RuntimeException("路径规划失败，错误码：" + obj.get("infocode"));
    }
    System.out.println(requiredMinutes);
    return requiredMinutes;

  }

  /**
   * 发送请求
   *
   * @param serverUrl 请求地址
   */
  private static String getResponse(String serverUrl) {
    // 用JAVA发起http请求，并返回json格式的结果
    StringBuilder result = new StringBuilder();
    try {
      URL url = new URL(serverUrl);
      URLConnection conn = url.openConnection();
      BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      String line;
      while ((line = in.readLine()) != null) {
        result.append(line);
      }
      in.close();
    } catch (Exception e) {
      System.out.println(e.getMessage());
      e.printStackTrace();
    }
    return result.toString();
  }
}

