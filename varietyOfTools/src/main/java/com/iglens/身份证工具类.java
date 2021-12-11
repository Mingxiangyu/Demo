package com.iglens;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 身份证工具类
 *
 * <p>先来了解一下身份证的组成规则： <br>
 * 1．号码的结构 公民身份号码是特征组合码，由十七位数字本体码和一位校验码组成。排列顺序从左至右依次为：六位数字地址码，八位数字出生日期码，三位数字顺序码和一位数字校验码。<br>
 * 2．地址码 表示编码对象常住户口所在县（市、旗、区）的行政区划代码，按GB/T2260的规定执行。<br>
 * 3．出生日期码 表示编码对象出生的年、月、日，按GB/T7408的规定执行，年、月、日代码之间不用分隔符。 <br>
 * 4．顺序码 表示在同一地址码所标识的区域范围内，对同年、同月、同日出生的人编定的顺序号，顺序码的奇数分配给男性，偶数分配给女性。<br>
 * 5．校验码 根据前面十七位数字码，按照ISO 7064:1983.MOD 11-2校验码计算出来的检验码。<br>
 *
 * <p>
 *
 * @author chen.jh
 * @version 1.0, 2018-01-04
 */
public class 身份证工具类  {

  /** 中国公民身份证号码最小长度。 */
  public static final int CHINA_ID_MIN_LENGTH = 15;

  /** 中国公民身份证号码最大长度。 */
  public static final int CHINA_ID_MAX_LENGTH = 18;

  /** 省、直辖市代码表 */
  public static final String[] CITY_CODE = {
    "11", "12", "13", "14", "15", "21", "22", "23", "31", "32", "33", "34", "35", "36", "37", "41",
    "42", "43", "44", "45", "46", "50", "51", "52", "53", "54", "61", "62", "63", "64", "65", "71",
    "81", "82", "91"
  };

