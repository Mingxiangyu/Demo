package org.demo.正则;

/** @author T480S */
public class 正则验证 {

  public static final String REG_MOBILE = "^1\\d{10}$"; // 判断是否是手机号
  public static final String REG_ALIPAY_USER_ID = "^2088\\d{12}$"; // 判断是支付宝用户Id 以2088开头的纯16位数字

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

  /** 正则验证 */
  public static boolean match(String text, String reg) {
    if (text == null) {
      return false;
    }
    return text.matches(reg);
  }
}
