package com.iglens.定时任务;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.config.CronTask;
import org.springframework.stereotype.Component;

/** 可参考spring 提供的定时任务注册类 {@link ScheduledTaskRegistrar} */
@Component
public class CronTaskRegistrar implements DisposableBean {

  /**
   * 缓存
   */
  private final Map<Runnable, ScheduledTask> scheduledTasks = new ConcurrentHashMap<>(16);

  @Autowired private TaskScheduler taskScheduler;

  public TaskScheduler getScheduler() {
    return this.taskScheduler;
  }
  /**
   * 添加一个定时任务
   * 其核心就是靠spring提供的 CronTask 类来实现
   *
   * @param task
   * @param cronExpression
   */
  public void addCronTask(Runnable task, String cronExpression) {
    addCronTask(new CronTask(task, cronExpression));
  }

  public void addCronTask(CronTask cronTask) {
    if (cronTask != null) {
      Runnable task = cronTask.getRunnable();
      if (this.scheduledTasks.containsKey(task)) {
        removeCronTask(task);
      }

      this.scheduledTasks.put(task, scheduleCronTask(cronTask));
    }
  }

  public void removeCronTask(Runnable task) {
    ScheduledTask scheduledTask = this.scheduledTasks.remove(task);
    if (scheduledTask != null) {
      scheduledTask.cancel();
    }
  }

  public ScheduledTask scheduleCronTask(CronTask cronTask) {
    ScheduledTask scheduledTask = new ScheduledTask();
    scheduledTask.future =
        this.taskScheduler.schedule(cronTask.getRunnable(), cronTask.getTrigger());

    return scheduledTask;
  }

  @Override
  public void destroy() {
    for (ScheduledTask task : this.scheduledTasks.values()) {
      task.cancel();
    }

    this.scheduledTasks.clear();
  }
}
