package com.iglens.地理;

import com.alibaba.fastjson.JSONObject;
import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class 高德实现路径规划 {

  private static final String API_URL = "https://restapi.amap.com/v3/direction/transit/integrated";
  private static final String key = "1736f136af56af6a0be7239e2a936f63";

  private static final OkHttpClient client = new OkHttpClient();

  public static void main(String[] args) throws Exception {
    // 构建请求URL
    String city = "北京";
    String origin = "116.481028,39.989643";
    String destination = "destination";
    extracted(origin, destination, city);
  }

  private static void extracted(String origin, String destination, String city) throws IOException {
    String url = API_URL + "?origin=" + origin +
        "&" + destination + "=116.434446,39.90816" +
        "&city=" + city +
        "&key=" + key;

    // 创建请求对象
    Request request = new Request.Builder()
        .url(url)
        .build();

    // 发送请求并处理响应
    try (Response response = client.newCall(request).execute()) {
        if (!response.isSuccessful()) {
            throw new RuntimeException("Unexpected code " + response);
        }

      // 打印响应体
        String string = response.body().string();
        JSONObject jsonObject = JSONObject.parseObject(string);
        JSONObject route = jsonObject.getJSONObject("route");
        System.out.println(string);
    }
  }
}