package com.iglens.动态定时任务;

import com.google.gson.Gson;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * SpringBoot创建动态定时任务的几种方式
 * https://blog.csdn.net/hk000001/article/details/143038049
 */
@RestController
public class cronController {
 
    @Autowired
    private ScheduleJobService scheduleJobService;
 
    /**
     * 测试上传的用例文件, 获取详细执行结果
     */
    @PostMapping("/add")
    void executeTestOneFile(@RequestBody ScheduleResult scheduleResult) {
        boolean valid = CronUtils.isValid(scheduleResult.getCronExpression());
        if (valid){
            System.out.println("校验成功， 添加任务");
            scheduleResult.setMethodParams(scheduleResult.getBranch()+scheduleResult.getCaseDir());
            scheduleJobService.addScheduleJob(scheduleResult);
        }else {
            System.out.println("校验失败");
        }
    }
 
    @PostMapping("/stop")
    void end(@RequestBody ScheduleResult scheduleResult) {
        Gson gson = new Gson();
        System.out.println("================");
        System.out.println(scheduleResult);
        System.out.println("=================");
        scheduleResult.setJobStatus(0);
        scheduleJobService.startOrStopScheduler(scheduleResult);
    }
 
    @PostMapping("/start")
    void start(@RequestBody ScheduleResult scheduleResult) {
        System.out.println("================");
        System.out.println(scheduleResult);
        System.out.println("=================");
        scheduleResult.setJobStatus(1);
        scheduleJobService.startOrStopScheduler(scheduleResult);
    }
 
    @PostMapping("/edit")
    void edit(@RequestBody ScheduleResult scheduleResult) {
        System.out.println("=======edit=========");
        System.out.println(scheduleResult);
        System.out.println("=================");
        scheduleJobService.editScheduleJob(scheduleResult);
    }
 
    @PostMapping("/delete")
    void delete(@RequestBody ScheduleResult scheduleResult) {
        System.out.println("=======delete=========");
        System.out.println(scheduleResult);
        System.out.println("=================");
        scheduleJobService.deleteScheduleJob(scheduleResult);
    }
 
    @GetMapping("/tasks")
    List<ScheduleResult> get() throws Exception {
        List<ScheduleResult> allTask = scheduleJobService.findAllTask();
        System.out.println("现在的定时任务数量 = " + allTask.size());
        System.out.println("现在的定时任务 = " + allTask);
        return allTask;
    }
}