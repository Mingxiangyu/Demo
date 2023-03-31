package com.iglens.pdf.itextpdf;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import java.io.IOException;
import java.net.URL;

public class 读取pdf特定内容 {

  public static void main(String[] args) throws IOException {
    String spec = "file:/C:\\Users\\zhouhuilin\\Desktop\\pdf.pdf";
    URL url = new URL(spec);
    readPdf(url); // 直接读全PDF面
  }

  public static void readPdf(URL url) {
    String pageContent = "";
    try {
      PdfReader reader = new PdfReader(url);
      int pageNum = reader.getNumberOfPages();
      for (int i = 1; i <= pageNum; i++) {
        pageContent += PdfTextExtractor.getTextFromPage(reader, i); // 读取第i页的文档内容
      }

      // System.out.println(pageContent);
      // 获取工作联系人
      int strStartIndex = pageContent.indexOf("联系人");
      int strEndIndex = pageContent.indexOf("二、施工组织措施");
      String a = pageContent.substring(strStartIndex, strEndIndex);
      String b = a.substring(4);
      System.out.println(b);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
    }
  }
}
