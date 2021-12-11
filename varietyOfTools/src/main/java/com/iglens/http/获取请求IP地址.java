package com.iglens.http;

import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/** 获取请求IP地址 */
@Slf4j
public class 获取请求IP地址 {
  public static void main(String[] args) {
    //        String ipAddr = 获取请求IP地址.getIpAddr();
    //        System.out.println(ipAddr);
  }

  /**
   * 获取IP地址
   *
   * <p>使用Nginx等反向代理软件， 则不能通过request.getRemoteAddr()获取IP地址
   * 如果使用了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP地址， <br>
   * X-Forwarded-For中第一个非unknown的有效IP字符串，则为真实IP地址
   */
  public static String getIpAddr(HttpServletRequest request) {
    String ip = null;
    try {
      ip = request.getHeader("x-forwarded-for");
      if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
        ip = request.getHeader("Proxy-Client-IP");
      }
      if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
        ip = request.getHeader("WL-Proxy-Client-IP");
      }
      if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
        ip = request.getHeader("HTTP_CLIENT_IP");
      }
      if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
        ip = request.getHeader("HTTP_X_FORWARDED_FOR");
      }
      if (StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
        ip = request.getRemoteAddr();
      }
    } catch (Exception e) {
      log.error("获取请求IP地址 ERROR ", e);
    }

    // 本机请求则获取网卡地址
    if ("127.0.0.1".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip)) {
      // 根据网卡获取本机IP
      try {
        InetAddress localHost = InetAddress.getLocalHost();
        ip = localHost.getHostAddress();
      } catch (UnknownHostException e) {
        e.printStackTrace();
      }
    }
    // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
    if (StringUtils.isNotEmpty(ip) && ip.length() > 15) {
      if (ip.indexOf(",") > 0) {
        ip = ip.substring(0, ip.indexOf(","));
      }
    }

    return ip;
  }
}
