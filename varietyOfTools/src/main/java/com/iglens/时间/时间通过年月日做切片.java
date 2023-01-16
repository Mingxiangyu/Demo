package com.iglens.时间;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class 时间通过年月日做切片 {

  public static void main(String[] args) {
    List<String> strings = sliceUpDateRange("2022-01", "2022-12", 2);
    System.out.println(strings);
  }

  /**
   * 日期范围 - 切片
   *
   * @param startDate 起始日期
   * @param endDate 结束日期
   * @param type 切片类型 1-年 2-月 3-日 4-小时
   * @return 切片日期
   */
  public static List<String> sliceUpDateRange(String startDate, String endDate, int type) {
    List<String> rs = new ArrayList<>();

    try {
      int dt = Calendar.DATE;
      String pattern = "yyyy-MM-dd";
      if (type == 1) {
        pattern = "yyyy";
        dt = Calendar.YEAR;
      } else if (type == 2) {
        pattern = "yyyy-MM";
        dt = Calendar.MONTH;
      } else if (type == 3) {
        pattern = "yyyy-MM-dd";
        dt = Calendar.DATE;
      } else if (type == 4) {
        pattern = "yyyy-MM-dd HH";
        dt = Calendar.HOUR_OF_DAY;
      }
      SimpleDateFormat sd = new SimpleDateFormat(pattern);
      Calendar sc = Calendar.getInstance();
      Calendar ec = Calendar.getInstance();
      sc.setTime(sd.parse(startDate));
      ec.setTime(sd.parse(endDate));
      while (sc.compareTo(ec) < 1) {
        rs.add(sd.format(sc.getTime()));
        sc.add(dt, 1);
      }
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return rs;
  }
}
