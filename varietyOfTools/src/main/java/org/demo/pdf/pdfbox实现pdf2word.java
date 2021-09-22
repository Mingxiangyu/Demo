package org.demo.pdf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

/**
 * 转化后丢失样式
 *
 * @author T480S
 */
public class pdfbox实现pdf2word {
  public static void main(String[] args) {
    try {
      String pdfFile = "E:\\Deploy-HT\\数据\\1.pdf";

      PDDocument doc = PDDocument.load(new File(pdfFile));
      pdfFile = pdfFile.substring(0, pdfFile.lastIndexOf("."));
      String fileName = pdfFile + ".doc";
      File file = new File(fileName);
      if (!file.exists()) {
        file.createNewFile();
      }
      FileOutputStream fos = new FileOutputStream(fileName);
      Writer writer = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
      PDFTextStripper stripper = new PDFTextStripper();
      stripper.setSortByPosition(true); // 排序
      stripper.setStartPage(1); // 设置转换的开始页
      int pagenumber = doc.getNumberOfPages(); // 获取所有页
      stripper.setEndPage(pagenumber); // 设置转换的结束页
      stripper.writeText(doc, writer);
      writer.close();
      doc.close();
      System.out.println("pdf转换word成功！");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}

//  原文链接：https://blog.csdn.net/weixin_41722928/article/details/88656869
