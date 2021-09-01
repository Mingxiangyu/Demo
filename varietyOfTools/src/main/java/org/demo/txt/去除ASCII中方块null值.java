package org.demo.txt;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.util.ArrayList;

public class 去除ASCII中方块null值 {
  public static void main(String[] args) {
    String s = "去除ASCII中方块null值";
    String s1 = trimAsciiNull(s);
    System.out.println(s1);
  }

  public static String trimAsciiNull(String line) {
    ArrayList<Byte> byteArrayList = new ArrayList<>();
    byte[] bytes = line.getBytes();
    for (byte aByte : bytes) {
      if (aByte!=0) {
        byteArrayList.add(aByte);
      }
    }

    byte[] result = new byte[byteArrayList.size()];
    for (int i = 0; i < byteArrayList.size(); i++) {
      result[i] = byteArrayList.get(i);
    }
    return new String(result, UTF_8);
  }
}
