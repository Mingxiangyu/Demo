package com.deepz.fileparse.parse;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.language.LanguageIdentifier;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.pdf.PDFParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

public class TikaParser {
  // String pathname = "C:\\Users\\zhouhuilin\\Desktop\\fileparse\\需求.txt";
  static String pathname =
      "G:\\软件备份\\MyDocument\\IDE\\[码匠笔记] - 2022-05-19 XXL-JOB分布式任务调度平台(真·保姆级教程).pdf";

  public static void main(String[] args) throws TikaException, IOException, SAXException {



    // test6();
    // test7();
    // fileToTxt(new File(pathname));
    Tika tika = new Tika();
  }
  /**
   * 根据Parser得到文档的内容
   *
   * @param f
   * @return
   */
  public static String fileToTxt(File f) {
    org.apache.tika.parser.Parser parser = new AutoDetectParser(); // 自动检测文档类型，自动创建相应的解析器
    InputStream is = null;
    try {
      Metadata metadata = new Metadata();
      metadata.set(Metadata.AUTHOR, "空号"); // 重新设置文档的媒体内容
      metadata.set(Metadata.RESOURCE_NAME_KEY, f.getName());
      is = new FileInputStream(f);
      ContentHandler handler = new BodyContentHandler();
      ParseContext context = new ParseContext();
      context.set(org.apache.tika.parser.Parser.class, parser);
      parser.parse(is, handler, metadata, context);
      for (String name : metadata.names()) {
        System.out.println(name + ":" + metadata.get(name));
      }
      return handler.toString();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (SAXException e) {
      e.printStackTrace();
    } catch (TikaException e) {
      e.printStackTrace();
    } finally {
      try {
        if (is != null) is.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return null;
  }

  public static void test6() {

    // Instantiating a file object
    File file = new File(pathname);

    // Parser method parameters
    AutoDetectParser parser = new AutoDetectParser();
    BodyContentHandler handler = new BodyContentHandler();
    Metadata metadata = new Metadata();
    ParseContext context = new ParseContext();
    FileInputStream content = null;
    try {
      content = new FileInputStream(file);
    } catch (FileNotFoundException e) {

      e.printStackTrace();
    }

    // Parsing the given document
    try {
      parser.parse(content, handler, metadata, context);
    } catch (IOException e) {
      e.printStackTrace();
    } catch (SAXException e) {
      e.printStackTrace();
    } catch (TikaException e) {
      e.printStackTrace();
    }

    LanguageIdentifier object = new LanguageIdentifier(handler.toString());
    System.out.println("Language name :" + object.getLanguage());

    System.out.println(handler.toString());
  }

  public static void test7() throws IOException, TikaException, SAXException {
    BodyContentHandler handler = new BodyContentHandler();
    Metadata metadata = new Metadata();
    FileInputStream inputstream = new FileInputStream(new File(pathname));
    ParseContext pcontext = new ParseContext();

    // parsing the document using PDF parser
    PDFParser pdfparser = new PDFParser();
    pdfparser.parse(inputstream, handler, metadata, pcontext);

    // getting the content of the document
    System.out.println("Contents of the PDF :" + handler.toString());
    // getting metadata of the document
    System.out.println("Metadata of the PDF:");
    String[] metadataNames = metadata.names();

    for (String name : metadataNames) {
      System.out.println(name + " : " + metadata.get(name));
    }
  }
}
