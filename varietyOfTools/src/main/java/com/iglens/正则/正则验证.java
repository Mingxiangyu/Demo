package com.iglens.正则;

import java.util.regex.Pattern;

/**
 * @author T480S
 */
public class 正则验证 {

  public static final String REG_MOBILE = "^1\\d{10}$"; // 判断是否是手机号
  public static final String REG_ALIPAY_USER_ID = "^2088\\d{12}$"; // 判断是支付宝用户Id 以2088开头的纯16位数字


  /**
   * 验证时间字符串格式输入是否正确
   */
  public static boolean isDateTime(String datetime) {
    Pattern p = Pattern.compile(
        "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1][0-9])|([2][0-4]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$");
    return p.matcher(datetime).matches();
  }

  /**
   * 判断是否是手机号
   *
   * @param str 字符
   * @return 是否
   */
  public static boolean isMobile(String str) {
    return match(str, REG_MOBILE);
  }

  /**
   * 判断是支付宝用户Id 以2088开头的纯16位数字
   *
   * @param str 字符
   * @return 是否
   */
  public static boolean isAlipayUserId(String str) {
    return match(str, REG_ALIPAY_USER_ID);
  }

  /**
   * 正则验证
   */
  public static boolean match(String text, String reg) {
    if (text == null) {
      return false;
    }
    return text.matches(reg);
  }

  /**
   * 判断是不是一个可能合法的email格式.
   */
  public static boolean isEmail(String email) {
    if (email == null) {
      return false;
    }
    byte[] by = email.getBytes();
    for (byte b : by) {
      if (/**/b != 0x2D /* - */ && //
          b != 0x2E /* . */ && //
          b != 0x40 /* @ */ && //
          b != 0x5F /* _ */ && //
          (b < 0x30 || b > 0x39 /* 0 ~ 9 */) && //
          (b < 0x61 || b > 0x7A /* a ~ z */) && //
          (b < 0x41 || b > 0x5A /* A ~ Z */)) {
        return false;
      }
    }
    return true;
  }
}
