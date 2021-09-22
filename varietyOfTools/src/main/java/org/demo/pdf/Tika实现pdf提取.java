package org.demo.pdf;

import java.io.File;
import org.apache.tika.Tika;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.utils.ParseUtils;

public class Tika实现pdf提取 {

  public static void main(String[] args) throws Exception {
    String s = "E:\\Deploy-HT\\数据\\1.pdf";
//    String parse = parse(s);
//    System.out.println(parse);
    System.out.println("----------------------------------------");
    //        String path1="D:\\testdata\\设计提出来-1.txt";
    //        String path2="D:\\testdata\\word.pdf";
    //        String path3="D:\\testdata\\配置.doc";
    //        System.out.println(parse(path3));

    tika();
  }
  /**
   * 方法一：使用ParseUtils解析 解析各种类型文件
   *
   * @param
   * @return 文件内容字符串
   */
  public static String parse(String path) {
    String result = "";
    TikaConfig tikaConfig = TikaConfig.getDefaultConfig();
    try {
      result = ParseUtils.getStringContent(new File(path), tikaConfig);
    } catch (Exception e) {
      //            log.debug("[by ninja.hzw]" + e);
    }
    return result;
  }

  public static void tika() throws Exception {
    Tika tika = new Tika();
    String[] tt =
        new String[] {
            "E:\\Deploy-HT\\数据\\1.pdf"
        };

    for (String file : tt) {
      System.out.println(file);
      System.out.println(tika.detect(new File(file)));
      String text = tika.parseToString(new File(file));
      System.out.print(text);
    }
  }
}
