package org.demo.时间;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/** @author T480S */
public class 获取当前天之后或之前X天日期 {

  /**
   * 日期格式
   */
  static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH");

  public static void main(String[] args) {
    List<String> test = test(7);
    System.out.println(test.size() + "\n" + test);
  }

  /**
   * 获取过去或者未来 任意天内的日期数组
   *
   * @param intervals intervals天内
   * @return 日期数组
   */
  public static List<String> test(int intervals) {
    List<String> allDayList = new ArrayList<>();
    ArrayList<String> pastDaysList = new ArrayList<>();
    ArrayList<String> fetureDaysList = new ArrayList<>();
    for (int i = 0; i < intervals; i++) {
      pastDaysList.add(getPastDate(i));
      fetureDaysList.add(getFetureDate(i));
    }
    allDayList.addAll(pastDaysList);
    allDayList.addAll(fetureDaysList);
    return allDayList;
  }

  /**
   * 获取过去第几天的日期
   *
   * @param past
   * @return
   */
  public static String getPastDate(int past) {
    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
    Date today = calendar.getTime();
    String result = format.format(today);
    return result;
  }

  /**
   * 获取未来 第 past 天的日期
   *
   * @param past
   * @return
   */
  public static String getFetureDate(int past) {
    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + past);
    Date today = calendar.getTime();
    String result = format.format(today);
    return result;
  }
}
