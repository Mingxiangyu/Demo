package org.yong.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Table;
import org.apache.poi.hwpf.usermodel.TableCell;
import org.apache.poi.hwpf.usermodel.TableRow;
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

@Slf4j
public class 按行读取 {

  /** word表格默认高度 */
  private static final int DEFAULT_HEIGHT = 500;

  /** word表格默认宽度 */
  private static final int DEFAULT_WIDTH = 1000;

  /** word表格转换参数 默认为/1 可以根据需求调整 */
  private static final int DEFAULT_DIV = 1;

  /** 目前没有提取word的字体大小 默认为12 */
  private static final Float DEFAULT_FONT_SIZE = 12.0F;

  /** word的全角空格 以及\t 制表符 */
  private static final String WORD_BLANK = "[\u00a0|\u3000|\u0020|\b|\t]";

  /** word的它自己造换行符 要换成string的换行符 */
  private static final String WORD_LINE_BREAK = "[\u000B|\r]";

//  private static final String WORD_LINE_BREAK = "[\u000B|\r|\u0007]";

  /** word table中的换行符和空格 */
  private static final String WORD_TABLE_FILTER = "[\\t|\\n|\\r|\\s+| +]";

  /** 计算表格行列信息时设置的偏移值 */
  private static final Float TABLE_EXCURSION = 5F;

  /** 抽取文字时去掉不必须字符正则 */
  private static final String splitter = "[\\t|\\n|\\r|\\s+|\u00a0+]";

  private static final String regexClearBeginBlank = "^" + splitter + "*|" + splitter + "*$";

