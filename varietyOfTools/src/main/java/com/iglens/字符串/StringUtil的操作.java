package com.iglens.字符串;


import org.apache.commons.lang3.StringUtils;

public class StringUtil的操作 {
  public static void main(String[] args) {

    是否包含给定字符集中的任何字符();

    获取该截取条件之后字符串();
  }

  private static void 是否包含给定字符集中的任何字符() {
    // 是否包含给定字符集中的任何字符。
    String s = "abc";
    boolean s1 = StringUtils.containsAny(s, "a", "d");
    System.out.println(s1);// true

    // 是否包含给定字符集中的任何字符。(a和b之间有一个字符相同就回返回true）
    String a = "航天侦查轻薄1";
    String a1 = "航通报（zqkz）";
    boolean a11 = StringUtils.containsAny(a1, a);
    System.out.println(a11);
  }

  private static void 获取该截取条件之后字符串() {
    String a = "/2020/10/10";
    String a1 = StringUtils.substringAfter(a, "/");
    System.out.println(a1); // 2020/10/10
  }
}
