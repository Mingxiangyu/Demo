package com.iglens.时间;

public class 毫秒转时间格式 {
  public static void main(String[] args) {
    String s = executeTime(System.currentTimeMillis());
    System.out.println(s);
  }

  /** 对下载所用的时间格式进行处理 */
  public static String executeTime(long time) {
    if (time < 1000) {
      return time + "ms";
    } else if (time < 1000 * 60) {
      return time / 1000 + "s";
    } else if (time < 1000 * 60 * 60) {
      return time / (1000 * 60) + "min";
    } else {
      return time / (1000 * 60 * 60) + "h";
    }
  }
}
