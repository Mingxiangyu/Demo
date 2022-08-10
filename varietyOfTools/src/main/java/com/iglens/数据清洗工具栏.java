package com.iglens;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 数据清洗工具栏
 */
public class 数据清洗工具栏 {

  /**
   * 清洗html标签
   */
  public static String cleanHtmlLabel(String inputString) {
    String htmlStr = inputString; // 含html标签的字符串
    String textStr = "";
    Pattern pScript;
    Matcher mScript;
    Pattern pStyle;
    Matcher mStyle;
    Pattern pHtml;
    Matcher mHtml;
    Pattern pHtml1;
    Matcher mHtml1;
    try {
      String regExScript = "<[//s]*?script[^>]*?>[//s//S]*?<[//s]*?///[//s]*?script[//s]*?>"; // 定义script的正则表达式{或<script[^>]*?>[//s//S]*?<///script>
      String regExStyle = "<[//s]*?style[^>]*?>[//s//S]*?<[//s]*?///[//s]*?style[//s]*?>"; // 定义style的正则表达式{或<style[^>]*?>[//s//S]*?<///style>
      String regExHtml = "<[^>]+>"; // 定义HTML标签的正则表达式
      String regExHtml1 = "<[^>]+";
      pScript = Pattern.compile(regExScript, Pattern.CASE_INSENSITIVE);
      mScript = pScript.matcher(htmlStr);
      htmlStr = mScript.replaceAll(""); // 过滤script标签

      pStyle = Pattern.compile(regExStyle, Pattern.CASE_INSENSITIVE);
      mStyle = pStyle.matcher(htmlStr);
      htmlStr = mStyle.replaceAll(""); // 过滤style标签

      pHtml = Pattern.compile(regExHtml, Pattern.CASE_INSENSITIVE);
      mHtml = pHtml.matcher(htmlStr);
      htmlStr = mHtml.replaceAll(""); // 过滤html标签

      pHtml1 = Pattern.compile(regExHtml1, Pattern.CASE_INSENSITIVE);
      mHtml1 = pHtml1.matcher(htmlStr);
      htmlStr = mHtml1.replaceAll(""); // 过滤html标签
      textStr = htmlStr;
      textStr = textStr.replaceAll("&nbsp;", "");
      textStr = textStr.replaceAll("&gt;", "");
      textStr = textStr.replaceAll("&lt;;", "");
      textStr = textStr.replaceAll("。。。", "");
    } catch (Exception e) {

    }
    return textStr.trim();// 返回文本字符串
  }

  /**
   * 获取图片路径
   */
  public static Set<String> getImgSrc(String s) {
    Matcher m = Pattern.compile("<img.*?src=\"?(.*?)(\"|>|\\s+)").matcher(s);
    Set<String> resultSet = new HashSet<>();
    while (m.find()) {
      resultSet.add(m.group(1));
    }
    return resultSet;
  }

}
