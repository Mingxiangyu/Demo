package com.iglens.时间;

import java.util.Calendar;
import java.util.Date;

public class 计算两个时间差 {

  public static void main(String[] args) {
    Date date = new Date();

    //获取一小时前时间
    Calendar c = Calendar.getInstance();
    // HOUR_OF_DAY 指一天中的小时
    c.set(Calendar.HOUR_OF_DAY, (c.get(Calendar.HOUR_OF_DAY) - 1));
    Date time = c.getTime();

    String datePoor = getDatePoor(date, time);
    System.out.println(datePoor);

    int i = differentDaysByMillisecond(date, time);
    System.out.println(i);
  }

  /** 计算两个时间差 */
  public static String getDatePoor(Date endDate, Date nowDate) {
    long nd = 1000 * 24 * 60 * 60;
    long nh = 1000 * 60 * 60;
    long nm = 1000 * 60;
    // long ns = 1000;
    // 获得两个时间的毫秒时间差异
    long diff = endDate.getTime() - nowDate.getTime();
    // 计算差多少天
    long day = diff / nd;
    // 计算差多少小时
    long hour = diff % nd / nh;
    // 计算差多少分钟
    long min = diff % nd % nh / nm;
    // 计算差多少秒//输出结果
    // long sec = diff % nd % nh % nm / ns;
    return day + "天" + hour + "小时" + min + "分钟";
  }

  /** 计算相差天数 */
  public static int differentDaysByMillisecond(Date date1, Date date2) {
    return Math.abs((int) ((date2.getTime() - date1.getTime()) / (1000 * 3600 * 24)));
  }
}
