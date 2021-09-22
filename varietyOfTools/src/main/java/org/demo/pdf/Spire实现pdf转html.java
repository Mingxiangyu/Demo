package org.demo.pdf;

import com.spire.pdf.FileFormat;
import com.spire.pdf.PdfDocument;

public class Spire实现pdf转html {

  public static void main(String[] args) {
    String path = "E:\\Deploy-HT\\数据\\1.pdf";
    String targetpath = "E:\\Deploy-HT\\数据\\1.html";

    // 加载PDF
    PdfDocument pdf = new PdfDocument();
    pdf.loadFromFile(path);
    // 保存为HTML格式
    pdf.saveToFile(targetpath, FileFormat.HTML);
  }
}
