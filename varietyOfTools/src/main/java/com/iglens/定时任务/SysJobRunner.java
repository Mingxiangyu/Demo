package com.iglens.定时任务;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class SysJobRunner implements CommandLineRunner {

  private static final Logger logger = LoggerFactory.getLogger(SysJobRunner.class);

  @Autowired
  private SysJobMapper sysJobMapper;

  @Autowired
  private CronTaskRegistrar cronTaskRegistrar;

  @Override
  public void run(String... args) {
    // 初始加载数据库里状态为正常的定时任务
    List<SysJobPO> jobList = sysJobMapper.getSysJobListByStatus(SysJobStatus.NORMAL.index());
    if (!CollectionUtils.isEmpty(jobList)) {
      for (SysJobPO job : jobList) {
        SchedulingRunnable task = new SchedulingRunnable(job.getBeanName(), job.getMethodName(), job.getMethodParams());
        cronTaskRegistrar.addCronTask(task, job.getCronExpression());
      }
      logger.info("定时任务已加载完毕...");
    }
  }
}