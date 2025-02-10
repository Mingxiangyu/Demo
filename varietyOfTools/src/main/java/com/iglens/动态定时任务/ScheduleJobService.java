package com.iglens.动态定时任务;

import java.text.SimpleDateFormat;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ScheduleJobService {
 
    @Autowired
    private CronTaskRegistrar cronTaskRegistrar;
 
    public void addScheduleJob(ScheduleResult scheduleResult) {
        long currentTimeMillis = System.currentTimeMillis();
        scheduleResult.setCreateTime(formatTimeYMD_HMS_SSS(currentTimeMillis));
        scheduleResult.setUpdateTime(formatTimeYMD_HMS_SSS(currentTimeMillis));
        scheduleResult.setJobId(findAllTask().size() + 1);
        if (scheduleResult.getJobStatus().equals(ScheduleJobStatus.NORMAL.ordinal())) {
            log.info("Stop or pause: is now on");
            cronTaskRegistrar.addCronTask(scheduleResult);
            return;
        }
        cronTaskRegistrar.getSchedulerJob().put(scheduleResult.getJobId(), scheduleResult);
    }
 
    public void editScheduleJob(ScheduleResult currentSchedule) {
        //先移除
        cronTaskRegistrar.removeCronTask(currentSchedule.getBeanName(), currentSchedule.getMethodName(), currentSchedule.getMethodParams());
        ScheduleResult pastScheduleJob = cronTaskRegistrar.getSchedulerByJobId(currentSchedule.getJobId());
        if (pastScheduleJob == null) {
            System.out.println("没有这个任务");
            return;
        }
        //然后判断是否开启, 如果开启的话，现在立即执行
        startOrStopSchedulerJob(currentSchedule, true);
    }
 
    public void deleteScheduleJob(ScheduleResult scheduleResult) {
        // 清除这个任务
        cronTaskRegistrar.removeCronTask(scheduleResult.getBeanName(), scheduleResult.getMethodName(), scheduleResult.getMethodParams());
        // 清除这个任务的数据
        cronTaskRegistrar.getSchedulerJob().remove(scheduleResult.getJobId());
    }
 
    public void startOrStopScheduler(ScheduleResult scheduleResult) {
        cronTaskRegistrar.getSchedulerJob().get(scheduleResult.getJobId()).setJobStatus(scheduleResult.getJobStatus());
        startOrStopSchedulerJob(scheduleResult, false);
    }
 
    private void startOrStopSchedulerJob(ScheduleResult scheduleResult, boolean update) {
        // 更新时间
        scheduleResult.setUpdateTime(formatTimeYMD_HMS_SSS(System.currentTimeMillis()));
        if (scheduleResult.getJobStatus().equals(ScheduleJobStatus.NORMAL.ordinal())) {
            System.out.println("停止或暂停：现在是开启");
            cronTaskRegistrar.addCronTask(scheduleResult);
            return;
        }
        System.out.println("停止或暂停：现在是暂停");
        if (update){
            cronTaskRegistrar.removeCronTask(scheduleResult);
            return;
        }
        cronTaskRegistrar.removeCronTask(scheduleResult.getBeanName(), scheduleResult.getMethodName(), scheduleResult.getMethodParams());
    }
 
    public List<ScheduleResult> findAllTask() {
        return cronTaskRegistrar.findAllTask();
    }
 
 
    // 转换为年-月-日 时:分:秒
    private String formatTimeYMD_HMS_SSS(long time) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS").format(time);
    }
}