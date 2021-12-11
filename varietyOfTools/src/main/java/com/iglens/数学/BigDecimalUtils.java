package com.iglens.数学;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/** 用于高精确处理常用的数学运算 */
public class BigDecimalUtils {
  // 默认除法运算精度
  private static final int DEF_DIV_SCALE = 10;

  public static void main(String[] args) {
    System.out.println(formatToNumber(new BigDecimal("3.435")));
    System.out.println(formatToNumber(new BigDecimal(0)));
    System.out.println(formatToNumber(new BigDecimal("0.00")));
    System.out.println(formatToNumber(new BigDecimal("0.001")));
    System.out.println(formatToNumber(new BigDecimal("0.006")));
    System.out.println(formatToNumber(new BigDecimal("0.206")));

    getPercent();
  }

  /** 获取百分比 */
  private static void getPercent() {
    NumberFormat currency = NumberFormat.getCurrencyInstance(); // 建立货币格式化引用 即在前面添加该符号￥
    NumberFormat percent = NumberFormat.getPercentInstance(); // 建立百分比格式化引用  即在后面面添加该符号%
    percent.setMaximumFractionDigits(3); // 百分比小数点最多3位

    BigDecimal loanAmount = new BigDecimal("15000.48"); // 贷款金额
    BigDecimal interestRate = new BigDecimal("0.008"); // 利率
    BigDecimal interest = loanAmount.multiply(interestRate); // 相乘

    System.out.println("贷款金额:\t" + currency.format(loanAmount));
    System.out.println("利率:\t" + percent.format(interestRate));
    System.out.println("利息:\t" + currency.format(interest));
  }

  /**
   * 1.0~1之间的BigDecimal小数，格式化后失去前面的0,则前面直接加上0。<br>
   * 2.传入的参数等于0，则直接返回字符串"0.00" <br>
   * 3.大于1的小数，直接格式化返回字符串 <br>
   *
   * @param obj 传入的小数
   * @return
   */
  public static String formatToNumber(BigDecimal obj) {
    DecimalFormat df = new DecimalFormat("#.00");
    if (obj.compareTo(BigDecimal.ZERO) == 0) {
      return "0.00";
    } else if (obj.compareTo(BigDecimal.ZERO) > 0 && obj.compareTo(new BigDecimal(1)) < 0) {
      return "0" + df.format(obj).toString();
    } else {
      return df.format(obj);
    }
  }

  /**
   * 提供精确的加法运算
   *
   * @param v1 被加数
   * @param v2 加数
   * @return 两个参数的和
   */
  public static double add(double v1, double v2) {
    BigDecimal b1 = new BigDecimal(Double.toString(v1));
    BigDecimal b2 = new BigDecimal(Double.toString(v2));
    return b1.add(b2).doubleValue();
  }

  /**
   * 提供精确的加法运算
   *
   * @param v1 被加数
   * @param v2 加数
   * @return 两个参数的和
   */
  public static BigDecimal add(String v1, String v2) {
    BigDecimal b1 = new BigDecimal(v1);
    BigDecimal b2 = new BigDecimal(v2);
    return b1.add(b2);
  }

  /**
   * 提供精确的加法运算
   *
   * @param v1 被加数
   * @param v2 加数
   * @param scale 保留scale 位小数
   * @return 两个参数的和
   */
  public static String add(String v1, String v2, int scale) {
    if (scale < 0) {
      throw new IllegalArgumentException("The scale must be a positive integer or zero");
    }
    BigDecimal b1 = new BigDecimal(v1);
    BigDecimal b2 = new BigDecimal(v2);
    return b1.add(b2).setScale(scale, BigDecimal.ROUND_HALF_UP).toString();
  }

  /**
   * 提供精确的减法运算
   *
   * @param v1 被减数
   * @param v2 减数
   * @return 两个参数的差
   */
  public static double sub(double v1, double v2) {
    BigDecimal b1 = new BigDecimal(Double.toString(v1));
    BigDecimal b2 = new BigDecimal(Double.toString(v2));
    return b1.subtract(b2).doubleValue();
  }

  /**
   * 提供精确的减法运算。
   *
   * @param v1 被减数
   * @param v2 减数
   * @return 两个参数的差
   */
  public static BigDecimal sub(String v1, String v2) {
    BigDecimal b1 = new BigDecimal(v1);
    BigDecimal b2 = new BigDecimal(v2);
    return b1.subtract(b2);
  }

  /**
   * 提供精确的减法运算
   *
   * @param v1 被减数
   * @param v2 减数
   * @param scale 保留scale 位小数
   * @return 两个参数的差
   */
  public static String sub(String v1, String v2, int scale) {
    if (scale < 0) {
      throw new IllegalArgumentException("The scale must be a positive integer or zero");
    }
    BigDecimal b1 = new BigDecimal(v1);
    BigDecimal b2 = new BigDecimal(v2);
    return b1.subtract(b2).setScale(scale, BigDecimal.ROUND_HALF_UP).toString();
  }

  /**
   * 提供精确的乘法运算
   *
   * @param v1 被乘数
   * @param v2 乘数
   * @return 两个参数的积
   */
  public static double mul(double v1, double v2) {
    BigDecimal b1 = new BigDecimal(Double.toString(v1));
    BigDecimal b2 = new BigDecimal(Double.toString(v2));
    return b1.multiply(b2).doubleValue();
  }

