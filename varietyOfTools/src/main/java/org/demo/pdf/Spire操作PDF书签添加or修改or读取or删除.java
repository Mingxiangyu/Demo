package org.demo.pdf;

import com.spire.pdf.PdfDocument;
import com.spire.pdf.PdfPageBase;
import com.spire.pdf.bookmarks.PdfBookmark;
import com.spire.pdf.bookmarks.PdfBookmarkCollection;
import com.spire.pdf.bookmarks.PdfTextStyle;
import com.spire.pdf.general.PdfDestination;
import com.spire.pdf.graphics.PdfRGBColor;
import com.spire.pdf.graphics.PdfSolidBrush;
import com.spire.pdf.graphics.PdfTrueTypeFont;
import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Point2D;
import java.io.FileWriter;
import java.io.IOException;

public class Spire操作PDF书签添加or修改or读取or删除 {

  public static void main(String[] args) throws IOException {
//    添加书签();
//    添加多级书签();
//    PDF文档添加书签();
//    修改书签();
//    设置打开PDF文档时展开或折叠书签();
    读取书签标题();
//    删除书签();
//    删除指定书签();
  }

  /** 可以通过该书签的索引删除，也可通过该书签的标题删除 */
  private static void 删除指定书签() {
    // 加载PDF
    PdfDocument pdf = new PdfDocument();
    pdf.loadFromFile("AddChildBookmark.pdf");

    // 获取书签的集合
    PdfBookmarkCollection bookmarkCollection = pdf.getBookmarks();
    // 从集合中删除第一个书签及其子书签
    // 通过书签索引删除
    // bookmarkCollection.removeAt(0);
    // 通过书签标题删除
    bookmarkCollection.remove("第一章 绪论");

    // 保存
    pdf.saveToFile("DeleteBookmark.pdf");
  }

  /** 删除了PDF文档中的所有书签 */
  private static void 删除书签() {
    // 加载PDF
    PdfDocument pdf = new PdfDocument();
    pdf.loadFromFile("AddChildBookmark.pdf");

    // 获取书签的集合
    PdfBookmarkCollection bookmarkCollection = pdf.getBookmarks();

    // 删除集合中的所有书签
    bookmarkCollection.clear();

    // 保存
    pdf.saveToFile("DeleteBookmark.pdf");
  }

