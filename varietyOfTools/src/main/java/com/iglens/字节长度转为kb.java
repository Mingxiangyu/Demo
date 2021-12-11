package com.iglens;

public class 字节长度转为kb {

  /**
   * 字节转kb/mb/gb
   * @param size
   * @return
   */
  public static  String getPrintSize(long size) {
    if (size < 1024) {
      return size + "B";
    } else {
      size = size / 1024;
    }
    if (size < 1024) {
      return size + "KB";
    } else {
      size = size / 1024;
    }
    if (size < 1024) {
      size = size * 100;
      return (size / 100) + "." + (size % 100) + "MB";
    } else {
      size = size * 100 / 1024;
      return (size / 100) + "."
          + (size % 100) + "GB";
    }
  }
}
