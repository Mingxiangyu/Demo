package org.demo.word;

import java.io.*;
import java.util.List;

import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFPictureData;

public class Word {

  public static String textPath = "src/main/resources/test.txt";
  public static String docPath = "C:\\Users\\T480S\\Desktop\\新建 DOCX 文档.docx";
  public static String imagePath = "src/main/resources/test.docx";

  public static void main(String args[]) {
    readDocxTextAndImage();
  }

  public static String readDocxTextAndImage() {

    File file = new File(docPath);
    try {
      // 用XWPFWordExtractor来获取文字
      FileInputStream fis = new FileInputStream(file);
      XWPFDocument document = new XWPFDocument(fis);
      XWPFWordExtractor xwpfWordExtractor = new XWPFWordExtractor(document);
      String text = xwpfWordExtractor.getText();
      System.out.println(text);
      //将获取到的文字存放到对应文件名中的txt文件中
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
      return text;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }
}
