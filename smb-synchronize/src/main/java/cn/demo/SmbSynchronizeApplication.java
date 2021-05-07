package cn.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @author Ray。
 * @date 2020/7/18 10:07
 */
@EnableJpaRepositories
@EnableJpaAuditing
@SpringBootApplication
@EnableScheduling
public class SmbSynchronizeApplication {
  public static void main(String[] args) {
    SpringApplication.run(SmbSynchronizeApplication.class, args);
    System.out.println("==========================拷贝共享文件夹服务已启动============================");
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
