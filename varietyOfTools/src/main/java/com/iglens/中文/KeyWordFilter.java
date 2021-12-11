package com.iglens.中文;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 【匹配度可以，速度较慢】
 * Java关键字过滤：http://blog.csdn.net/linfssay/article/details/7599262
 * @author ShengDecheng
 *
 */
public class KeyWordFilter {

  private static Pattern pattern = null;
  private static int keywordsCount = 0;

  // 从words.properties初始化正则表达式字符串
  private static void initPattern() {
    StringBuffer patternBuffer = new StringBuffer();
    try {
      //words.properties
      InputStream in = KeyWordFilter.class.getClassLoader().getResourceAsStream("keywords.properties");
      Properties property = new Properties();
      property.load(in);
      Enumeration<?> enu = property.propertyNames();
      patternBuffer.append("(");
      while (enu.hasMoreElements()) {
        String scontent = (String) enu.nextElement();
        patternBuffer.append(scontent + "|");
        //System.out.println(scontent);
        keywordsCount ++;
      }
      patternBuffer.deleteCharAt(patternBuffer.length() - 1);
      patternBuffer.append(")");
      //System.out.println(patternBuffer);
      // unix换成UTF-8
      // pattern = Pattern.compile(new
      // String(patternBuf.toString().getBytes("ISO-8859-1"), "UTF-8"));
      // win下换成gb2312
      // pattern = Pattern.compile(new String(patternBuf.toString()
      // .getBytes("ISO-8859-1"), "gb2312"));
      // 装换编码
      pattern = Pattern.compile(patternBuffer.toString());
    } catch (IOException ioEx) {
      ioEx.printStackTrace();
    }
  }

  private static String doFilter(String str) {
    Matcher m = pattern.matcher(str);
//        while (m.find()) {// 查找符合pattern的字符串
//            System.out.println("The result is here :" + m.group());
//        }
    // 选择替换方式，这里以* 号代替
    str = m.replaceAll("*");
    return str;
  }

  public static void main(String[] args) {
    long startNumer = System.currentTimeMillis();
    initPattern();
    //String str = "我日，艹，fuck，你妹的 干啥呢";
    System.out.println("敏感词的数量：" + keywordsCount);
    String str = "你好呀，我这里有敏感词汇，来过滤我呀";
    System.out.println("被检测字符串长度:"+str.length());
    str = doFilter(str);
    //高效Java敏感词、关键词过滤工具包_过滤非法词句：http://blog.csdn.net/ranjio_z/article/details/6299834
    //FilteredResult result = WordFilterUtil.filterText(str, '*');
    long endNumber = System.currentTimeMillis();
    System.out.println("总共耗时:"+(endNumber-startNumer)+"ms");
    System.out.println("替换后的字符串为:\n"+str);
    //System.out.println("替换后的字符串为:\n"+result.getFilteredContent());
    //System.out.println("替换后的字符串为1:\n"+result.getOriginalContent());
    //System.out.println("替换后的字符串为2:\n"+result.getBadWords());
  }
}
