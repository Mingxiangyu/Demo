package com.iglens;

import java.net.InetAddress;
import java.net.UnknownHostException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
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
@Slf4j
@EnableAsync
@EnableJpaAuditing
@SpringBootApplication
public class DemoApplication {
  public static void main(String[] args) {
    ConfigurableApplicationContext application = SpringApplication.run(DemoApplication.class, args);
    Environment env = application.getEnvironment();
    String ip = null;
    try {
      ip = InetAddress.getLocalHost().getHostAddress();
    } catch (UnknownHostException e) {
      e.printStackTrace();
    }
    String port = env.getProperty("server.port");
    String path = env.getProperty("server.servlet.context-path");
    log.info(
        "\n----------------------------------------------------------\n\t"
            + "Application Jeecg-Boot is running! Access URLs:\n\t"
            + "Local: \t\thttp://localhost:"
            + port
            + path
            + "/\n\t"
            + "External: \thttp://"
            + ip
            + ":"
            + port
            + path
            + "/\n\t"
            + "Swagger文档: \thttp://"
            + ip
            + ":"
            + port
            + path
            + "/doc.html\n"
            + "----------------------------------------------------------");

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
        .apiInfo(apiInfo()) // 添加下面的apiinfo信息
        .select()
        .apis(RequestHandlerSelectors.any())
        .paths(PathSelectors.regex("(?!/error.*).*"))
        .build();
  }

  /** @return swagger上表头 */
  private ApiInfo apiInfo() {
    return new ApiInfoBuilder()
        .title("Swagger3接口文档")
        .description("更多请咨询服务开发者Mxy。")
        .contact(new Contact("Mxy。", "http://www.baidu.com", "Mxy@foxmail.com"))
        .version("1.0")
        .build();
  }
}
