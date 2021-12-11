package com.iglens.定时任务;

import java.util.Date;

/** @author T480S */
public class SysJobPO {
  /** 任务ID */
  private Integer jobId;
  /** bean名称 */
  private String beanName;
  /** 方法名称 */
  private String methodName;
  /** 方法参数 */
  private String methodParams;
  /** cron表达式 */
  private String cronExpression;
  /** 状态（1正常 0暂停） */
  private Integer jobStatus;
  /** 备注 */
  private String remark;
  /** 创建时间 */
  private Date createTime;
  /** 更新时间 */
  private Date updateTime;

  public Integer getJobId() {
    return jobId;
  }

  public void setJobId(Integer jobId) {
    this.jobId = jobId;
  }

  public String getBeanName() {
    return beanName;
  }

  public void setBeanName(String beanName) {
    this.beanName = beanName;
  }

  public String getMethodName() {
    return methodName;
  }

  public void setMethodName(String methodName) {
    this.methodName = methodName;
  }

  public String getMethodParams() {
    return methodParams;
  }

  public void setMethodParams(String methodParams) {
    this.methodParams = methodParams;
  }

  public String getCronExpression() {
    return cronExpression;
  }

  public void setCronExpression(String cronExpression) {
    this.cronExpression = cronExpression;
  }

  public Integer getJobStatus() {
    return jobStatus;
  }

  public void setJobStatus(Integer jobStatus) {
    this.jobStatus = jobStatus;
  }

  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public Date getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
  }
}
