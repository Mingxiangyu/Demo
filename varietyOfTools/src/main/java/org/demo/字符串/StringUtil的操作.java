package org.demo.字符串;


import org.apache.commons.lang3.StringUtils;

public class StringUtil的操作 {
  public static void main(String[] args) {
    String s = "abc";
    boolean b = StringUtils.containsAny(s, "a", "d");
    System.out.println(b);// true

    获取该截取条件之后字符串();
  }

  private static void 获取该截取条件之后字符串() {
    String a = "/2020/10/10";
    String a1 = StringUtils.substringAfter(a, "/");
    System.out.println(a1); // 2020/10/10
  }
}
