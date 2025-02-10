package com.iglens.动态定时任务;

import lombok.Data;
 
@Data
public class ScheduleResult {
 
    /**
     * 任务ID
     */
    private Integer jobId;
 
    /**
     * bean名称
     */
    private String beanName;
 
    /**
     * 方法名称
     */
    private String methodName;
 
    /**
     * 方法参数: 执行service里面的哪一种方法
     */
    private String methodParams;
 
    /**
     * cron表达式
     */
    private String cronExpression;
 
    /**
     * 状态（1正常 0暂停）
     */
    private Integer jobStatus;
 
    /**
     * 备注
     */
    private String remark;
 
    /**
     * 创建时间
     */
    private String createTime;
 
    /**
     * 更新时间
     */
    private String updateTime;
 
}