package com.iglens;

public class 睡眠随机时间 {
  public static void main(String[] args) {
    sleepRandomly();
  }

  public static void sleepRandomly() {
    try {
      long millis = getMillis();
      System.out.println(millis);
      Thread.sleep(millis);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException(e);
    }
  }

  private static long getMillis() {
    return (long) (Math.random() * 20_000 + 30_000);
  }
}
