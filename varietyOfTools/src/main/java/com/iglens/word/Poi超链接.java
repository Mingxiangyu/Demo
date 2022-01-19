package com.iglens.word;

import java.io.FileOutputStream;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFHyperlinkRun;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRelation;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHyperlink;

public class Poi超链接 {

  /**
   * 为当前paragraph 添加超链接
   *
   * @param paragraph
   * @param uri 超链接地址
   * @return
   */
  static XWPFHyperlinkRun createHyperlinkRun(XWPFParagraph paragraph, String uri) {
    String rId =
        paragraph
            .getPart()
            .getPackagePart()
            .addExternalRelationship(uri, XWPFRelation.HYPERLINK.getRelation())
            .getId();

    CTHyperlink cthyperLink = paragraph.getCTP().addNewHyperlink();
    cthyperLink.setId(rId);
    cthyperLink.addNewR();

    return new XWPFHyperlinkRun(cthyperLink, cthyperLink.getRArray(0), paragraph);
  }

  public static void main(String[] args) throws Exception {

    XWPFDocument document = new XWPFDocument();

    XWPFParagraph paragraph = document.createParagraph();
    XWPFRun run = paragraph.createRun();
    run.setText("This is a text paragraph having a link to Google ");

    XWPFHyperlinkRun hyperlinkrun = createHyperlinkRun(paragraph, "https://www.google.de");
    hyperlinkrun.setText("https://www.google.de");
    hyperlinkrun.setColor("0000FF");
    hyperlinkrun.setUnderline(UnderlinePatterns.SINGLE);

    run = paragraph.createRun();
    run.setText(" in it.");

    //    XWPFFooter footer = document.createFooter(HeaderFooterType.DEFAULT);//需要poi3.17
    //    paragraph = footer.createParagraph();//需要poi3.17
    run = paragraph.createRun();
    run.setText("Email: ");

    hyperlinkrun = createHyperlinkRun(paragraph, "mailto:me@example.com");
    hyperlinkrun.setText("me@example.com");
    hyperlinkrun.setColor("0000FF");
    hyperlinkrun.setUnderline(UnderlinePatterns.SINGLE);

    FileOutputStream out = new FileOutputStream("CreateWordHyperlinks.docx");
    document.write(out);
    out.close();
    document.close();
  }
}
