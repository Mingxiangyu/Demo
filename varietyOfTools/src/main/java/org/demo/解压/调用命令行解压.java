package org.demo.解压;

import java.io.File;
import org.apache.commons.lang.StringUtils;

public class 调用命令行解压 {
  /**
   * 采用命令行方式解压文件
   *
   * @param zipFile 压缩文件
   * @param destDir 解压结果路径
   * @param cmdPath WinRAR.exe的路径，也可以在代码中写死
   * @return
   */
  public static boolean realExtract(File zipFile, String destDir, String cmdPath) {
    // 解决路径中存在/..格式的路径问题
    destDir = new File(destDir).getAbsoluteFile().getAbsolutePath();
    while (destDir.contains("..")) {
      String[] sepList = destDir.split("\\\\");
      StringBuilder destDirBuilder = new StringBuilder();
      for (int i = 0; i < sepList.length; i++) {
        if (!"..".equals(sepList[i]) && i < sepList.length - 1 && "..".equals(sepList[i + 1])) {
          i++;
        } else {
          destDirBuilder.append(sepList[i]).append(File.separator);
        }
      }
      destDir = destDirBuilder.toString();
    }

    boolean bool = false;
    if (!zipFile.exists()) {
      return false;
    }

    // 开始调用命令行解压，参数-o+是表示覆盖的意思
    if (StringUtils.isEmpty(cmdPath)) {
      cmdPath = "E:\\workspace\\operation-uprr-manage\\src\\main\\resources\\cmd\\WinRAR.exe";
    }
    String path = zipFile.getPath();
    System.out.println("path:" + path);
    String cmd = cmdPath + " X -o+ " + zipFile + " " + destDir;
    //    String cmd = cmdPath + " X " + path + " " + destDir;
    System.out.println(cmd);
    try {
      Process proc = Runtime.getRuntime().exec(cmd);
      if (proc.waitFor() != 0) {
        if (proc.exitValue() == 0) {
          bool = false;
        }
      } else {
        bool = true;
      }
    } catch (Exception e) {
      System.out.println("ooo");
      e.printStackTrace();
    }
    System.out.println("解压" + (bool ? "成功" : "失败"));
    return bool;
  }

  public static void main(String[] args) {
    //将文件名称中的所有空格都替换为带有双引号的空格
//    filePath = filePath.replaceAll(" ", "\" \"");
    // 需要解压的rar文件所在路径不能有空格，否则rar软件报没有找到压缩文件
    String zipFilePath = "E:\\Deploy-DJ\\运行环境\\DJXGApplication(1).rar";
    // 解压到的路径同样不能有空格，否则rar软件报没有可提取的文件
    String destDir = "E:\\Deploy-DJ\\运行环境";
    String cmdPath = "C:\\Program Files\\WinRAR\\Rar.exe";
    realExtract(new File(zipFilePath), destDir, cmdPath);
  }
}
