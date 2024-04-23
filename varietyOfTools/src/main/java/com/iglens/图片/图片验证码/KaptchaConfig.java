package com.iglens.图片.图片验证码;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import java.util.Properties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 生成验证码配置
 */
@Configuration
public class KaptchaConfig {

  @Bean
  public DefaultKaptcha producer() {
    Properties properties = new Properties();
    //图片边框
    properties.setProperty("kaptcha.border", "no");
    //文本集合，验证码值从此集合中获取
    properties.setProperty("kaptcha.textproducer.char.string", "ABCDEGHJKLMNRSTUWXY23456789");
    //字体颜色
    properties.setProperty("kaptcha.textproducer.font.color", "0,84,144");
    //干扰颜色
    properties.setProperty("kaptcha.noise.color", "0,84,144");
    //字体大小
    properties.setProperty("kaptcha.textproducer.font.size", "30");
    //背景颜色渐变，开始颜色
    properties.setProperty("kaptcha.background.clear.from", "247,255,234");
    //背景颜色渐变，结束颜色
    properties.setProperty("kaptcha.background.clear.to", "247,255,234");
    //图片宽
    properties.setProperty("kaptcha.image.width", "125");
    //图片高
    properties.setProperty("kaptcha.image.height", "35");
    properties.setProperty("kaptcha.session.key", "code");
    //验证码长度
    properties.setProperty("kaptcha.textproducer.char.length", "4");
    //字体
    properties.setProperty("kaptcha.textproducer.font.names", "Arial,Courier,cmr10,宋体,楷体,微软雅黑");
    properties.put("kaptcha.textproducer.char.space", "5");
    Config config = new Config(properties);
    DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
    defaultKaptcha.setConfig(config);
    return defaultKaptcha;
  }
}