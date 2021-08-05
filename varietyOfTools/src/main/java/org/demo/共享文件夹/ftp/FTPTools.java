package org.demo.共享文件夹.ftp;

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

/**
 * 通过FTP上传文件
 *
 * @author T480S
 * @date 2018/2/11 21:43
 */
public class FTPTools {
  private static final Logger log = LoggerFactory.getLogger(FTPTools.class);

  /* 解决文件夹和文件名乱码*/
  /** 本地字符编码 */
  private static String LOCAL_CHARSET = "GBK";

  // FTP协议里面，规定文件名编码为iso-8859-1
  private static String SERVER_CHARSET = "ISO-8859-1";

  // 设置私有不能实例化
  private FTPTools() {}

  public static void main(String[] args) throws FileNotFoundException {
    String hostname = "172.20.10.3";
    int port = 21;
    String username = "ceshi";
    String password = "test";
    String workingPath = "/test";
    String str = "C:\\Users\\T480S\\Desktop\\模板数据结构.xlsx";
    InputStream fileInputStream = new FileInputStream(new File(str));
    String saveName = "TS文档.xlsx";
    try {
      // 上传文件时，文件名称需要做编码转换
      saveName = new String(saveName.getBytes(LOCAL_CHARSET), SERVER_CHARSET);
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    }

    System.out.println(
        FTPTools.upload(
            hostname, port, username, password, workingPath, fileInputStream, saveName));
  }

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
          // 因为ftp只支持一级级创建，固拆分后一级级创建
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
        log.error("工作目录不存在", e);
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
   */
  public static void disconnect(FTPClient ftpClient) {
    if (ftpClient.isConnected()) {
      try {
        ftpClient.disconnect();
        log.info("已关闭连接");
      } catch (IOException e) {
        log.error("没有关闭连接", e);
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
      // 设置连接超时时间
      ftpClient.setConnectTimeout(1000 * 120);
      ftpClient.connect(hostname, port);
      // 开启服务器对UTF-8的支持，如果服务器支持就用UTF-8编码，否则就使用本地编码（GBK）.
      if (FTPReply.isPositiveCompletion(ftpClient.sendCommand("OPTS UTF8", "ON"))) {
        LOCAL_CHARSET = "UTF-8";
      }
      ftpClient.setControlEncoding(LOCAL_CHARSET);
      // 设置传输超时时间为120秒
      ftpClient.setDataTimeout(1000 * 120);
      // 设置超时
      ftpClient.setSoTimeout(1000 * 120);
      /*
      出现这个报错信息：Host attempting data connection 192.168.90.151 is not same as server 192.168.90.12
      添加下面代码
      该代码段为取消服务器获取自身Ip地址和提交的host进行匹配，否则当不一致时报出以上异常。
       */
      ftpClient.setRemoteVerificationEnabled(false);
      if (ftpClient.login(username, password)) {
        // 设置文件类型必须放到登录后才生效，否则上传上去的文件内容依然乱码
        ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
        log.info("连接ftp成功");
        flag = true;
      } else {
        disconnect(ftpClient);
        throw new RuntimeException("登录FTP失败，报告未生成！可能FTP用户名或密码错误！");
      }
    } catch (IOException e) {
      log.error("连接失败，可能ip或端口错误", e);
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
      // 将缓存区变大，提升上传效果
      ftpClient.setBufferSize(1024 * 1024 * 10);
      flag = ftpClient.storeFile(saveName, fileInputStream);
      log.info("上传成功");
    } catch (IOException e) {
      log.error("上传失败", e);
    } finally {
      disconnect(ftpClient);
    }
    return flag;
  }
}
