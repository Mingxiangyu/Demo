package com.iglens.ofd;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AppTest {

  //创建日志对象
  Logger logger = LogManager.getLogger(this.getClass());

  @Test
  public void test1() {
    logger.info("logback的日志信息过来了");
    logger.error("logback的错误信息过来了");
  }
}