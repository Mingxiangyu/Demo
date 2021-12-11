package com.iglens.字符串.截取字符串;

import static java.util.regex.Pattern.compile;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class 获取两个特定字符中间的字符串 {
  public static void main(String[] args) {
    String str = "【测试1】【测试2】";
    String regexPre = "【";
    String regexSuf = "】";
    List<String> data = getData(str, regexPre, regexSuf);
    System.out.println(data);
  }

  /**
   * 获取两个特定字符中间的字符串 <br>
   * "【测试1】【测试2】" [测试1, 测试2] <br>
   * "【测试1】" [测试1] <br>
   * "测试1】" []
   *
   * @param str doc中文本文档
   * @param regexPre 前特定符号
   * @param regexSuf 后特定符号
   * @return 需要的数据集合 例子为：（：111.33,114.6；）
   */
  public static List<String> getData(String str, String regexPre, String regexSuf) {
    String regex = regexPre + "(.*?)" + regexSuf;
    System.out.println("regex为：" + regex);
    Pattern p = compile(regex);
    Matcher m = p.matcher(str);
    List<String> stringList = new ArrayList<>();
    while (m.find()) {
      stringList.add(m.group(1));
      System.out.println(m.group(1));
    }
    return stringList;
  }
}
