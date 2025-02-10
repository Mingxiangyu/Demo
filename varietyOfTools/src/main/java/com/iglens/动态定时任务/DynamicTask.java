package com.iglens.动态定时任务;

import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;
 
@Component
public class DynamicTask {
 
    @Autowired
    private TaskScheduler taskScheduler;
 
    public void scheduleTask() {
        taskScheduler.schedule(() -> System.out.println("动态任务执行：" + System.currentTimeMillis()), new Date(System.currentTimeMillis() + 5000));
    }
}