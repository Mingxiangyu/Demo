package com.iglens.word;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFStyle;
import org.apache.poi.xwpf.usermodel.XWPFStyles;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyle;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STStyleType;

public class WordTitle {
//  /**
//   * word整体样式
//   */
//  private static CTStyles wordStyles = null;
//
//  /**
//   * Word整体样式
//   */
//  static {
//    XWPFDocument template;
//    try {
//      // 读取模板文档
//      template = new XWPFDocument(new FileInputStream("D:/format.docx"));
//      // 获得模板文档的整体样式
//      wordStyles = template.getStyle();
//    } catch (FileNotFoundException e) {
//      e.printStackTrace();
//    } catch (IOException e) {
//      e.printStackTrace();
//    } catch (XmlException e) {
//      e.printStackTrace();
//    }
//  }

  // 模板方式实现
  public static void formatDoc() throws IOException {
    // 新建的word文档对象
    XWPFDocument doc = new XWPFDocument();
    // 获取新建文档对象的样式
    XWPFStyles newStyles = doc.createStyles();
    // 关键行// 修改设置文档样式为静态块中读取到的样式
//    newStyles.setStyles(wordStyles);

    // 开始内容输入
    // 标题1，1级大纲
    XWPFParagraph para1 = doc.createParagraph();
    // 关键行// 1级大纲
    para1.setStyle("1");
    XWPFRun run1 = para1.createRun();
    // 标题内容
    run1.setText("标题 1");

    // 标题2
    XWPFParagraph para2 = doc.createParagraph();
    // 关键行// 2级大纲
    para2.setStyle("2");
    XWPFRun run2 = para2.createRun();
    // 标题内容
    run2.setText("标题 2");

    // 正文
    XWPFParagraph paraX = doc.createParagraph();
    XWPFRun runX = paraX.createRun();
    // 正文内容
    runX.setText("正文");

    // word写入到文件
    FileOutputStream fos = new FileOutputStream("D:/myDoc.docx");
    doc.write(fos);
    fos.close();
  }

  // main
  public static void main(String[] args) throws Exception {
    // 读取模板方式写word
//    formatDoc();

    // 自定义样式方式写word
    writeSimpleDocxFile();
  }

  /**
   * 自定义样式方式写word，参考statckoverflow的源码
   *
   * @throws IOException
   */
  public static void writeSimpleDocxFile() throws IOException {
    XWPFDocument docxDocument = new XWPFDocument();

    // 老外自定义了一个名字，中文版的最好还是按照word给的标题名来，否则级别上可能会乱
    addCustomHeadingStyle(docxDocument, "标题 1", 1);
    addCustomHeadingStyle(docxDocument, "标题 2", 2);

    // 标题1
    XWPFParagraph paragraph = docxDocument.createParagraph();
    XWPFRun run = paragraph.createRun();
    run.setText("标题 1");
    paragraph.setStyle("标题 1");

    // 标题2
    XWPFParagraph paragraph2 = docxDocument.createParagraph();
    XWPFRun run2 = paragraph2.createRun();
    run2.setText("标题 2");
    paragraph2.setStyle("标题 2");

    // 正文
    XWPFParagraph paragraphX = docxDocument.createParagraph();
    XWPFRun runX = paragraphX.createRun();
    runX.setText("正文");

    // word写入到文件
    FileOutputStream fos = new FileOutputStream("D:/myDoc2.docx");
    docxDocument.write(fos);
    fos.close();
  }

  /**
   * 增加自定义标题样式。这里用的是stackoverflow的源码
   *
   * @param docxDocument 目标文档
   * @param strStyleId 样式名称
   * @param headingLevel 样式级别
   */
  private static void addCustomHeadingStyle(XWPFDocument docxDocument, String strStyleId, int headingLevel) {

    CTStyle ctStyle = CTStyle.Factory.newInstance();
    ctStyle.setStyleId(strStyleId);

    CTString styleName = CTString.Factory.newInstance();
    styleName.setVal(strStyleId);
    ctStyle.setName(styleName);

    CTDecimalNumber indentNumber = CTDecimalNumber.Factory.newInstance();
    indentNumber.setVal(BigInteger.valueOf(headingLevel));

    // lower number > style is more prominent in the formats bar
    ctStyle.setUiPriority(indentNumber);

    CTOnOff onoffnull = CTOnOff.Factory.newInstance();
    ctStyle.setUnhideWhenUsed(onoffnull);

    // style shows up in the formats bar
    ctStyle.setQFormat(onoffnull);

    // style defines a heading of the given level
    CTPPr ppr = CTPPr.Factory.newInstance();
    ppr.setOutlineLvl(indentNumber);
    ctStyle.setPPr(ppr);

    XWPFStyle style = new XWPFStyle(ctStyle);

    // is a null op if already defined
    XWPFStyles styles = docxDocument.createStyles();

    style.setType(STStyleType.PARAGRAPH);
    styles.addStyle(style);

  }
}

