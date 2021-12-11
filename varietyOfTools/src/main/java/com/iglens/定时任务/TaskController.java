package com.iglens.定时任务;

import com.iglens.http.获取请求IP地址;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TaskController {
  @Autowired private SysJobMapper sysJobMapper;

  @Autowired private CronTaskRegistrar cronTaskRegistrar;

  @Autowired private HttpServletRequest request; // 测试获取当前请求

  @PostMapping("/addTask")
  public String addTask(SysJobPO sysJob) {
    String ipAddr = 获取请求IP地址.getIpAddr(request); // 获取当前请求ip地址
    boolean success = sysJobMapper.addSysJob(sysJob);
    if (!success) {
      return "新增失败";
    } else {
      if (sysJob.getJobStatus().equals(SysJobStatus.NORMAL.index())) {
        SchedulingRunnable task =
            new SchedulingRunnable(
                sysJob.getBeanName(), sysJob.getMethodName(), sysJob.getMethodParams());
        // 注册定时任务
        cronTaskRegistrar.addCronTask(task, sysJob.getCronExpression());
      }
    }
    return "SUCCESS";
  }

  @DeleteMapping("/deleteTask/{jobId}")
  public String deleteTask(@PathVariable Integer jobId) {
    SysJobPO existJob = sysJobMapper.findTaskJobByJobId(jobId);
    boolean success = sysJobMapper.deleteTaskJobByJobId(jobId);
    if (!success) {
      return "删除失败";
    } else {
      if (existJob.getJobStatus().equals(SysJobStatus.NORMAL.index())) {
        SchedulingRunnable task =
            new SchedulingRunnable(
                existJob.getBeanName(), existJob.getMethodName(), existJob.getMethodParams());
        // 删除定时任务
        cronTaskRegistrar.removeCronTask(task);
      }
    }
    return "SUCCESS";
  }

  /**
   * 修改定时任务
   *
   * @param sysJob
   * @return
   */
  @PostMapping("/updateTask")
  public String updateTaskJob(SysJobPO sysJob) {
    SysJobPO existJob = sysJobMapper.findTaskJobByJobId(sysJob.getJobId());
    boolean success = sysJobMapper.updateTaskJob(sysJob);
    if (!success) return "修改成功";
    else {
      // 1. 先删除原来的定时任务（Map缓存）
      if (existJob.getJobStatus().equals(SysJobStatus.NORMAL.index())) {
        SchedulingRunnable task =
            new SchedulingRunnable(
                existJob.getBeanName(), existJob.getMethodName(), existJob.getMethodParams());
        cronTaskRegistrar.removeCronTask(task);
      }
      // 2. 新增定时任务（放到Map缓存中）
      if (sysJob.getJobStatus().equals(SysJobStatus.NORMAL.index())) {
        SchedulingRunnable task =
            new SchedulingRunnable(
                sysJob.getBeanName(), sysJob.getMethodName(), sysJob.getMethodParams());
        cronTaskRegistrar.addCronTask(task, sysJob.getCronExpression());
      }
    }
    return "SUCCESS";
  }

  /** 启，停定时任务的状态切换 */
  @GetMapping("/trigger/{jobId}")
  public String triggerTaskJob(@PathVariable Integer jobId) {
    SysJobPO existJob = sysJobMapper.findTaskJobByJobId(jobId);
    // 1.如果原先是启动状态 ，那么就停止吧（从Map缓存中删除， 并将表中状态置为0）
    if (existJob.getJobStatus().equals(SysJobStatus.NORMAL.index())) {
      SchedulingRunnable task =
          new SchedulingRunnable(
              existJob.getBeanName(), existJob.getMethodName(), existJob.getMethodParams());
      cronTaskRegistrar.removeCronTask(task);
      existJob.setJobStatus(0);
      sysJobMapper.updateTaskJob(existJob);
    } else {
      SchedulingRunnable task =
          new SchedulingRunnable(
              existJob.getBeanName(), existJob.getMethodName(), existJob.getMethodParams());
      cronTaskRegistrar.addCronTask(task, existJob.getCronExpression());
      existJob.setJobStatus(1);
      sysJobMapper.updateTaskJob(existJob);
    }
    return "SUCCESS";
  }
}
