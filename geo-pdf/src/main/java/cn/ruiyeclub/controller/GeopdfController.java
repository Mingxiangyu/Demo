package cn.trans.controller;

import cn.trans.services.GeoPdfService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** @author zhouhuilin */
@RestController
@Api("微信支付")
public class GeopdfController {

  /** 服务对象 */
  @Autowired private GeoPdfService pdfService;

  @ApiOperation(value = "下载对账单")
  @GetMapping("/download-pdf")
  public void downloadPdf(@RequestParam String tifPath, HttpServletResponse response) {
    pdfService.downloadPdf(tifPath, response);
  }
}
