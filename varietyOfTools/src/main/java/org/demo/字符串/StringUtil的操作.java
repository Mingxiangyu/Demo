package org.demo.字符串;


import org.apache.commons.lang3.StringUtils;

public class StringUtil的操作 {
  public static void main(String[] args) {
    String s = "abc";
    boolean b = StringUtils.containsAny(s, "a", "d");
    System.out.println(b);// true


  }
}