  public static void main(String[] args) {
    String pathname = "C:\\Users\\T480S\\Desktop\\5-10开发计划.doc";
    //    String pathname = "C:\\Users\\T480S\\Desktop\\5-10开发计划.docx";
    try (FileInputStream fileInputStream = new FileInputStream(pathname)) {
      if (pathname.toLowerCase().endsWith("docx")) {
        XWPFDocument docx = new XWPFDocument(fileInputStream);
        String docxTableCell = getDocxTableCell(docx);
        System.out.println(docxTableCell);
      } else {
        HWPFDocument doc = new HWPFDocument(fileInputStream);
        List<String> docTableCell = getDocTableCell(doc);
        System.out.println(docTableCell);
      }

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /** 解析doc表格 得到结构对象 其中返回的String不包括表格中抽取的文字 这里默认对单元格中的文字做去换行操作 */
  private static List<String> getDocTableCell(HWPFDocument doc) {
    // 得到文档的读取范围
    Range range = doc.getRange();
    List<String> stList = new ArrayList<>();

    // 开始抽取doc中的文字
//    StringBuilder docText = new StringBuilder();
    int i1 = range.numParagraphs();
    for (int i = 0; i < i1; i++) {
      Paragraph paragraph = range.getParagraph(i);
      String text1 = paragraph.text();
//      System.out.println(
//          "+++++++++++++++++++\n"
//              + "第: "
//              + i
//              + "行字段值为:"
//              + text1
//              + "\n==============================");
      // 拿出段落中不包括表格的文字
      if (paragraph.isInTable()) {
        boolean inTable = paragraph.isInTable();
        System.out.println("是否为表格: "+inTable);
        //如果当前paragraph为表格,则通过该方法获取到该表格
        Table table = range.getTable(paragraph);
        for (int i2 = 0; i2 < table.numRows(); i2++) {
          StringBuilder cellBuilder = new StringBuilder();
          TableRow row = table.getRow(i2);
          for (int i3 = 0; i3 < row.numCells(); i3++) {
            TableCell cell = row.getCell(i3);
            String text = cell.text();
            if (StringUtils.isBlank(text)) {
              continue;
            }
            // 将word中的特有字符转化为普通的换行符、空格符等
            String textWithSameBlankAndBreak =
                text.replaceAll(WORD_BLANK, " ")
                    .replaceAll(WORD_LINE_BREAK, "\n")
                    .replaceAll("\n+", "\n");
            // 去除word特有的不可见字符
            String textClearBeginBlank =
                textWithSameBlankAndBreak.replaceAll(WORD_TABLE_FILTER, "");
            // 为抽取的每一个段落加上\n作为换行符标识
            cellBuilder.append(textClearBeginBlank).append("\t");
          }
          stList.add(cellBuilder.toString());
}
        } else {
        String text = paragraph.text();
        if (StringUtils.isBlank(text)) {
          continue;
        }
        String textWithSameBlankAndBreak =
            text.replaceAll(WORD_BLANK, " ").replaceAll(WORD_LINE_BREAK, "\n");
        String clearBeginBlank = textWithSameBlankAndBreak.replaceAll(regexClearBeginBlank, "");
//        docText.append(clearBeginBlank).append("\n");
        stList.add(clearBeginBlank);



//        try {
//          // 寻找表格的开始位置和结束位置(表格结束后更新循环中下标位置)
//          int index = i;
//          int endIndex = index;
//          // 拿出表格中文字
//          StringBuilder tableOriginText = new StringBuilder();
//          for (; index < range.numParagraphs(); index++) {
//            Paragraph tableParagraph = range.getParagraph(index);
//            if (!tableParagraph.isInTable() || tableParagraph.getTableLevel() < 1) {
//              //表格执行完成,跳出循环
//              endIndex = index;
//              break;
//            } else {
//              String text = tableParagraph.text();
//              tableOriginText.append(text);
//            }
//          }
//          //更新外层循环index,避免再次扫描到该表格
//          i = endIndex - 1;
//          // 过滤掉表格中所有不可见符号
//          String tableOriginTextWithoutBlank =
//              tableOriginText.toString().replaceAll(WORD_TABLE_FILTER, "");
//          String textWithSameBlankAndBreak =
//              tableOriginTextWithoutBlank
//                  .replaceAll(WORD_BLANK, " ")
//                  .replaceAll(WORD_LINE_BREAK, "\n");
//          String clearBeginBlank = textWithSameBlankAndBreak.replaceAll(regexClearBeginBlank, "");
//          docText.append(clearBeginBlank).append("\n");
//        } catch (Exception e) {
//          log.error(e.getMessage(), e);
//        }
      }
    }
    return stList;
  }

  public static String getDocxTableCell(XWPFDocument docx) {
    //    StringBuilder docxText = new StringBuilder();
    List<String> stList = new ArrayList<>();
    Iterator<IBodyElement> iter = docx.getBodyElementsIterator();
    while (iter.hasNext()) {
      IBodyElement element = iter.next();
      if (element instanceof XWPFParagraph) {
        // 获取段落元素
        XWPFParagraph paragraph = (XWPFParagraph) element;
        String text = paragraph.getText();
        if (StringUtils.isBlank(text)) {
          continue;
        }
        // 将word中的特有字符转化为普通的换行符、空格符等
        String textWithSameBlankAndBreak =
            text.replaceAll(WORD_BLANK, " ")
                .replaceAll(WORD_LINE_BREAK, "\n")
                .replaceAll("\n+", "\n");
        // 去除word特有的不可见字符
        String textClearBeginBlank = textWithSameBlankAndBreak.replaceAll(regexClearBeginBlank, "");
        // 为抽取的每一个段落加上\n作为换行符标识
        //        docxText.append(textClearBeginBlank).append("\n");
        stList.add(textClearBeginBlank);
      } else if (element instanceof XWPFTable) {
        XWPFTable table = (XWPFTable) element;
        List<XWPFTableRow> rows = table.getRows();
        // 读取每一行数据 TODO 可以依据需要设置i的值(修改为fori循环即可)
        for (XWPFTableRow row : rows) {
          StringBuilder cellBuilder = new StringBuilder();
          // 读取每一列数据
          List<XWPFTableCell> cells = row.getTableCells();
          for (XWPFTableCell cell : cells) {
            // 输出当前的单元格的数据
            String cellValue = cell.getText();
            if (StringUtils.isBlank(cellValue)) {
              continue;
            }
            // 将word中的特有字符转化为普通的换行符、空格符等
            String textWithSameBlankAndBreak =
                cellValue
                    .replaceAll(WORD_BLANK, " ")
                    .replaceAll(WORD_LINE_BREAK, "\n")
                    .replaceAll("\n+", "\n");
            // 去除word特有的不可见字符
            String textClearBeginBlank =
                textWithSameBlankAndBreak.replaceAll(WORD_TABLE_FILTER, "");
            // 为抽取的每一个段落加上\n作为换行符标识
            cellBuilder.append(textClearBeginBlank).append("\t");
          }
          //          docxText.append(cellBuilder.toString()).append("\n");
          stList.add(cellBuilder.toString());
        }
      }
    }
    return stList.toString();
  }
}
