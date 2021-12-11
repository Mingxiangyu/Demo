package com.iglens.共享文件夹.smb;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileOutputStream;

/**
 * <b>所属模块：</b>牧羊仒.测试<br>
 * <b>类名称：</b>CopyFileToLan<br>
 * <b>类描述：</b> 本地文件写入局域网共享文件夹 <br>
 * <b>版本：</b>V1.0<br>
 * <b>创建人：</b><a href="mailto:han_huayi@163.com">牧羊仒</a><br>
 * <b>创建时间：</b>2016年6月8日 下午3:12:36<br>
 */
public class win上共享文件夹上传 {

  public static void main(String[] args) {

    InputStream in = null;
    OutputStream out = null;
    try {
      // 测试文件
      File localFile = new File("C:\\Users\\T480S\\Desktop\\TS文档.docx");

      String host = "192.168.0.124"; // 远程服务器的地址
      String username = "873694747@qq.com"; // 用户名
      String password = "Wh970219"; // 密码
      String path = "/TestShare/"; // 远程服务器共享文件夹名称

      String remoteUrl =
          "smb://"
              + username
              + ":"
              + password
              + "@"
              + host
              + path
              + (path.endsWith("/") ? "" : "/");
      SmbFile remoteFile = new SmbFile(remoteUrl + "/" + localFile.getName());

      remoteFile.connect();

      in = new BufferedInputStream(new FileInputStream(localFile));
      out = new BufferedOutputStream(new SmbFileOutputStream(remoteFile));

      byte[] buffer = new byte[4096];
      int len = 0;
      while ((len = in.read(buffer, 0, buffer.length)) != -1) {
        out.write(buffer, 0, len);
      }
      out.flush();
    } catch (Exception e) {
      e.printStackTrace();
      String msg = "发生错误：" + e.getLocalizedMessage();
      System.out.println(msg);
    } finally {
      try {
        if (out != null) {
          out.close();
        }
        if (in != null) {
          in.close();
        }
      } catch (Exception e) {
      }
    }
  }
}
