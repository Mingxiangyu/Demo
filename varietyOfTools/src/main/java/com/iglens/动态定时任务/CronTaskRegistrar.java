package com.iglens.动态定时任务;

import com.iglens.定时任务.ScheduledTask;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.config.CronTask;
import org.springframework.stereotype.Component;
 
/**
 * 添加定时任务注册类，用来增加、删除定时任务。
 */
@Component
public class CronTaskRegistrar implements DisposableBean {
 
    private final Map<Runnable, ScheduledTask> scheduledTasks = new ConcurrentHashMap<>(16);
    private final Map<Integer, ScheduleResult> schedulerJob = new HashMap<>();
 
    @Autowired
    private TaskScheduler taskScheduler;
 
    public TaskScheduler getScheduler() {
        return this.taskScheduler;
    }
 
    public void addCronTask(ScheduleResult scheduleResult) {
        SchedulingRunnable task = new SchedulingRunnable(scheduleResult.getBeanName(), scheduleResult.getMethodName(), scheduleResult.getMethodParams());
        String cronExpression = scheduleResult.getCronExpression();
 
        CronTask cronTask = new CronTask(task, cronExpression);
        // 如果当前包含这个任务，则移除
        if (this.scheduledTasks.containsKey(task)) {
            removeCronTask(scheduleResult.getBeanName(), scheduleResult.getMethodName(), scheduleResult.getMethodParams());
        }
        schedulerJob.put(scheduleResult.getJobId(), scheduleResult);
        this.scheduledTasks.put(task, scheduleCronTask(cronTask));
    }
 
    public void removeCronTask(String beanName, String methodName, String methodParams) {
        SchedulingRunnable task = new SchedulingRunnable(beanName, methodName, methodParams);
        ScheduledTask scheduledTask = this.scheduledTasks.remove(task);
        if (scheduledTask != null) {
            scheduledTask.cancel();
        }
    }
 
    public void removeCronTask(ScheduleResult scheduleResult) {
        schedulerJob.put(scheduleResult.getJobId(), scheduleResult);
        removeCronTask(scheduleResult.getBeanName(), scheduleResult.getMethodName(), scheduleResult.getMethodParams());
    }
 
    public ScheduledTask scheduleCronTask(CronTask cronTask) {
        ScheduledTask scheduledTask = new ScheduledTask();
        scheduledTask.future = this.taskScheduler.schedule(cronTask.getRunnable(), cronTask.getTrigger());
        return scheduledTask;
    }
 
    public Map<Runnable, ScheduledTask> getScheduledTasks() {
        return scheduledTasks;
    }
 
    public Map<Integer, ScheduleResult> getSchedulerJob() {
        return schedulerJob;
    }
 
    @Override
    public void destroy() {
        for (ScheduledTask task : this.scheduledTasks.values()) {
            task.cancel();
        }
 
        this.scheduledTasks.clear();
    }
 
    public ScheduleResult getSchedulerByJobId(Integer jobId) {
        for (ScheduleResult job : findAllTask()) {
            if (jobId.equals(job.getJobId())) {
                return job;
            }
        }
        return null;
    }
 
    public List<ScheduleResult> findAllTask() {
        List<ScheduleResult> ScheduleResults = new ArrayList<>();
        Set<Map.Entry<Integer, ScheduleResult>> entries = schedulerJob.entrySet();
        for (Map.Entry<Integer, ScheduleResult> en : entries) {
            ScheduleResults.add(en.getValue());
        }
        return ScheduleResults;
    }
}