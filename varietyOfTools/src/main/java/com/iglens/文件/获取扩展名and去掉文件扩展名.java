package com.iglens.文件;

import org.apache.commons.io.FilenameUtils;

public class 获取扩展名and去掉文件扩展名 {
  /*
   * Java文件操作 获取文件扩展名
   *
   *  Created on: 2011-8-2
   *      Author: blueeagle
   */
  public static String getExtensionName(String filename) {
    String ext = FilenameUtils.getExtension(filename).toLowerCase(); // FilenameUtils提供的获取扩展名方法

    if ((filename != null) && (filename.length() > 0)) {
      int dot = filename.lastIndexOf('.');
      if ((dot > -1) && (dot < (filename.length() - 1))) {
        return filename.substring(dot + 1);
      }
    }
    return filename;
  }
  /*
   * Java文件操作 获取不带扩展名的文件名
   *
   *  Created on: 2011-8-2
   *      Author: blueeagle
   */
  public static String getFileNameNoEx(String filename) {
    String baseName = FilenameUtils.getBaseName(filename); // FilenameUtils提供的获取没有扩展类型的文件名方法

    if ((filename != null) && (filename.length() > 0)) {
      int dot = filename.lastIndexOf('.');
      if ((dot > -1) && (dot < (filename.length()))) {
        return filename.substring(0, dot);
      }
    }
    return filename;
  }
}
