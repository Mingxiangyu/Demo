package org.demo.word;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Table;
import org.apache.poi.hwpf.usermodel.TableCell;
import org.apache.poi.hwpf.usermodel.TableIterator;
import org.apache.poi.hwpf.usermodel.TableRow;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

/**
 * 读取word文档中表格数据，支持doc、docx
 *
 * @author Fise19
 */
public class 获取word中表格doc和docx通用 {
  public static void main(String[] args) {
    获取word中表格doc和docx通用 test = new 获取word中表格doc和docx通用();
    //    String docFilePath = "C:\\Users\\T480S\\Desktop\\新建 DOC 文档.doc";
    //    String docFilePath = "C:\\Users\\T480S\\Desktop\\新建 DOCX 文档.docx";
    String docFilePath = "C:\\\\Users\\\\T480S\\\\Desktop\\\\5-10开发计划.docx";
    int tableNum = 0;

    test.getTable(docFilePath, tableNum);
  }

  /**
   * 读取文档中表格
   *
   * @param filePath 文件路径
   */
  public void getTable(String filePath, int tableNum) {
    List<Map<Integer, Map<Integer, String>>> tableByDoc = new ArrayList<>();
    try (FileInputStream in = new FileInputStream(filePath)) {
      if (filePath.toLowerCase().endsWith("docx")) {
        tableByDoc = getTableByDocx(in, tableNum);
      } else {
        tableByDoc = getTableByDoc(in, tableNum);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    System.out.println(tableByDoc.toString());
  }

  /**
   * 获取doc内表格 即office2003版本
   *
   * @param in 输入流
   * @param tableNum 设置需要读取的第几个表格(从1开始算,如果为0证明读取所有表格)
   * @throws IOException 异常
   * @return 表集合<行数,<单元格数,值>>
   */
  private List<Map<Integer, Map<Integer, String>>> getTableByDoc(FileInputStream in, int tableNum)
      throws IOException {
    POIFSFileSystem pfs = new POIFSFileSystem(in);
    HWPFDocument hwpf = new HWPFDocument(pfs);
    // 得到文档的读取范围
    Range range = hwpf.getRange();
    TableIterator it = new TableIterator(range);

    // 过滤需要读取表格前的无用表格
    if (0 != tableNum) {
      for (int i = 0; i < tableNum - 1; i++) {
        it.hasNext();
        it.next();
      }
    }
    List<Map<Integer, Map<Integer, String>>> tableList = new ArrayList<>();
    while (it.hasNext()) {
      Table tb = it.next();
      System.out.println("这是第" + tableNum + "个表的数据");
      // 迭代行，默认从0开始,TODO 可以依据需要设置i的值,改变起始行数，也可设置读取到那行，只需修改循环的判断条件即可
      int row = 1;
      Map<Integer, Map<Integer, String>> rowMap = new LinkedHashMap<>();
      for (int i = 0; i < tb.numRows(); i++) {
        TableRow tr = tb.getRow(i);
        int cell = 1;
        Map<Integer, String> cellMap = new LinkedHashMap<>();
        // 迭代列，默认从0开始
        for (int j = 0; j < tr.numCells(); j++) {
          // 取得单元格
          TableCell td = tr.getCell(j);
          // 取得单元格的内容(单元格内每一行是一个numParagraphs)
          String cellValue = "";
          for (int k = 0; k < td.numParagraphs(); k++) {
            Paragraph para = td.getParagraph(k);
            // 去除后面的特殊符号
            String text = para.text();
            if (null != text && !"".equals(text)) {
              cellValue = text.substring(0, text.length() - 1);
              //              cellValue += text;
            }
            System.out.print(cellValue + "\t");
            cellMap.put(cell, cellValue);
            cell++;
          }
        }
        rowMap.put(row, cellMap);
        row++;
        System.out.println();
      }
      // 过滤需要读取表格后的无用表格
      if (0 != tableNum) {
        while (it.hasNext()) {
          it.next();
          tableNum += 1;
        }
      }
      tableList.add(rowMap);
    }
    return tableList;
  }

  /**
   * 处理docx格式 即office2007以后版本
   *
   * @param in 文件输入流
   * @param tableNum 需要读取的第几个表格(从1开始算,如果为0证明读取所有表格)
   * @throws IOException 异常
   * @return 表集合<行数,<单元格数,值>>
   */
  private List<Map<Integer, Map<Integer, String>>> getTableByDocx(FileInputStream in, int tableNum)
      throws IOException {
    // word 2007 图片不会被读取， 表格中的数据会被放在字符串的最后
    XWPFDocument xwpf = new XWPFDocument(in);
    // 得到word中的所有表格
    Iterator<XWPFTable> it = xwpf.getTablesIterator();

    // 过滤前面不需要的表格 如果为0证明读取所有表格
    if (0 != tableNum) {
      for (int i = 0; i < tableNum - 1; i++) {
        if (it.hasNext()) {
          it.next();
        }
      }
    }

    List<Map<Integer, Map<Integer, String>>> tableList = new ArrayList<>();
    while (it.hasNext()) {
      XWPFTable table = it.next();
      System.out.println("这是第" + tableNum + "个表的数据");
      List<XWPFTableRow> rows = table.getRows();
      int rowIndex = 1;
      Map<Integer, Map<Integer, String>> rowMap = new LinkedHashMap<>();
      // 读取每一行数据 TODO 可以依据需要设置i的值(修改为fori循环即可)
      for (XWPFTableRow row : rows) {
        // 读取每一列数据
        int cellIndex = 1;
        Map<Integer, String> cellMap = new LinkedHashMap<>();
        List<XWPFTableCell> cells = row.getTableCells();
        for (XWPFTableCell cell : cells) {
          // 输出当前的单元格的数据
          String cellValue = cell.getText();
          System.out.print(cellValue + "\t");
          cellMap.put(cellIndex, cellValue);
          cellIndex++;
        }
        System.out.println();
        rowMap.put(rowIndex, cellMap);
        rowIndex++;
      }
      // 过滤多余的表格
      if (0 != tableNum) {
        while (it.hasNext()) {
          it.next();
          tableNum += 1;
        }
      }
      tableList.add(rowMap);
    }
    return tableList;
  }
}
