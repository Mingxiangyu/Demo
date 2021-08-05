package org.demo.word;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

public class poi获取word文档自动生成的序号 {

  public static void main(String[] args) throws IOException {
    String filePath = "C:\\Users\\T480S\\Desktop\\新建 Microsoft Word 文档.docx";
    File file = new File(filePath);
    FileInputStream fis = new FileInputStream(file);
    XWPFDocument xdoc = new XWPFDocument(fis);
    Iterator<XWPFParagraph> paragraphsIterator = xdoc.getParagraphsIterator();

    while (paragraphsIterator.hasNext()) {
      XWPFParagraph item = paragraphsIterator.next();
      //            List<XWPFRun> runs = item.getRuns();
      //            for (XWPFRun r : runs) {
      //                //是否加粗
      //        boolean bold = r.getCTR().getRPr().isSetB();
      //
      //            }
      p(getNumText(item) + item.getText());

      fis.close();
    }
  }

  private static void p(Object o) {
    System.out.println(o);
  }

  // 根据XWPFParagraph的getNumLevelText()获取的级别文本返回级别
  private static int getLevel(String numLevelText) {
    if (null == numLevelText) {
      return 0;
    }
    if ("%1.".equals(numLevelText)||"%1、".equals(numLevelText)) {
      return 1;
    }
    if (numLevelText.endsWith(".%1")) {
      return 2;
    }
    if ("(%1)".equals(numLevelText)) {
      return 3;
    }
    if ("(%2)".equals(numLevelText)) {
      return 4;
    }
    return -1;
  }

  // 只支持4级
  // 1.
  //  1.1
  //   (1)
  //      (a)
  private static int level_1_counter = 0;
  private static int level_2_counter = 0;
  private static int level_3_counter = 0;
  private static int level_4_counter = 0;

  private static String getNumText(XWPFParagraph item) {
    int level = getLevel(item.getNumLevelText());
    if (level == 0) {
      return "";
    }
    if (level == -1) {
      String space = "";
      if (level_4_counter != 0) {
        space = getSpace(4);
      } else if (level_3_counter != 0) {
        space = getSpace(3);
      } else if (level_2_counter != 0) {
        space = getSpace(2);
      } else if (level_1_counter != 0) {
        space = getSpace(1);
      }
      return space + item.getNumLevelText();
    }
    if (level == 1) {
      level_1_counter++;
      level_2_counter = 0;
      level_3_counter = 0;
      level_4_counter = 0;
      // 如 1.1
      return level_1_counter + ". ";
    }
    if (level == 2) {
      level_2_counter++;
      level_3_counter = 0;
      level_4_counter = 0;
      // 如 1.1
      return getSpace(1)
          + String.valueOf(level_1_counter)
          + "."
          + String.valueOf(level_2_counter)
          + " ";
    }
    if (level == 3) {
      level_3_counter++;
      level_4_counter = 0;
      // 如 (1)
      return getSpace(2) + "(" + level_3_counter + ")";
    }
    if (level == 4) {
      level_4_counter++;
      // 如 (a)
      return getSpace(3) + "(" + (char) (96 + level_4_counter) + ")";
    }

    return "";
  }

  private static String getSpace(int i) {
    String ret = "";
    for (int k = 0; k < i; k++) {
      ret += " ";
    }
    return ret;
  }
}
