package com.iglens.http;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class URL接码 {
  public static void main(String[] args) {
    String s = "%E8%A1%8C%E6%94%BF%E5%8C%BA%E5%88%92";
    String decode = null;
    try {
      // String gbk = URLEncoder.encode(s, "GBK");
      decode = URLDecoder.decode(s,"UTF-8");
    } catch (UnsupportedEncodingException e) {


    }
    System.out.println(decode);
    //
  }
}