  /** 每位加权因子 */
  public static final int[] POWER = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};

  /** 第18位校检码 */
  public static final String[] VERIFY_CODE = {"1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"};
  /** 最低年限 */
  public static final int MIN = 1930;

  public static Map<String, String> cityCodes = new HashMap<>();
  /** 台湾身份首字母对应数字 */
  public static Map<String, Integer> twFirstCode = new HashMap<>();
  /** 香港身份首字母对应数字 */
  public static Map<String, Integer> hkFirstCode = new HashMap<>();

  static {
    cityCodes.put("11", "北京");
    cityCodes.put("12", "天津");
    cityCodes.put("13", "河北");
    cityCodes.put("14", "山西");
    cityCodes.put("15", "内蒙古");
    cityCodes.put("21", "辽宁");
    cityCodes.put("22", "吉林");
    cityCodes.put("23", "黑龙江");
    cityCodes.put("31", "上海");
    cityCodes.put("32", "江苏");
    cityCodes.put("33", "浙江");
    cityCodes.put("34", "安徽");
    cityCodes.put("35", "福建");
    cityCodes.put("36", "江西");
    cityCodes.put("37", "山东");
    cityCodes.put("41", "河南");
    cityCodes.put("42", "湖北");
    cityCodes.put("43", "湖南");
    cityCodes.put("44", "广东");
    cityCodes.put("45", "广西");
    cityCodes.put("46", "海南");
    cityCodes.put("50", "重庆");
    cityCodes.put("51", "四川");
    cityCodes.put("52", "贵州");
    cityCodes.put("53", "云南");
    cityCodes.put("54", "西藏");
    cityCodes.put("61", "陕西");
    cityCodes.put("62", "甘肃");
    cityCodes.put("63", "青海");
    cityCodes.put("64", "宁夏");
    cityCodes.put("65", "新疆");
    cityCodes.put("71", "台湾");
    cityCodes.put("81", "香港");
    cityCodes.put("82", "澳门");
    cityCodes.put("91", "国外");
    twFirstCode.put("A", 10);
    twFirstCode.put("B", 11);
    twFirstCode.put("C", 12);
    twFirstCode.put("D", 13);
    twFirstCode.put("E", 14);
    twFirstCode.put("F", 15);
    twFirstCode.put("G", 16);
    twFirstCode.put("H", 17);
    twFirstCode.put("J", 18);
    twFirstCode.put("K", 19);
    twFirstCode.put("L", 20);
    twFirstCode.put("M", 21);
    twFirstCode.put("N", 22);
    twFirstCode.put("P", 23);
    twFirstCode.put("Q", 24);
    twFirstCode.put("R", 25);
    twFirstCode.put("S", 26);
    twFirstCode.put("T", 27);
    twFirstCode.put("U", 28);
    twFirstCode.put("V", 29);
    twFirstCode.put("X", 30);
    twFirstCode.put("Y", 31);
    twFirstCode.put("W", 32);
    twFirstCode.put("Z", 33);
    twFirstCode.put("I", 34);
    twFirstCode.put("O", 35);
    hkFirstCode.put("A", 1);
    hkFirstCode.put("B", 2);
    hkFirstCode.put("C", 3);
    hkFirstCode.put("R", 18);
    hkFirstCode.put("U", 21);
    hkFirstCode.put("Z", 26);
    hkFirstCode.put("X", 24);
    hkFirstCode.put("W", 23);
    hkFirstCode.put("O", 15);
    hkFirstCode.put("N", 14);
  }

  /**
   * 将15位身份证号码转换为18位
   *
   * @param idCard 15位身份编码
   * @return 18位身份编码
   */
  public static String conver15CardTo18(String idCard) {
    String idCard18;
    if (idCard.length() != CHINA_ID_MIN_LENGTH) {
      return null;
    }
    if (isNum(idCard)) {
      // 获取出生年月日
      String birthday = idCard.substring(6, 12);
      Date birthDate = null;
      try {
        birthDate = new SimpleDateFormat("yyMMdd").parse(birthday);
      } catch (ParseException e) {
        e.printStackTrace();
      }
      Calendar cal = Calendar.getInstance();
      if (birthDate != null) {
        cal.setTime(birthDate);
      }
      // 获取出生年(完全表现形式,如：2010)
      String sYear = String.valueOf(cal.get(Calendar.YEAR));
      idCard18 = idCard.substring(0, 6) + sYear + idCard.substring(8);
      // 转换字符数组
      char[] cArr = idCard18.toCharArray();
      if (cArr != null) {
        int[] iCard = converCharToInt(cArr);
        int iSum17 = getPowerSum(iCard);
        // 获取校验位
        String sVal = getCheckCode18(iSum17);
        if (sVal.length() > 0) {
          idCard18 += sVal;
        } else {
          return null;
        }
      }
    } else {
      return null;
    }
    return idCard18;
  }

  /**
   * 根据正则校验18位身份证
   *
   * @param idCard 身份编码
   * @return
   */
  public static boolean validateIDCardByRegex(String idCard) {
    String curYear = "" + Calendar.getInstance().get(Calendar.YEAR);
    int y3 = Integer.parseInt(curYear.substring(2, 3));
    int y4 = Integer.parseInt(curYear.substring(3, 4));
    // 43 0103 1973 0 9 30 051 9
    return idCard.matches(
        "^(1[1-5]|2[1-3]|3[1-7]|4[1-6]|5[0-4]|6[1-5]|71|8[1-2])\\d{4}(19\\d{2}|20([0-"
            + (y3 - 1)
            + "][0-9]|"
            + y3
            + "[0-"
            + y4
            + "]))(((0[1-9]|1[0-2])(0[1-9]|[1-2][0-9]|3[0-1])))\\d{3}([0-9]|x|X)$");
    // 44 1825 1988 0 7 1 3 003 4
  }

  /**
   * 验证身份证是否合法
   *
   * @param idCard 身份证号
   * @return 是否合法
   */
  public static boolean validateIdCard(String idCard) {
    String card = idCard.trim();
    if (validateIdCard18(card)) {
      return true;
    }
    if (validateIdCard15(card)) {
      return true;
    }
    String[] cardval = validateIdCard10(card);
    if (cardval != null) {
      return "true".equals(cardval[2]);
    }
    return false;
  }

  /**
   * 验证大陆身份证是否合法
   *
   * @param idCard
   * @return
   */
  public static boolean validateMainLandIdCard(String idCard) {
    String card = idCard.trim();
    if (validateIdCard18(card)) {
      return true;
    }
    return validateIdCard15(card);
  }

  /**
   * 验证18位身份编码是否合法
   *
   * @param idCard 身份编码
   * @return 是否合法
   */
  public static boolean validateIdCard18(String idCard) {
    boolean bTrue = false;
    if (idCard.length() == CHINA_ID_MAX_LENGTH) {
      // 前17位
      String code17 = idCard.substring(0, 17);
      // 第18位
      String code18 = idCard.substring(17, CHINA_ID_MAX_LENGTH);
      if (isNum(code17)) {
        char[] cArr = code17.toCharArray();
        if (cArr != null) {
          int[] iCard = converCharToInt(cArr);
          int iSum17 = getPowerSum(iCard);
          // 获取校验位
          String val = getCheckCode18(iSum17);
          if (val.length() > 0) {
            if (val.equalsIgnoreCase(code18)) {
              bTrue = true;
            }
          }
        }
      }
    }
    // 校验通过 且 正则校验通过 且 可以获取到省份信息
    return bTrue && validateIDCardByRegex(idCard) && getProvinceByIdCard(idCard) != null;
  }

  /**
   * 验证15位身份编码是否合法
   *
   * @param idCard 身份编码
   * @return 是否合法
   */
  public static boolean validateIdCard15(String idCard) {
    if (idCard.length() != CHINA_ID_MIN_LENGTH) {
      return false;
    }
    if (isNum(idCard)) {
      String proCode = idCard.substring(0, 2);
      if (cityCodes.get(proCode) == null) {
        return false;
      }
      String birthCode = idCard.substring(6, 12);
      Date birthDate = null;
      try {
        birthDate = new SimpleDateFormat("yy").parse(birthCode.substring(0, 2));
      } catch (ParseException e) {
        e.printStackTrace();
      }
      Calendar cal = Calendar.getInstance();
      if (birthDate != null) {
        cal.setTime(birthDate);
      }
      return valiDate(
          cal.get(Calendar.YEAR),
          Integer.parseInt(birthCode.substring(2, 4)),
          Integer.parseInt(birthCode.substring(4, 6)));
    } else {
      return false;
    }
  }

  /**
   * 验证10位身份编码是否合法
   *
   * @param idCard 身份编码
   * @return 身份证信息数组
   *     <p>[0] - 台湾、澳门、香港 [1] - 性别(男M,女F,未知N) [2] - 是否合法(合法true,不合法false) 若不是身份证件号码则返回null
   */
  public static String[] validateIdCard10(String idCard) {
    String[] info = new String[3];
    String card = idCard.replaceAll("[\\(|\\)]", "");
    if (card.length() != 8 && card.length() != 9 && idCard.length() != 10) {
      return null;
    }
    if (idCard.matches("^[a-zA-Z][0-9]{9}")) { // 台湾info[0]="台湾";//
      // System.out.println("11111");String char2=idCard.substring(1,2);if(char2.equals("1"))info[1]="M";System.out.println("MMMMMMM");elseif(char2.equals("2"))info[1]="F";System.out.println("FFFFFFF");elseinfo[1]="N";info[2]="false";System.out.println("NNNN");returninfo;info[2]=validateTWCard(idCard)?"true":"false";elseif(idCard.matches("[1|5|7][0−9]6(?[0−9A−Z])?")) { // 澳门
      info[0] = "澳门";
      info[1] = "N";
    } else if (idCard.matches("^[A-Z]{1,2}[0-9]{6}\\(?[0-9A]\\)?$")) { // 香港
      info[0] = "香港";
      info[1] = "N";
      info[2] = validateHKCard(idCard) ? "true" : "false";
    } else {
      return null;
    }
    return info;
  }

  /**
   * 验证台湾身份证号码
   *
   * @param idCard 身份证号码
   * @return 验证码是否符合
   */
  public static boolean validateTWCard(String idCard) {
    String start = idCard.substring(0, 1);
    String mid = idCard.substring(1, 9);
    String end = idCard.substring(9, 10);
    Integer iStart = twFirstCode.get(start);
    int sum = iStart / 10 + (iStart % 10) * 9;
    char[] chars = mid.toCharArray();
    int iflag = 8;
    for (char c : chars) {
      sum = sum + Integer.parseInt(c + "") * iflag;
      iflag--;
    }
    return (sum % 10 == 0 ? 0 : (10 - sum % 10)) == Integer.valueOf(end);
  }

  /**
   * 验证香港身份证号码(存在Bug，部份特殊身份证无法检查)
   *
   * <p>身份证前2位为英文字符，如果只出现一个英文字符则表示第一位是空格，对应数字58 前2位英文字符A-Z分别对应数字10-35 最后一位校验码为0-9的数字加上字符"A"，"A"代表10
   *
   * <p>将身份证号码全部转换为数字，分别对应乘9-1相加的总和，整除11则证件号码有效
   *
   * @param idCard 身份证号码
   * @return 验证码是否符合
   */
  public static boolean validateHKCard(String idCard) {
    String card = idCard.replaceAll("[\\(|\\)]", "");
    int sum = 0;
    if (card.length() == 9) {
      sum =
          ((int) card.substring(0, 1).toUpperCase().toCharArray()[0] - 55) * 9
              + ((int) card.substring(1, 2).toUpperCase().toCharArray()[0] - 55) * 8;
      card = card.substring(1, 9);
    } else {
      sum = 522 + ((int) card.substring(0, 1).toUpperCase().toCharArray()[0] - 55) * 8;
    }
    String mid = card.substring(1, 7);
    String end = card.substring(7, 8);
    char[] chars = mid.toCharArray();
    Integer iflag = 7;
    for (char c : chars) {
      sum = sum + Integer.parseInt(c + "") * iflag;
      iflag--;
    }
    if ("A".equals(end.toUpperCase())) {
      sum = sum + 10;
    } else {
      sum = sum + Integer.parseInt(end);
    }
    return sum % 11 == 0;
  }

  /**
   * 将字符数组转换成数字数组
   *
   * @param ca 字符数组
   * @return 数字数组
   */
  public static int[] converCharToInt(char[] ca) {
    int len = ca.length;
    int[] iArr = new int[len];
    try {
      for (int i = 0; i < len; i++) {
        iArr[i] = Integer.parseInt(String.valueOf(ca[i]));
      }
    } catch (NumberFormatException e) {
      e.printStackTrace();
    }
    return iArr;
  }

  /**
   * 将身份证的每位和对应位的加权因子相乘之后，再得到和值
   *
   * @param iArr
   * @return 身份证编码。
   */
  public static int getPowerSum(int[] iArr) {
    int iSum = 0;
    if (POWER.length == iArr.length) {
      for (int i = 0; i < iArr.length; i++) {
        for (int j = 0; j < POWER.length; j++) {
          if (i == j) {
            iSum = iSum + iArr[i] * POWER[j];
          }
        }
      }
    }
    return iSum;
  }

  /**
   * 将power和值与11取模获得余数进行校验码判断
   *
   * @param iSum
   * @return 校验位
   */
  public static String getCheckCode18(int iSum) {
    String sCode = "";
    switch (iSum % 11) {
      case 10:
        sCode = "2";
        break;
      case 9:
        sCode = "3";
        break;
      case 8:
        sCode = "4";
        break;
      case 7:
        sCode = "5";
        break;
      case 6:
        sCode = "6";
        break;
      case 5:
        sCode = "7";
        break;
      case 4:
        sCode = "8";
        break;
      case 3:
        sCode = "9";
        break;
      case 2:
        sCode = "x";
        break;
      case 1:
        sCode = "0";
        break;
      case 0:
        sCode = "1";
        break;
    }
    return sCode;
  }

  /**
   * 根据身份编号获取年龄
   *
   * @param idCard 身份编号
   * @return 年龄
   */
  public static int getAgeByIdCard(String idCard) {
    int iAge = 0;
    if (idCard.length() == CHINA_ID_MIN_LENGTH) {
      idCard = conver15CardTo18(idCard);
    }
    String year = idCard.substring(6, 10);
    Calendar cal = Calendar.getInstance();
    int iCurrYear = cal.get(Calendar.YEAR);
    iAge = iCurrYear - Integer.parseInt(year);
    return iAge;
  }

  /**
   * 根据身份编号获取生日
   *
   * @param idCard 身份编号
   * @return 生日(yyyyMMdd)
   */
  public static String getBirthByIdCard(String idCard) {
    int len = idCard.length();
    if (len < CHINA_ID_MIN_LENGTH) {
      return null;
    } else if (len == CHINA_ID_MIN_LENGTH) {
      idCard = conver15CardTo18(idCard);
    }
    return idCard.substring(6, 14);
  }

  /**
   * 根据身份编号获取生日
   *
   * @param idCard 身份编号
   * @return 生日(yyyy-MM-dd)
   */
  public static String getBirthByIdCard2(String idCard) {
    int len = idCard.length();
    if (len < CHINA_ID_MIN_LENGTH) {
      return null;
    } else if (len == CHINA_ID_MIN_LENGTH) {
      idCard = conver15CardTo18(idCard);
    }
    return idCard.substring(6, 10)
        + "-"
        + idCard.substring(10, 12)
        + "-"
        + idCard.substring(12, 14);
  }

  /**
   * 根据身份编号获取生日年
   *
   * @param idCard 身份编号
   * @return 生日(yyyy)
   */
  public static Integer getYearByIdCard(String idCard) {
    int len = idCard.length();
    if (len < CHINA_ID_MIN_LENGTH) {
      return null;
    } else if (len == CHINA_ID_MIN_LENGTH) {
      idCard = conver15CardTo18(idCard);
    }
    return Integer.valueOf(idCard.substring(6, 10));
  }

  /**
   * 根据身份编号获取生日月
   *
   * @param idCard 身份编号
   * @return 生日(MM)
   */
  public static Short getMonthByIdCard(String idCard) {
    int len = idCard.length();
    if (len < CHINA_ID_MIN_LENGTH) {
      return null;
    } else if (len == CHINA_ID_MIN_LENGTH) {
      idCard = conver15CardTo18(idCard);
    }
    return Short.valueOf(idCard.substring(10, 12));
  }

  /**
   * 根据身份编号获取生日天
   *
   * @param idCard 身份编号
   * @return 生日(dd)
   */
  public static Short getDateByIdCard(String idCard) {
    int len = idCard.length();
    if (len < CHINA_ID_MIN_LENGTH) {
      return null;
    } else if (len == CHINA_ID_MIN_LENGTH) {
      idCard = conver15CardTo18(idCard);
    }
    return Short.valueOf(idCard.substring(12, 14));
  }

  /**
   * 根据身份编号获取性别
   *
   * @param idCard 身份编号
   * @return 性别(M-男，F-女，N-未知)
   */
  public static String getGenderByIdCard(String idCard) {
    String sGender = "N";
    if (idCard.length() == CHINA_ID_MIN_LENGTH) {
      idCard = conver15CardTo18(idCard);
    }
    String sCardNum = idCard.substring(16, 17);
    if (Integer.parseInt(sCardNum) % 2 != 0) {
      sGender = "M";
    } else {
      sGender = "F";
    }
    return sGender;
  }

  /**
   * 根据身份编号获取户籍省份
   *
   * @param idCard 身份编码
   * @return 省级编码。
   */
  public static String getProvinceByIdCard(String idCard) {
    int len = idCard.length();
    String sProvince = null;
    String sProvinNum = "";
    if (len == CHINA_ID_MIN_LENGTH || len == CHINA_ID_MAX_LENGTH) {
      sProvinNum = idCard.substring(0, 2);
    }
    sProvince = cityCodes.get(sProvinNum);
    return sProvince;
  }

  /**
   * 数字验证
   *
   * @param val
   * @return 提取的数字。
   */
  public static boolean isNum(String val) {
    return val != null && !"".equals(val) && val.matches("^[0-9]*$");
  }

  /**
   * 验证小于当前日期 是否有效
   *
   * @param iYear 待验证日期(年)
   * @param iMonth 待验证日期(月 1-12)
   * @param iDate 待验证日期(日)
   * @return 是否有效
   */
  public static boolean valiDate(int iYear, int iMonth, int iDate) {
    Calendar cal = Calendar.getInstance();
    int year = cal.get(Calendar.YEAR);
    int datePerMonth;
    if (iYear < MIN || iYear >= year) {
      return false;
    }
    if (iMonth < 1 || iMonth > 12) {
      return false;
    }
    switch (iMonth) {
      case 4:
      case 6:
      case 9:
      case 11:
        datePerMonth = 30;
        break;
      case 2:
        boolean dm =
            ((iYear % 4 == 0 && iYear % 100 != 0) || (iYear % 400 == 0))
                && (iYear > MIN && iYear < year);
        datePerMonth = dm ? 29 : 28;
        break;
      default:
        datePerMonth = 31;
    }
    return (iDate >= 1) && (iDate <= datePerMonth);
  }

  /**
   * 根据身份证号，自动获取对应的星座
   *
   * @param idCard 身份证号码
   * @return 星座
   */
  public static String getConstellationById(String idCard) {
    if (!validateIdCard(idCard)) {
      return "";
    }
    int month = getMonthByIdCard(idCard);
    int day = getDateByIdCard(idCard);
    String strValue = "";
    if ((month == 1 && day >= 20) || (month == 2 && day <= 18)) {
      strValue = "水瓶座";
    } else if ((month == 2 && day >= 19) || (month == 3 && day <= 20)) {
      strValue = "双鱼座";
    } else if ((month == 3 && day > 20) || (month == 4 && day <= 19)) {
      strValue = "白羊座";
    } else if ((month == 4 && day >= 20) || (month == 5 && day <= 20)) {
      strValue = "金牛座";
    } else if ((month == 5 && day >= 21) || (month == 6 && day <= 21)) {
      strValue = "双子座";
    } else if ((month == 6 && day > 21) || (month == 7 && day <= 22)) {
      strValue = "巨蟹座";
    } else if ((month == 7 && day > 22) || (month == 8 && day <= 22)) {
      strValue = "狮子座";
    } else if ((month == 8 && day >= 23) || (month == 9 && day <= 22)) {
      strValue = "处女座";
    } else if ((month == 9 && day >= 23) || (month == 10 && day <= 23)) {
      strValue = "天秤座";
    } else if ((month == 10 && day > 23) || (month == 11 && day <= 22)) {
      strValue = "天蝎座";
    } else if ((month == 11 && day > 22) || (month == 12 && day <= 21)) {
      strValue = "射手座";
    } else if ((month == 12 && day > 21) || (month == 1 && day <= 19)) {
      strValue = "魔羯座";
    }
    return strValue;
  }

  /**
   * 根据身份证号，自动获取对应的生肖
   *
   * @param idCard 身份证号码
   * @return 生肖
   */
  public static String getZodiacById(String idCard) {
    if (!validateIdCard(idCard)) {
      return "";
    }
    String[] sSX = {"猪", "鼠", "牛", "虎", "兔", "龙", "蛇", "马", "羊", "猴", "鸡", "狗"};
    int year = getYearByIdCard(idCard);
    int end = 3;
    int x = (year - end) % 12;
    String retValue = "";
    retValue = sSX[x];
    return retValue;
  }

  /**
   * 根据身份证号，自动获取对应的天干地支
   *
   * @param idCard 身份证号码
   * @return 天干地支
   */
  public static String getChineseEraById(String idCard) {
    if (!validateIdCard(idCard)) {
      return "";
    }
    String sTG[] = {"癸", "甲", "乙", "丙", "丁", "戊", "己", "庚", "辛", "任"};
    String sDZ[] = {"亥", "子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌"};
    int year = getYearByIdCard(idCard);
    int i = (year - 3) % 10;
    int j = (year - 3) % 12;
    String retValue = "";
    retValue = sTG[i] + sDZ[j];
    return retValue;
  }

  public static void main(String[] args) {
    String idCard = "411504199809228934";
    System.out.println("验证大陆身份证：" + validateIdCard(idCard));
    System.out.println("验证香港身份证：" + validateHKCard("Z2673259"));
    System.out.println("获取性别：" + getGenderByIdCard(idCard));
    System.out.println("获取年龄：" + getAgeByIdCard(idCard));
    System.out.println("获取生日(yyyyMMdd)：" + getBirthByIdCard(idCard));
    System.out.println("身份编号(yyyy-MM-dd)：" + getBirthByIdCard2(idCard));
    System.out.println("获取生日月：" + getMonthByIdCard(idCard));
    System.out.println("获取生日天：" + getDateByIdCard(idCard));
    System.out.println("获取对应的星座：" + getConstellationById(idCard));
    System.out.println("获取对应的生肖：" + getZodiacById(idCard));
    System.out.println("获取对应的天干地支：" + getChineseEraById(idCard));
    System.out.println("获取户籍省份：" + getProvinceByIdCard(idCard));
  }
}