  /**
   * 读取PDF文档中的书签标题
   *
   * @throws IOException wenjian
   */
  private static void 读取书签标题() throws IOException {
    // 加载PDF
    PdfDocument pdf = new PdfDocument();
    pdf.loadFromFile("AddChildBookmark.pdf");

    // 获取书签的集合
    PdfBookmarkCollection bookmarks = pdf.getBookmarks();

    StringBuilder builder = new StringBuilder();

    // 调用ReadBookmarks方法读取书签标题
    ReadBookmarks(bookmarks, builder);

    // 写入到文本文件
    FileWriter fw = new FileWriter("Bookmarks.txt");
    try {
      fw.write(builder.toString());
      fw.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /** PDF书签默认为打开模式，但我们可以设置将PDF书签折叠起来： */
  private static void 设置打开PDF文档时展开或折叠书签() {
    // 加载PDF
    PdfDocument pdf = new PdfDocument();
    pdf.loadFromFile("AddChildBookmark.pdf");

    // false为折叠，true为展开
    pdf.getViewerPreferences().setBookMarkExpandOrCollapse(false);

    // 保存
    pdf.saveToFile("CollapseBookmark.pdf");
  }

  /** 修改现有书签的标题和字体颜色 */
  private static void 修改书签() {
    // 加载PDF
    PdfDocument pdf = new PdfDocument();
    pdf.loadFromFile("AddChildBookmark.pdf");

    // 获取PDF书签集合
    PdfBookmarkCollection bookmarkCollection = pdf.getBookmarks();
    // 获取第一个书签
    PdfBookmark bookmark = bookmarkCollection.get(0);

    // 获取该书签下的第一个子书签
    PdfBookmark childBookmark = bookmark.get(0);
    // 修改子书签的标题
    childBookmark.setTitle("第二级书签");
    // 修改子书签的字体颜色
    childBookmark.setColor(new PdfRGBColor(Color.RED));

    // 保存
    pdf.saveToFile("ModifyBookmark.pdf");
  }

  /** 给现有的PDF文档添加书签 */
  private static void PDF文档添加书签() {
    // 加载PDF
    PdfDocument pdf = new PdfDocument();
    pdf.loadFromFile("Sample.pdf");

    // 获取第一页
    PdfPageBase page = pdf.getPages().get(0);

    // 添加书签
    PdfBookmark bookmark = pdf.getBookmarks().add("明天早上数");

    // 设置书签的文本格式，字体颜色，指向页面和位置
    bookmark.setDisplayStyle(PdfTextStyle.Bold);
    bookmark.setColor(new PdfRGBColor(Color.BLACK));
    bookmark.setDestination(new PdfDestination(page));
    bookmark.getDestination().setLocation(new Point2D.Float(0, 10));

    // 保存
    pdf.saveToFile("AddBkmktoExistPDF.pdf");
  }

  /** 创建一个PDF文档，并给它添加多级（一级和二级）书签 */
  private static void 添加多级书签() {
    // 创建PDF
    PdfDocument pdf = new PdfDocument();
    // 添加一页
    PdfPageBase page = pdf.getPages().add();

    float x = 0;
    float y = 50;

    // 在页面上绘制文字
    PdfTrueTypeFont font = new PdfTrueTypeFont(new Font("Arial Unicode MS", Font.PLAIN, 14), true);
    PdfSolidBrush brush = new PdfSolidBrush(new PdfRGBColor(Color.BLACK));
    page.getCanvas().drawString("第一章 绪论", font, brush, new Point2D.Float(x, y));
    page.getCanvas()
        .drawString("1.1 传热学的研究内容及其在科学技术和工程中的应用", font, brush, new Point2D.Float(x, y + 50));

    // 添加书签
    PdfBookmark bookmark = pdf.getBookmarks().add("第一章 绪论");
    bookmark.setDisplayStyle(PdfTextStyle.Bold);
    bookmark.setColor(new PdfRGBColor(new Color(46, 139, 87)));
    bookmark.setDestination(new PdfDestination(page));
    bookmark.getDestination().setLocation(new Point2D.Float(x, y));

    // 添加第二级书签
    PdfBookmark childBookmark = bookmark.add("1.1 传热学的研究内容及其在科学技术和工程中的应用");
    childBookmark.setDisplayStyle(PdfTextStyle.Italic);
    childBookmark.setColor(new PdfRGBColor(Color.BLACK));
    childBookmark.setDestination(new PdfDestination(page));
    childBookmark.getDestination().setLocation(new Point2D.Float(x, y + 50));

    // 保存
    pdf.saveToFile("AddChildBookmark.pdf");
  }

  /** 创建一个PDF文档，添加书签并设置书签的格式 */
  private static void 添加书签() {
    // 创建PDF
    PdfDocument pdf = new PdfDocument();
    // 添加一页
    PdfPageBase page = pdf.getPages().add();

    float x = 0;
    float y = 50;

    // 在页面上绘制文本内容
    PdfTrueTypeFont font = new PdfTrueTypeFont(new Font("Arial Unicode MS", Font.PLAIN, 14), true);
    PdfSolidBrush brush = new PdfSolidBrush(new PdfRGBColor(Color.BLACK));
    page.getCanvas().drawString("第一章 绪论", font, brush, new Point2D.Float(x, y));

    // 添加书签
    PdfBookmark bookmark = pdf.getBookmarks().add("第一章 绪论");
    // 设置书签的文本格式为加粗
    bookmark.setDisplayStyle(PdfTextStyle.Bold);
    // 设置书签的字体颜色
    bookmark.setColor(new PdfRGBColor(new Color(46, 139, 87))); // SeaGreen
    // 设置书签指向的页面
    bookmark.setDestination(new PdfDestination(page));
    // 设置书签指向的页面具体位置
    bookmark.getDestination().setLocation(new Point2D.Float(x, y));

    // 保存
    pdf.saveToFile("AddBookmark.pdf");
  }

  // ReadBookmarks方法
  static void ReadBookmarks(PdfBookmarkCollection bookmarks, StringBuilder builder)
      throws IOException {

    if (bookmarks.getCount() > 0) {
      for (PdfBookmark parentBookmark : (Iterable<PdfBookmark>) bookmarks) {
        builder.append(parentBookmark.getTitle() + "\r\n");
        ReadBookmarks(parentBookmark, builder);
      }
    }
  }
}
