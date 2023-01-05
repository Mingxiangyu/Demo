package cn.trans.services;

import cn.trans.model.TransContrastResult;
import cn.trans.model.TransParam;

/** 文本翻译服务接口 @author mxy @Date 2022-05-01 */
public interface MicrosoftTransService {

  /**
   * 通用翻译API接口
   *
   * @param param 翻译参数
   * @return
   */
  String transForString(TransParam param);

  /**
   * 公网对照翻译
   *
   * @param param 翻译参数
   * @return
   */
  TransContrastResult translationForContrast(TransParam param);

  /**
   * 语种识别
   *
   * @param text
   * @return
   */
  String detectedLanguage(String text);
}
