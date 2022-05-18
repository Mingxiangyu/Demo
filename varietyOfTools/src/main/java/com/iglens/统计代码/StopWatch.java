package com.iglens.统计代码;

import java.util.concurrent.TimeUnit;

public class StopWatch {
  public static void main(String[] args) throws InterruptedException {
    TraceWatch traceWatch = new TraceWatch();

    traceWatch.start("function1");
    TimeUnit.SECONDS.sleep(10); // 模拟业务代码
    traceWatch.stop();

    traceWatch.start("function2");
    TimeUnit.SECONDS.sleep(11); // 模拟业务代码
    traceWatch.stop();

    traceWatch.record("function1", 1); // 直接记录耗时

    System.out.println(traceWatch.getTaskMap());
  }
}