  /**
   * 提供精确的乘法运算
   *
   * @param v1 被乘数
   * @param v2 乘数
   * @return 两个参数的积
   */
  public static BigDecimal mul(String v1, String v2) {
    BigDecimal b1 = new BigDecimal(v1);
    BigDecimal b2 = new BigDecimal(v2);
    return b1.multiply(b2);
  }

  /**
   * 提供精确的乘法运算
   *
   * @param v1 被乘数
   * @param v2 乘数
   * @param scale 保留scale 位小数
   * @return 两个参数的积
   */
  public static double mul(double v1, double v2, int scale) {
    BigDecimal b1 = new BigDecimal(Double.toString(v1));
    BigDecimal b2 = new BigDecimal(Double.toString(v2));
    return round(b1.multiply(b2).doubleValue(), scale);
  }

  /**
   * 提供精确的乘法运算
   *
   * @param v1 被乘数
   * @param v2 乘数
   * @param scale 保留scale 位小数
   * @return 两个参数的积
   */
  public static String mul(String v1, String v2, int scale) {
    if (scale < 0) {
      throw new IllegalArgumentException("The scale must be a positive integer or zero");
    }
    BigDecimal b1 = new BigDecimal(v1);
    BigDecimal b2 = new BigDecimal(v2);
    return b1.multiply(b2).setScale(scale, BigDecimal.ROUND_HALF_UP).toString();
  }

  /**
   * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到 小数点以后10位，以后的数字四舍五入
   *
   * @param v1 被除数
   * @param v2 除数
   * @return 两个参数的商
   */
  public static double div(double v1, double v2) {
    return div(v1, v2, DEF_DIV_SCALE);
  }

  /**
   * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入
   *
   * @param v1 被除数
   * @param v2 除数
   * @param scale 表示表示需要精确到小数点以后几位。
   * @return 两个参数的商
   */
  public static double div(double v1, double v2, int scale) {
    if (scale < 0) {
      throw new IllegalArgumentException("The scale must be a positive integer or zero");
    }
    BigDecimal b1 = new BigDecimal(Double.toString(v1));
    BigDecimal b2 = new BigDecimal(Double.toString(v2));
    return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
  }

  /**
   * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入
   *
   * @param v1 被除数
   * @param v2 除数
   * @param scale 表示需要精确到小数点以后几位
   * @return 两个参数的商
   */
  public static String div(String v1, String v2, int scale) {
    if (scale < 0) {
      throw new IllegalArgumentException("The scale must be a positive integer or zero");
    }
    BigDecimal b1 = new BigDecimal(v1);
    BigDecimal b2 = new BigDecimal(v1);
    return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).toString();
  }

  /**
   * 提供精确的小数位四舍五入处理
   *
   * @param v 需要四舍五入的数字
   * @param scale 小数点后保留几位
   * @return 四舍五入后的结果
   */
  public static double round(double v, int scale) {
    if (scale < 0) {
      throw new IllegalArgumentException("The scale must be a positive integer or zero");
    }
    BigDecimal b = new BigDecimal(Double.toString(v));
    return b.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
  }

  /**
   * 提供精确的小数位四舍五入处理
   *
   * @param v 需要四舍五入的数字
   * @param scale 小数点后保留几位
   * @return 四舍五入后的结果
   */
  public static String round(String v, int scale) {
    if (scale < 0) {
      throw new IllegalArgumentException("The scale must be a positive integer or zero");
    }
    BigDecimal b = new BigDecimal(v);
    return b.setScale(scale, BigDecimal.ROUND_HALF_UP).toString();
  }

  /**
   * 取余数
   *
   * @param v1 被除数
   * @param v2 除数
   * @param scale 小数点后保留几位
   * @return 余数
   */
  public static String remainder(String v1, String v2, int scale) {
    if (scale < 0) {
      throw new IllegalArgumentException("The scale must be a positive integer or zero");
    }
    BigDecimal b1 = new BigDecimal(v1);
    BigDecimal b2 = new BigDecimal(v2);
    return b1.remainder(b2).setScale(scale, BigDecimal.ROUND_HALF_UP).toString();
  }

  /**
   * 取余数 BigDecimal
   *
   * @param v1 被除数
   * @param v2 除数
   * @param scale 小数点后保留几位
   * @return 余数
   */
  public static BigDecimal remainder(BigDecimal v1, BigDecimal v2, int scale) {
    if (scale < 0) {
      throw new IllegalArgumentException("The scale must be a positive integer or zero");
    }
    return v1.remainder(v2).setScale(scale, BigDecimal.ROUND_HALF_UP);
  }

  /**
   * 比较大小
   *
   * @param v1 被比较数
   * @param v2 比较数
   * @return 如果v1 大于v2 则 返回true 否则false
   */
  public static boolean compare(String v1, String v2) {
    BigDecimal b1 = new BigDecimal(v1);
    BigDecimal b2 = new BigDecimal(v2);
    int bj = b1.compareTo(b2);
    boolean res;
    res = bj > 0;
    return res;
  }
}
