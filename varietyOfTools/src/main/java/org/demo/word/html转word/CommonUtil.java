package org.demo.word.html转word;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class CommonUtil {
  public static String urlToBase64(String imgUrl) {
    InputStream inputStream = null;
    ByteArrayOutputStream outputStream = null;
    byte[] buffer = null;
    try {
      // 创建URL
      URL url = new URL(imgUrl);
      // 创建链接
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("GET");
      conn.setConnectTimeout(5000);
      inputStream = conn.getInputStream();
      outputStream = new ByteArrayOutputStream();
      // 将内容读取内存中
      buffer = new byte[1024];
      int len = -1;
      while ((len = inputStream.read(buffer)) != -1) {
        outputStream.write(buffer, 0, len);
      }
      buffer = outputStream.toByteArray();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (inputStream != null) {
        try {
          // 关闭inputStream流
          inputStream.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      if (outputStream != null) {
        try {
          // 关闭outputStream流
          outputStream.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    // 对字节数组Base64编码
    //    String base64 = "data:image/png;base64," + new BASE64Encoder().encode(buffer);
    return null;
  }
}
