package com.iglens.字符串;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

public class 字符串写入到文件中 {
  public static void main(String[] args) {
    String filePath = "";
    String text = "";
    writerFile(filePath, text);
    //
  }

  public static void writerFile(String filePath, String text) {
    try (Writer outputStreamWriter =
        new OutputStreamWriter(new FileOutputStream(new File(filePath)))) {
      outputStreamWriter.write(text);
      outputStreamWriter.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
