package org.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/** @author T480S */
@EnableAsync
@EnableJpaAuditing
@SpringBootApplication
public class DemoApplication {
  public static void main(String[] args) {
    SpringApplication.run(DemoApplication.class, args);

    // 启动方式二
    //    ConfigurableApplicationContext context = new SpringApplicationBuilder()
    //        .sources(DemoApplication.class)
    //        .bannerMode(Banner.Mode.CONSOLE)
    //        .run(args);
    //    原文链接：https://blog.csdn.net/qq_32691791/article/details/113033007
  }

  /**
   * 屏蔽swaggerUI中显示的basicerror
   *
   * @return 空
   */
  @Bean
  public Docket demoApi() {
    return new Docket(DocumentationType.OAS_30)
        .select()
        .apis(RequestHandlerSelectors.any())
        .paths(PathSelectors.regex("(?!/error.*).*"))
        .build();
  }
}
