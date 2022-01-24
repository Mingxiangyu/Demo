package com.iglens.线程池;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 线程池配置类
 *
 * @author T480S
 */
@Configuration
@EnableAsync
public class ExecutorConfig {
  private static final Logger logger = LoggerFactory.getLogger(ExecutorConfig.class);

  @Value("${async.executor.thread.core_pool_size}")
  private int corePoolSize;

  @Value("${async.executor.thread.max_pool_size}")
  private int maxPoolSize;

  @Value("${async.executor.thread.queue_capacity}")
  private int queueCapacity;

  @Value("${async.executor.thread.name.prefix}")
  private String namePrefix;

  @Bean(name = "asyncServiceExecutor")
  public Executor asyncServiceExecutor() {
    logger.info("start asyncServiceExecutor");
    ThreadPoolTaskExecutor executor = new VisiableThreadPoolTaskExecutor();
    // 配置核心线程数
    executor.setCorePoolSize(corePoolSize);
    // 配置最大线程数,只有在缓冲队列满了之后才会申请超过核心线程数的线程
    executor.setMaxPoolSize(maxPoolSize);
    // 配置队列大小
    executor.setQueueCapacity(queueCapacity);
    // 配置线程池中的线程的名称前缀
    executor.setThreadNamePrefix(namePrefix);
    // 设置线程活跃时间（秒）,当超过了核心线程之外的线程在空闲时间到达之后会被销毁
    executor.setKeepAliveSeconds(200);

    /*
     * 当线程池的任务缓存队列已满并且线程池中的线程数目达到maximumPoolSize，如果还有任务到来就会采取任务拒绝策略 通常有以下四种策略：
     * ThreadPoolExecutor.AbortPolicy:丢弃任务并抛出RejectedExecutionException异常。
     * ThreadPoolExecutor.DiscardPolicy：也是丢弃任务，但是不抛出异常。
     * ThreadPoolExecutor.DiscardOldestPolicy：丢弃队列最前面的任务，然后重新尝试执行任务（重复此过程）
     * ThreadPoolExecutor.CallerRunsPolicy：重试添加当前的任务，自动重复调用 execute() 方法，直到成功
     */
    executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
    // 等待所有任务结束后再关闭线程池
//    executor.setWaitForTasksToCompleteOnShutdown(true);
    // 执行初始化
    executor.initialize();
    return executor;
  }
}
