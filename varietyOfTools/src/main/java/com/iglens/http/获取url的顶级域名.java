package com.iglens.http;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class 获取url的顶级域名 {

  public static void main(String[] args) {
    String url = "https://list.jd.com/list.html?cat=9987,653,655&page=1";
    String topDomain = getTopDomain(url);
    System.out.println(topDomain); //sout: jd.com
  }

  /**
   * 获取url的顶级域名
   * 类似的工具类可以直接从网上去找
   *
   * @param url
   * @return
   */
  public static String getTopDomain(String url) {

    if(url != null) {
      try {
        String host = new URL(url).getHost().toLowerCase();// 此处获取值转换为小写
        Pattern pattern = Pattern.compile("[^\\.]+(\\.com\\.cn|\\.net\\.cn|\\.org\\.cn|\\.gov\\.cn|\\.com|\\.net|\\.cn|\\.org|\\.cc|\\.me|\\.tel|\\.mobi|\\.asia|\\.biz|\\.info|\\.name|\\.tv|\\.hk|\\.公司|\\.中国|\\.网络)");
        Matcher matcher = pattern.matcher(host);
        while (matcher.find()) {
          return matcher.group();
        }
      } catch (MalformedURLException e) {
        e.printStackTrace();
      }
    }
    return null;
  }
}
