package org.demo.图片;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

public class 将图片URL转为file {

  /**
   * 将图片URL转为file
   *
   * @param url 图片url
   * @return File
   * @author dyc date: 2020/9/4 14:54
   */
  public static File urlToFile(String url) {
    // 对本地文件命名
    String fileName = url.substring(url.lastIndexOf("."));
    File file = null;

    URL urlfile;
    InputStream inStream = null;
    OutputStream os = null;
    try {
      // 创建临时文件
      file = File.createTempFile("net_url", fileName);
      // 下载
      urlfile = new URL(url);
      inStream = urlfile.openStream();
      os = new FileOutputStream(file);

      int bytesRead;
      int len = 8192;
      byte[] buffer = new byte[len];
      while ((bytesRead = inStream.read(buffer, 0, len)) != -1) {
        os.write(buffer, 0, bytesRead);
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        if (null != os) {
          os.close();
        }
        if (null != inStream) {
          inStream.close();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    return file;
  }
}
