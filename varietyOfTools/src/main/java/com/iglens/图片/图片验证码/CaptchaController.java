package com.iglens.图片.图片验证码;

import com.alibaba.fastjson.JSON;
import com.wf.captcha.ArithmeticCaptcha;
import com.wf.captcha.ChineseCaptcha;
import com.wf.captcha.ChineseGifCaptcha;
import com.wf.captcha.GifCaptcha;
import com.wf.captcha.SpecCaptcha;
import com.wf.captcha.base.Captcha;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class CaptchaController {

  @Autowired
  private CaptchaService captchaService;
  @Autowired
  private StringRedisTemplate redisTemplate;

  /**
   * 验证码
   */
  @GetMapping("/captcha/digit")
  @ApiOperation(value = "获取数字验证码", notes = "获取数字验证码", tags = "验证码相关")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "uuid", value = "uuid", required = true, paramType = "query")
  })
  public void captcha(HttpServletResponse response, String uuid) throws IOException {
    response.setHeader("Cache-Control", "no-store, no-cache");
    response.setContentType("image/jpeg");
    log.info("获取验证码，uuid：{}", uuid);
    //获取图片验证码
    BufferedImage image = captchaService.getCaptcha(uuid);
    log.info("获取验证码，uuid：{},return:{}", uuid, JSON.toJSONString(image));
    ServletOutputStream out = response.getOutputStream();
    ImageIO.write(image, "jpg", out);
    IOUtils.closeQuietly(out);
  }

  @GetMapping("/captcha/graphics")
  @ApiOperation(value = "获取图形验证码", notes = "获取图形验证码", tags = "验证码相关")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "uuid", value = "uuid", required = true, paramType = "query"),
      @ApiImplicitParam(name = "type", value = "类型 png:png gif:gif cn:中文 cngif:中文gif arithmeti:算术", required = false, paramType = "query")
  })
  public void captcha(HttpServletRequest request, HttpServletResponse response,
      @RequestParam String uuid,
      @RequestParam(defaultValue = "arithmeti", required = false) String type) throws Exception {
    // 设置请求头为输出图片类型
    response.setContentType("image/gif");
    response.setHeader("Pragma", "No-cache");
    response.setHeader("Cache-Control", "no-cache");
    response.setDateHeader("Expires", 0);

    Captcha captcha = null;
    switch (type) {
      case "png":
        captcha = new SpecCaptcha(130, 48);
        break;
      case "gif":
        // gif类型
        captcha = new GifCaptcha(130, 48);
        break;
      case "cn":
        // 中文类型
        captcha = new ChineseCaptcha(130, 48, 5, new Font("楷体", Font.PLAIN, 28));
        break;
      case "cngif":
        // 中文gif类型
        captcha = new ChineseGifCaptcha(130, 48, 5, new Font("楷体", Font.PLAIN, 28));
        break;
      case "arithmeti":
        // 算术类型
        ArithmeticCaptcha arithmeticCaptcha = new ArithmeticCaptcha(130, 48);
        arithmeticCaptcha.setLen(3);  // 几位数运算，默认是两位
        arithmeticCaptcha.getArithmeticString();  // 获取运算的公式：3+2=?
        arithmeticCaptcha.text();  // 获取运算的结果：5
        captcha = arithmeticCaptcha;
        break;
      default:
        new SpecCaptcha(130, 48);
        break;
    }
    log.info("验证码:{}", captcha.text());
    // 设置字体
    //captcha.setFont(new Font("Verdana", Font.PLAIN, 32));  // 有默认字体，可以不用设置
    // 设置类型，纯数字、纯字母、字母数字混合
    captcha.setCharType(Captcha.TYPE_DEFAULT);

    //缓存验证码
    redisTemplate.opsForValue().set(uuid, captcha.text().toLowerCase());
    // 输出图片流
    captcha.out(response.getOutputStream());
  }
}
