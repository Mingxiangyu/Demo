package org.demo.时间;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;

public class 时间合法性验证 {
  public static void main(String[] args) {
    String s = "2020-02-30 23:59:61";
    String date = getDate(s);
    System.out.println(date);
  }

  public static String getDate(String dateString) {
    //  日期格式必须为yyyy-MM-dd HH:mm:ss
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    try {
      Date parse = sdf.parse(dateString);
      String format = sdf.format(parse);
      if (!StringUtils.equals(format, dateString)) {
        throw new RuntimeException("format:" + format);
      }
      return format;
    } catch (ParseException e) {
      throw new RuntimeException("接收数据中时间不匹配！");
    }
  }
}
