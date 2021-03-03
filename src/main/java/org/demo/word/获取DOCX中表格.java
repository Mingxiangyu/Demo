package org.demo.word;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.apache.poi.POIXMLProperties;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

public class 获取DOCX中表格 {
  public static void main(String[] args) {
    String path = "C:\\Users\\T480S\\Desktop\\新建 DOCX 文档.docx";
    try {
      getTable(path);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void getTable(String path) throws IOException {
    InputStream is = new FileInputStream(path);
    XWPFDocument doc = new XWPFDocument(is);
    XWPFWordExtractor extractor = new XWPFWordExtractor(doc);
    String text = extractor.getText();
    System.out.println(text);
    POIXMLProperties.CoreProperties coreProps = extractor.getCoreProperties();
    String title = coreProps.getTitle();
    System.out.println(title);

    // 获取文档中所有的表格
    List<XWPFTable> tables = doc.getTables();
    List<XWPFTableRow> rows;
    List<XWPFTableCell> cells;
    for (XWPFTable table : tables) {
      // 表格属性
      // CTTblPr pr = table.getCTTbl().getTblPr();
      // 获取表格对应的行
      rows = table.getRows();
      for (XWPFTableRow row : rows) {
        // 获取行对应的单元格
        cells = row.getTableCells();
        for (XWPFTableCell cell : cells) {
          System.out.println(cell.getText());
          ;
        }
      }
    }
  }
}
