package cn.demo.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

/**
 * 定时任务并行执行配置类
 **/
@Configuration
@EnableScheduling
public class ScheduledConfig implements SchedulingConfigurer {

  @Override
  public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
    TaskScheduler scheduler = this.taskScheduler();
    scheduledTaskRegistrar.setTaskScheduler(scheduler);
  }

  @Bean(destroyMethod = "shutdown")
  public ThreadPoolTaskScheduler taskScheduler() {
    ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
    scheduler.setPoolSize(5);
    scheduler.setThreadNamePrefix("task-");
    scheduler.setAwaitTerminationSeconds(60);
    scheduler.setWaitForTasksToCompleteOnShutdown(true);
    return scheduler;
  }
}

