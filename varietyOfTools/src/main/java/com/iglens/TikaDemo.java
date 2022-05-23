package com.iglens;

import com.google.common.io.Files;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.microsoft.ooxml.OOXMLParser;
import org.apache.tika.parser.pdf.PDFParser;
import org.apache.tika.parser.txt.TXTParser;
import org.apache.tika.sax.BodyContentHandler;
import org.apache.tika.sax.ExpandedTitleContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

public class TikaDemo {
  // TIKA常见格式文件抽取内容并做预处理 https://www.freesion.com/article/3355931478/
  // 使用Tika进行文档解析抽取 https://blog.csdn.net/yangang1223/article/details/101367870
  // Tika提取txt文档内容，以及乱码处理 https://blog.csdn.net/weixin_39841589/article/details/85333906
  // 使用Tika进行文本抽取 https://blog.csdn.net/tianshan2010/article/details/105026877/
  // Tika教程 https://www.yiibai.com/tika
  // 如何使用Tika提取文件内容 https://blog.csdn.net/qq_42629988/article/details/112802828
  // 如何从word、excel、pdf等文件中提取文字（Tika）https://www.dandelioncloud.cn/article/details/1510962503600427010

  public static void 提取url中的文字信息() throws IOException, TikaException {
    Tika tika = new Tika();
    String s = tika.parseToString(new URL("https://www.baidu.com"));
    System.out.println(s);
  }

  public static void 提取word中的文字() throws IOException, TikaException {
    Tika tika = new Tika();
    File file = new File("文档.docx");
    String s = tika.parseToString(file);
    System.out.println(s);
  }

  public static void 提取excel中的文字() throws IOException, TikaException {
    Tika tika = new Tika();
    File file = new File("工作簿.xlsx");
    String s = tika.parseToString(file);
    System.out.println(s);
  }

  public static void 提取pdf文件中的文字() throws IOException, TikaException {
    Tika tika = new Tika();
    File file = new File("pdf文件.pdf");
    String s = tika.parseToString(file);
    System.out.println(s);
  }

  /**
   * Tika提取pdf文件内容
   *
   * @return
   */
  public String paserPdf() {
    try {
      File file = new File("C:\\Users\\FileRecv\\test1.pdf");
      BodyContentHandler handler = new BodyContentHandler();
      Metadata metadata = new Metadata();
      FileInputStream fileInputStream = new FileInputStream(file);
      ParseContext parseContext = new ParseContext();

      // 提取图像信息
      // JpegParser JpegParser = new JpegParser();
      // 提取PDF
      PDFParser pdfParser = new PDFParser();
      pdfParser.parse(fileInputStream, handler, metadata, parseContext);

      return handler.toString();
      /*String[] names = metadata.names();
      for (String name : names) {
          System.out.println("name:"+metadata.get(name));
      }*/
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "";
  }

  /** Tika提取Excel内容 @return */
  public String parseExcel() {
    try {
      File file = new File("C:\\Users\\FileRecv\\book1.xlsx");

      BodyContentHandler handler = new BodyContentHandler();
      Metadata metadata = new Metadata();
      FileInputStream fileInputStream = new FileInputStream(file);
      ParseContext parseContext = new ParseContext();

      OOXMLParser msofficeparser = new OOXMLParser();
      msofficeparser.parse(fileInputStream, handler, metadata, parseContext);
      return handler.toString();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "";
  }

  /** Tika提取文本文档 @return */
  public String parseTxt() {
    try {
      File file = new File("C:\\Users\\FileRecv\\笔记.txt");

      BodyContentHandler handler = new BodyContentHandler();
      Metadata metadata = new Metadata();
      FileInputStream fileInputStream = new FileInputStream(file);
      ParseContext parseContext = new ParseContext();

      TXTParser txtParser = new TXTParser();
      txtParser.parse(fileInputStream, handler, metadata, parseContext);
      return handler.toString();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "";
  }

  /**
   * Tika获取文件格式以及提取doc文件
   *
   * @return
   * @throws IOException
   * @throws TikaException
   */
  public String parseDoc() throws IOException, TikaException {
    File file = new File("C:\\Users\\FileRecv\\oracle安装教程.docx");
    Tika tika = new Tika();
    // 获取格式
    String detect = tika.detect(file);
    // 获取内容
    String filecontent = tika.parseToString(file);
    System.out.println(filecontent);
    return detect;
  }

  /**
   * Tika AutoDetectParser类来识别和抽取内容
   *
   * @throws TikaException
   * @throws SAXException
   * @throws IOException
   */
  public static void getTextFronPDF() throws IOException, SAXException, TikaException {
    // 构建InputStream来读取数据
    InputStream input =
        // pathname可以写文件路径，pdf，word，html等
        new FileInputStream(new File("./myfile/Active Learning.pdf"));
    BodyContentHandler textHandler = new BodyContentHandler();
    Metadata matadata = new Metadata(); // Metadata对象保存了作者，标题等元数据
    // 当调用parser，AutoDetectParser会自动估计文档MIME类型，此处输入pdf文件，因此可以使用PDFParser
    Parser parser = new AutoDetectParser();
    ParseContext context = new ParseContext();
    parser.parse(input, textHandler, matadata, context); // 执行解析过程
    input.close();
    System.out.println("Title: " + matadata.get(Metadata.TITLE));
    System.out.println("Type: " + matadata.get(Metadata.TYPE));
    System.out.println("Body: " + textHandler.toString()); // 从textHandler打印正文
  }

  // 原文链接：https://blog.csdn.net/tianshan2010/article/details/105026877/
  public static Map<String, Object> parseFile(File file) {
    Map<String, Object> meta = new HashMap<String, Object>();
    Parser parser = new AutoDetectParser();
    InputStream input = null;
    try {
      Metadata metadata = new Metadata();
      metadata.set(Metadata.CONTENT_ENCODING, "utf-8");
      metadata.set(Metadata.RESOURCE_NAME_KEY, file.getName());
      input = new FileInputStream(file);
      ContentHandler handler = new BodyContentHandler(10 * 1024 * 1024);

      ParseContext context = new ParseContext();
      context.set(Parser.class, parser);
      parser.parse(input, handler, metadata, context);
      for (String name : metadata.names()) {
        meta.put(name, metadata.get(name));
      }
      meta.put("content", handler.toString());
      return meta;
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        if (input != null) {
          input.close();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return null;
  }

  public static String extractHtml(File file) throws IOException {
    byte[] bytes = Files.toByteArray(file);
    AutoDetectParser tikaParser = new AutoDetectParser();
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    SAXTransformerFactory factory = (SAXTransformerFactory) SAXTransformerFactory.newInstance();
    TransformerHandler handler;
    try {
      handler = factory.newTransformerHandler();
    } catch (TransformerConfigurationException ex) {
      throw new IOException(ex);
    }
    handler.getTransformer().setOutputProperty(OutputKeys.METHOD, "html");
    handler.getTransformer().setOutputProperty(OutputKeys.INDENT, "yes");
    handler.getTransformer().setOutputProperty(OutputKeys.ENCODING, "UTF-8");
    handler.setResult(new StreamResult(out));
    ExpandedTitleContentHandler handler1 = new ExpandedTitleContentHandler(handler);
    try {
      tikaParser.parse(new ByteArrayInputStream(bytes), handler1, new Metadata());
    } catch (SAXException | TikaException ex) {
      throw new IOException(ex);
    }
    return new String(out.toByteArray(), "UTF-8");
  }
}
