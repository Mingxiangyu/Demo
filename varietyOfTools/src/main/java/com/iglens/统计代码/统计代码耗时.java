package com.iglens.统计代码;


import org.springframework.util.StopWatch;

/**
 * @author DELL 原文链接： {@link ‘https://blog.csdn.net/duleilewuhen/article/details/114379693’}
 */
public class 统计代码耗时 {

  /**
   * 该方法为spring提供，需引 import org.springframework.util.StopWatch; <br>
   * <p>
   *   注意事项
   * StopWatch对象不是设计为线程安全的，并且不使用同步。 <br>
   * 一个StopWatch实例一次只能开启一个task，不能同时start多个task <br>
   * 在该task还没stop之前不能start一个新的task，必须在该task stop之后才能开启新的task <br>
   * 若要一次开启多个，需要new不同的StopWatch实例 <br>
   * <p>
   * 构造器和一些关键方法 <br>
   * new StopWatch()	构建一个新的秒表，不开始任何任务。 <br>
   * new StopWatch(String id)	创建一个指定了id的StopWatch <br>
   * String getId()	返回此秒表的ID <br>
   * void start(String taskName)	不传入参数，开始一个无名称的任务的计时。 <br>
   * 传入String类型的参数来开始指定任务名的任务计时 <br>
   * void stop()	停止当前任务的计时 <br>
   * boolean isRunning()	是否正在计时某任务 <br>
   * String currentTaskName()	当前正在运行的任务的名称（如果有） <br>
   * long getTotalTimeMillis()	所有任务的总体执行时间(毫秒单位) <br>
   * double getTotalTimeSeconds()	所有任务的总时间（以秒为单位） <br>
   * String getLastTaskName()	上一个任务的名称 <br>
   * long getLastTaskTimeMillis()	上一个任务的耗时(毫秒单位) <br>
   * int getTaskCount()	定时任务的数量 <br>
   * String shortSummary()	总运行时间的简短描述 <br>
   * String prettyPrint()	优美地打印所有任务的详细耗时情况 <br>
   */
  public static void main(String[] args) throws InterruptedException {
    StopWatch stopWatch = new StopWatch();

    // 任务一模拟休眠3秒钟
    stopWatch.start("TaskOneName");
    Thread.sleep(1000 * 3);
    System.out.println("当前任务名称：" + stopWatch.currentTaskName());
    stopWatch.stop();

    // 任务一模拟休眠10秒钟
    stopWatch.start("TaskTwoName");
    Thread.sleep(1000 * 10);
    System.out.println("当前任务名称：" + stopWatch.currentTaskName());
    stopWatch.stop();

    // 任务一模拟休眠10秒钟
    stopWatch.start("TaskThreeName");
    Thread.sleep(1000 * 10);
    System.out.println("当前任务名称：" + stopWatch.currentTaskName());
    stopWatch.stop();

    // 打印出耗时
    System.out.println(stopWatch.prettyPrint());
    System.out.println(stopWatch.shortSummary());
    // stop后它的值为null
    System.out.println(stopWatch.currentTaskName());

    // 最后一个任务的相关信息
    System.out.println(stopWatch.getLastTaskName());
    System.out.println(stopWatch.getLastTaskInfo());

    // 任务总的耗时  如果你想获取到每个任务详情（包括它的任务名、耗时等等）可使用
    System.out.println("所有任务总耗时：" + stopWatch.getTotalTimeMillis());
    System.out.println("任务总数：" + stopWatch.getTaskCount());
    System.out.println("所有任务详情：" + stopWatch.getTaskInfo());
  }

  /**
   * 该方法为apache 提供，需引 import apache commons lang3
   * <p>
   * 构造器和一些关键方法 <br>
   * new StopWatch()	构建一个新的秒表，不开始任何任务。
   * static StopWatch createStarted()
   * void start()	开始计时
   * void stop()	停止当前任务的计时
   * void reset()	重置计时
   * void split()	设置split点
   * void unsplit()
   * void suspend()	暂停计时, 直到调用resume()后才恢复计时
   * void resume()	恢复计时
   * long getTime()	统计从start到现在的计时
   * long getTime(final TimeUnit timeUnit)
   * long getNanoTime()
   * long getSplitTime()	获取从start 到 最后一次split的时间
   * long getSplitNanoTime()
   * long getStartTime()
   * boolean isStarted()
   * boolean isSuspended()
   * boolean isStopped()
   */
  // public static void main(String[] args) throws InterruptedException {
  //   //创建后立即start，常用
  //   StopWatch watch = StopWatch.createStarted();
  //   // StopWatch watch = new StopWatch();
  //   // watch.start();
  //
  //
  //   Thread.sleep(1000);
  //   System.out.println(watch.getTime());
  //   System.out.println("统计从开始到现在运行时间：" + watch.getTime() + "ms");
  //
  //   Thread.sleep(1000);
  //   watch.split();
  //   System.out.println("从start到此刻为止的时间：" + watch.getTime());
  //   System.out.println("从开始到第一个切入点运行时间：" + watch.getSplitTime());
  //   Thread.sleep(1000);
  //   watch.split();
  //   System.out.println("从开始到第二个切入点运行时间：" + watch.getSplitTime());
  //
  //   // 复位后, 重新计时
  //   watch.reset();
  //   watch.start();
  //   Thread.sleep(1000);
  //   System.out.println("重新开始后到当前运行时间是：" + watch.getTime());
  //
  //   // 暂停 与 恢复
  //   watch.suspend();
  //   System.out.println("暂停2秒钟");
  //   Thread.sleep(2000);
  //
  //   // 上面suspend，这里要想重新统计，需要恢复一下
  //   watch.resume();
  //   System.out.println("恢复后执行的时间是：" + watch.getTime());
  //
  //   Thread.sleep(1000);
  //   watch.stop();
  //
  //   System.out.println("花费的时间》》" + watch.getTime() + "ms");
  //   // 直接转成s
  //   System.out.println("花费的时间》》" + watch.getTime(TimeUnit.SECONDS) + "s");
  // }
}
