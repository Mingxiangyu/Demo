package com.iglens;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.Data;
import org.springframework.scheduling.annotation.Async;

public class 临时任务进度条 {

  // 异步调用进度条方法
  // 1，while（true）
  // 2，查询该任务状态，为暂停或完成，直接结束
  // 3，sleep随机秒数
  // 4，读取任务数据进度值，进行随机数添加后并存储
  // 5，加到99后，直接结束该方法
  // 6，完成任务方法，更新状态，同时设置进度值为100

  @Async
  public void start(Integer taskId) {
    while (true) {

      // TaskEntity byId = this.getById(taskId);
      TaskEntity byId = new TaskEntity();
      if (byId == null) {
        return;
      }
      if (!"1".equals(byId.getStatus())) {
        return;
      }
      sleepRandomly(1, 2);

      Double progressPercentage = byId.getProgressPercentage();
      if (progressPercentage == null) {
        progressPercentage = 0D;
      }
      double d = Math.random();
      System.out.println("sbyte：" + d);
      double v = progressPercentage + d;
      System.out.println("加完后：" + v);
      if (v > 99.9) {
        return;
      }
      // 注意，不要使用update全量更新，会覆盖其他地方修改的数据状态
      // byId.setProgressPercentage(v);
      // this.updateById(byId);

      UpdateWrapper<TaskEntity> updateWrapper = new UpdateWrapper<>();
      updateWrapper.eq("id", byId.getId());
      updateWrapper.set("progress_percentage", v);
      // 调用mybatis的部分字段更新接口
      // update(null, updateWrapper);
    }
  }

  /**
   * @param startRange 起始范围（单位为秒）
   * @param endRange 结束范围（单位为秒）
   */
  public static void sleepRandomly(int startRange, int endRange) {
    try {
      long millis = (long) (Math.random() * startRange * 1000 + endRange * 1000);
      System.out.println("sleep time: " + millis);
      Thread.sleep(millis);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException(e);
    }
  }
}

@Data
class TaskEntity {

  String id;

  /** 状态值 */
  String status;

  /** 进度百分比 */
  Double progressPercentage;
}
