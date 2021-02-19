package org.demo.数学;

import java.util.Random;

public class 生成随机数 {
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

  public static void main(String[] args) {
    randNum(100,98);
  }

  private static void randNum(int max, int min) {
    int ran2 = (int) (Math.random() * (max - min) + min);
    System.out.println(ran2);
  }

}
