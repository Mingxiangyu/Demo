package com.iglens.爬虫;

import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.URL;
import java.net.URLConnection;
import org.apache.commons.io.IOUtils;

public class 检查代理有效性 {



  //检查代理有效性
  public static boolean checkIpUsefull(String ip, Integer port) {
    try {
      URL url = new URL("http://www.baidu.com");
      InetSocketAddress addr = new InetSocketAddress(ip, port);
      java.net.Proxy proxy = new java.net.Proxy(java.net.Proxy.Type.HTTP, addr);
      InputStream in;
      try {
        URLConnection conn = url.openConnection(proxy);
        conn.setConnectTimeout(2000);
        conn.setReadTimeout(2000);
        in = conn.getInputStream();
      } catch (Exception e) {
        return false;
      }
      String s = IOUtils.toString(in);
      if (s.indexOf("baidu") > 0) {
        return true;
      }
      return false;
    } catch (Exception e) {
      return false;
    }
  }
}
