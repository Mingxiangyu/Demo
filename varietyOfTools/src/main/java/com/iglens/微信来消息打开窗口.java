package com.iglens;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 微信来消息弹窗小工具 @作者： 清风不踏雪 @版本： v1.0 @作用； 实现了微信、企业微信来消息弹窗，会直接打开程序窗口 @实现原理：
 * 通过CMD命令获取注册表中微信、企业微信的安装和存档信息。 再通过存档信息循环遍历，找出加密的数据库文件， 最后判断文件的最后修改时间，然后根据安装信息，弹窗。 @感谢：
 * 判断思路来源：（找不到链接了，是个批处理来着）作者:phenix 读取注册表方法来源：csdn，天真吴邪xie (学会了怎么使用 Runtime.getRuntime().exec()
 * 执行CMD命令，并获取返回值)
 */
public class 微信来消息打开窗口 {
  public static void main(String[] args) {
    start();
  }

  /** 开始运行程序方法 */
  public static void start() {
    // 创建工具类
    try {
      // 创建工具类
      Tools weChat = new Tools();
      Tools wXWork = new Tools(true);
      // 判断程序是否在运行
      if ((weChat.getWXStatus() || wXWork.getWXStatus())
          && (!weChat.getFile().isDirectory() || !wXWork.getFile().isDirectory())) {
        Timer timer = new Timer("WXTanChuan"); // 创建定时器
        // weChat是微信、wXWork是企业微信；0表示等待0毫秒后开始执行；1000*3表示每隔3秒重复执行一次
        timer.schedule(weChat, 0, 1000 * 3); // 开始执行任务
        timer.schedule(wXWork, 0, 1000 * 3); // 开始执行任务
        while (weChat.getWXStatus() || wXWork.getWXStatus()) {
          if (!wXWork.getWXStatus()) {
            wXWork.cancel();
          } else if (!weChat.getWXStatus()) {
            weChat.cancel();
          }
          System.gc();
        }
        timer.cancel();
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}

class Tools extends TimerTask {
  // 加密数据库文件名
  private final String weChatTargetFile = "MSG0.db-wal";
  private final String wXWorktargetFile = "message.db-wal";
  // cmd查询注册表返回结果前缀
  private final String workPathPrefix;
  private final String filesSavePathPrefix;
  private final String exeName;
  // 微信、企业微信的工作、存档的绝对路径
  // 默认路径是为了防止未安装程序，出现的异常
  private String workPath;
  private String filesSavePath;
  // 获取工作路径的CMD命令
  private String getWorkPathCmd;
  // 获取存档路径的CMD命令
  private String getFilesSavePathCmd;
  // 是否企业微信
  private boolean isEnterprise;
  // 加密数据库文件的绝对路径
  private File file;
  // 数据库最后修改的时间
  private String date = "1970-01-01 08:00:00";

  @Override
  public void run() {
    try {
      String newDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(file.lastModified());
      if (!date.equals(newDate) && getWXStatus()) {
        date = newDate;
        java.lang.Process wx = Runtime.getRuntime().exec(workPath + "\\" + exeName);
        // System.out.println(exeName + "消息最新更新于：" + date );//测试、预留、备用
        Thread.sleep(1000);
        if (wx.isAlive()) {
          wx.destroy();
        }
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
  /** 这个方法用来获取微信、企业为的工作和存档路径 实现原理为读取注册表的内容 */
  private void getPath() {
    String[] workPath = getRunCmdResult(getWorkPathCmd);
    String[] filesSavePath = getRunCmdResult(getFilesSavePathCmd);
    // 判断然后再给变量赋值，得到运行目录，和存档目录。
    if (workPath != null && workPath.length >= 3) {
      if (workPath[0].equals(workPathPrefix)) {
        if (workPath[2].endsWith("exe")) {
          workPath[2] = workPath[2].substring(0, workPath[2].lastIndexOf("\\"));
        }
        this.workPath = workPath[2];
      }
    }
    if (filesSavePath != null && workPath.length >= 3) {
      if (filesSavePath[0].equals(filesSavePathPrefix)) {
        if (filesSavePath[2].endsWith("exe")) {
          filesSavePath[2] = filesSavePath[2].substring(0, filesSavePath[2].lastIndexOf("\\"));
        }
        this.filesSavePath = filesSavePath[2];
      }
    }
  }
  /** 因为有些是改过默认存档位置的，所以直接从注册表获取方便。 这个方法用来获取当前系统用户的文档位置 @Return 文档的位置 */
  private String getUsersDocumentsPath() {
    String[] strs =
        getRunCmdResult(
            "reg query \"HKEY_CURRENT_USER\\SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Explorer\\User Shell Folders\" | findstr \"Personal\"");
    if (strs != null && strs.length >= 3) {
      if (strs[2].startsWith("%USERPROFILE%")) {
        return System.getProperty("user.home") + "\\Documents";
      }
      return strs[2];
    }
    return System.getProperty("user.home") + "\\Documents";
  }

  /**
   * 这个方法用来执行CMD命令 @Param cmd cmd命令,最好配合findstr命令使用,因为此方法只返回一行数据
   *
   * @return 执行结果,此方法只返回一行数据,命令不对、结果为空，返回null
   */
  private String[] getRunCmdResult(String cmd) {
    try {
      java.lang.Process pos = Runtime.getRuntime().exec("cmd /c " + cmd);
      InputStreamReader inp = new InputStreamReader(pos.getInputStream());
      BufferedReader buf = new BufferedReader(inp);
      String str;
      if ((str = buf.readLine()) != null) {
        str = str.trim();
        if (pos.isAlive()) {
          pos.destroy();
        }
        return str.split("    ");
      }
      if (pos.isAlive()) {
        pos.destroy();
      }
      return null;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
  /**
   * 获取微信、企业微信运行状态
   *
   * @return true表示正在运行，false表示未在运行
   */
  public boolean getWXStatus() {
    return getExeStatus(exeName);
  }
  /**
   * 获取程序的运行状态
   *
   * @param name 程序名字
   * @return true表示正在运行，false表示未在运行
   */
  private boolean getExeStatus(String name) {
    String[] strings = getRunCmdResult("tasklist | findstr \" " + exeName + "\"");
    if (strings != null) {
      return true;
    }
    return false;
  }
  /**
   * 获取微信加密数据库文件的绝对路径
   *
   * @param filePath 微信的安装目录
   */
  private void getWeChatTargetFile(String filePath) {
    File workDir = new File(filePath);
    if (workDir.isDirectory()) {
      File[] files = workDir.listFiles();
      for (File file1 : files) {
        if (file1.isDirectory()) {
          getWeChatTargetFile(file1.getAbsolutePath());
        } else if (String.valueOf(file1.getName()).startsWith("wxid_")) {
          getWeChatTargetFile(file1.getAbsolutePath());
        } else if ("Msg".equals(String.valueOf(file1.getName()))) {
          getWeChatTargetFile(file1.getAbsolutePath());
        } else if ("Multi".equals(String.valueOf(file1.getName()))) {
          getWeChatTargetFile(file1.getAbsolutePath());
        } else if (weChatTargetFile.equals(String.valueOf(file1.getName()))) {
          file = file1;
          return;
        }
      }
    }
  }

  /**
   * 用来获取企业微信的加密数据库文件的绝对路径
   *
   * @param filePath 企业微信的安装目录
   */
  private void getWXWorkTargetFile(String filePath) {
    File workDir = new File(filePath);
    if (workDir.isDirectory()) {
      File[] files = workDir.listFiles();
      for (File file1 : files) {
        boolean b = false;
        // 判断文件夹名字是不是数字
        try {
          Long.valueOf(String.valueOf(file1.getName()));
          b = true;
        } catch (NumberFormatException e) {
        }
        if (file1.isDirectory()) {
          getWXWorkTargetFile(file1.getAbsolutePath());
        } else if (b) {
          getWXWorkTargetFile(file1.getAbsolutePath());
        } else if ("Data".equals(String.valueOf(file1.getName()))) {
          getWXWorkTargetFile(file1.getAbsolutePath());
        } else if (wXWorktargetFile.equals(String.valueOf(file1.getName()))) {
          file = file1;
          return;
        }
      }
    }
  }

  /** 无参构造方法 */
  public Tools() throws IOException {
    this(false);
  }

  /**
   * 有参构造
   *
   * @param isEnterprise true表示是企业微信，false表示是微信
   */
  public Tools(boolean isEnterprise) throws IOException {
    this.isEnterprise = isEnterprise;
    exeName = isEnterprise ? "WXWork.exe" : "WeChat.exe";
    String s =
        "reg query HKEY_CURRENT_USER\\SOFTWARE\\Tencent\\" + (isEnterprise ? "WXWork" : "WeChat");
    getWorkPathCmd = s + " | findstr \"" + (isEnterprise ? "Executable" : "InstallPath") + "\"";
    getFilesSavePathCmd =
        s + " | findstr \"" + (isEnterprise ? "DataLocationPath" : "FileSavePath") + "\"";
    filesSavePath = getUsersDocumentsPath() + "\\" + (isEnterprise ? "WXWork" : "WeChat Files");
    if (isEnterprise) {
      workPathPrefix = "Executable";
      filesSavePathPrefix = "DataLocationPath";
      workPath = "C:\\Program Files\\WXWork"; // 默认安装位置
      getPath();
      file = new File(filesSavePath);
      getWXWorkTargetFile(filesSavePath);
    } else {
      workPathPrefix = "InstallPath";
      filesSavePathPrefix = "FileSavePath";
      workPath = "C:\\Program Files (x86)\\Tencent\\WeChat"; // 默认安装位置
      getPath();
      file = new File(filesSavePath);
      getWeChatTargetFile(filesSavePath);
    }
  }

  public String getWorkPath() {
    return workPath;
  }

  public String getFilesSavePath() {
    return filesSavePath;
  }

  public File getFile() {
    return file;
  }
}
