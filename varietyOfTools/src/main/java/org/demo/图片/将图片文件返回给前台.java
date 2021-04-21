package org.demo.图片;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;

public class 将图片文件返回给前台 {

  /**
   * 将图片文件返回给前台
   *
   * @param response 响应流
   * @param importPath 存储路径
   * @throws IOException
   */
  @GetMapping("/GetFile")
  public void getFile(HttpServletResponse response, String importPath) throws IOException {
    // 读取路径下面的文件
    File file = new File(importPath);
    File picFile = null;
    for (File f : file.listFiles()) {
      if (f.getName().contains("文件名")) {
        // 根据路径获取文件
        picFile = new File(f.getPath());
        // 获取文件后缀名格式
        String ext = picFile.getName().substring(picFile.getName().indexOf("."));
        // 判断图片格式,设置相应的输出文件格式
        if (ext.equals("jpg")) {
          response.setContentType("image/jpeg");
        } else if (ext.equals("JPG")) {
          response.setContentType("image/jpeg");
        } else if (ext.equals("png")) {
          response.setContentType("image/png");
        } else if (ext.equals("PNG")) {
          response.setContentType("image/png");
        }
      }
    }
    // 读取指定路径下面的文件
    InputStream in = new FileInputStream(picFile);
    OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
    // 创建存放文件内容的数组
    byte[] buff = new byte[1024];
    // 所读取的内容使用n来接收
    int n;
    // 当没有读取完时,继续读取,循环
    while ((n = in.read(buff)) != -1) {
      // 将字节数组的数据全部写入到输出流中
      outputStream.write(buff, 0, n);
    }
    // 强制将缓存区的数据进行输出
    outputStream.flush();
    // 关流
    outputStream.close();
    in.close();
  }
}
