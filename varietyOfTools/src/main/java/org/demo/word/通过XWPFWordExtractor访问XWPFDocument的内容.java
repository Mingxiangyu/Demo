package org.demo.word;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.poi.POIXMLProperties.CoreProperties;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

public class 通过XWPFWordExtractor访问XWPFDocument的内容 {
  public static void main(String[] args) {
    try {
      String pathname = "C:\\Users\\T480S\\Desktop\\ces.docx";
      testReadByExtractor(pathname);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 通过XWPFWordExtractor访问XWPFDocument的内容
   * @throws Exception
   */
  public static void testReadByExtractor(String pathname) throws Exception {
    InputStream is = new FileInputStream(pathname);
    XWPFDocument doc = new XWPFDocument(is);
    XWPFWordExtractor extractor = new XWPFWordExtractor(doc);
    String text = extractor.getText();
    System.out.println(text);
    CoreProperties coreProps = extractor.getCoreProperties();
    通过XWPFWordExtractor访问XWPFDocument的内容.printCoreProperties(coreProps);
    通过XWPFWordExtractor访问XWPFDocument的内容.close(is);
  }

  /**
   * 输出CoreProperties信息
   * @param coreProps
   */
  private static void printCoreProperties(CoreProperties coreProps) {
    System.out.println("==============category==============");
    System.out.println(coreProps.getCategory());   //分类
    System.out.println("==============creator==============");
    System.out.println(coreProps.getCreator()); //创建者
    System.out.println("==============created==============");
    System.out.println(coreProps.getCreated()); //创建时间
    System.out.println("==============title==============");
    System.out.println(coreProps.getTitle());   //标题
  }

  /**
   * 关闭输入流
   * @param is
   */
  private static void close(InputStream is) {
    if (is != null) {
      try {
        is.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

}