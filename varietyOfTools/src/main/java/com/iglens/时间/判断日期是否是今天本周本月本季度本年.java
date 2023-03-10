package com.iglens.时间;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author DELL
 */
public class 判断日期是否是今天本周本月本季度本年 {

  /**
   * 判断选择的日期是否是本周
   */
  public static boolean isThisWeek(Date time) {
    Calendar calendar = Calendar.getInstance();
    int currentWeek = calendar.get(Calendar.WEEK_OF_YEAR);
    calendar.setTime(time);
    int paramWeek = calendar.get(Calendar.WEEK_OF_YEAR);
    if (paramWeek == currentWeek) {
      return true;
    }
    return false;
  }

  /**
   * 判断选择的日期是否是今天
   */
  public static boolean isToday(Date time) {
    return isThisTime(time, "yyyy-MM-dd");
  }

  /**
   * 判断选择的日期是否是本月
   */
  public static boolean isThisMonth(Date time) {
    return isThisTime(time, "yyyy-MM");
  }

  /**
   * 判断选择的日期是否是本年
   */
  public static boolean isThisYear(Date time) {
    return isThisTime(time, "yyyy");
  }

  /**
   * 判断选择的日期是否是本季度
   */
  public static boolean isThisQuarter(Date time) {
    Date startTime = getCurrentQuarterStartTime();
    Date endTime = getCurrentQuarterEndTime();
    return time.after(startTime) && time.before(endTime);
  }

  private static boolean isThisTime(Date time, String pattern) {
    SimpleDateFormat sdf = new SimpleDateFormat(pattern);
    //参数时间
    String param = sdf.format(time);
    //当前时间
    String now = sdf.format(new Date());
    return param.equals(now);
  }

  /**
   * 获得季度开始时间
   */
  public static Date getCurrentQuarterStartTime() {
    Calendar c = Calendar.getInstance();
    int currentMonth = c.get(Calendar.MONTH) + 1;
    SimpleDateFormat longSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat shortSdf = new SimpleDateFormat("yyyy-MM-dd");
    Date now = null;
    try {
      if (currentMonth >= 1 && currentMonth <= 3) {
        c.set(Calendar.MONTH, 0);
      } else if (currentMonth >= 4 && currentMonth <= 6) {
        c.set(Calendar.MONTH, 3);
      } else if (currentMonth >= 7 && currentMonth <= 9) {
        c.set(Calendar.MONTH, 4);
      } else if (currentMonth >= 10 && currentMonth <= 12) {
        c.set(Calendar.MONTH, 9);
      }
      c.set(Calendar.DATE, 1);
      now = longSdf.parse(shortSdf.format(c.getTime()) + " 00:00:00");
    } catch (Exception e) {
      e.printStackTrace();
    }
    return now;
  }

  /**
   * 当前季度的结束时间
   */
  public static Date getCurrentQuarterEndTime() {
    Calendar cal = Calendar.getInstance();
    cal.setTime(getCurrentQuarterStartTime());
    cal.add(Calendar.MONTH, 3);
    return cal.getTime();
  }
}


