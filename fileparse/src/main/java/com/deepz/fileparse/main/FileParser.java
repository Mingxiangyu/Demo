package com.deepz.fileparse.main;

import com.deepz.fileparse.domain.dto.FileDto;
import com.deepz.fileparse.domain.vo.StructableFileVo;
import com.deepz.fileparse.domain.vo.StructablePdfVo;
import com.deepz.fileparse.domain.vo.StructablePdfVo.Head;
import com.deepz.fileparse.domain.vo.StructableWordVo;
import com.deepz.fileparse.parse.Parser;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.io.FilenameUtils;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.microsoft.ooxml.OOXMLParser;
import org.apache.tika.sax.BodyContentHandler;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;

/**
 * @author xming
 * @description
 */
public class FileParser {

  /** bean类型容器 key：文件后缀 value 对应解析类实现类 */
  private static Map<String, Object> beanDefinitions = new ConcurrentHashMap<>();

  static {
    Reflections reflections =
        new Reflections(
            "com.deepz.fileparse.parse", new TypeAnnotationsScanner(), new SubTypesScanner());
    Set<Class<?>> typeClass =
        reflections.getTypesAnnotatedWith(com.deepz.fileparse.annotation.Parser.class, true);
    for (Class<?> clazz : typeClass) {
      Object parserImpl = null;
      try {
        parserImpl = clazz.getConstructor().newInstance();
      } catch (Exception e) {
        e.printStackTrace();
      }
      com.deepz.fileparse.annotation.Parser annotation =
          clazz.getAnnotation(com.deepz.fileparse.annotation.Parser.class);
      String[] fileTypes = annotation.fileType();
      for (String fileType : fileTypes) {
        beanDefinitions.put(fileType, parserImpl);
      }
    }
  }

  /** 文件解析策略 */
  private Parser parser;

  static String pathname = "G:\\软件备份\\Project\\J2\\信大\\相关文档\\多源情报分析系统部署-最终完善版(新增gis).docx";

  public static void main(String[] args) {
    // PdfParser pdfParser = new PdfParser();
    //
    // StructablePdfVo parse = pdfParser.parse(pathname);
    // System.out.println(parse.getContent());
    // System.out.println(parse.getHeads());

    File file = new File(pathname);
    FileDto fileDto = new FileDto();
    try (FileInputStream fileInputStream = new FileInputStream(file)) {
      // 传入的后缀不能带.,支持的格式都在Parse的策略类里注解配置
      String s = FilenameUtils.getExtension(pathname).toLowerCase();
      System.out.println(s);
      fileDto.setSuffx(s);
      fileDto.setInputStream(fileInputStream);
      FileParser fileParser = new FileParser();
      StructableFileVo superClass = fileParser.parse(fileDto);
      System.out.println(superClass.getContent());
      System.out.println("文字内容结束");
      if (s.equals("pdf")) {
        StructablePdfVo parse = (StructablePdfVo) superClass;
        List<Head> heads = parse.getHeads();
        System.out.println(heads.toString());
      }else if (s.equals("doc")||s.equals("docx")) {
        StructableWordVo parse = (StructableWordVo) superClass;
        List<StructableWordVo.Head> heads = parse.getHeads();
        System.out.println(heads.toString());
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static String parsePdf() {

    try {

      BodyContentHandler handler = new BodyContentHandler();

      Metadata metadata = new Metadata();

      FileInputStream inputstream = new FileInputStream(new File(pathname));

      ParseContext pcontext = new ParseContext();

      // parsing the document using PDF parser

      OOXMLParser pdfparser = new OOXMLParser();

      pdfparser.parse(inputstream, handler, metadata, pcontext);

      // getting the content of the document

      System.out.println("Contents of the PDF :" + handler.toString());

      // 元数据提取

      System.out.println("Metadata of the PDF:");
      String[] metadataNames = metadata.names();
      for (String name : metadataNames) {
        System.out.println(name + " : " + metadata.get(name));
      }

    } catch (Exception e) {

      e.printStackTrace();
    }

    return "";
  }

  public <T> T parse(FileDto fileDto) {
    // String suffix = path.substring(path.lastIndexOf('.') + 1, path.length());
    this.parser = (Parser) beanDefinitions.get(fileDto.getSuffx());

    return (T) parser.parse(fileDto);
  }

  public Parser getParser() {
    return parser;
  }

  public void setParser(Parser parser) {
    this.parser = parser;
  }
}
