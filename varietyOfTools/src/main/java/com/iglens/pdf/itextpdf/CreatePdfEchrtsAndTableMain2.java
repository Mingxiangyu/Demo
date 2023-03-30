package com.iglens.pdf.itextpdf;

import cn.hutool.core.date.DateUtil;
import cn.hutool.extra.template.TemplateException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.core.io.ClassPathResource;

/** 根据模板填充数据及图片，动态生成数据列表 原文链接：https://blog.csdn.net/qq_45699784/article/details/127791747 */
public class CreatePdfEchrtsAndTableMain2 {

  private static final String TITLE = "这个是标题，可有可无";

  public static void main(String[] args) {
    String path = "输出结果.pdf";
    try {
      createPdfFile(new File(path));
    } catch (IOException | DocumentException e) {
      e.printStackTrace();
    }
  }

  public static void createPdfFile(File response)
      throws IOException, DocumentException, TemplateException {
    // 设置请求返回类型
    // response.setHeader("Content-Disposition", "attachment; filename=测试.pdf");
    // OutputStream outputStream = response.getOutputStream();
    OutputStream outputStream = new FileOutputStream(response);
    // 模板路径，放到项目里用这个ClassPathResource
    ClassPathResource classPathResource = new ClassPathResource("templates/test1.pdf");
    InputStream inputStream = classPathResource.getInputStream();

    PdfReader reader = new PdfReader(inputStream);
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    PdfStamper ps = new PdfStamper(reader, bos);

    // 设置字体
    final BaseFont font =
        BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
    ArrayList<BaseFont> fontList = new ArrayList<>();
    fontList.add(font);

    // 提取表单,这个是模板画好的文本框
    AcroFields s = ps.getAcroFields();
    s.setSubstitutionFonts(fontList);

    s.setFieldProperty("jrfk", "textfont", font, null);
    s.setFieldProperty("bjzs", "textfont", font, null);
    s.setFieldProperty("type", "textfont", font, null);
    s.setFieldProperty("createTime", "textfont", font, null);
    s.setFieldProperty("title", "textfont", font, null);
    s.setField("jrfk", "10");
    s.setField("bjzs", "20");
    s.setField("type", "日报");
    s.setField("createTime", DateUtil.now());
    s.setField("title", TITLE);

    // 添加图片
    PdfContentByte cb = ps.getOverContent(1);
    // 添加logo
    Rectangle logo = s.getFieldPositions("logo").get(0).position;
    Image logoImage =
        Image.getInstance(
            "https://img1.baidu.com/it/u=3646261857,3326755268&fm=253&app=138&size=w931&n=0&f=JPG&fmt=auto?sec=1668186000&t=20050fc88fc3feb1f9d28392f4595ec6");
    // 根据域的大小缩放图片，我这里宽度在原有的域基础上加了100，你们可以自己调节
    logoImage.scaleToFit(logo.getWidth() + 100, logo.getHeight());
    logoImage.setAlignment(Image.MIDDLE);
    logoImage.setAbsolutePosition(logo.getLeft(), logo.getBottom());
    cb.addImage(logoImage);

    // 获取统计图
    // 获取域
    Rectangle rlt = s.getFieldPositions("rlt").get(0).position;
    // 热力图
    Image rltImage =
        Image.getInstance(
            "https://img0.baidu.com/it/u=4043177345,1055141017&fm=253&app=138&size=w931&n=0&f=PNG&fmt=auto?sec=1668186000&t=8cfdc5c95cc0070eb91946d780ee8dc3");
    // 根据域大小设置缩放图片
    rltImage.scaleToFit(rlt.getWidth() + 100, rlt.getHeight());
    // 设置居中
    rltImage.setAlignment(Image.MIDDLE);
    // 绝对定位
    rltImage.setAbsolutePosition(rlt.getLeft(), rlt.getBottom());
    // 图片旋转，这个可以将图片进行一个旋转，看自己需求
    //        rltImage.setRotationDegrees(90);
    cb.addImage(rltImage);

    // 按机构统计图
    // 这个是生成echarts的类，如果需要生成echarts可以去看我的另一个文章，上面前言已经提到了
    // App app1 = new App();
    // byte[] echarts1 = app1.createEcharts("ajg.ftl");
    // Image ajgImage = Image.getInstance(echarts1);
    // Rectangle ajg = s.getFieldPositions("ajg").get(0).position;
    // 根据域大小设置缩放图片
    // ajgImage.scaleToFit(ajg.getWidth(), 400);
    // 设置居中
    // ajgImage.setAlignment(Image.MIDDLE);
    // 绝对定位
    // ajgImage.setAbsolutePosition(ajg.getLeft(), ajg.getBottom());
    // cb.addImage(ajgImage);
    // 按机构排名，这个是在图片的基础上还要添加数据，这个模板可以画好
    // for (int i = 1; i <= 3; i++) {
    //   s.setFieldProperty("ajg" + i, "textfont", font, null);
    //   s.setField("ajg" + i, "机构" + i);
    // }
    // App app = new App();
    // byte[] echarts = app.createEcharts("option.ftl");
    // 按业务
    // Rectangle ayw = s.getFieldPositions("ayw").get(0).position;
    // Image aywImage = Image.getInstance(echarts);
    // 设根据域大小设置缩放图片
    // aywImage.scaleToFit(ayw.getWidth(), 400);
    // 设置居中
    // aywImage.setAlignment(Image.MIDDLE);
    // 绝对定位
    // aywImage.setAbsolutePosition(ayw.getLeft(), ayw.getBottom());
    // cb.addImage(aywImage);
    // 按业务排名
    // for (int i = 1; i <= 3; i++) {
    //   s.setFieldProperty("ayw" + i, "textfont", font, null);
    //   s.setField("ayw" + i, "机构" + i);
    // }

    // 按场合
    // Rectangle acj = s.getFieldPositions("acj").get(0).position;
    // Image acjImage = Image.getInstance(echarts);
    // 设根据域大小设置缩放图片
    // acjImage.scaleToFit(acj.getWidth(), 400);
    // 设置居中
    // acjImage.setAlignment(Image.MIDDLE);
    // 绝对定位
    // acjImage.setAbsolutePosition(acj.getLeft(), acj.getBottom());
    // cb.addImage(acjImage);
    // 按场景排名
    // for (int i = 1; i <= 3; i++) {
    //   s.setFieldProperty("acj" + i, "textfont", font, null);
    //   s.setField("acj" + i, "机构" + i);
    // }

    // 按等级
    // Rectangle adj = s.getFieldPositions("adj").get(0).position;
    // Image adjImage = Image.getInstance(echarts);
    // 设根据域大小设置缩放图片
    // adjImage.scaleToFit(adj.getWidth(), 400);
    // 设置居中
    // adjImage.setAlignment(Image.MIDDLE);

    // 绝对定位
    // adjImage.setAbsolutePosition(adj.getLeft(), adj.getBottom());
    // cb.addImage(adjImage);
    // 按场景排名
    for (int i = 1; i <= 3; i++) {
      s.setFieldProperty("adj" + i, "textfont", font, null);
      s.setField("adj" + i, "机构" + i);
    }
    ps.setFormFlattening(true);
    ps.close();

    // *******************填充编辑好后的pdf**************
    reader = new PdfReader(bos.toByteArray());
    Rectangle pageSize = reader.getPageSize(1);
    Document document = new Document(pageSize);
    PdfWriter writer = PdfWriter.getInstance(document, outputStream);
    writer.setPageEvent(new PageEvent());
    // 打开文档
    document.open();
    PdfContentByte cbUnder = writer.getDirectContentUnder();
    PdfImportedPage pageTemplate = writer.getImportedPage(reader, 1);
    cbUnder.addTemplate(pageTemplate, 0, 0);
    // 重新开一页面
    document.newPage();
    createTable(writer, document);
    //        document.newPage();
    createTableYq(writer, document);
    document.close();
    outputStream.close();
  }

