package com.iglens.定时任务;

import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface SysJobMapper {
  @Insert("INSERT INTO sys_job(jobId,beanName,methodParams,cronExpression,jobStatus,remark,createTime,updateTime,methodName) values (#{jobId}," +
      "#{beanName},#{methodParams},#{cronExpression},#{jobStatus},#{remark},#{createTime},#{updateTime},#{methodName})")
  boolean addSysJob(SysJobPO sysJob);

  @Select("select * from sys_job where jobStatus =  #{jobStatus}")
  List<SysJobPO> getSysJobListByStatus(Integer jobStatus);

  @Select("select * from sys_job where jobId =  #{jobId}")
  SysJobPO findTaskJobByJobId(Integer jobId);

  @Delete("delete  from sys_job where jobId =  #{jobId}")
  boolean deleteTaskJobByJobId(Integer jobId);

  /**
   * 这儿只是修改corn表达式 和 状态。 测试使用
   *
   * @param sysJobPO
   * @return
   */
  @Update("update sys_job set cronExpression = #{cronExpression} , jobStatus =  #{jobStatus} where  jobId=  #{jobId}")
  boolean updateTaskJob(SysJobPO sysJobPO);
}