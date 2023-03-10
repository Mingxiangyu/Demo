package cn.trans.config;

import feign.codec.Encoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

  // // 这个encoder是用于上传MultipartFile的
  // // 原文链接：https://blog.csdn.net/u013205984/article/details/122447315
  // @Bean
  // public Encoder springFormEncoder() {
  //   return new SpringFormEncoder();
  // }

  @Autowired private ObjectFactory<HttpMessageConverters> converters;

  @Bean
  // 解决feign.codec.EncodeException: class java.util.ArrayList is not a type supported by this
  // encoder
  // 原文链接：https://blog.csdn.net/qq_39609151/article/details/105789888
  public Encoder feignFormEncoder() {
    return new SpringEncoder(converters);
  }
}
