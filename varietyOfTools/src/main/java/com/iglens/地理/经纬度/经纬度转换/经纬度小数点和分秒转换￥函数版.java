package com.iglens.地理.经纬度.经纬度转换;

import org.apache.commons.lang.StringUtils;

public class 经纬度小数点和分秒转换￥函数版 {

  public static void main(String[] args) {
    String coord = "180°24″";
    String s = convertCoord(coord);
    System.out.println(s); // 180.00666666666666
  }

  /**
   * 经纬度转换 ，度分秒转度
   *
   * @param jwd 度分秒
   * @return 度
   */
  public static String convertCoord(String jwd) {

    // 如果不为空并且存在度单位
    if (StringUtils.isNotEmpty(jwd) && (jwd.contains("°"))) {
      // 计算前进行数据处理
      jwd = jwd.replace("E", "").replace("N", "").replace(":", "").replace("：", "");
      double d, m = 0, s = 0;
      d = Double.parseDouble(jwd.split("°")[0]);
      //      // 不同单位的分，可扩展
      if (jwd.contains("′")) {
        // 正常的′
        m = Double.parseDouble(jwd.split("°")[1].split("′")[0]);
      } else if (jwd.contains("'")) {
        // 特殊的'
        m = Double.parseDouble(jwd.split("°")[1].split("'")[0]);
      }
      // 不同单位的秒，可扩展
      if (jwd.contains("″")) {
        // 正常的″
        // 有时候没有分 如：112°10.25″
        s =
            jwd.contains("′")
                ? Double.parseDouble(jwd.split("′")[1].split("″")[0])
                : Double.parseDouble(jwd.split("°")[1].split("″")[0]);
      } else {
        // 特殊的''
        String s1 = "''";
        if (jwd.contains(s1)) {
          // 有时候没有分 如：112°10.25''
          s =
              jwd.contains("'")
                  ? Double.parseDouble(jwd.split("'")[1].split(s1)[0])
                  : Double.parseDouble(jwd.split("°")[1].split(s1)[0]);
        }
      }
      // 计算并转换为string
      double d1 = d + m / 60 + s / 60 / 60;
      // W 西 N 北 S 南 E 东
      if (StringUtils.containsIgnoreCase(jwd, "W") || StringUtils.containsIgnoreCase(jwd, "S")) {
        d1 = -d1;
      }
      jwd = String.valueOf(d1);
    }
    return jwd;
  }
}
