package com.iglens.文件;

import java.io.UnsupportedEncodingException;

public class 读取jar包的所在目录 {

  public static void main(String[] args) {
    读取jar包的所在目录 s = new 读取jar包的所在目录();
    s.test();
  }

  public void test() {
    String
        path = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
    path = path.substring(1, path.length());
    int endIndex = path.lastIndexOf("/");
    path = path.substring(0, endIndex);
    try {
      path = java.net.URLDecoder.decode(path, "utf-8");
    } catch (UnsupportedEncodingException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    System.out.println(path);
  }

}
