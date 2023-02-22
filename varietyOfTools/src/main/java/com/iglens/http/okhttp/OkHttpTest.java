package com.iglens.http.okhttp;

public class OkHttpTest {
  public static void main(String[] args) {
    String sync =
        OkHttpUtils.builder()
            .url("http://47.243.130.178:18081/spider/"+"软件文档")
            .get()
            .sync();
    System.out.println(sync);
  }
}
