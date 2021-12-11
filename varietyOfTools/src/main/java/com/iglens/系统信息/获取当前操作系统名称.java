package com.iglens.系统信息;

public class 获取当前操作系统名称 {

  public static void main(String[] args) {
    System.out.println(isMac());
    System.out.println(isWindows());
    System.out.println(isWindows10());
  }

  public static boolean isMac() {
    return System.getProperties().getProperty("os.name").toUpperCase().indexOf("MAC OS") != -1;
  }

  public static boolean isWindows() {
    return System.getProperties().getProperty("os.name").toUpperCase().indexOf("WINDOWS") != -1;
  }

  public static boolean isWindows10() {
    return System.getProperties().getProperty("os.name").toUpperCase().indexOf("WINDOWS 10") != -1;
  }

}
