package com.iglens.pdf;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import org.apache.pdfbox.io.RandomAccessBuffer;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;

/**
 * @author T480S
 */
public class Pdfbox读取pdf {

  public static void main(String[] args) {
    String filePath = "E:\\Deploy-HT\\数据\\1.pdf";
    String targetPath = "E:\\Deploy-HT\\数据\\ExtractText1.txt";

    String s = readpdftext(filePath);
    System.out.println(s);

    FileWriter writer;
    try {
      // 将StringBuilder对象中的文本写入到文本文件
      writer = new FileWriter(targetPath);
      writer.write(s);
      writer.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * 读取本地pdf
   *
   * @param filePath pdf路径
   * @return 解析后文字
   */
  public static String readpdftext(String filePath) {
    PDFTextStripper pdfStripper;
    PDDocument pdDoc;
    File file = new File(filePath);
    try {
      pdfStripper = new PDFTextStripper();
      pdDoc = PDDocument.load(file);
      return pdfStripper.getText(pdDoc);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return "";
  }

  /**
   * 读取网络上pdf文件
   *
   * @param urlPath 网络pdf路径
   * @return
   */
  public static String readpdf(String urlPath) {
    try {
      URL url = new URL(urlPath);
      URLConnection connection = url.openConnection();
      InputStream inputStream = connection.getInputStream();

      PDFParser parser = new PDFParser(new RandomAccessBuffer(inputStream));
      parser.parse();
      PDDocument pdDoc = parser.getPDDocument();
      if (!pdDoc.isEncrypted()) {
        PDFTextStripperByArea stripper = new PDFTextStripperByArea();
        stripper.setSortByPosition(true);
        PDFTextStripper textStripper = new PDFTextStripper();
        return textStripper.getText(pdDoc);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return "";
  }
}
