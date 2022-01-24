package com.iglens.word;

import com.documents4j.api.DocumentType;
import com.documents4j.api.IConverter;
import com.documents4j.job.LocalConverter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 需要调用cmd命令执行转换
 * @author ming
 */
public class Word转pdf_WIN版 {
  public static void main(String[] args) {
    String wordName = "/Users/ming/Documents/明翔宇-Java-4年的副本.docx";
    String pdfName = "/Users/ming/Documents/明翔宇-Java-4年的副本.pdf";
    word2Pdf(wordName, pdfName);
  }

  private static void word2Pdf(String wordName, String pdfName) {
    File inputWord = new File(wordName);
    File outputFile = new File(pdfName);
    try {
      InputStream docxInputStream = new FileInputStream(inputWord);
      OutputStream outputStream = new FileOutputStream(outputFile);
      IConverter converter = LocalConverter.builder().build();
      converter
          .convert(docxInputStream)
          .as(DocumentType.DOCX)
          .to(outputStream)
          .as(DocumentType.PDF)
          .execute();
      outputStream.close();
      System.out.println("success");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
