package org.demo.kettle;

import lombok.extern.slf4j.Slf4j;

  @Slf4j
  public class KettleTaskJob {


    public void run() throws Exception {
      log.info("*****kettle定时任务运行开始******");
      String transFileName = "D:/01develop/06pdi-ce-8.1.0.0-365/ktr/goods.ktr";
      KettleUtil.callNativeTrans(transFileName);
      log.info("*****kettle定时任务运行结束******");
    }

    public void runTrans(String transFileName) throws Exception {
      log.info("*****kettle定时任务运行开始******");
      // 获取URL
      String filePath=FileUtil.getResourceFilePath(transFileName);
      KettleUtil.callNativeTrans(filePath);
      log.info("*****kettle定时任务运行结束******");
    }

    public void runJob() throws Exception {
      log.info("*****kettle定时任务运行开始******");
      String transFileName = "D:/01develop/06pdi-ce-8.1.0.0-365/job/goods.kjb";
      KettleUtil.callNativeJob(transFileName);
      log.info("*****kettle定时任务运行结束******");
    }

    public static void main(String[] args) throws Exception {
      KettleTaskJob job = new KettleTaskJob();
      String transFileName="/ktr/goods.ktr";
      job.runTrans(transFileName);
      //job.runJob();
    }
}
