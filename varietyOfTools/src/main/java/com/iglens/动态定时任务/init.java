package com.iglens.动态定时任务;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
 
@Component
public class init  implements CommandLineRunner {
 
    @Autowired
    private ScheduleJobService scheduleJobService;
 
    @Override
    public void run(String... args) throws Exception {
        System.out.println("开始珍惜");
        ScheduleResult scheduleResult = new ScheduleResult();
        scheduleResult.setBeanName("c1");
        scheduleResult.setMethodName("test1");
        scheduleResult.setCronExpression("0/25 * * * * *");
        scheduleResult.setJobStatus(1);
        scheduleResult.setMethodParams("test1");
        scheduleJobService.addScheduleJob(scheduleResult);
        scheduleJobService.findAllTask();
    }
}