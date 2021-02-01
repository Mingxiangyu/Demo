package org.demo.file.image;
/**
 *项目:Java编程实现查找指定文件夹及子文件夹中的图片
 *时间:2019/8/21
 *作者:郑翰林
 */
import java.io.File;
public class Java编程实现查找指定文件夹及子文件夹中的图片 {
  public final static void  picture(File dir) {
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

        }
      }

    } else {
      throw new IllegalArgumentException("既不是文件也不是文件夹");
    }

  }

  public static void main(String[] args) {
    picture(new File("E:\\Track相关\\数据\\主要港口信息\\阿普拉海军基地"));
  }
}
