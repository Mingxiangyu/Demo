package org.demo.txt;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class 获取TXT文本编码格式 {

  /** 文本判断，否则认为GB2312（即ANSI类型的TXT）
   * @param file 文件
   * @return 编码类型
   */
  public static String getTextFileEncode(File file) {
    String code = "GB2312";
    try (FileInputStream fileInputStream = new FileInputStream(file)) {
      byte[] head = new byte[3];
      fileInputStream.read(head);
      if (head[0] == -1 && head[1] == -2) {
        code = "UTF-16";
      }
      if (head[0] == -2 && head[1] == -1) {
        code = "Unicode";
      }
      if (head[0] == -17 && head[1] == -69 && head[2] == -65) {
        code = "UTF-8";
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return code;
  }
}
