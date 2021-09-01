package org.demo.txt;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class 一次读取txt内所有内容 {
  public static void main(String[] args) {
    String filePath = "C:\\Users\\T480S\\Desktop\\新建文本文档.txt";
    String string = readToString(filePath);
    System.out.println(string);
  }

  public static String readToString(String filePath) {
    File file = new File(filePath);
    String encoding = 获取TXT文本编码格式.getTextFileEncode(file);
    long filelength = file.length();
    byte[] filecontent = new byte[(int) filelength];
    try {
      FileInputStream in = new FileInputStream(file);
      in.read(filecontent);
      in.close();
      return new String(filecontent, encoding);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }
}
