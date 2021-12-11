package com.iglens.数学;

import java.util.Random;
import org.apache.commons.lang3.RandomStringUtils;

public class 生成随机数 {

  public static void main(String[] args) {
    //    randNum(100, 98);
    randomString();
  }

  /**
   * 生成[min,max]之间的随机整数
   *
   * @param min
   * @param max
   * @return
   */
  public static int randomNum(int min, int max) {

    Random random = new Random();
    int s = random.nextInt(max) % (max - min + 1) + min;
    return s;
  }

  private static void randNum(int max, int min) {
    int ran2 = (int) (Math.random() * (max - min) + min);
    System.out.println(ran2);
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
