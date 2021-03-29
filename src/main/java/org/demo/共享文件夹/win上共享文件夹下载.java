package org.demo.共享文件夹;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;

/**
 * <b>所属模块：</b>牧羊仒.测试<br>
 * <b>类名称：</b>CopyLanFileToLocal<br>
 * <b>类描述：</b> 读取局域网共享文件夹文件，到本地文件夹 <br>
 * <b>版本：</b>V1.0<br>
 * <b>创建人：</b><a href="mailto:han_huayi@163.com">牧羊仒</a><br>
 * <b>创建时间：</b>2016年6月8日 下午3:29:09<br>
 */
public class win上共享文件夹下载 {

  public static void main(String[] args) {
    InputStream in = null;
    //        ByteArrayOutputStream out = null ;
    OutputStream out = null;
    try {
      // 目标文件名
      String fileName = "新建 Microsoft Access 数据库.accdb";

      // 本地文件
      String localPath = "d:/";

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
      System.out.println(remoteUrl);

      // 创建远程文件对象
      SmbFile remoteFile = new SmbFile(remoteUrl + "/" + fileName);
      remoteFile.connect();

      // 创建文件流
      in = new BufferedInputStream(new SmbFileInputStream(remoteFile));
      out = new BufferedOutputStream(new FileOutputStream(new File(localPath + fileName)));

      // 读取文件内容
      byte[] buffer = new byte[4096];
      int len = 0;
      while ((len = in.read(buffer, 0, buffer.length)) != -1) {
        out.write(buffer, 0, len);
      }

      out.flush();
    } catch (Exception e) {
      String msg = "下载远程文件出错：" + e.getLocalizedMessage();
      System.out.println(msg);
      e.printStackTrace();
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
