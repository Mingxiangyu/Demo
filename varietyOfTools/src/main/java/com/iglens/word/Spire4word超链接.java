package com.iglens.word;

import com.spire.doc.Document;
import com.spire.doc.DocumentObject;
import com.spire.doc.FieldType;
import com.spire.doc.FileFormat;
import com.spire.doc.Section;
import com.spire.doc.documents.DocumentObjectType;
import com.spire.doc.documents.HorizontalAlignment;
import com.spire.doc.documents.HyperlinkType;
import com.spire.doc.documents.Paragraph;
import com.spire.doc.documents.ParagraphStyle;
import com.spire.doc.fields.DocPicture;
import com.spire.doc.fields.Field;
import java.util.ArrayList;
import java.util.List;
/**
 * 原文链接：https://blog.csdn.net/weixin_42498989/article/details/114565214
 *
 * @author T480S
 */
public class Spire4word超链接 {
  public static void main(String[] args) {
    //    Document document = 添加word中超链接();

    修改超链接();
  }

  /** */
  private static void 修改超链接() {
    // 加载Word文档
    Document doc = new Document();
    doc.loadFromFile("C:\\Users\\T480s\\Desktop\\ces1.docx");

    List<Field> hyperlinks = new ArrayList<>();
    // 遍历文档中的节
    for (Section section : (Iterable<? extends Section>) doc.getSections()) {
      // 遍历每个节中的段落
      for (Paragraph para : (Iterable<Paragraph>) section.getParagraphs()) {
        for (DocumentObject obj : (Iterable<DocumentObject>) para.getChildObjects()) {
          // 找到超链接并将其添加至list中
          if (obj.getDocumentObjectType().equals(DocumentObjectType.Field)) {
            Field field = (Field) obj;

            if (field.getType().equals(FieldType.Field_Hyperlink)) {
              hyperlinks.add(field);
            }
          }
        }
      }
    }

    // 修改第一个超链接的展示文字和链接地址
    hyperlinks.get(0).setCode("HYPERLINK \"https://www.zhihu.com/topic/19649017/hot");
    hyperlinks.get(0).setFieldText("徐志摩(现代新月派代表诗人)");

    // 保存文档
    doc.saveToFile("C:\\Users\\T480s\\Desktop\\ces1.docx", FileFormat.Docx_2013);
  }

  /** */
  private static Document 添加word中超链接() {
    // 创建Word文档
    Document doc = new Document();
    Section section = doc.addSection();

    // 添加网页链接
    Paragraph paragraph = section.addParagraph();
    paragraph.appendText("网页链接：");
    paragraph.appendHyperlink(
        "https://www.jianshu.com/u/96431825b792", "个人主页", HyperlinkType.Web_Link);

    // 添加邮箱链接
    paragraph = section.addParagraph();
    paragraph.appendText("邮箱链接：");
    paragraph.appendHyperlink(
        "mailto:2540321664@qq.com", "2540321664@qq.com", HyperlinkType.E_Mail_Link);

    // 添加文档链接
    paragraph = section.addParagraph();
    paragraph.appendText("文档链接：");
    String filePath = "C:\\Users\\Test1\\Desktop\\财务预算.xlsx";
    paragraph.appendHyperlink(filePath, "点击查看财务预算", HyperlinkType.File_Link);

    // 添加图片超链接
    paragraph = section.addParagraph();
    paragraph.appendText("图片链接：");
    paragraph = section.addParagraph();
    DocPicture picture =
        paragraph.appendPicture("C:\\Users\\T480s\\Desktop\\微信图片编辑_20210430145740.jpg");
    paragraph.appendHyperlink(
        "https://www.jianshu.com/u/96431825b792", picture, HyperlinkType.Web_Link);

    // 创建段落样式
    ParagraphStyle style1 = new ParagraphStyle(doc);
    style1.setName("style");
    style1.getCharacterFormat().setFontName("宋体");
    doc.getStyles().add(style1);

    for (int i = 0; i < section.getParagraphs().getCount(); i++) {
      // 将段落居中
      section.getParagraphs().get(i).getFormat().setHorizontalAlignment(HorizontalAlignment.Center);
      // 段落末尾自动添加间隔
      section.getParagraphs().get(i).getFormat().setAfterAutoSpacing(true);
      // 应用段落样式
      section.getParagraphs().get(i).applyStyle(style1.getName());
    }

    // 保存文档
    doc.saveToFile("C:\\Users\\T480s\\Desktop\\ces1.docx", FileFormat.Docx_2013);
    return doc;
  }
}
