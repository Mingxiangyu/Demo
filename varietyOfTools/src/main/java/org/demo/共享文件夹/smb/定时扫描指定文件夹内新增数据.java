package org.demo.共享文件夹.smb;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import jcifs.smb.SmbFileOutputStream;

public class 定时扫描指定文件夹内新增数据 {

  public static void main(String[] args) {
    Long lastmodify = 1619174461324L;

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

    //TODO 20分钟扫描一次
    try {
      Calendar rightNow = Calendar.getInstance();
      int month = rightNow.get(Calendar.MONTH) + 1; //第一个月从0开始，所以得到月份＋1
      int day = rightNow.get(Calendar.DAY_OF_MONTH);
      // 创建远程文件对象
      String url = remoteUrl + "/" + month + "/" + day + "/";
      System.out.println(url);
      SmbFile remoteFile = new SmbFile(url);
      remoteFile.connect();

      SmbFile copyremoteFile = new SmbFile(copyremoteUrl);
      copyremoteFile.connect();

      if (!remoteFile.exists()) {
        System.out.println("远程路径为: " + url + " 不存在,无法拷贝");
      }
      SmbFile[] smbFiles = remoteFile.listFiles();
      Arrays.sort(smbFiles, new CompratorByLastModified());
      System.out.println(smbFiles[0].getLastModified());
//        System.out.println(smbFiles[0].getName());

      for (SmbFile smbFile : smbFiles) {
        if (smbFile.getLastModified() <= lastmodify) {
          continue;
        }
        System.out.println(smbFile.getName());
        try (InputStream in = new BufferedInputStream(new SmbFileInputStream(smbFile));
            OutputStream out = new BufferedOutputStream(
                new SmbFileOutputStream(copyremoteFile + smbFile.getName()))) {

          // 读取文件内容
          byte[] buffer = new byte[4096];
          int len;
          while ((len = in.read(buffer, 0, buffer.length)) != -1) {
            out.write(buffer, 0, len);
          }
          SmbFile newRemoteFile = new SmbFile(copyremoteFile + smbFile.getName());


          out.flush();
          newRemoteFile.setLastModified(1);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      //      System.out.println(Arrays.toString(smbFiles));

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  static class CompratorByLastModified implements Comparator<SmbFile> {

    @Override
    public int compare(SmbFile o1, SmbFile o2) {
      long diff = 0;
      try {
        diff = o1.lastModified() - o2.lastModified();
      } catch (SmbException e) {
        e.printStackTrace();
      }
      if (diff > 0) {
        return -1;//倒序正序控制 这里返回-1则为倒序 返回1则为正序
      } else if (diff == 0) {
        return 0;
      } else {
        return 1;//倒序正序控制  这里返回1为倒序,返回-1则为正序
      }
    }

  }
}
