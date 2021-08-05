package org.demo;

import cn.hutool.core.io.file.FileNameUtil;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

public class http地址转换为输入流or下载url文件 {

  public static void main(String[] args) throws IOException {
    //    String httpPath = "http://www.dianping.com/shop/93433662";
    String httpPath = "https://tile.openstreetmap.org/10/534/356.png";
    // 通过url获取文件名后缀
    String fileNameFromURL = UrlUtils.getFileNameFromURL((httpPath));
    // 获取后缀(缺少 .）
    String suffix = FileNameUtil.getSuffix(fileNameFromURL);
    // 创建临时文件
    String prefix = UUID.randomUUID().toString();
    System.out.println(prefix);
    File tempFile = File.createTempFile(prefix, "." + suffix);
    try (FileOutputStream os = new FileOutputStream(tempFile); ) {
      InputStream inStream = getInputStreamByUrlFile(new URL(httpPath));
      int bytesRead;
      int len = 8192;
      byte[] buffer = new byte[len];
      while ((bytesRead = inStream.read(buffer, 0, len)) != -1) {
        os.write(buffer, 0, bytesRead);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * 将url文件转换为文件流
   *
   * @param fileUrl
   * @return
   * @throws IOException
   */
  public static InputStream getInputStreamByUrlFile(URL fileUrl) throws IOException {
    /*
     * 如果 copyURLToFile 报 Server returned HTTP response code: 403 for URL:http：//XXXXX
     * 原因在于服务器端禁止抓取,可以伪造一个User-Agent骗过服务器
     * 但是有些网址还是不行
     *
     * @link 原文链接：https://blog.csdn.net/weixin_44152538/article/details/87622300
     */
    HttpURLConnection connection = (HttpURLConnection) fileUrl.openConnection();
    // 防止屏蔽程序抓取而返回403错误
    connection.setRequestProperty(
        "User-Agent",
        "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36)"); // 防止报403错误。

    // 设置超时间为3秒
    connection.setConnectTimeout(3 * 1000);

    // 获取服务器响应代码
    int responsecode = connection.getResponseCode();
    if (responsecode == 200) {
      return connection.getInputStream();
    } else {
      System.out.println("获取不到网页的数据，服务器响应代码为：" + responsecode);
    }

    return null;
  }
}
