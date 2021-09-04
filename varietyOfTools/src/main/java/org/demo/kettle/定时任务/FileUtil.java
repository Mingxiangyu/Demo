package org.demo.kettle.定时任务;

import java.net.URL;

public class FileUtil {

  /**
   * 获取资源文件地址
   *
   * @param fullFileName
   * @return
   */
  public static String getResourceFilePath(String fullFileName) {
    URL url = FileUtil.class.getResource(fullFileName);
    String path = url.getPath();
    return path;
  }
}
