package com.iglens.字符串;

import org.apache.commons.lang3.RandomStringUtils;

public class 生成随机字符串 {

  /**
   * 产生随机字符串
   *
   * @param length 字符串的长度
   * @return 随机的字符串
   */
  public static String generateString(int length) {
    if (length < 1) length = 6;
    String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    String genStr = "";
    for (int index = 0; index < length; index++) {
      genStr = genStr + str.charAt((int) ((Math.random() * 100) % 26));
    }
    return genStr;
  }


  /** 生成随机字符串 */
  public static void randomString() {
    // 创建一个长度为64个字符的随机数字字符串.
    String result = RandomStringUtils.random(64, false, true);
    System.out.println("创建一个长度为64个字符的随机数字字符串 = " + result);

    // 创建长度为64个字符的随机字母字符串.
    result = RandomStringUtils.randomAlphabetic(64);
    System.out.println("创建长度为64个字符的随机字母字符串 = " + result);

    // 创建长度为32个字符的随机ascii字符串.
    result = RandomStringUtils.randomAscii(32);
    System.out.println("创建长度为32个字符的随机ascii字符串 = " + result);

    // 根据定义的数组创建一个32个字符的字符串长度字符，包括数字和字母字符.
    result = RandomStringUtils.random(32, 0, 20, true, true, "qw32rfHIJk9iQ8Ud7h0X".toCharArray());
    System.out.println("根据定义的数组创建一个32个字符的字符串长度字符 = " + result);
  }
}
