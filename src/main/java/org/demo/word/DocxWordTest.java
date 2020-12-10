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

import org.apache.poi.hpsf.DocumentSummaryInformation;
import org.apache.poi.hpsf.SummaryInformation;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.hwpf.usermodel.*;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFPictureData;

import java.io.*;
import java.util.List;

public class DocxWordTest {
  public static void main(String[] args) {
    DocxWordTest docxWordTest = new DocxWordTest();
    docxWordTest.poiReadDocxTest();
  }

  public void poiReadDocxTest() {
    File file = new File("E:\\ktkj\\日本.docx");
    try {
      FileInputStream fis = new FileInputStream(file);
      XWPFDocument xdoc = new XWPFDocument(fis);
      XWPFWordExtractor extractor = new XWPFWordExtractor(xdoc);
      String doc1 = extractor.getText();
      System.out.println(doc1);
      List<XWPFPictureData> allPictures = xdoc.getAllPictures();
      fis.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
