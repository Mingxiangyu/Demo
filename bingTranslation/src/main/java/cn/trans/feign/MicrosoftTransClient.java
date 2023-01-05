package cn.trans.feign;

import cn.trans.interceptor.MicrosoftTokenInterceptor;
import cn.trans.model.MicrosoftTransParamItem;
import cn.trans.model.MicrosoftTransResultItem;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/** 微软在线翻译 客户端接口 @author mxy @Date 2022-12-22 */
@FeignClient(
    qualifier = "microsoftTransClient",
    name = "microsoft.trans",
    url = "https://api.cognitive.microsofttranslator.com",
    configuration = {MicrosoftTokenInterceptor.class})
public interface MicrosoftTransClient {

  /**
   * 微软在线翻译
   *
   * @param from 翻译源语言，可设置为 auto
   * @param to 翻译目标语言，不可设置为 auto
   * @param apiVersion 接口版本，默认3.0
   * @param includeSentenceLength 是否包含字符位置
   * @return
   */
  @PostMapping(value = "/translate")
  List<MicrosoftTransResultItem> translation(
      @RequestBody List<MicrosoftTransParamItem> textItems,
      @RequestParam(value = "from", defaultValue = "en") String from,
      @RequestParam(value = "to", defaultValue = "zh-CN") String to,
      @RequestParam(value = "api-version", defaultValue = "3.0") String apiVersion,
      @RequestParam(value = "includeSentenceLength", defaultValue = "true")
          Boolean includeSentenceLength);
}
