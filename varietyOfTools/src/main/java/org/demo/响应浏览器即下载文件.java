package org.demo;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

public class 响应浏览器即下载文件 {

  /**
   * 响应给浏览器
   *
   * @param downloadPath 下载路径(即实体文件在本机路径）
   * @param response 响应
   * @param fileName 文件名
   */
  public static void download(String downloadPath, HttpServletResponse response, String fileName) {
    response.setContentType("application/octet-stream;");
    fileName = new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
    response.setHeader("Content-disposition", "attachment; filename=" + fileName);
    try (BufferedInputStream inputStream =
            new BufferedInputStream(new FileInputStream(downloadPath));
        ServletOutputStream outputStream = response.getOutputStream()) {

      byte[] b = new byte[2048];
      int len;
      while ((len = inputStream.read(b)) > 0) {
        outputStream.write(b, 0, len);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
