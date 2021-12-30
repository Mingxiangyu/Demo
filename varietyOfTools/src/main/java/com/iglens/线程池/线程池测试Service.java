package com.iglens.线程池;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class 线程池测试Service {

  @Async("asyncServiceExecutor") // 对于非默认的异步任务再通过@Async("otherTaskExecutor")来指定线程池名称
  public void executeAsync() {
    log.info("start executeAsync");

    System.out.println("异步线程要做的事情");
    System.out.println("可以在这里执行批量插入等耗时的事情");

    log.info("end executeAsync");
  }
}
