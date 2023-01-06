package cn.trans.controller;

import cn.trans.model.R;
import cn.trans.model.TransContrastResult;
import cn.trans.model.TransParam;
import cn.trans.services.MicrosoftTransService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 翻译接口
 *
 * @author mxy
 */
@Api(tags = "翻译")
@RestController
@CrossOrigin
@RequestMapping("/trans")
@Slf4j
public class TransController {

    @Autowired
    private MicrosoftTransService microsoftTransService;

    @ApiOperation("文本翻译")
    @GetMapping("/translation")
    public R<String> translation(@RequestBody TransParam param) {
        if (log.isDebugEnabled()) {
            log.debug("参数 --> param:{}", param);
        }

        try {
            return R.ok().data(microsoftTransService.transForString(param));
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            return R.error().message("标题翻译失败，请稍后重试");
        }
    }

    @ApiOperation("公网对照翻译")
    @GetMapping("/translationForContrast")
    public R<TransContrastResult> translationForContrast(@RequestBody TransParam param) {
        if (log.isDebugEnabled()) {
            log.debug("参数 --> param:{}", param);
        }
        try {
            return R.ok().data(microsoftTransService.translationForContrast(param));
        } catch (Exception e) {
            log.error(e.getMessage(),e);
            return R.error().message("正文翻译失败，请稍后重试");
        }
    }

}
