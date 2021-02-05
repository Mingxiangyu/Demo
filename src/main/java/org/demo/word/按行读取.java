package org.demo.word;

/**
 * Description:按行读取 工具类
 *
 * <p>Copyright: Copyright (c)2019
 *
 * <p>Company: Tope
 *
 * <p>@version 1.0
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.POIXMLTextExtractor;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class 按行读取 {
  private static final Logger log = LoggerFactory.getLogger(按行读取.class);

  public static void main(String[] args) {
    String filePath =
        //        "E:\\Deploy-七里渠\\相关\\脱\\tb\\kb\\xxxxxxxxxxxxxxxxxxxxxxx.doc";
        "E:\\Deploy-七里渠\\相关\\脱\\tb\\tb\\doc+若干pic.doc";
    try {
      readWord(filePath);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * @Description: POI 读取 word
   *
   * @create: 2019-07-27 9:48
   * @update logs
   * @throws Exception
   */
  public static List<String> readWord(String filePath) throws Exception {

    List<String> linList = new ArrayList<String>();
    String buffer = "";
    try {
      if (filePath.endsWith(".doc")) {
        InputStream is = new FileInputStream(new File(filePath));
        WordExtractor ex = new WordExtractor(is);
        buffer = ex.getText();
        ex.close();

        if (buffer.length() > 0) {
          // 使用回车换行符分割字符串
          String[] arry = buffer.split("\\r\\n");
          for (String string : arry) {
            linList.add(string.trim());
          }
        }
      } else if (filePath.endsWith(".docx")) {
        OPCPackage opcPackage = POIXMLDocument.openPackage(filePath);
        POIXMLTextExtractor extractor = new XWPFWordExtractor(opcPackage);
        buffer = extractor.getText();
        extractor.close();

        if (buffer.length() > 0) {
          // 使用换行符分割字符串
          String[] arry = buffer.split("\\n");
          for (String string : arry) {
            linList.add(string.trim());
          }
        }
      } else {
        return null;
      }
      log.info(linList.toString());
      System.out.println(linList.toString());
      return linList;
    } catch (Exception e) {
      System.out.print("error---->" + filePath);
      e.printStackTrace();
      return null;
    }
  }
}
