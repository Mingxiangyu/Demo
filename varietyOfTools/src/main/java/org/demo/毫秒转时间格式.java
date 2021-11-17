package org.demo;

public class 毫秒转时间格式 {

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
