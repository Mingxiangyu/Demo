package com.iglens.时间;

import java.lang.management.ManagementFactory;
import java.util.Date;

public class 获取服务器启动时间 {

  /** 获取服务器启动时间 */
  public static Date getServerStartDate() {
    long time = ManagementFactory.getRuntimeMXBean().getStartTime();
    return new Date(time);
  }
}
