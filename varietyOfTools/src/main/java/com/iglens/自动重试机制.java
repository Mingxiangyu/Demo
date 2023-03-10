package com.iglens;

import org.springframework.http.HttpStatus;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.client.HttpServerErrorException;

import java.time.LocalDateTime;

/**
 * @author xming
 * @link 原文链接：https://blog.csdn.net/hanxiaotongtong/article/details/108019319
 * spring-retry使用介绍
 * @link 原文链接：https://blog.csdn.net/weixin_41307800/article/details/124604598
 */
@EnableRetry
public class 自动重试机制 {

  public static void main(String[] args) {
    test();
  }

  @Retryable(
      value = HttpServerErrorException.class,
      maxAttempts = 3,
      backoff = @Backoff(delay = 5000L, multiplier = 2))
  public static String test() {
    System.out.println("发起远程API请求:" + (LocalDateTime.now()));
    /*
    @Retryable注解的方法在发生异常时会重试，参数说明：
    value：当指定异常发生时会进行重试 ,HttpClientErrorException是RestClientException的子类。
    include：和value一样，默认空。如果 exclude也为空时，所有异常都重试
    exclude：指定异常不重试，默认空。如果 include也为空时，所有异常都重试
    maxAttemps：最大重试次数，默认3
    backoff：重试等待策略，默认空
    @Backoff注解为重试等待的策略，参数说明：
    delay：指定重试的延时时间，默认为1000毫秒
    multiplier：指定延迟的倍数，比如设置delay=5000，multiplier=2时，第一次重试为5秒后，第二次为10(5x2)秒，第三次为20(10x2)秒。
     */
    throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
