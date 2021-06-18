package org.demo.字符串;

import java.util.HashMap;
import java.util.Map;

/** @author T480S */
public class 根据变量表达式替换内容 {

  public static void main(String[] args) {
    String s = "{id}:123456";
    Map<String, String> map = new HashMap<>();
    map.put("id", "userId");
    s = replaceVariables(s, map);
    System.out.println(s);// userId:123456
  }
  /**
   * 根据变量表达式（例："{id}"或"{info.name}"）替换内容
   *
   * @param s 文字
   * @param map 替换的字符
   * @return 替换后的结果
   */
  public static String replaceVariables(String s, Map<String, String> map) {
    final String[] ss = new String[] {s};
    map.forEach(
        (key, value) ->
            ss[0] =
                ss[0].replaceAll("\\{\\s*" + key.replaceAll("\\.", "\\\\.") + "\\s*\\}", value));
    return ss[0];
  }
}
