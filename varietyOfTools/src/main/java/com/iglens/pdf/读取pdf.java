package com.iglens.pdf;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

/** 功能 PDF读写类 @CreateTime 2011-4-14 下午02:44:11 */
@Deprecated
public class 读取pdf {

  //  public static final String CHARACTOR_FONT_CH_FILE = "SIMFANG.TTF";  //仿宋常规
  public static final String CHARACTOR_FONT_CH_FILE = "SIMHEI.TTF"; // 黑体常规

  public static final Rectangle PAGE_SIZE = PageSize.A4;
  public static final float MARGIN_LEFT = 50;
  public static final float MARGIN_RIGHT = 50;
  public static final float MARGIN_TOP = 50;
  public static final float MARGIN_BOTTOM = 50;
  public static final float SPACING = 20;

  private Document document = null;

  /**
   * 功能：创建导出数据的目标文档
   *
   * @param fileName 存储文件的临时路径
   * @return
   */
  public void createDocument(String fileName) {
    File file = new File(fileName);
    FileOutputStream out = null;
    document = new Document(PAGE_SIZE, MARGIN_LEFT, MARGIN_RIGHT, MARGIN_TOP, MARGIN_BOTTOM);
    try {
      out = new FileOutputStream(file);
      //          PdfWriter writer =
      PdfWriter.getInstance(document, out);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (DocumentException e) {
      e.printStackTrace();
    }
    // 打开文档准备写入内容
    document.open();
  }

  /**
   * 将章节写入到指定的PDF文档中
   *
   * @param chapter
   * @return
   */
  public void writeChapterToDoc(Chapter chapter) {
    try {
      if (document != null) {
        if (!document.isOpen()) {
          document.open();
        }
        document.add(chapter);
      }
    } catch (DocumentException e) {
      e.printStackTrace();
    }
  }

  /**
   * 功能 创建PDF文档中的章节
   *
   * @param title 章节标题
   * @param chapterNum 章节序列号
   * @param alignment 0表示align=left，1表示align=center
   * @param numberDepth 章节是否带序号 设值=1 表示带序号 1.章节一；1.1小节一...，设值=0表示不带序号
   * @param font 字体格式
   * @return Chapter章节
   */
  public static Chapter createChapter(
      String title, int chapterNum, int alignment, int numberDepth, Font font) {
    Paragraph chapterTitle = new Paragraph(title, font);
    chapterTitle.setAlignment(alignment);
    Chapter chapter = new Chapter(chapterTitle, chapterNum);
    chapter.setNumberDepth(numberDepth);
    return chapter;
  }

  /**
   * 功能：创建某指定章节下的小节
   *
   * @param chapter 指定章节
   * @param title 小节标题
   * @param font 字体格式
   * @param numberDepth 小节是否带序号 设值=1 表示带序号 1.章节一；1.1小节一...，设值=0表示不带序号
   * @return section在指定章节后追加小节
   */
  public static Section createSection(Chapter chapter, String title, Font font, int numberDepth) {
    Section section = null;
    if (chapter != null) {
      Paragraph sectionTitle = new Paragraph(title, font);
      sectionTitle.setSpacingBefore(SPACING);
      section = chapter.addSection(sectionTitle);
      section.setNumberDepth(numberDepth);
    }
    return section;
  }

  /**
   * 功能：向PDF文档中添加的内容
   *
   * @param text 内容
   * @param font 内容对应的字体
   * @return phrase 指定字体格式的内容
   */
  public static Phrase createPhrase(String text, Font font) {
    Phrase phrase = new Paragraph(text, font);
    return phrase;
  }

  /**
   * 功能：创建列表
   *
   * @param numbered 设置为 true 表明想创建一个进行编号的列表
   * @param lettered 设置为true表示列表采用字母进行编号，为false则用数字进行编号
   * @param symbolIndent
   * @return list
   */
  public static List createList(boolean numbered, boolean lettered, float symbolIndent) {
    List list = new List(numbered, lettered, symbolIndent);
    return list;
  }

  /**
   * 功能：创建列表中的项
   *
   * @param content 列表项中的内容
   * @param font 字体格式
   * @return listItem
   */
  public static ListItem createListItem(String content, Font font) {
    ListItem listItem = new ListItem(content, font);
    return listItem;
  }

  /**
   * 功能：创造字体格式
   *
   * @param fontname
   * @param size 字体大小
   * @param style 字体风格
   * @param color 字体颜色
   * @return Font
   */
  public static Font createFont(String fontname, float size, int style, BaseColor color) {
    Font font = FontFactory.getFont(fontname, size, style, color);
    return font;
  }

  /**
   * 功能： 返回支持中文的字体---仿宋
   *
   * @param size 字体大小
   * @param style 字体风格
   * @param color 字体 颜色
   * @return 字体格式
   */
  public static Font createCHineseFont(float size, int style, BaseColor color) {
    BaseFont bfChinese = null;
    try {
      bfChinese =
          BaseFont.createFont(CHARACTOR_FONT_CH_FILE, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
    } catch (DocumentException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return new Font(bfChinese, size, style, color);
  }

  /** 最后关闭PDF文档 */
  public void closeDocument() {
    if (document != null) {
      document.close();
    }
  }

  /**
   * 读PDF文件，使用了pdfbox开源项目
   *
   * @param fileName
   */
  public static void readPDF(String fileName) {
    File file = new File(fileName);
    FileInputStream in = null;
    try {
      in = new FileInputStream(fileName);
      // 新建一个PDF解析器对象
      PDFParser parser = new PDFParser(new RandomAccessFile(file, "rw"));
      // 对PDF文件进行解析
      parser.parse();
      // 获取解析后得到的PDF文档对象
      PDDocument pdfdocument = parser.getPDDocument();
      // 新建一个PDF文本剥离器
      PDFTextStripper stripper = new PDFTextStripper();
      stripper.setSortByPosition(true); // sort设置为true 则按照行进行读取，默认是false
      // 从PDF文档对象中剥离文本
      String result = stripper.getText(pdfdocument);
      FileWriter fileWriter = new FileWriter(new File("pdf.txt"));
      fileWriter.write(result);
      fileWriter.flush();
      fileWriter.close();
      System.out.println("PDF文件的文本内容如下：");
      System.out.println(result);

    } catch (Exception e) {
      System.out.println("读取PDF文件" + file.getAbsolutePath() + "生失败！" + e);
      e.printStackTrace();
    } finally {
      if (in != null) {
        try {
          in.close();
        } catch (IOException e1) {
        }
      }
    }
  }

  /**
   * 测试pdf文件的创建
   *
   * @param args
   */
  public static void main(String[] args) {

    String fileName = "C:\\Users\\tizzy\\Desktop\\测试.pdf"; // 这里先手动把绝对路径的文件夹给补上。
    读取pdf pdfUtil = new 读取pdf();
//    pdfUtil.writeChapterToDoc(chapter);
    pdfUtil.closeDocument();
  }
}
