package com.iglens.list;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class 生成指定起始范围的有序集合 {
  public static void main(String[] args) {
    生成指定起始范围的有序集合(-1, 1, 12);
  }

  /**
   * 生成指定起始范围的有序集合
   *
   * @param startIndex 开始下标
   * @param Stepping 步进值
   * @param size 数量
   */
  public static void 生成指定起始范围的有序集合(int startIndex, int Stepping, int size) {
    if (size < 0) {
      throw new RuntimeException("数量不能为负数");
    }
    List<Integer> list =
        Stream.iterate(startIndex, item -> item + Stepping)
            .limit(size)
            .collect(Collectors.toList());
    System.out.println(list);
  }
}
