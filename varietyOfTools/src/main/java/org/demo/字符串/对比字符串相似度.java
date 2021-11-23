package org.demo.字符串;

import cn.hutool.core.util.StrUtil;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class 对比字符串相似度 {
  public static void main(String[] args) {
    String a = "航天侦查轻薄1";
//    String a = "航天侦查轻薄";
    String a1 = "航天侦查轻薄（zqkz）";
//    String a1 = "航通报（zqkz）";
    double similar = StrUtil.similar(a, a1); // 用较大的字符串长度作为分母，相似子串作为分子计算出字串相似度
    System.out.println(similar);

    String s = countPercent(a, a1);
    System.out.println(s);
  }

  /**
   * str在str1中字符的出现率（忽略每个字符位置）
   *
   * @param str
   * @param str1
   * @return
   */
  public static String countPercent(String str, String str1) {
    DecimalFormat df = new DecimalFormat("0.00");
    int count = 0;
    Map<Character, Integer> map = new HashMap<>();
    char[] cs = str1.toCharArray();
    for (char c : cs) {
      map.put(c, (map.get(c) == null ? 1 : map.get(c) + 1));
    }
    for (int i = 0; i < str.length(); i++) {
      if (Objects.nonNull(map.get(str.charAt(i)))) {
        if (map.get(str.charAt(i)) != -1) {
          int s = map.get(str.charAt(i)) - 1;
          if (s == 0) {
            map.put(str.charAt(i), -1);
            count++;
          } else if (s > 0) {
            map.put(str.charAt(i), s);
            count++;
          }
        }
      }
    }

    double result = ((double) count / str1.length()) * 100;
    return df.format(result) + "%";
  }
}
