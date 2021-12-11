package com.iglens.图片;

import java.io.File;
/** 项目:Java编程实现查找指定文件夹及子文件夹中的图片 时间:2019/8/21 */
public class Java编程实现查找指定文件夹及子文件夹中的图片 {
  public static void picture(File dir) {
    if (dir.isFile()) {
      throw new IllegalArgumentException("不是文件夹");
    } else if (dir.isDirectory()) {
      File[] files = dir.listFiles();
      if (files == null) {
        return;
      }
      for (File file : files) {
        if (file.isFile()) {
          String name = file.getName();
          String k = name.substring(name.lastIndexOf('.') + 1);
          k = k.toLowerCase();
          if (k.matches("jpg|jpeg|gif|png|bmp")) {
            System.out.println(file.getPath());
          }

        } else if (file.isDirectory()) {
          picture(file);
        } else {
          // TODO

        }
      }

    } else {
      throw new IllegalArgumentException("既不是文件也不是文件夹");
    }
  }

  public static void main(String[] args) {
    picture(new File("E:\\Deploy-Track\\数据\\主要港口信息\\埃弗里特海军基地"));
  }
}
