package com.iglens;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
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
        .apiInfo(apiInfo())//添加下面的apiinfo信息
        .select()
        .apis(RequestHandlerSelectors.any())
        .paths(PathSelectors.regex("(?!/error.*).*"))
        .build();
  }

  /** @return swagger上表头*/
  private ApiInfo apiInfo() {
    return new ApiInfoBuilder()
        .title("Swagger3接口文档")
        .description("更多请咨询服务开发者Mxy。")
        .contact(new Contact("Mxy。", "http://www.baidu.com", "Mxy@foxmail.com"))
        .version("1.0")
        .build();
  }
}
