package org.demo.pdf;

import java.io.File;
import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class ReadPdf {
  public static void main(String[] args) {

    String s = readPDFText("C:\\Users\\T480S\\Desktop\\DF-5B-DF-5B-001.pdf");
    System.out.println(s);
  }

  public static String readPDFText(String filePath) {
    PDFTextStripper pdfStripper;
    PDDocument pdDoc;
    File file = new File(filePath);
    try {
      pdfStripper = new PDFTextStripper();
      pdDoc = PDDocument.load(file);
      return pdfStripper.getText(pdDoc);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return "";
  }
}
