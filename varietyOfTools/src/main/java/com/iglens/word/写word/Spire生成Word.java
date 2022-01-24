package com.iglens.word.写word;

import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.io.file.FileWriter;
import com.spire.doc.Document;
import com.spire.doc.FieldType;
import com.spire.doc.FileFormat;
import com.spire.doc.HeaderFooter;
import com.spire.doc.Section;
import com.spire.doc.documents.HorizontalAlignment;
import com.spire.doc.documents.PageNumberStyle;
import com.spire.doc.documents.Paragraph;
import com.spire.doc.fields.TextRange;
import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

public class Spire生成Word {
  // 这里是根据解析出的xml抽取出的警告水印的样式及其xml标签，方便下面替换用
  // 如果这里只是替换文字的话会有空行，所以直接将整个标签替换
  private static final String WARN =
      "<w:p><w:pPr /><w:r><w:rPr><w:color w:val=\"FF0000\" /><w:sz w:val=\"24\" /></w:rPr><w:t xml:space=\"preserve\">Evaluation Warning: The document was created with Spire.Doc for JAVA.</w:t></w:r></w:p>";

  public static void main(String[] args) {
    // 保存到临时固定地址
    //    String tempPath = "F:\\tempDocFiles";
    //    String pathname = "C:\\Users\\T480S\\Desktop\\新建 Microsoft Word 文档.docx";
    String tempPath = "/Users/ming/Documents/";
    String pathname = "/Users/ming/Documents/新建 Microsoft Word 文档.docx";

    try {
      exportInfoToWord(tempPath, pathname);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * 导出文件
   *
   * @return
   * @param tempPath
   * @param pathname
   */
  public static void exportInfoToWord(String tempPath, String pathname) throws IOException {
    // 创建Word文档
    Document doc = new Document();
    // 添加一个目录的section
    Section section = doc.addSection();
    Paragraph para = section.addParagraph();
    TextRange tr = para.appendText("目 录");
    // 设置字体大小和颜色
    tr.getCharacterFormat().setTextColor(Color.black);
    tr.getCharacterFormat().setFontName("宋体");
    tr.getCharacterFormat().setFontSize(20);
    para.getFormat().setHorizontalAlignment(HorizontalAlignment.Center);
    // 设置段后间距
    para.getFormat().setAfterSpacing(10);
    // 添加段落
    para = section.addParagraph();
    // 通过指定最低的Heading级别1和最高的Heading级别3，创建包含Heading 1、2、3，制表符前导符和右对齐页码的默认样式的Word目录。标题级别范围必须介于1到9之间
    para.appendTOC(1, 3);
    // 获取http客户端
    CloseableHttpClient client = HttpClients.createDefault();
    List<String> fileAddress = new ArrayList<String>();
    fileAddress.add(
        "http://114.116.236.37:9000/image/file/676faf54-105a-4f36-8efe-3e73729eb69f.jpg");
    //    fileAddress.add(
    //
    // "https://image.baidu.com/search/detail?ct=503316480&z=0&ipn=d&word=%E5%9B%BE%E7%89%87&hs=2&pn=0&spn=0&di=17490&pi=0&rn=1&tn=baiduimagedetail&is=0%2C0&ie=utf-8&oe=utf-8&cl=2&lm=-1&cs=2151136234%2C3513236673&os=4170018733%2C872394297&simid=4078116598%2C576678135&adpicid=0&lpn=0&ln=30&fr=ala&fm=&sme=&cg=&bdtype=0&oriquery=%E5%9B%BE%E7%89%87&objurl=https%3A%2F%2Fgimg2.baidu.com%2Fimage_search%2Fsrc%3Dhttp%3A%2F%2F2c.zol-img.com.cn%2Fproduct%2F124_500x2000%2F748%2FceZOdKgDAFsq2.jpg%26refer%3Dhttp%3A%2F%2F2c.zol-img.com.cn%26app%3D2002%26size%3Df9999%2C10000%26q%3Da80%26n%3D0%26g%3D0n%26fmt%3Djpeg%3Fsec%3D1619681267%26t%3D3af9e19849c841743b1ac392e44f5f53&fromurl=ippr_z2C%24qAzdH3FAzdH3F4_z%26e3Bz5s_z%26e3Bv54_z%26e3BvgAzdH3Fw6ptvsjAzdH3F98bn9ac_z%26e3Bip4s%3Fetw%3Dtg1jx&gsm=1&islist=&querylist=");
    //    fileAddress.add(
    //
    // "https://image.baidu.com/search/detail?ct=503316480&z=0&ipn=d&word=%E5%9B%BE%E7%89%87&hs=2&pn=0&spn=0&di=17490&pi=0&rn=1&tn=baiduimagedetail&is=0%2C0&ie=utf-8&oe=utf-8&cl=2&lm=-1&cs=2151136234%2C3513236673&os=4170018733%2C872394297&simid=4078116598%2C576678135&adpicid=0&lpn=0&ln=30&fr=ala&fm=&sme=&cg=&bdtype=0&oriquery=%E5%9B%BE%E7%89%87&objurl=https%3A%2F%2Fgimg2.baidu.com%2Fimage_search%2Fsrc%3Dhttp%3A%2F%2F2c.zol-img.com.cn%2Fproduct%2F124_500x2000%2F748%2FceZOdKgDAFsq2.jpg%26refer%3Dhttp%3A%2F%2F2c.zol-img.com.cn%26app%3D2002%26size%3Df9999%2C10000%26q%3Da80%26n%3D0%26g%3D0n%26fmt%3Djpeg%3Fsec%3D1619681267%26t%3D3af9e19849c841743b1ac392e44f5f53&fromurl=ippr_z2C%24qAzdH3FAzdH3F4_z%26e3Bz5s_z%26e3Bv54_z%26e3BvgAzdH3Fw6ptvsjAzdH3F98bn9ac_z%26e3Bip4s%3Fetw%3Dtg1jx&gsm=1&islist=&querylist=");
    //    for (String address : fileAddress) {
    //      // 添加一个section
    //      section = doc.addSection();
    //      // 添加一个段落
    //      para = section.addParagraph();
    //      para.appendText(address);
    //      // 应用Heading 1样式到段落
    //      para.applyStyle(BuiltinStyle.Heading_1);
    //      section.addParagraph();
    //
    //      // 通过httpget方式来实现我们的get请求
    //      HttpGet httpGet = new HttpGet(address);
    //      // 设置请求头信息
    //      httpGet.setHeader(
    //          "User-Agent",
    //          "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko)
    // Chrome/45.0.2454.101 Safari/537.36");
    //      // 通过client调用execute方法，得到我们的执行结果就是一个response，所有的数据都封装在response里面了
    //      CloseableHttpResponse httpResponse = client.execute(httpGet);
    //      // HttpEntity 是一个中间的桥梁，在httpClient里面，是连接我们的请求与响应的一个中间桥梁，所有的请求参数都是通过HttpEntity携带过去的
    //      // 所有的响应的数据，也全部都是封装在HttpEntity里面
    //      HttpEntity entity = httpResponse.getEntity();
    //      InputStream in = entity.getContent();
    //
    //      // 添加图片段落
    //      Paragraph paraPicture = section.addParagraph();
    //      DocPicture picture = paraPicture.appendPicture(in);
    //      // 设置图片宽度
    //      picture.setWidth(490f);
    //      // 设置图片高度
    //      picture.setHeight(400f);
    //
    //      // 添加第一个段落
    //      Paragraph paraText = section.addParagraph();
    //      // 给第一个段落和第二个段落设置水平居中对齐方式
    //      paraPicture.getFormat().setHorizontalAlignment(HorizontalAlignment.Center);
    //      paraText.getFormat().setHorizontalAlignment(HorizontalAlignment.Center);
    //
    //      // 设置第一个段落的段后间距
    //      paraPicture.getFormat().setAfterSpacing(15f);
    //      // 释放资源
    //      EntityUtils.consume(entity);
    //    }

    // 获取第一个节中的页脚
    HeaderFooter footer = doc.getSections().get(0).getHeadersFooters().getFooter();
    // 添加段落到页脚
    Paragraph footerParagraph = footer.addParagraph();
    // 添加文字、页码域和总页数域到段落
    footerParagraph.appendText("第");
    footerParagraph.appendField("page number", FieldType.Field_Page);
    footerParagraph.appendText("页");
    footerParagraph.getFormat().setHorizontalAlignment(HorizontalAlignment.Center);
    // 设置目录页码数字格式为罗马数字
    doc.getSections().get(0).getPageSetup().setPageNumberStyle(PageNumberStyle.Roman_Lower);
    // 设置内容页码数字格式为阿拉伯数字
    //    doc.getSections().get(1).getPageSetup().setPageNumberStyle(PageNumberStyle.Arabic);
    // 设置第二节页码从新开始编码，并设置起始页码数字
    //    doc.getSections().get(1).getPageSetup().setRestartPageNumbering(true);
    //    doc.getSections().get(1).getPageSetup().setPageStartingNumber(1);

    // 更新目录
    doc.updateTableOfContents();

    File dir = new File(tempPath);
    if (!dir.exists()) {
      dir.mkdir();
    }
    // 临时文件路径 TODO 后缀不能写死
    String filePath = tempPath + File.separator + UUID.randomUUID()+ ".docx";
    doc.saveToFile(filePath , FileFormat.Docx);
    // TODO 临时文件 filePath 需要删除

    removeWarning1(pathname, filePath);

    // 输出word内容文件流，提供前台下载
    //    response.reset();
    //    response.setContentType("application/x-msdownload");
    //    String fileName = "" + System.currentTimeMillis() + ".docx";
    //    response.addHeader("Content-Disposition", "attachment; filename=" + fileName);
    //    //    delFile(dir);
    //    ByteArrayOutputStream ostream = new ByteArrayOutputStream();
    //    OutputStream servletOS = null;
    //    try {
    //      servletOS = response.getOutputStream();
    //      document.write(ostream);
    //      servletOS.write(ostream.toByteArray());
    //      servletOS.flush();
    //      servletOS.close();
    //    } catch (Exception e) {
    //      e.printStackTrace();
    //    }
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
  
  /**
   * 通过替换xml内文件实现水印删除
   *
   * @param docPath
   */
  private static void removeWarning(String docPath) {
    // 原文件
    File file = new File(docPath);
    /*
     * 消除警告开始 创建解析对象，注意这里的包是cn.hutool.core.io.file.FileReader
     * 因为这里使用jdk自带的java.io.FileReader时候解析出的xml不全
     */
    FileReader fileReader = new FileReader(file);
    String str = fileReader.readString();
    // 替换
    str = str.replaceAll(WARN, "");
    // 输出，这里的包我就直接用的java.io,用hutool也没问题
    FileWriter fileWriter = new FileWriter(file);
    fileWriter.write(str); // 消除警告结束
  }
}
