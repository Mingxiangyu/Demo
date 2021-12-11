package com.iglens.kettle.定时任务;


import java.net.URL;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class KettleTaskJob {

  public static void main(String[] args) throws Exception {
    KettleTaskJob job = new KettleTaskJob();
    String transFileName = "/ktr/goods.ktr";
//    job.runTrans(transFileName);
     job.runJob();
  }

  public void run() throws Exception {
    log.info("*****kettle定时任务运行开始******");
    String transFileName = "D:/01develop/06pdi-ce-8.1.0.0-365/ktr/goods.ktr";
    KettleUtil.callNativeTrans(transFileName);
    log.info("*****kettle定时任务运行结束******");
  }

  public void runTrans(String transFileName) throws Exception {
    log.info("*****kettle定时任务运行开始******");
    // 获取URL
    String filePath = getResourceFilePath(transFileName);
    KettleUtil.callNativeTrans(filePath);
    log.info("*****kettle定时任务运行结束******");
  }

  public void runJob() throws Exception {
    log.info("*****kettle定时任务运行开始******");
    String transFileName = "C:\\Users\\T480S\\Desktop\\测试\\alldatabase.kjb";
    KettleUtil.callNativeJob(transFileName);
    log.info("*****kettle定时任务运行结束******");
  }


  /**
   * 获取资源文件地址
   *
   * @param fullFileName
   * @return
   */
  public static String getResourceFilePath(String fullFileName) {
    URL url = KettleTaskJob.class.getResource(fullFileName);
    String path = url.getPath();
    return path;
  }
}
