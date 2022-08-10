package cn.ruiyeclub.services;

import javax.servlet.http.HttpServletResponse;

/**
 * @author zhouhuilin
 */
public interface GeoPdfService {
  void downloadPdf(String tifPath, HttpServletResponse response);
}
