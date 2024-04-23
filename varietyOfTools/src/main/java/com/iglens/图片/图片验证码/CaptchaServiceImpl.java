package com.iglens.图片.图片验证码;

import com.google.code.kaptcha.Producer;
import java.awt.image.BufferedImage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CaptchaServiceImpl implements CaptchaService {

  @Autowired
  private Producer producer;
  @Autowired
  private StringRedisTemplate redisTemplate;

  /**
   * 生成并缓存验证码，返给前端图片
   */
  @Override
  public BufferedImage getCaptcha(String uuid) {
    if (StringUtils.isEmpty(uuid)) {
      throw new RuntimeException("uuid不能为空");
    }
    //生成文字验证码
    String code = producer.createText();
    log.info("uuid:{},验证码：{}", uuid, code);
    //缓存验证码
    redisTemplate.opsForValue().set(uuid, code);
    return producer.createImage(code);
  }

  /**
   * 校验验证码
   */
  @Override
  public boolean validate(String uuid, String code) {
    String cacheCode = redisTemplate.opsForValue().get(uuid);
    if (StringUtils.isEmpty(cacheCode)) {
      return false;
    }
    //删除缓存验证码
    redisTemplate.delete(uuid);
    return cacheCode.equalsIgnoreCase(code);
  }
}