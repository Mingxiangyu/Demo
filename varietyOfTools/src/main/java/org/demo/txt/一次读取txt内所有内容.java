package org.demo.txt;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class 一次读取txt内所有内容 {
  public static void main(String[] args) {
    String filePath = "C:\\Users\\T480S\\Desktop\\新建文本文档.txt";
    String string = readToString(filePath);
    System.out.println(string);
  }

  public static String readToString(String filePath) {
    // TODO 写死编码后续会出现乱码问题
    String encoding = "UTF-8";
    File file = new File(filePath);
    long filelength = file.length();
    byte[] filecontent = new byte[(int) filelength];
    try {
      FileInputStream in = new FileInputStream(file);
      in.read(filecontent);
      in.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    try {
      return new String(filecontent, encoding);
    } catch (UnsupportedEncodingException e) {
      System.err.println("The OS does not support " + encoding);
      e.printStackTrace();
      return null;
    }
  }
}
