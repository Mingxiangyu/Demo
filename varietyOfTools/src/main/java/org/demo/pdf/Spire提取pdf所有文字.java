package org.demo.pdf;

import com.spire.pdf.PdfDocument;
import com.spire.pdf.PdfPageBase;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author T480S <br>
 * TODO 通过spire操作后会在文件中出现水印
 */
public class Spire提取pdf所有文字 {
  public static void main(String[] args) {
    String path = "E:\\Deploy-HT\\数据\\1.pdf";
    String targetPath = "E:\\Deploy-HT\\数据\\ExtractText.txt";
    获取pdf(path, targetPath);
  }

  private static void 获取pdf(String path, String targetPath) {
    // 创建PdfDocument实例
    PdfDocument doc = new PdfDocument();
    // 加载PDF文件
    doc.loadFromFile(path);

    // 创建StringBuilder实例
    StringBuilder sb = new StringBuilder();

    PdfPageBase page;
    // 遍历PDF页面，获取每个页面的文本并添加到StringBuilder对象
    for (int i = 0; i < doc.getPages().getCount(); i++) {
      page = doc.getPages().get(i);
      sb.append(page.extractText(true));
    }
    FileWriter writer;
    try {
      // 将StringBuilder对象中的文本写入到文本文件
      writer = new FileWriter(targetPath);
      writer.write(sb.toString());
      writer.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }

    doc.close();
  }
}
