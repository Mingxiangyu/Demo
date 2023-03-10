package com.iglens.时间;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

public class 获取间隔的日期列表 {

  /**
   * 获取间隔的日期列表
   *
   * @param preDate 开始日期(yyyy-MM-dd格式)
   * @param endDate 截止日期(yyyy-MM-dd格式)
   */
  private List<LocalDate> getRangeDays(Date preDate, Date endDate) {
    LocalDate pDate = preDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    LocalDate eDate = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    return getRangeDays(pDate, eDate);
  }

  /**
   * 获取间隔的日期列表
   *
   * @param preDate 开始日期
   * @param endDate 截止日期
   */
  private List<LocalDate> getRangeDays(LocalDate preDate, LocalDate endDate) {
    List<LocalDate> dates = new ArrayList<>();
    // 间隔的天数
    long betweenDays = ChronoUnit.DAYS.between(preDate, endDate);
    if (betweenDays < 1) {
      // 开始日期<=截止日期
      return dates;
    }
    // 创建一个从开始日期、每次加一天的无限流，限制到截止日期为止
    Stream.iterate(preDate, c -> c.plusDays(1))
        .limit(betweenDays + 1)
        .forEach(dates::add);
    return dates;
  }
}
