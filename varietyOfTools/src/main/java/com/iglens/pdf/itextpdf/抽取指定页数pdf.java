package com.iglens.pdf.itextpdf;

import cn.hutool.core.io.FileUtil;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfCopy;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSmartCopy;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

/** PDF工具类
 * @author xming
 *   原文链接：https://blog.csdn.net/ranxiaotop/article/details/125953773*/
public class 抽取指定页数pdf {

  public static void main(String[] args) {
    String spec = "C:\\Users\\zhouhuilin\\Desktop\\pdf.pdf";
    String extractspec = "C:\\Users\\zhouhuilin\\Desktop\\pdfextract.pdf";
    extract(spec, extractspec, 5, 120);

    // extract(
    //     spec,
    //     extractspec,
    //     new ArrayList<Integer>() {
    //       {
    //         add(6);
    //       }
    //     });
  }

  /**
   * 抽取PDF文件
   *
   * @param sourceFile 源PDF文件路径
   * @param targetFile 目标PDF文件路径
   * @param extractedPageNums 需要抽取的页码
   */
  public static void extract(
      String sourceFile, String targetFile, List<Integer> extractedPageNums) {
    Objects.requireNonNull(sourceFile);
    Objects.requireNonNull(targetFile);
    PdfReader reader = null;
    Document document = null;
    FileOutputStream outputStream = null;
    try {
      // 读取源文件
      reader = new PdfReader(sourceFile);
      // 创建新的文档
      document = new Document();
      // 创建目标PDF文件
      File file = new File(targetFile);
      if (!file.exists()) {
        FileUtil.newFile(targetFile);
      }
      outputStream = new FileOutputStream(targetFile);
      PdfCopy pdfCopy = new PdfSmartCopy(document, outputStream);

      // 获取源文件的页数
      int pages = reader.getNumberOfPages();
      document.open();

      // 注意此处的页码是从1开始
      for (int page = 1; page <= pages; page++) {
        // 如果是指定的页码，则进行复制
        if (extractedPageNums.contains(page)) {
          pdfCopy.addPage(pdfCopy.getImportedPage(reader, page));
        }
      }
    } catch (IOException | DocumentException e) {
      e.printStackTrace();
    } finally {
      if (reader != null) {
        reader.close();
      }

      if (document != null) {
        document.close();
      }

      if (outputStream != null) {
        try {
          outputStream.flush();
          outputStream.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  /**
   * 抽取PDF文件
   * @param sourceFile 源PDF文件路径
   * @param targetFile 目标PDF文件路径
   * @param fromPageNum 起始页码
   * @param toPageNum 结束页码
   */
  public static void extract(String sourceFile, String targetFile, int fromPageNum, int toPageNum) {
    Objects.requireNonNull(sourceFile);
    Objects.requireNonNull(targetFile);
    PdfReader reader = null;
    Document document = null;
    FileOutputStream outputStream = null;
    try {
      // 读取源文件
      reader = new PdfReader(sourceFile);
      // 创建新的文档
      document = new Document();
      // 创建目标PDF文件
      outputStream = new FileOutputStream(targetFile);
      PdfCopy pdfCopy = new PdfSmartCopy(document, outputStream);

      // 获取源文件的页数
      int pages = reader.getNumberOfPages();
      document.open();

      // 注意此处的页码是从1开始
      for (int page = 1; page <= pages; page++) {
        if (page >= fromPageNum && page <= toPageNum) {
          pdfCopy.addPage(pdfCopy.getImportedPage(reader, page));
        }
      }
    } catch (IOException | DocumentException e) {
      e.printStackTrace();
    } finally {
      if (reader != null) {
        reader.close();
      }

      if (document != null) {
        document.close();
      }

      if (outputStream != null) {
        try {
          outputStream.flush();
          outputStream.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }
}
