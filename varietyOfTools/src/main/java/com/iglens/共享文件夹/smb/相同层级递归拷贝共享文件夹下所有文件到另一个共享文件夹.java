package com.iglens.共享文件夹.smb;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import jcifs.smb.SmbFileOutputStream;

/**
 * <b>所属模块：</b>牧羊仒.测试<br>
 * <b>类名称：</b>CopyLanFileToLocal<br>
 * <b>类描述：</b> 读取局域网共享文件夹文件，到本地文件夹 <br>
 * <b>版本：</b>V1.0<br>
 * <b>创建人：</b><a href="mailto:han_huayi@163.com">牧羊仒</a><br>
 * <b>创建时间：</b>2016年6月8日 下午3:29:09<br>
 */
public class 相同层级递归拷贝共享文件夹下所有文件到另一个共享文件夹 {

  public static void main(String[] args) {

    String host = "172.20.10.2"; // 远程服务器的地址
    String username = "T480S"; // 用户名
    String password = "ktkj123456"; // 密码
    String path = "/test/"; // 远程服务器共享文件夹名称

    String copypath = "/test1/"; // 远程服务器共享文件夹名称

    String remoteUrl =
        "smb://" + username + ":" + password + "@" + host + path + (path.endsWith("/") ? "" : "/");
    System.out.println(remoteUrl);

    String copyremoteUrl =
        "smb://"
            + username
            + ":"
            + password
            + "@"
            + host
            + copypath
            + (copypath.endsWith("/") ? "" : "/");
    System.out.println(copyremoteUrl);

    try {
      // 创建远程文件对象
      SmbFile remoteFile = new SmbFile(remoteUrl);
      remoteFile.connect();

      SmbFile copyremoteFile = new SmbFile(copyremoteUrl);
      copyremoteFile.connect();

      SmbFile[] smbFiles = remoteFile.listFiles();

      copyFile(copyremoteUrl, copyremoteFile, smbFiles);
      //      System.out.println(Arrays.toString(smbFiles));

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static void copyFile(String copyremoteUrl, SmbFile copyremoteFile, SmbFile[] smbFiles) {
    InputStream in = null;
    OutputStream out = null;
    try {
      for (SmbFile smbFile : smbFiles) {
        if (smbFile.isDirectory()) {
          String dfspath = smbFile.getUncPath();
          System.out.println(dfspath);
          String[] split = dfspath.split("\\\\");
          System.out.println(Arrays.toString(split));
          String interiorcopyremoteUrl = copyremoteUrl + split[split.length - 1] + "/";
          SmbFile interiorCopyremoteFile = new SmbFile(interiorcopyremoteUrl);
          if (!interiorCopyremoteFile.exists()) {
            interiorCopyremoteFile.mkdir();
          }
          copyFile(interiorcopyremoteUrl, interiorCopyremoteFile, smbFile.listFiles());
          continue;
        }
        in = new BufferedInputStream(new SmbFileInputStream(smbFile));
        out = new BufferedOutputStream(new SmbFileOutputStream(copyremoteFile + smbFile.getName()));

        // 读取文件内容
        byte[] buffer = new byte[4096];
        int len;
        while ((len = in.read(buffer, 0, buffer.length)) != -1) {
          out.write(buffer, 0, len);
        }

        out.flush();
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        if (out != null) {
          out.close();
        }
        if (in != null) {
          in.close();
        }
      } catch (Exception ignored) {
      }
    }
  }
}
