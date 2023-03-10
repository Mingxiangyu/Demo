package com.iglens.http;

import java.io.BufferedInputStream;
import java.io.File;
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
    File file = new File(downloadPath);
    //  文件存在才下载
    if (!file.exists()) {
      // 抛出异常，提示文件不存在
      return;
    }
    response.setContentType("application/octet-stream;");
    // response.setContentType("application/force-download");// 设置强制下载不打开

    // 2. 告诉浏览器下载的方式以及一些设置
    // 解决文件名乱码问题，获取浏览器类型，转换对应文件名编码格式，IE要求文件名必须是utf-8, firefo要求是iso-8859-1编码
    // String agent = request.getHeader("user-agent");
    // if (agent.contains("FireFox")) {
    //   filename = new String(filename.getBytes("UTF-8"), "iso-8859-1");
    // } else {
    //   filename = URLEncoder.encode(filename, "UTF-8");
    // }

    fileName = new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
    // 设置一个响应头，无论是否被浏览器解析，都下载
    response.setHeader("Content-disposition", "attachment; filename=" + fileName);
    try (
    // 1.读取要下载的内容
    BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(file));
        // 将要下载的文件内容通过输出流写到浏览器
        ServletOutputStream outputStream = response.getOutputStream()) {

      byte[] b = new byte[2048];
      int len;
      while ((len = inputStream.read(b)) > 0) {
        outputStream.write(b, 0, len);
      }
      outputStream.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
