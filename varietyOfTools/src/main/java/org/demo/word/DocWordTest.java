package org.demo.word;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Bookmark;
import org.apache.poi.hwpf.usermodel.Bookmarks;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Section;
import org.apache.poi.hwpf.usermodel.Table;
import org.apache.poi.hwpf.usermodel.TableCell;
import org.apache.poi.hwpf.usermodel.TableIterator;
import org.apache.poi.hwpf.usermodel.TableRow;

public class DocWordTest {
  public static void main(String[] args) {
    try {
      DocWordTest.testReadByDoc();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void testReadByDoc() throws Exception {
    //    String docFilePath = "C:\\Users\\T480S\\Desktop\\航天\\doc若干pic.doc";
    //    String docFilePath = "E:\\Deploy-七里渠\\相关\\脱\\tb\\zk\\xxxxxxx.doc";
    String docFilePath = "C:\\Users\\T480S\\Desktop\\ParseWordUtil.doc";

    InputStream is =
        new FileInputStream(docFilePath);
    // 输出文本，这步读取不到------TODO 只能读取DOC格式
    HWPFDocument doc = new HWPFDocument(is);
    System.out.println("=========================文本信息==========================");
    System.out.println("-------------使用getDocumentText()获取文本---------------");
    System.out.println(doc.getDocumentText());
    System.out.println("-----------------使用getText()获取文本-------------------");
    System.out.println(doc.getText());
    // 输出书签信息
    DocWordTest.printInfo(doc.getBookmarks());

    Range range = doc.getRange();
    // range信息
    DocWordTest.printInfo(range);

    // 读表格
    DocWordTest.readTable(range);

    // 读列表
    DocWordTest.readList(range);

    DocWordTest.closeStream(is);
  }

  /**
   * 关闭输入流
   *
   * @param is
   */
  private static void closeStream(InputStream is) {
    if (is != null) {
      try {
        is.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * 输出书签信息
   *
   * @param bookmarks
   */
  private static void printInfo(Bookmarks bookmarks) {
    int count = bookmarks.getBookmarksCount();
    System.out.println("=========================书签信息==========================");
    System.out.println("书签数量：" + count);
    Bookmark bookmark;
    for (int i = 0; i < count; i++) {
      bookmark = bookmarks.getBookmark(i);
      System.out.println("书签" + (i + 1) + "的名称是：" + bookmark.getName());
      System.out.println("开始位置：" + bookmark.getStart());
      System.out.println("结束位置：" + bookmark.getEnd());
    }
  }

  private static void readTable(Range range) {
    System.out.println("=========================表格信息==========================");
    // 遍历range范围内的table。
    TableIterator tableIter = new TableIterator(range);

    while (tableIter.hasNext()) {
      Table table = tableIter.next();
      // 开始位置
      int start = table.getStartOffset();
      // 结束位置
      int end = table.getEndOffset();
      System.out.printf("开始位置%d,结束位置%d\r\n", start, end);

      // 获取行的数目
      int rowNum = table.numRows();
      for (int j = 0; j < rowNum; j++) {
        // 获取每一行
        TableRow row = table.getRow(j);
        int cellNum = row.numCells();
        for (int k = 0; k < cellNum; k++) {
          // 获取每一列
          TableCell cell = row.getCell(k);
          // 输出单元格的文本
          System.out.printf("第%d行第%d列的内容是: %s", j + 1, k + 1, cell.text().trim());
          System.out.println();
        }
      }
    }
  }

  /**
   * 读列表
   *
   * @param range
   */
  private static void readList(Range range) {
    System.out.println("=========================列表信息==========================");
    int num = range.numParagraphs();
    for (int i = 0; i < num; i++) {
      Paragraph paragraph = range.getParagraph(i);
      //      if (paragraph.()) {
      //        System.out.println("list : " + paragraph.text());
      //      }
    }
  }

  /**
   * 输出Range
   *
   * @param range
   */
  private static void printInfo(Range range) {
    System.out.println("=========================Range信息==========================");
    System.out.println("-------------------------段落信息-------------------------");
    // 获取段落数
    int paraNum = range.numParagraphs();
    System.out.println("段落数为 ： " + paraNum);
    for (int i = 0; i < paraNum; i++) {
      System.out.println("段落" + (i + 1) + "内容为：" + range.getParagraph(i).text());
    }

    System.out.println("-------------------------小节信息-------------------------");
    int secNum = range.numSections();
    System.out.println("小节数为 ： " + paraNum);
    System.out.println(secNum);
    Section section;
    for (int i = 0; i < secNum; i++) {
      section = range.getSection(i);
      System.out.println(section.text());
    }
  }
}
