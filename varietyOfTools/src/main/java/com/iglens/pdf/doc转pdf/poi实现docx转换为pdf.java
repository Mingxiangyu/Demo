package com.iglens.pdf.doc转pdf;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import org.apache.poi.xwpf.converter.core.utils.StringUtils;
import org.apache.poi.xwpf.converter.pdf.PdfConverter;
import org.apache.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

/**
 * @author T480S
 */
public class poi实现docx转换为pdf {

  public static void main(String[] args) {
    String filepath = "C:\\Users\\T480S\\Desktop\\1.docx";
    String outpath = "C:\\Users\\T480S\\Desktop\\我是结果.pdf";
    // 如果报org.apache.poi.POIXMLDocumentPart.getPackageRelationship()Lorg/apache/poi/openxml4j/opc/PackageRelationship;<br>
    // 证明是poi版本太高，3.15版poi可以正常使用
    try (InputStream source = new FileInputStream(filepath);
        OutputStream target = new FileOutputStream(outpath); ) {
      PdfOptions options = PdfOptions.create();
      wordConverterToPdf(source, target, options, null);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * 将word文档， 转换成pdf, 中间替换掉变量
   *
   * @param source 源为word文档， 必须为docx文档
   * @param target 目标输出
   * @param params 需要替换的变量
   * @throws Exception
   */
  public static void wordConverterToPdf(
      InputStream source, OutputStream target, Map<String, String> params) throws Exception {
    wordConverterToPdf(source, target, null, params);
  }

  /**
   * 将word文档， 转换成pdf, 中间替换掉变量
   *
   * @param source 源为word文档， 必须为docx文档
   * @param target 目标输出
   * @param params 需要替换的变量
   * @param options PdfOptions.create().fontEncoding( "windows-1250" ) 或者其他
   * @throws Exception
   */
  public static void wordConverterToPdf(
      InputStream source, OutputStream target, PdfOptions options, Map<String, String> params)
      throws IOException {
    XWPFDocument doc = new XWPFDocument(source);
    paragraphReplace(doc.getParagraphs(), params);
    for (XWPFTable table : doc.getTables()) {
      for (XWPFTableRow row : table.getRows()) {
        for (XWPFTableCell cell : row.getTableCells()) {
          paragraphReplace(cell.getParagraphs(), params);
        }
      }
    }
    PdfConverter.getInstance().convert(doc, target, options);
  }

  /** 替换段落中内容 */
  private static void paragraphReplace(List<XWPFParagraph> paragraphs, Map<String, String> params) {

    if (params != null) {
      for (XWPFParagraph p : paragraphs) {
        for (XWPFRun r : p.getRuns()) {
          String content = r.getText(r.getTextPosition());
          if (StringUtils.isNotEmpty(content) && params.containsKey(content)) {
            r.setText(params.get(content), 0);
          }
        }
      }
    }
  }
}
