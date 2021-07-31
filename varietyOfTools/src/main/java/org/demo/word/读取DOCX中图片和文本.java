package org.demo.word;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFFooter;
import org.apache.poi.xwpf.usermodel.XWPFHeader;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFPictureData;
import org.apache.poi.xwpf.usermodel.XWPFTable;

public class 读取DOCX中图片和文本 {

  public static String textPath = "src/main/resources/test.txt";
  public static String docPath = "C:\\Users\\T480S\\Desktop\\新建 DOCX 文档.docx";
  //  public static String docPath = "C:\\Users\\T480S\\Desktop\\新建 DOC 文档.doc";
  public static String imagePath = "src/main/resources/";

  public static void main(String args[]) {
    readDocxTextAndImage();
  }

  /** 读取DOCX中图片和文本 */
  public static void readDocxTextAndImage() {

    File file = new File(docPath);
    try {
      // 用XWPFWordExtractor来获取文字
      FileInputStream fis = new FileInputStream(file);
      XWPFDocument document = new XWPFDocument(fis);
      // 段落
      List<XWPFParagraph> paragraphs = document.getParagraphs();
      // 表格
      List<XWPFTable> tables = document.getTables();
      // 图片拼接
      List<XWPFPictureData> allPictures = document.getAllPictures();
      // 页眉
      List<XWPFHeader> headerList = document.getHeaderList();
      // 页脚
      List<XWPFFooter> footerList = document.getFooterList();

      XWPFWordExtractor xwpfWordExtractor = new XWPFWordExtractor(document);
      String text = xwpfWordExtractor.getText();
      System.out.println(text);
      // 将获取到的文字存放到对应文件名中的txt文件中
      PrintStream ps = new PrintStream(textPath);
      ps.println(text);

      // 用XWPFDocument的getAllPictures来获取所有的图片
      List<XWPFPictureData> picList = document.getAllPictures();
      for (XWPFPictureData pic : picList) {
        byte[] bytev = pic.getData();
        // 大于1000bites的图片我们才弄下来，消除word中莫名的小图片的影响
        if (bytev.length > 300) {
          FileOutputStream fos = new FileOutputStream(imagePath + pic.getFileName());
          fos.write(bytev);
        }
      }
      fis.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
