package org.demo.共享文件夹.smb;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import jcifs.UniAddress;
import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileOutputStream;
import jcifs.smb.SmbSession;

/**
 * @author T480S
 */
public class 写入共享文件夹 {
  // 共享文件夹所在服务器ip
  private static String USER_DOMAIN = "192.168.0.124";
//  private static String USER_DOMAIN = "192.168.0.126";
  // 访问用户
  private static String USER_ACCOUNT = "PEIZHI WANG";
  // 访问密码
  private static String USER_PWS = "Wh970219";
  // 共享文件夹地址
  private static final String shareDirectory = "smb://192.168.0.124/TestShare";
  // 字节长度
  private static final int byteLen = 1024;

  /**
   * 向共享目录上传文件
   *
//   * @param shareDirectory 共享目录
   * @param localFile 本地目录中的文件路径
   * @date 2019-01-10 20:16
   */
  public void smbPut(File localFile) {
    // 域服务器验证
    NtlmPasswordAuthentication auth =
        new NtlmPasswordAuthentication("", USER_ACCOUNT, USER_PWS);
    String fileName = localFile.getName();

    try {
      UniAddress  address = UniAddress.getByName("192.168.0.124");
      SmbSession.logon(address, auth);
    } catch (UnknownHostException | SmbException e) {
      throw new RuntimeException(e);
    }

    SmbFile remoteFile;
    try {
      remoteFile = new SmbFile(shareDirectory +  "/" + fileName, auth);
      remoteFile.connect();
    } catch (MalformedURLException e) {
      throw new RuntimeException("生成远程文件出错");
    } catch (IOException e) {
      throw new RuntimeException("生成远程文件出错"+e);

    }

    try (InputStream in = new BufferedInputStream(new FileInputStream(localFile));
        OutputStream out = new BufferedOutputStream(new SmbFileOutputStream(remoteFile)); ) {
      byte[] buffer = new byte[byteLen];
      while (in.read(buffer) != -1) {
        out.write(buffer);
        buffer = new byte[byteLen];
      }
      out.flush();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    String localFile = "C:\\Users\\T480S\\Desktop\\TS文档.docx";
    写入共享文件夹 test = new 写入共享文件夹();
    test.smbPut(new File(localFile));
  }
}
