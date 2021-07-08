package org.demo.文件;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class 获取文件的创建时间 {
  public static void main(String[] args) {
    String path = "C:\\Users\\T480S\\Desktop\\模板数据结构.xlsx";
    Path p = Paths.get(path);

    try {
      BasicFileAttributes att = Files.readAttributes(p, BasicFileAttributes.class); // 获取文件的属性
      // 创建时间
      long creationTime = att.creationTime().toMillis();
      System.out.println(creationTime);

      // 最后访问时间
      long lastAccessTime = att.lastAccessTime().toMillis();
      System.out.println(lastAccessTime);

      // 最后修改时间
      long lastModifiedTime = att.lastModifiedTime().toMillis();
      System.out.println(lastModifiedTime);

      long milliSecond = lastModifiedTime;
      Date date = new Date();
      date.setTime(milliSecond);
      Calendar cal = Calendar.getInstance();
      cal.setTime(date);
      Integer year = cal.get(Calendar.YEAR);
      int month = cal.get(Calendar.MONTH) + 1; // 第一个月从0开始，所以得到月份＋1
      int day = cal.get(Calendar.DAY_OF_MONTH);
      System.out.println("year: " + year + " month: " + month + " day: " + day);
      System.out.println(new SimpleDateFormat().format(date));
      //      原文链接：https://blog.csdn.net/li_canhui/article/details/88173766

    } catch (IOException e1) {

      e1.printStackTrace();
    }
  }
}
