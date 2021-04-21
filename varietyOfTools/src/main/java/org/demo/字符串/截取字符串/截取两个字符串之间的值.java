package org.demo.字符串.截取字符串;

import static java.util.regex.Pattern.compile;

import java.util.regex.Matcher;
import java.util.regex.Pattern;



  public class 截取两个字符串之间的值 {
    public static void main(String[] args){
      String str = "abc<icon>def</icon>deftfh<icon>a</icon>";

      Pattern p= compile("<icon>(\\w+)</icon>");
      Matcher m=p.matcher(str);
      while(m.find()){
        System.out.println(m.group(1));

      }


    }
  }

