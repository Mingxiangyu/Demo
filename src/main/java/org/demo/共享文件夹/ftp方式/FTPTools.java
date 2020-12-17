package org.demo.共享文件夹.ftp方式;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 通过FTP上传文件 @Author lvhaibao @Date 2018/2/11 21:43 */
public class FTPTools {
  // 用于打印日志
  private static final Logger log = LoggerFactory.getLogger(FTPTools.class);

  /* 解决文件夹和文件名乱码*/
  /** 本地字符编码 */
  private static String LOCAL_CHARSET = "GBK";

  // FTP协议里面，规定文件名编码为iso-8859-1
  private static String SERVER_CHARSET = "ISO-8859-1";

  // 设置私有不能实例化
  private FTPTools() {}

  /**
   * 上传
   *
   * @param hostname
   * @param port
   * @param username
   * @param password
   * @param workingPath 服务器的工作目录
   * @param inputStream 文件的输入流
   * @param saveName 要保存的文件名
   * @return
   */
  public static boolean upload(
      String hostname,
      int port,
      String username,
      String password,
      String workingPath,
      InputStream inputStream,
      String saveName) {
    boolean flag = false;
    FTPClient ftpClient = new FTPClient();
    // 1 测试连接
    if (connect(ftpClient, hostname, port, username, password)) {
      try {
        // TODO 调试时查看该目录下文件
        FTPFile[] ftpFiles = ftpClient.listFiles(workingPath);
        for (FTPFile ftpFile : ftpFiles) {
          System.out.println(ftpFile.getName());
        }
        // 2 检查工作目录是否存在
        if (!ftpClient.changeWorkingDirectory(workingPath)) {
          // 如果目录不存在则创建目录
          String[] dirs = workingPath.split("/");
          String tempPath = "";
          for (String dir : dirs) {
            if (null == dir || "".equals(dir)) {
              continue;
            }
            tempPath += "/" + dir;
            if (!ftpClient.changeWorkingDirectory(tempPath)) {
              // ftp创建文件夹是否成功
              boolean b = ftpClient.makeDirectory(tempPath);
              if (!b) {
                return flag;
              } else {
                ftpClient.changeWorkingDirectory(tempPath);
              }
            }
          }
        }
        // 3 检查是否上传成功
        flag = storeFile(ftpClient, saveName, inputStream);

      } catch (IOException e) {
        log.error("工作目录不存在");
        e.printStackTrace();
      } finally {
        disconnect(ftpClient);
      }
    }
    return flag;
  }

  /**
   * 断开连接
   *
   * @param ftpClient
   * @throws Exception
   */
  public static void disconnect(FTPClient ftpClient) {
    if (ftpClient.isConnected()) {
      try {
        ftpClient.disconnect();
        log.error("已关闭连接");
      } catch (IOException e) {
        log.error("没有关闭连接");
        e.printStackTrace();
      }
    }
  }

  /**
   * 测试是否能连接
   *
   * @param ftpClient
   * @param hostname ip或域名地址
   * @param port 端口
   * @param username 用户名
   * @param password 密码
   * @return 返回真则能连接
   */
  public static boolean connect(
      FTPClient ftpClient, String hostname, int port, String username, String password) {
    boolean flag = false;
    try {
      ftpClient.enterLocalPassiveMode();
      ftpClient.connect(hostname, port);
      // 开启服务器对UTF-8的支持，如果服务器支持就用UTF-8编码，否则就使用本地编码（GBK）.
      if (FTPReply.isPositiveCompletion(ftpClient.sendCommand("OPTS UTF8", "ON"))) {
        LOCAL_CHARSET = "UTF-8";
      }
      ftpClient.setControlEncoding(LOCAL_CHARSET);
      if (ftpClient.login(username, password)) {
        //设置文件类型必须放到登录后才生效，否则上传上去的文件内容依然乱码
      ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
        log.info("连接ftp成功");
        flag = true;
      } else {
        log.error("连接ftp失败，可能用户名或密码错误");
        try {
          disconnect(ftpClient);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    } catch (IOException e) {
      log.error("连接失败，可能ip或端口错误");
      e.printStackTrace();
    }
    return flag;
  }

  /**
   * 上传文件
   *
   * @param ftpClient
   * @param saveName 全路径。如/home/public/a.txt
   * @param fileInputStream 输入的文件流
   * @return
   */
  public static boolean storeFile(
      FTPClient ftpClient, String saveName, InputStream fileInputStream) {
    boolean flag = false;
    try {
      ftpClient.setFileTransferMode(FTPClient.BINARY_FILE_TYPE);
      flag = ftpClient.storeFile(saveName, fileInputStream);
      log.error("上传成功");
    } catch (IOException e) {
      log.error("上传失败");
      e.printStackTrace();
    } finally {
      disconnect(ftpClient);
    }
    return flag;
  }

  /** @Author lvhaibao @Date 2018/2/11 22:20 */
  public static void main(String[] args) throws FileNotFoundException {
    String hostname = "192.168.0.134";
    int port = 21;
    String username = "ceshi";
    String password = "test";
    String workingPath = "/test";
//    String str = "C:\\Users\\T480S\\Desktop\\TS文档.docx";
    String str = "C:\\Users\\T480S\\Desktop\\DF-5B-test-DF-5B-001-test.pdf";
    InputStream fileInputStream = new FileInputStream(new File(str));
//    String saveName = "/test/TS文档.docx";
    String saveName = "/test/DF-5B-test-DF-5B-001-test.pdf";
    try {
      // 如果名称支持UTF8则Local字符集修改为UTF8，否则使用GBK
      saveName = new String(saveName.getBytes(LOCAL_CHARSET), SERVER_CHARSET);
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }

    System.out.println(
        FTPTools.upload(
            hostname, port, username, password, workingPath, fileInputStream, saveName));
  }
}
