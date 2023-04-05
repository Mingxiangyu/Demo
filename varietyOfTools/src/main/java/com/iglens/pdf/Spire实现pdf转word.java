package com.iglens.pdf;

import com.spire.pdf.FileFormat;
import com.spire.pdf.PdfDocument;

public class Spire实现pdf转word {

  public static void main(String[] args) {
    String path = "C:\\Users\\zhouhuilin\\Desktop\\pdf.pdf";
    String targetpath = "C:\\Users\\zhouhuilin\\Desktop\\1.docx";

    // 加载PDF
    PdfDocument pdf = new PdfDocument();
    pdf.loadFromFile(path);
    // 保存为Word格式
    pdf.saveToFile(targetpath, FileFormat.DOCX);
  }
}
