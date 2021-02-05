package org.demo.word;

// import com.sinitek.sirm.web.plm.funddate.MatchingObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.model.PicturesTable;
import org.apache.poi.hwpf.model.StyleDescription;
import org.apache.poi.hwpf.model.StyleSheet;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFStyle;
import org.apache.poi.xwpf.usermodel.XWPFStyles;
import org.apache.xmlbeans.XmlException;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyle;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyles;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STStyleType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** @author T480S */
public class ParseWordUtil {
  private static final Logger log = LoggerFactory.getLogger(ParseWordUtil.class);

  // word整体样式
  private static CTStyles wordStyles = null;

  public static void main(String[] args) {
    String filePath =
        //        "E:\\Deploy-七里渠\\相关\\脱\\tb\\kb\\xxxxxxxxxxxxxxxxxxxxxxx.doc";
        "E:\\Deploy-七里渠\\相关\\脱\\tb\\tb\\doc+若干pic.doc";
    try {
      getWordText(filePath);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void getWordStyle(String filepath) {
    XWPFDocument template;
    try {
      // 读取模板文档
      template = new XWPFDocument(new FileInputStream(filepath));
      // 获得模板文档的整体样式
      wordStyles = template.getStyle();
    } catch (FileNotFoundException e) {
      log.error("未找到文件", e);
    } catch (IOException e) {
      log.error("", e);
    } catch (XmlException e) {
      log.error("XML转换异常", e);
    }
  }

  // 获取word文档标题
  public static List<String> getWordTitles(String filepath) throws IOException {
    String filename = getWordVersion(filepath);
    if (".docx".equals(filename)) {
      return getWordTitles2007(filepath);
    } else {
      return getWordTitlesAndContext2003(filepath, 1); // 1：只获取标题；2：只获取内容；3：标题和内容
    }
  }

  // 获取word文档内容
  public static List<String> getWordText(String filepath) throws Exception {
    String filename = getWordVersion(filepath);
    if (".docx".equals(filename)) {
      return getParagraphText2007(filepath);
    } else {
      return getWordTitlesAndContext2003(filepath, 3);
    }
  }

  // 获取文件版本，97基本已经淘汰不考虑，只针对03和07版本word
  public static String getWordVersion(String filepath) {
    File file = new File(filepath);
    String filename = file.getName();
    // filename = filename.substring(0, filename.lastIndexOf("."));
    filename = filename.substring(filename.lastIndexOf("."), filename.length());
    return filename;
  }

  /**
   * 获取03版word文档标题和内容
   *
   * @param path 文件路径
   * @param type 1：只获取标题；2：只获取内容；3：标题和内容都获取
   * @return list
   * @throws IOException
   */
  public static List<String> getWordTitlesAndContext2003(String path, Integer type)
      throws IOException {
    InputStream is = new FileInputStream(path);
    HWPFDocument doc = new HWPFDocument(is);
    Range r = doc.getRange();
    List<String> list = new ArrayList<String>();
    List<String> titles = new ArrayList<String>();
    List<String> context = new ArrayList<String>();
    for (int i = 0; i < r.numParagraphs(); i++) {
      Paragraph p = r.getParagraph(i);
      // check if style index is greater than total number of styles
      int numStyles = doc.getStyleSheet().numStyles();
      int styleIndex = p.getStyleIndex();
      String contexts = p.text();
      list.add(contexts); // 标题+内容

      if (numStyles > styleIndex) {
        StyleSheet style_sheet = doc.getStyleSheet();
        StyleDescription style = style_sheet.getStyleDescription(styleIndex);
        String styleName = style.getName();
        if (styleName != null && styleName.contains("标题")) {
          String text = p.text();
          titles.add(text);
        } else if (styleName != null && styleName.contains("正文")) {
          String text = p.text();
          context.add(text);
        }
      }
    }

    // 得到word数据流
    byte[] dataStream = doc.getDataStream();
    // 用于在一段范围内获得段落数
    int numCharacterRuns = r.numCharacterRuns();
    // System.out.println("CharacterRuns 数:"+numCharacterRuns);
    // 负责图像提取 和 确定一些文件某块是否包含嵌入的图像。
    PicturesTable table = new PicturesTable(doc, dataStream, null, null, null);

    // 文章图片编号
    /*int i = 1;
    for(int j=0 ; j<numCharacterRuns ; j++){
        //这个类表示一个文本运行，有着共同的属性。
        CharacterRun run = r.getCharacterRun(j);
        //是否存在图片
        boolean bool = table.hasPicture(run);
        if(bool) {
            //返回图片对象绑定到指定的CharacterRun
            Picture pic = table.extractPicture(run, true);
            //图片的内容字节写入到指定的输出流。
            pic.writeImageContent(new FileOutputStream("D:temp"+filename+"_"+i+".jpg"));
            i++;
        }
    }*/
    log.info("test");
    if (type == 1) {
      log.info("titles: " + titles);
      return titles;
    } else if (type == 2) {
      log.info("context: " + context);
      return context;
    }
    log.info("list: " + list.toString());
    return list;
  }

  // 获取2007版word标题 （这个方法有一点问题）
  public static List<String> getWordTitles2007(String path) throws IOException {
    InputStream is = new FileInputStream(path);
    XWPFDocument doc = new XWPFDocument(is);
    // HWPFDocument doc = new HWPFDocument(is);
    // Range r = doc.getRange();
    List<XWPFRun> listRun;
    List<XWPFParagraph> listParagraphs = doc.getParagraphs(); // 得到段落信息
    List<String> list = new ArrayList<String>();

    /*for (int i = 0; i<listParagraphs.size(); i++) {
        System.out.println(listParagraphs.get(i).getRuns().get(0).getText(0));
        String str = listParagraphs.get(i).getRuns().get(0).getText(0);
        list.add(str);
    }*/

    List<XWPFParagraph> paras = doc.getParagraphs();
    for (XWPFParagraph para : paras) {
      // 当前段落的属性
      // CTPPr pr = para.getCTP().getPPr();
      if (para.getText() != null && !"".equals(para.getText()) && !"r".equals(para.getText())) {
        System.out.println(para.getText().trim());
        String str = para.getText();
        String str1 = "  " + para.getText().replaceAll("\\n", "").replaceAll("\\t", "") + "\n";
        list.add(str);
      }
    }

    /*XWPFWordExtractor extractor = new XWPFWordExtractor(doc);
    String text = extractor.getText();
    // System.out.println(text);
    POIXMLProperties.CoreProperties coreProps = extractor.getCoreProperties();
    String title = coreProps.getTitle();
    System.out.println(title);*/

    // 获取文档中所有的表格
    /*List<XWPFTable> tables = doc.getTables();
    List<XWPFTableRow> rows;
    List<XWPFTableCell> cells;
    for (XWPFTable table : tables) {
        // 表格属性
        // CTTblPr pr = table.getCTTbl().getTblPr();
        // 获取表格对应的行
        rows = table.getRows();
        for (XWPFTableRow row : rows) {
            //获取行对应的单元格
            cells = row.getTableCells();
            for (XWPFTableCell cell : cells) {
                System.out.println(cell.getText());;
            }
        }
    }*/
    close(is);

    return list;
  }

  // 获取2007版word文档内容
  public static List<String> getParagraphText2007(String filePath) throws Exception {
    InputStream is = new FileInputStream(filePath);
    XWPFDocument doc = new XWPFDocument(is);

    List<String> context = new ArrayList<String>();
    List<XWPFParagraph> paras = doc.getParagraphs();
    for (XWPFParagraph para : paras) {
      String str = "  " + para.getText().replaceAll("\\n", "").replaceAll("\\t", "") + "\n";
      context.add(str);
    }

    // 获取文档中所有的表格
    /*List<XWPFTable> tables = doc.getTables();
    List<XWPFTableRow> rows;
    List<XWPFTableCell> cells;
    for (XWPFTable table : tables) {
        // 表格属性
        // CTTblPr pr = table.getCTTbl().getTblPr();
        // 获取表格对应的行
        rows = table.getRows();
        for (XWPFTableRow row : rows) {
            //获取行对应的单元格
            cells = row.getTableCells();
            for (XWPFTableCell cell : cells) {
                context.add(cell.getText());
            }
        }
    }*/
    close(is);
    log.info("context:{}", context);
    return context;
  }

  /**
   * 将对比结果写入表格
   *
   * <p>// * @param size 对比list size // * @param object 短句对比结果
   *
   * @throws Exception
   */
  //  public static void writeTable(int size, List<MatchingObject> object, String returnPath)
  //      throws Exception {
  //    XWPFDocument doc = new XWPFDocument();
  //    // 获取新建文档对象的样式
  //    XWPFStyles newStyles = doc.createStyles();
  //    // 关键行 // 修改设置文档样式为静态块中读取到的样式
  //    // newStyles.setStyles(wordStyles);
  //    // 创建一个表格
  //    XWPFTable table = doc.createTable(size, 2);
  //    // 这里增加的列原本初始化创建的行在通过getTableCells()方法获取时获取不到，但通过row新增的就可以。
  //    // table.addNewCol(); //给表格增加一列
  //    // table.createRow(); //给表格新增一行
  //    List<XWPFTableRow> rows = table.getRows();
  //    // 表格属性
  //    CTTblPr tablePr = table.getCTTbl().addNewTblPr();
  //    // 表格宽度
  //    CTTblWidth width = tablePr.addNewTblW();
  //    width.setW(BigInteger.valueOf(9000));
  //    XWPFTableRow row;
  //    List<XWPFTableCell> cells;
  //    XWPFTableCell cell;
  //    int rowSize = rows.size();
  //    int cellSize;
  //    for (int i = 0; i < rowSize; i++) {
  //      row = rows.get(i);
  //      // 新增单元格
  //      // row.addNewTableCell();
  //      // 设置行的高度
  //      row.setHeight(400);
  //      // 行属性
  //      // CTTrPr rowPr = row.getCtRow().addNewTrPr();
  //      // 这种方式是可以获取到新增的cell的。
  //      // List<CTTc> list = row.getCtRow().getTcList();
  //      cells = row.getTableCells();
  //      cellSize = cells.size();
  //      for (int j = 0; j < cellSize; j++) {
  //        cell = cells.get(j);
  //        if (object.get(i).getMark() != 0) {
  //          // 设置单元格的颜色
  //          cell.setColor("ff0000"); // 红色
  //        }
  //        // 单元格属性
  //        CTTcPr cellPr = cell.getCTTc().addNewTcPr();
  //        cellPr.addNewVAlign().setVal(STVerticalJc.CENTER);
  //        if (j == 0) {
  //          cellPr.addNewTcW().setW(BigInteger.valueOf(4500));
  //          if (object.get(i).getMark() == 2) { // 新增
  //            cell.setText("");
  //          } else { // 不变、新增、修改
  //            cell.setText(object.get(i).getData());
  //          }
  //        } else if (j == 1) {
  //          cellPr.addNewTcW().setW(BigInteger.valueOf(4500));
  //          if (object.get(i).getMark() == 3) { // 修改
  //            cell.setText(object.get(i).getDataAds());
  //          } else if (object.get(i).getMark() == 1) { // 删除
  //            cell.setText("");
  //          } else {
  //            cell.setText(object.get(i).getData());
  //          }
  //        }
  //      }
  //    }
  //    // 文件不存在时会自动创建
  //    OutputStream os = new FileOutputStream(returnPath);
  //    // 写入文件
  //    doc.write(os);
  //    close(os);
  //  }

  // 模板方式实现写word
  public static void formatDoc() throws IOException {
    // 新建的word文档对象
    XWPFDocument doc = new XWPFDocument();
    // 获取新建文档对象的样式
    XWPFStyles newStyles = doc.createStyles();
    // 关键行// 修改设置文档样式为静态块中读取到的样式
    newStyles.setStyles(wordStyles);

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
    FileOutputStream fos = new FileOutputStream("D://myDoc1.docx");
    doc.write(fos);
    fos.close();
  }

  // 自定义样式写word
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

  // 增加自定义标题
  private static void addCustomHeadingStyle(
      XWPFDocument docxDocument, String strStyleId, int headingLevel) {

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

  /**
   * 关闭输入流
   *
   * @param is 输入流
   */
  private static void close(InputStream is) {
    if (is != null) {
      try {
        is.close();
      } catch (IOException e) {
        log.error("流关闭异常", e);
      }
    }
  }

  /**
   * 关闭输出流
   *
   * @param os 输出流
   */
  private static void close(OutputStream os) throws Exception {
    if (os != null) {
      try {
        os.close();
      } catch (IOException e) {
        log.error("流关闭异常", e);
      }
    }
  }
}
