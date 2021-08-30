package org.demo.http;

import java.io.IOException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class 避免URL越权访问 {

  public void doFilter(ServletRequest request, ServletResponse response) throws IOException {
    HttpServletRequest req = (HttpServletRequest) request;
    HttpServletResponse resp = (HttpServletResponse) response;
    // 通过request获取header中 REFERER（即记录了该HTTP请求的来源地址）
    String conString = req.getHeader("REFERER"); // 获取父url
    // 如果该值为空认为是浏览器URL直接输入，进行重定向到首页
    if ("".equals(conString) || null == conString) {
      // 当前请求url
      String servletPath = req.getServletPath();
      if (servletPath.contains("index.jsp") || servletPath.contains("admin/login.jsp")) {
        // 如果是登录，或者通用界面则不做处理，正常访问
        /*
        dosomething。。。
         */
      } else {
        // 重定向
        resp.sendRedirect("/ejuornal/index.jsp");
      }
    }
  }
}
