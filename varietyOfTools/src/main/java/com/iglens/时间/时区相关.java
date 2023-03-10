package com.iglens.时间;

import java.util.Date;
import java.util.TimeZone;

public class 时区相关 {

  public static final long SECOND = 1 * 1000;
  public static final long MINUTE = 60 * SECOND;
  public static final long HOUR = 60 * MINUTE;
  public static final long DAY = 24 * HOUR;
  public static final long WEEK = 7 * DAY;
  public static final TimeZone tz = TimeZone.getDefault();

  /**
   * 获取当前系统时区.
   */
  public static final int getTimezone() {
    return (int) (时区相关.tz.getRawOffset() / 时区相关.HOUR);
  }

  /**
   * 将本地时间转换成指定时区的时间.
   */
  public static final long changeLocalTimeZone(long ts /* 本地时间, 毫秒. */,
      int gmt /* 指定时区偏移, 小时 . */) {
    return (ts - 时区相关.tz.getRawOffset() /* 回归零时区. */) + (gmt * 时区相关.HOUR);
  }

  /**
   * 将本地时间转换成指定时区的时间.
   */
  public static final Date changeLocalTimeZone2date(long ts /* 本地时间, 毫秒. */,
      int gmt /* 指定时区偏移, 小时 . */) {
    return new Date(时区相关.changeLocalTimeZone(ts, gmt));
  }

  /**
   * 返回当前时间在零时区的绝对时间.
   */
  public static final Date nowGmt0() {
    return new Date(System.currentTimeMillis() - 时区相关.tz.getRawOffset() /* 回归零时区. */);
  }

  /**
   * 将指定GM+0时间回归到GMT+x.
   */
  public static final Date gotoGmtxOld(Date date /* 具有gmt0时区的绝对时间. */, int gmt /* 要返回的时区. */) {
    return new Date(date.getTime() + gmt * 时区相关.HOUR);
  }

  /**
   * 将指定时间回归到GMT+0.
   */
  public static final Date gotoGmt0Old(Date date /* 具有gmt时区的绝对时间. */, int gmt /* date的时区. */) {
    return new Date((date.getTime() - gmt * 时区相关.HOUR));
  }

  /**
   * 将本地时区绝对时间转换成目标时区的绝对时间.
   */
  public static final Date gotoGmtx(long ts /* 本时绝对时间. */, int gmtSec /* 要返回的时区(秒) */) {
    return new Date(
        (ts - 时区相关.tz.getRawOffset() /* 去零时区. */) + (gmtSec * 时区相关.SECOND /* 去目标时区. */));
  }

  /**
   * 将指定GMT+x时间回归到GMT+0.
   */
  public static final Date gmtxGoto0(Date date /* 具有gmtSec时区的绝对时间. */, int gmtSec /* date的时区. */) {
    return new Date((date.getTime() - gmtSec * 时区相关.SECOND));
  }

  /**
   * 将指定GM+0时间回归到GMT+x.
   */
  public static final Date gmt0Gotox(Date date /* 具有gmt0时区的绝对时间. */, int gmtSec /* 要返回的时区(秒). */) {
    return new Date(date.getTime() + gmtSec * 时区相关.SECOND);
  }

  /**
   * 本地时间去零时区.
   */
  public static final Date gotoGmt0(Date date /* 具有本地时区的时间 */) {
    return new Date(date.getTime() - 时区相关.tz.getRawOffset());
  }

  /**
   * 零时区时间去本地时区.
   */
  public static final Date gotoLocal(Date date/* 具有0时区的时间. */) {
    return new Date(date.getTime() + 时区相关.tz.getRawOffset());
  }
}
