package com.iglens.word;

import com.spire.doc.Document;
import com.spire.doc.Section;
import com.spire.doc.documents.Paragraph;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class spire获取word自动生产的序号 {

  public static void main(String[] args) throws IOException {
    String filePath = "C:\\Users\\T480S\\Desktop\\新建 Microsoft Word 文档.docx";
    String txtPath = "C:\\Users\\T480S\\Desktop\\GetTitle.txt";

    getTitle(filePath, txtPath);
  }

  private static void getTitle(String filePath, String txtPath) throws IOException {
    // 加载Word测试文档
    Document doc = new Document();
    doc.loadFromFile(filePath);

    // 保存标题内容到.txt文档
    File file = new File(txtPath);
    if (file.exists()) {
      file.delete();
    }
    file.createNewFile();
    FileWriter fw = new FileWriter(file, true);
    BufferedWriter bw = new BufferedWriter(fw);

    // 遍历section
    for (int i = 0; i < doc.getSections().getCount(); i++) {
      Section section = doc.getSections().get(i);
      // 遍历Paragraph
      for (int j = 0; j < section.getParagraphs().getCount(); j++) {
        Paragraph paragraph = section.getParagraphs().get(j);

        // 获取标题
        if (paragraph.getStyleName().matches("1")) // 段落为“标题1”的内容
        {
          // 获取段落标题内容
          String text = paragraph.getText();
          // 写入文本到txt文档
          bw.write("标题1: " + text + "\r");
        }
        // 获取标题
        if (paragraph.getStyleName().matches("2")) // 段落为“标题2”的内容
        {
          // 获取段落标题内容
          String text = paragraph.getText();
          // 写入文本到txt文档
          bw.write("标题2: " + text + "\r");
        }
        // 获取标题
        if (paragraph.getStyleName().matches("3")) // 段落为“标题3”的内容
        {
          // 获取段落标题内容
          String text = paragraph.getText();
          // 写入文本到txt文档
          bw.write("标题3: " + text + "\r");
        }
        // 获取标题
        if (paragraph.getStyleName().matches("4")) // 段落为“标题4”的内容
        {
          // 获取段落标题内容
          String text = paragraph.getText();
          // 写入文本到txt文档
          bw.write("标题4: " + text + "\r");
        }

        bw.write("\n");
      }
    }
    bw.flush();
    bw.close();
    fw.close();
  }
}
