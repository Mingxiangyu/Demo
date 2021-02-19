package org.demo.时间;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class 时间解析 {

  public static void main(String args[]) {
    Date newTime = new Date();
    // 设置时间格式
    SimpleDateFormat sdf1 = new SimpleDateFormat("y-M-d h:m:s a E");
    SimpleDateFormat sdf2 = new SimpleDateFormat("yy-MM-dd hh:mm:ss a E");
    SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MMM-ddd hhh:mmm:sss a E");
    SimpleDateFormat sdf4 = new SimpleDateFormat("yyyyy-MMMM-dddd hhhh:mmmm:ssss a E");
    SimpleDateFormat sdf5 = new SimpleDateFormat("yyyyMMdd");

    // 获取的时间，是本机的时间
    String formatDate1 = sdf1.format(newTime);
    String formatDate2 = sdf2.format(newTime);
    String formatDate3 = sdf3.format(newTime);
    String formatDate4 = sdf4.format(newTime);

    try {
      Date parse = sdf5.parse("20210205");
      int year = parse.getYear();
      String format = sdf2.format(parse);
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(parse);
      int calendaryear = calendar.get(Calendar.YEAR);
      System.out.println("year:"+calendaryear);

      System.out.println(year);
      System.out.println(format);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    System.out.println(formatDate1);
    System.out.println(formatDate2);
    System.out.println(formatDate3);
    System.out.println(formatDate4);
  }
  //  字符串"yyyy-MM-dd hh:mm:ss",其中:

  //  yyyy : 代表年(不去区分大小写) 假设年份为 2017

  //      "y" , "yyy" , "yyyy" 匹配的都是4位完整的年 如 : "2017"

  //      "yy" 匹配的是年分的后两位 如 : "15"

  //  超过4位,会在年份前面加"0"补位 如 "YYYYY"对应"02017"

  //  MM : 代表月(只能使用大写) 假设月份为 9

  //      "M" 对应 "9"

  //      "MM" 对应 "09"

  //      "MMM" 对应 "Sep"

  //      "MMMM" 对应 "Sep"

  //  超出3位,仍然对应 "September"

  //  dd : 代表日(只能使用小写) 假设为13号

  //    "d" , "dd" 都对应 "13"

  //  超出2位,会在数字前面加"0"补位. 例如 "dddd" 对应 "0013"

  //  hh : 代表时(区分大小写,大写为24进制计时,小写为12进制计时) 假设为15时

  //    "H" , "HH" 都对应 "15" , 超出2位,会在数字前面加"0"补位. 例如 "HHHH" 对应 "0015"

  //      "h" 对应 "3"

  //      "hh" 对应 "03" , 超出2位,会在数字前面加"0"补位. 例如 "hhhh" 对应 "0003"

  //  mm : 代表分(只能使用小写) 假设为32分

  //    "m" , "mm" 都对应 "32" ,  超出2位,会在数字前面加"0"补位. 例如 "mmmm" 对应 "0032"

  //  ss : 代表秒(只能使用小写) 假设为15秒

  //    "s" , "ss" 都对应 "15" , 超出2位,会在数字前面加"0"补位. 例如 "ssss" 对应 "0015"

  //  E : 代表星期(只能使用大写) 假设为 Sunday

  //    "E" , "EE" , "EEE" 都对应 "Sun"

  //      "EEEE" 对应 "Sunday" , 超出4位 , 仍然对应 "Sunday"

  //  a : 代表上午还是下午,如果是上午就对应 "AM" , 如果是下午就对应 "PM"
}
