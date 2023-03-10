package cn.trans.interceptor;

import cn.trans.feign.MicrosoftTokenClient;
import cn.trans.utils.SpringContext;
import cn.trans.utils.StringUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpHeaders;

/** 添加token到headers中 @author mxy @Date: 2022/12/22 */
@Slf4j
public class MicrosoftTokenInterceptor implements RequestInterceptor {

  @Autowired private StringRedisTemplate redisTemplate;

  private final String MICROSOFT_TOKEN_PREFIX = "MICROSOFT_TOKEN";

  @Override
  public void apply(RequestTemplate requestTemplate) {
    MicrosoftTokenClient microsoftTokenClient = SpringContext.getBean(MicrosoftTokenClient.class);

    ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
    String redisKey = MICROSOFT_TOKEN_PREFIX;
    String accessToken = valueOperations.get(redisKey);
    if (StringUtils.isBlank(accessToken)) {
      accessToken = microsoftTokenClient.translateAuth();
      if (StringUtils.isNotBlank(accessToken)) {
        long offset = 1000 * 60 * 10;
        valueOperations.set(redisKey, accessToken, offset, TimeUnit.MILLISECONDS);
      } else {
        throw new RuntimeException("获取翻译token失败");
      }
    }

    if (StringUtils.isNotBlank(accessToken)) {
      requestTemplate.header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
    }
  }
}
