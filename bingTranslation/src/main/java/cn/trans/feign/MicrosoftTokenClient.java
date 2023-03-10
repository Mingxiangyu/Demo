package cn.trans.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 微软在线翻译 获取token接口
 *
 * @author mxy
 */
@FeignClient(qualifier = "microsoftTokenClient", name = "microsoft.token", url = "https://edge.microsoft.com")
public interface MicrosoftTokenClient {


    /**
     * 获取token
     *
     * @return
     */
    @GetMapping(value = "/translate/auth")
    String translateAuth();

}

