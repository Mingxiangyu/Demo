package org.demo;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class http地址转换为输入流 {

  public static void main(String[] args) {
    //输入httpurl获取输入流
    try {
      InputStream inputStream = getInputStream(new URL("Http://"));
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }
  }

  /**
   * 将http地址转换为输入流
   *
   * @param fileUrl 网络url
   * @return 文件流
   */
  public static InputStream getInputStream(URL fileUrl) {
    try {
      HttpURLConnection conn = (HttpURLConnection) fileUrl.openConnection();
      // 设置超时间为3秒
      conn.setConnectTimeout(3 * 1000);
      // 防止屏蔽程序抓取而返回403错误
      conn.setRequestProperty(
          "User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
      // 得到输入流
      return conn.getInputStream();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }
}
