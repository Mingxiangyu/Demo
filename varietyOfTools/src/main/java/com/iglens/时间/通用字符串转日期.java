package com.iglens.时间;

import java.text.ParseException;
import java.util.Date;
import org.apache.commons.lang.time.DateUtils;

public class 通用字符串转日期 {

  private static String[] parsePatterns = {
    "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
    "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
    "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"
  };

  /** 日期型字符串转化为日期 格式 */
  public static Date parseDate(Object str) {
    if (str == null) {
      return null;
    }
    try {
      return DateUtils.parseDate(str.toString(), parsePatterns);
    } catch (ParseException e) {
      return null;
    }
  }
}
