package com.iglens.时间;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

public class LocalDate获取本月第一天以及最后一天 {

  public static void main(String[] args) {
    LocalDate currDate = LocalDate.now();

    // 当月第一天
    LocalDate firstDayOfMonth = currDate.with(TemporalAdjusters.firstDayOfMonth());

    // 当月最后一天
    LocalDate lastDayOfMonth = currDate.with(TemporalAdjusters.lastDayOfMonth());
  }
}
