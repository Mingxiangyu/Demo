package com.iglens.时间;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class 获取当前时间前一个小时的时间 {
  public static void main(String[] args) {
    beforeOneHourToNowDate();
  }

  /** 获取当前时间前一个小时的时间 */
  public static void beforeOneHourToNowDate() {
    Calendar c = Calendar.getInstance();
    // HOUR_OF_DAY 指一天中的小时
    c.set(Calendar.HOUR_OF_DAY, (c.get(Calendar.HOUR_OF_DAY) - 1));

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    System.out.println("当前系统时间：" + sdf.format(new Date()));
    System.out.println("一小时前的系统时间：" + sdf.format(c.getTime()));
  }
}