  // 为一个表格添加内容
  public static PdfPCell createSetCell(String value, Font font) {
    PdfPCell cell = new PdfPCell();
    cell.setPhrase(new Phrase(value, font));
    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    return cell;
  }

  // 添加表格
  public static void createTable(PdfWriter writer, Document document)
      throws DocumentException, IOException {

    PdfPTable table = new PdfPTable(new float[] {30, 80, 50, 50, 50});
    table.setTotalWidth(520);
    table.setPaddingTop(500);
    table.setLockedWidth(true);
    table.setHorizontalAlignment(Element.ALIGN_CENTER); // 居中
    table.writeSelectedRows(0, -1, 500, 800, writer.getDirectContentUnder());
    // 每页都显示表头,输入几就是第几行的表头固定
    table.setHeaderRows(2);
    table.setHeaderRows(3);

    // 定义数据的字体
    BaseFont baseFont = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
    Font textFont = new Font(baseFont, 10, Font.NORMAL);
    PdfPCell cell = new PdfPCell(new Paragraph(" ", textFont));

    cell.setHorizontalAlignment(Element.ALIGN_LEFT);

    cell.setVerticalAlignment(Element.ALIGN_BOTTOM);

    cell.setBorder(Rectangle.NO_BORDER);

    cell.setColspan(5);

    table.addCell(cell);

    // 表头信息
    PdfPCell heandCell = new PdfPCell();
    heandCell.setRowspan(1);
    heandCell.setColspan(5);
    heandCell.setFixedHeight(60);
    heandCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
    heandCell.setHorizontalAlignment(Element.ALIGN_CENTER);
    heandCell.setPhrase(new Phrase(TITLE + "对账情况表", textFont));
    table.addCell(heandCell);

    // 表字段
    String title[] = {"序号", "机构", "已对账", "未对账", "对账率%"};
    for (int i = 0; i < title.length; i++) {
      PdfPCell heardCell = new PdfPCell();
      heardCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
      heardCell.setHorizontalAlignment(Element.ALIGN_CENTER);
      heardCell.setPhrase(new Phrase(title[i], textFont));
      heardCell.setMinimumHeight(20);
      table.addCell(heardCell);
    }

    // 列表数据

    List<DuizhangDomain> duizhangDomains = new ArrayList<>();
    for (int i = 1; i <= 1000; i++) {
      DuizhangDomain duizhangDomain = new DuizhangDomain();
      duizhangDomain.setJg("机构" + i).setYdz(i).setWdz(i).setDzl(new BigDecimal(i));
      duizhangDomains.add(duizhangDomain);
    }

    for (int i = 0; i < duizhangDomains.size(); i++) {
      PdfPCell setCell1 = createSetCell((i + 1) + "", textFont);
      PdfPCell setCell2 = createSetCell(duizhangDomains.get(i).getJg(), textFont);
      PdfPCell setCell3 = createSetCell(duizhangDomains.get(i).getYdz().toString(), textFont);
      PdfPCell setCell4 = createSetCell(duizhangDomains.get(i).getWdz().toString(), textFont);
      PdfPCell setCell5 = createSetCell(duizhangDomains.get(i).getDzl() + "%", textFont);
      table.addCell(setCell1);
      table.addCell(setCell2);
      table.addCell(setCell3);
      table.addCell(setCell4);
      table.addCell(setCell5);
    }
    document.add(table);
  }

