package com.iglens.图片.图片验证码;

import java.awt.image.BufferedImage;

public interface CaptchaService {
    boolean validate(String uuid, String code);
    BufferedImage getCaptcha(String uuid);
}