package org.demo;

import java.io.Closeable;
import java.util.Objects;

/**
 * @author T480S
 */
public class 多个流关闭的简单方法 {

  /** 多个流关闭的简单方法 */
  private void close(Closeable... closeables) {

    // 空直接返回
    if (closeables == null || closeables.length == 0) {
      return;
    }

    // 循环关闭流
    for (Closeable closeable : closeables) {
      try {
        if (Objects.nonNull(closeable)) {
          closeable.close();
        }
      } catch (Exception e) {
        System.out.println("关闭流异常：{}" + e);
      }
    }
  }
}
