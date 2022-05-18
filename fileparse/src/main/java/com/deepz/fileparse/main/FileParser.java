package com.deepz.fileparse.main;

import com.deepz.fileparse.domain.dto.FileDto;
import com.deepz.fileparse.domain.vo.StructableFileVo;
import com.deepz.fileparse.domain.vo.StructablePdfVo;
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
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;

/**
 * @author zhangdingping
 * @date 2019/7/25 18:29
 * @description
 */
public class FileParser {

  /** 文件解析策略 */
  private Parser parser;

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

  public static void main(String[] args) {
    FileDto fileDto = new FileDto();
    String pathname = "G:\\软件备份\\MyDocument\\IDE\\blog.csdn.net-Gogs-搭建自己的Git服务器.pdf";
    // String pathname = "G:\\软件备份\\Project\\J2\\信大\\相关文档\\多源情报分析系统部署-最终完善版(新增gis).docx";
    File file = new File(pathname);
    try (FileInputStream fileInputStream = new FileInputStream(file)) {
      // 传入的后缀不能带.,支持的格式都在Parse的策略类里注解配置
      String s = FilenameUtils.getExtension(pathname).toLowerCase();
      System.out.println(s);
      fileDto.setSuffx(s);
      fileDto.setInputStream(fileInputStream);
      FileParser fileParser = new FileParser();
      StructableFileVo parse = fileParser.parse(fileDto);
      if (s.equals("pdf")) {
        StructablePdfVo parse1 = (StructablePdfVo) parse;
        System.out.println(parse1.getContent());
      } else if (s.equals("doc")||s.equals("docx")) {
        StructableWordVo parse1 = (StructableWordVo) parse;
        System.out.println(parse1.getContent());
        System.out.println("文字内容结束");
        List<StructableWordVo.Head> heads = parse1.getHeads();
        System.out.println(heads.toString());
      }
      System.out.println("文字内容结束");
      // List<StructablePdfVo.Head> heads = parse.getHeads();
      // System.out.println(heads.toString());
    } catch (IOException e) {
      e.printStackTrace();
    }
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
