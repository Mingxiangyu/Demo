package com.iglens.字符串;

import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 * @author T480S
 */
public class 字符串集合拼接为字符串 {

  public static void main(String[] args) {
    List<String> strings = string2List(",");
    System.out.println(strings);
    String string = list2String(strings);
    System.out.println(string);
  }

  /**
   * 字符串转为集合
   *
   * @param regex 截取符
   * @return 集合
   */
  public static List<String> string2List(String regex) {
    // 字符串
    String str = "篮球,足球,,\\n,,排球";
    // 用逗号将字符串分开，得到字符串数组
    String[] strs = str.split(regex);
    // 将字符串数组转换成集合
    return Arrays.asList(strs);
  }

  /**
   * 把集合转换成字符串
   *
   * @param list 集合
   * @return 字符串集合
   */
  public static String list2String(List<String> list) {
    return StringUtils.join(list, "");
  }
}
