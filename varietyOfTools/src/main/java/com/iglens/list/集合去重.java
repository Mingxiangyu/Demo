package com.iglens.list;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

public class 集合去重 {


  /**
   * 使用 LinkedHashSet 去重 能去重又能保证集合的顺
   *
   * @param list
   */
  public static void method_3(List<Integer> list) {
    LinkedHashSet<Integer> set = new LinkedHashSet<>(list);
    System.out.println("去重集合:" + set);
  }

  /**
   * 使用 Stream 去重
   * @param list
   */
  public static void method_5(List<Integer> list) {
    list = list.stream().distinct().collect(Collectors.toList());
    System.out.println("去重集合:" + list);
  }
}
