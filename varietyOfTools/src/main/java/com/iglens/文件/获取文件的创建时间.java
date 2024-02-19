package com.iglens.文件;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.IdentityHashMap;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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



  /**
   * 获取文件集合
   *
   * @param file 根路径
   * @param pattern 日期格式，保证和前台创建任务时格式一致
   */
  private IdentityHashMap<String, File> getFileResult(File file, String pattern) {
    if (!file.exists()) {
      log.error("获取本地磁盘文件时，路径不存在：" + file.getPath());
      return new IdentityHashMap<>();
    }
    IdentityHashMap<String, File> result = new IdentityHashMap<>();

    if (file.isDirectory()) {
      File[] files = file.listFiles();
      if (files == null) {
        return result;
      }
      for (File f : files) {
        result.putAll(getFileResult(f, pattern));
      }
    } else {
      // 获取文件修改时间
      Instant instant = this.getFileCreateInstant(file);
      String format = DateTimeFormatter.ofPattern(pattern).withZone(ZoneId.systemDefault())
          .format(instant);
      result.put(format, file);
    }
    return result;
  }

  private Instant getFileCreateInstant(File file) {
    Path path = Paths.get(file.getAbsolutePath());
    // 根据path获取文件的基本属性类
    BasicFileAttributes attrs = null;
    try {
      attrs = Files.readAttributes(path, BasicFileAttributes.class);
    } catch (IOException e) {
      log.error(e.getMessage(), e);
    }
    // 从基本属性类中获取文件修改时间
    return attrs.lastModifiedTime().toInstant();
  }
}
