package com.iglens.word;

import com.spire.doc.Document;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

public class Spire查找文本并替换所有匹配项为新文本 {

  // 这里是根据解析出的xml抽取出的警告水印的样式及其xml标签，方便下面替换用
  // 如果这里只是替换文字的话会有空行，所以直接将整个标签替换
  private static final String WARN =
      "<w:p><w:pPr /><w:r><w:rPr><w:color w:val=\"FF0000\" /><w:sz w:val=\"24\" /></w:rPr><w:t xml:space=\"preserve\">Evaluation Warning: The document was created with Spire.Doc for JAVA.</w:t></w:r></w:p>";

  public static void main(String[] args) {

    //创建 Document 类的对象
    Document document = new Document();

    //载入Word文档
    // String filePath = "C:\\Users\\zhouhuilin\\Desktop\\Docker自动化部署.docx";
    String filePath = "C:\\Users\\zhouhuilin\\Desktop\\问题.doc";
    document.loadFromFile(filePath);

    //将所有“鹿”的匹配项替换为“水牛”
    document.replace("数据", "服雾", false, true);

    //保存结果文档
    document.saveToFile(filePath);
    try {
      if (filePath.endsWith(".doc")) {
        removeWarning(filePath,filePath);
    } else if (filePath.endsWith("docx")) {
      removeWarning1(filePath,filePath);
    }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /** docx移除spire水印方法
   * @param newFilePath 移除水印后docx文件
   * @param oldFilePath 移除水印前docx文件
   * @throws IOException
   */
  private static void removeWarning1(String newFilePath, String oldFilePath) throws IOException {
    // 重新读取文档，进行操作
    InputStream is = new FileInputStream(oldFilePath );
    XWPFDocument document = new XWPFDocument(is);
    // 以上Spire.Doc 生成的文件会自带警告信息，这里来删除Spire.Doc 的警告
    document.removeBodyElement(0);
    FileOutputStream fos = new FileOutputStream(new File(newFilePath));
    ByteArrayOutputStream ostream = new ByteArrayOutputStream();
    try {
      document.write(ostream);
      fos.write(ostream.toByteArray());
      fos.flush();
      fos.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /** 通过替换xml内文件实现水印删除 */
  private static void removeWarning(String newFilePath, String oldFilePath) throws IOException {
    // 原文链接：https://blog.csdn.net/weixin_59158648/article/details/124346707
    InputStream is = new FileInputStream(oldFilePath );
    HWPFDocument hwpfDocument = new HWPFDocument(is);
    //以上Spire.Doc 生成的文件会自带警告信息，这里来删除Spire.Doc 的警告
    //hwpfDocument.delete() 该方法去掉文档指定长度的内容
    hwpfDocument.delete(0,70);
    //输出word内容文件流，新输出路径位置
    OutputStream os=new FileOutputStream(new File(newFilePath));
    try {
      hwpfDocument.write(os);
    } catch (Exception e) {
      e.printStackTrace();
    }finally {
      hwpfDocument.close();
      os.close();
      is.close();
    }
  }
}
