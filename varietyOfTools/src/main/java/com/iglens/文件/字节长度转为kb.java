package com.iglens.文件;

import cn.hutool.core.io.unit.DataSizeUtil;

public class 字节长度转为kb {

  /**
   * 字节转kb/mb/gb
   *
   * @param size
   * @return
   */
  public static String getPrintSize(long size) {
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
      return (size / 100) + "." + (size % 100) + "GB";
    }
  }

  public static void main(String[] args) {

    // 通过hutool实现
    long b = 1L;
    long kb = 1024L;
    long mb = 1048576L;
    long gb = 1073741842L;
    long tb = 1099511627776L;
    System.out.println("1b:" + DataSizeUtil.format(b));
    System.out.println("1kb:" + DataSizeUtil.format(kb));
    System.out.println("1mb:" + DataSizeUtil.format(mb));
    System.out.println("1gb:" + DataSizeUtil.format(gb));
    String format = DataSizeUtil.format(tb);
    System.out.println("1tb:" + format);

    long parse = DataSizeUtil.parse("1MB");
    System.out.println(parse);
  }
}
