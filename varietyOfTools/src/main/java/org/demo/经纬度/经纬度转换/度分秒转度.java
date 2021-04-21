package org.demo.经纬度.经纬度转换;

import org.apache.commons.lang.StringUtils;

public class 度分秒转度 {
  /**
   * 经纬度转换 ，度分秒转度
   *
   * @param str 1
   * @author Cai_YF
   * @return
   */
  public static String Dms2D(String jwd) {
    if (StringUtils.isNotEmpty(jwd) && (jwd.contains("°"))) { // 如果不为空并且存在度单位
      // 计算前进行数据处理
      jwd = jwd.replace("E", "").replace("N", "").replace(":", "").replace("：", "");
      double d = 0, m = 0, s = 0;
      d = Double.parseDouble(jwd.split("°")[0]);
      // 不同单位的分，可扩展
      if (jwd.contains("′")) { // 正常的′
        m = Double.parseDouble(jwd.split("°")[1].split("′")[0]);
      } else if (jwd.contains("'")) { // 特殊的'
        m = Double.parseDouble(jwd.split("°")[1].split("'")[0]);
      }
      // 不同单位的秒，可扩展
      if (jwd.contains("″")) { // 正常的″
        // 有时候没有分 如：112°10.25″
        s =
            jwd.contains("′")
                ? Double.parseDouble(jwd.split("′")[1].split("″")[0])
                : Double.parseDouble(jwd.split("°")[1].split("″")[0]);
      } else if (jwd.contains("''")) { // 特殊的''
        // 有时候没有分 如：112°10.25''
        s =
            jwd.contains("'")
                ? Double.parseDouble(jwd.split("'")[1].split("''")[0])
                : Double.parseDouble(jwd.split("°")[1].split("''")[0]);
      }

      double d1 = d + m / 60 + s / 60 / 60;
      if (StringUtils.containsIgnoreCase(jwd, "W") || StringUtils.containsIgnoreCase(jwd, "S")) {
        d1 = -d1;
      }
      jwd = String.valueOf(d1);
      // 使用BigDecimal进行加减乘除
      /*BigDecimal bd = new BigDecimal("60");
      BigDecimal d = new BigDecimal(jwd.contains("°")?jwd.split("°")[0]:"0");
      BigDecimal m = new BigDecimal(jwd.contains("′")?jwd.split("°")[1].split("′")[0]:"0");
      BigDecimal s = new BigDecimal(jwd.contains("″")?jwd.split("′")[1].split("″")[0]:"0");
      //divide相除可能会报错（无限循环小数），要设置保留小数点
      jwd = String.valueOf(d.add(m.divide(bd,6,BigDecimal.ROUND_HALF_UP)
                 .add(s.divide(bd.multiply(bd),6,BigDecimal.ROUND_HALF_UP))));*/
    }
    return jwd;
  }

  public static void main(String[] args) {
//    36°56′57″N，76°19′58″W
    String s1 = "25°32′6.36″N";
    //    String dms = "129°11′21.40″E";
    //    String dms = "113°12′39.6″E";
    String dms = "76°19′58″W";
    String s = 度分秒转度.Dms2D(dms);
    System.out.println(s);
  }
}