  public static void createTableYq(PdfWriter writer, Document document)
      throws DocumentException, IOException {

    PdfPTable table = new PdfPTable(new float[] {80, 50});
    table.setTotalWidth(520);
    table.setPaddingTop(500);
    table.setLockedWidth(true);
    table.setHorizontalAlignment(Element.ALIGN_CENTER); // 居中
    table.writeSelectedRows(0, -1, 500, 800, writer.getDirectContentUnder());
    // 每页都显示表头,输入几就是第几行的表头固定
    table.setHeaderRows(2);
    table.setHeaderRows(3);

    // 定义数据的字体
    BaseFont baseFont = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
    Font textFont = new Font(baseFont, 10, Font.NORMAL);

    // 这个是为了区分两个表格加的一个间隔，可以去掉
    PdfPCell cell = new PdfPCell(new Paragraph(" ", textFont));
    cell.setHorizontalAlignment(Element.ALIGN_LEFT);
    cell.setVerticalAlignment(Element.ALIGN_BOTTOM);
    cell.setBorder(Rectangle.NO_BORDER);
    cell.setColspan(2);
    table.addCell(cell);

    // 表头信息
    PdfPCell heandCell = new PdfPCell();
    heandCell.setRowspan(1);
    heandCell.setColspan(2);
    heandCell.setFixedHeight(60);
    heandCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
    heandCell.setHorizontalAlignment(Element.ALIGN_CENTER);
    heandCell.setPhrase(new Phrase(TITLE + "逾期表", textFont));
    table.addCell(heandCell);

    // 表字段
    String title[] = {"机构名称", "逾期数"};
    for (int i = 0; i < title.length; i++) {
      PdfPCell heardCell = new PdfPCell();
      heardCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
      heardCell.setHorizontalAlignment(Element.ALIGN_CENTER);
      heardCell.setPhrase(new Phrase(title[i], textFont));
      heardCell.setMinimumHeight(20);
      table.addCell(heardCell);
    }

    // 列表数据

    List<YqTable> yqTables = new ArrayList<>();
    for (int i = 1; i <= 1000; i++) {
      YqTable yq = new YqTable();
      yq.setJg("逾期机构" + i).setYqs(i);
      yqTables.add(yq);
    }

    for (int i = 0; i < yqTables.size(); i++) {
      PdfPCell setCell2 = createSetCell(yqTables.get(i).getJg(), textFont);
      PdfPCell setCell3 = createSetCell(yqTables.get(i).getYqs().toString(), textFont);
      table.addCell(setCell2);
      table.addCell(setCell3);
    }
    document.add(table);
  }
}
