package org.demo.word;

import cn.hutool.core.io.FileTypeUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Objects;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.POIXMLTextExtractor;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;

public class 读取word {
  public static void main(String[] args) {
    String path = "C:\\Users\\T480S\\Desktop\\新建 DOCX 文档.docx";
    String[] strings = readWord(path);
    System.out.println(Arrays.toString(strings));
  }

  /**
   * 按行读取doc内容
   *
   * @param path doc文件
   * @return
   */
  public static String[] readWord(String path) {
    String[] arry = null;
    String buffer;
    try (InputStream is = new FileInputStream(new File(path)); ) {
      String type = FileTypeUtil.getTypeByPath(path);
      if (Objects.equals(type, "doc")) {
        WordExtractor ex = new WordExtractor(is);
        buffer = ex.getText();
        ex.close();

        if (buffer.length() > 0) {
          // 使用回车换行符分割字符串
          arry = buffer.split("\\r\\n");
        }
      } else if (Objects.equals(type, "docx")) {
        OPCPackage opcPackage = POIXMLDocument.openPackage(path);
        POIXMLTextExtractor extractor = new XWPFWordExtractor(opcPackage);
        buffer = extractor.getText();
        extractor.close();

        if (buffer.length() > 0) {
          // 使用换行符分割字符串
          arry = buffer.split("\\n");
        }
      } else {
        throw new RuntimeException("格式不为文档，无法解析！");
      }
    } catch (Exception e) {
      throw new IllegalArgumentException(e);
    }
    return arry;
  }
}
