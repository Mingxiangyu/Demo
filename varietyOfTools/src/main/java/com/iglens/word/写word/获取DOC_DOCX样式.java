package com.iglens.word.写word;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.model.StyleDescription;
import org.apache.poi.hwpf.model.StyleSheet;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFPicture;
import org.apache.poi.xwpf.usermodel.XWPFRun;

public class 获取DOC_DOCX样式 {

  public static void main(String[] args) throws Exception {
    String filePath = "C:\\Users\\T480S\\Desktop\\ces.docx";
    if (filePath.endsWith("doc")) {
      获取doc样式(filePath);
    }else if(filePath.endsWith("docx")){
      获取docx样式(filePath);
    };
  }

  public static void 获取doc样式(String filePath) throws IOException {
    InputStream is = new FileInputStream(filePath);
    HWPFDocument doc = new HWPFDocument(is);
    Range r = doc.getRange(); // 文档范围
    System.out.println("段落数：" + r.numParagraphs());

    for (int i = 0; i < r.numParagraphs(); i++) {
      Paragraph p = r.getParagraph(i); // 获取段落
      int numStyles = doc.getStyleSheet().numStyles();
      int styleIndex = p.getStyleIndex();
      if (numStyles > styleIndex) {
        StyleSheet styleSheet = doc.getStyleSheet();
        StyleDescription style = styleSheet.getStyleDescription(styleIndex);
        String styleName = style.getName(); // 获取每个段落样式名称

        System.out.println("样式为：" + styleName);
        // 获取自己理想样式的段落文本信息
        String styleLoving = "级别2：四号黑体 20磅 前18 后12 左对齐";
        if (styleName != null && styleName.contains(styleLoving)) {
          String text = p.text(); // 段落文本
          System.out.println(text);
        }
      }
    }
    doc.close();
  }

  public static void 获取docx样式(String filePath) throws IOException {
    InputStream is = new FileInputStream(filePath);
    XWPFDocument document = new XWPFDocument(is);
    List<XWPFParagraph> paragraphs = document.getParagraphs();

    for (XWPFParagraph xwpfParagraph : paragraphs) {
      System.out.println("------------------------------------");
      System.out.println(xwpfParagraph.getText());
      /*
      对齐方式
      1	左对齐
      2	居中
      3	右对齐
       */
      int value = xwpfParagraph.getAlignment().getValue();
      System.out.println("对齐方式：" + value);

      List<XWPFRun> runs = xwpfParagraph.getRuns();
      for (XWPFRun run : runs) {
        //        字体
        String fontFamily = run.getFontFamily();
        System.out.println("字体：" + fontFamily);
        //        字号
        int fontSize = run.getFontSize();
        System.out.println("字号：" + fontSize);
        //        是否粗体
        boolean bold = run.isBold();
        System.out.println("是否粗体：" + bold);
        //        是否斜体
        boolean italic = run.isItalic();
        System.out.println("是否斜体：" + italic);

        /*
        常用下划线
        1	单下划线
        3	双下划线
                 */
        int value1 = run.getUnderline().getValue();
        System.out.println("常用下划线：" + value1);
        //        颜色
        String color = run.getColor();
        System.out.println("颜色：" + color);

        //        着重号
        boolean dot =
            run.getCTR().getRPr() != null
                && run.getCTR().getRPr().getEm() != null
                && run.getCTR().getRPr().getEm().getVal().toString().equals("dot");
        System.out.println("着重号：" + dot);
        // 获取嵌入图片
        List<XWPFPicture> pictures = run.getEmbeddedPictures();
        // 获取文字
        String text = run.text();
      }
    }
    document.close();
  }
}
