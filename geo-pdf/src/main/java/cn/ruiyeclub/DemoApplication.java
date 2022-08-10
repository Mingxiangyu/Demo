package cn.ruiyeclub;

import java.net.InetAddress;
import java.net.UnknownHostException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/** @author T480S */
@Slf4j
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
    String path  = "null";
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
