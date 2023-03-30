package com.iglens.pdf.itextpdf;


import cn.hutool.core.date.DateUtil;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
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
import freemarker.template.TemplateException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.core.io.ClassPathResource;

/**
 * 模板第一页不占满，生成表格
 */
public class CreatePdfEchrtsAndTableMain3 {

  public void createPdfFile(HttpServletResponse response) throws IOException, DocumentException, TemplateException {
    //设置请求返回类型
    response.setHeader("Content-Disposition", "attachment; filename=测试.pdf");
    OutputStream outputStream = response.getOutputStream();
    //模板路径，放到项目里用这个ClassPathResource
    ClassPathResource classPathResource = new ClassPathResource("templates/test3.pdf");
    InputStream inputStream = classPathResource.getInputStream();

    PdfReader reader = new PdfReader(inputStream);
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    PdfStamper ps = new PdfStamper(reader, bos);

    //设置字体
    final BaseFont font = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
    ArrayList<BaseFont> fontList = new ArrayList<>();
    fontList.add(font);

    //提取表单
    AcroFields s = ps.getAcroFields();
    s.setSubstitutionFonts(fontList);

    s.setFieldProperty("type", "textfont", font, null);
    s.setFieldProperty("createTime", "textfont", font, null);
    s.setFieldProperty("title", "textfont", font, null);
    s.setField("type", "日报");
    s.setField("createTime", DateUtil.now());
    s.setField("title", "这是title，模板画的位置框");
    ps.setFormFlattening(true);
    ps.close();

    //*******************填充编辑好后的pdf**************
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
    //添加间隙,这里为进行了一个封装，因为这个模板第一页只有一些title啥的，
    //重开一页太浪费，只需要确定表格要在什么位置生成，添加一个间隙就可以了
    createBlankTable(writer, document, font, 180);

    createTable(writer, document);

    createBlankTable(writer, document, font, 20);
    createTableYq(writer, document);
    document.close();
    outputStream.close();
  }

  //为一个表格添加内容
  public PdfPCell createSetCell(String value, Font font) {
    PdfPCell cell = new PdfPCell();
    cell.setPhrase(new Phrase(value, font));
    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    return cell;
  }


  //添加表格
  public void createTable(PdfWriter writer, Document document) throws DocumentException, IOException {

    PdfPTable table = new PdfPTable(new float[]{30, 80, 50, 50, 50});
    table.setTotalWidth(520);
    table.setPaddingTop(500);
    table.setLockedWidth(true);
    table.setHorizontalAlignment(Element.ALIGN_CENTER);//居中
    table.writeSelectedRows(0, -1, 500, 800, writer.getDirectContentUnder());
    //每页都显示表头,输入几就是第几行的表头固定
    table.setHeaderRows(1);
    table.setHeaderRows(2);

    //定义数据的字体
    BaseFont baseFont = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
    Font textFont = new Font(baseFont, 10, Font.NORMAL);

    //表头信息
    PdfPCell heandCell = new PdfPCell();
    heandCell.setRowspan(1);
    heandCell.setColspan(5);
    heandCell.setFixedHeight(30);
    heandCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
    heandCell.setHorizontalAlignment(Element.ALIGN_CENTER);
    heandCell.setPhrase(new Phrase("对账情况表", textFont));
    table.addCell(heandCell);

    //表字段
    String title[] = {"序号", "机构", "已对账", "未对账", "对账率%"};
    for (int i = 0; i < title.length; i++) {
      PdfPCell heardCell = new PdfPCell();
      heardCell.setRowspan(1);
      heardCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
      heardCell.setHorizontalAlignment(Element.ALIGN_CENTER);
      heardCell.setPhrase(new Phrase(title[i], textFont));
      heardCell.setMinimumHeight(20);
      table.addCell(heardCell);

    }


    //列表数据

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


  public void createTableYq(PdfWriter writer, Document document) throws DocumentException, IOException {

    PdfPTable table = new PdfPTable(new float[]{80, 50});
    table.setTotalWidth(520);
    table.setPaddingTop(500);
    table.setLockedWidth(true);
    table.setHorizontalAlignment(Element.ALIGN_CENTER);//居中
    table.writeSelectedRows(0, -1, 500, 800, writer.getDirectContentUnder());
    //每页都显示表头,输入几就是第几行的表头固定
    table.setHeaderRows(2);
    table.setHeaderRows(3);

    //定义数据的字体
    BaseFont baseFont = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
    Font textFont = new Font(baseFont, 10, Font.NORMAL);

    //表头信息
    PdfPCell heandCell = new PdfPCell();
    heandCell.setRowspan(1);
    heandCell.setColspan(2);
    heandCell.setFixedHeight(30);
    heandCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
    heandCell.setHorizontalAlignment(Element.ALIGN_CENTER);
    heandCell.setPhrase(new Phrase("逾期表", textFont));
    table.addCell(heandCell);

    //表字段
    String title[] = {"机构名称", "逾期数"};
    for (int i = 0; i < title.length; i++) {
      PdfPCell heardCell = new PdfPCell();
      heardCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
      heardCell.setHorizontalAlignment(Element.ALIGN_CENTER);
      heardCell.setPhrase(new Phrase(title[i], textFont));
      heardCell.setMinimumHeight(20);
      table.addCell(heardCell);
    }

    //列表数据

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

  /**
   * 创建表格跟表格之间的空白间隔
   */
  public void createBlankTable(PdfWriter writer, Document document, BaseFont font, int height) throws DocumentException {
    PdfPTable table = new PdfPTable(new float[]{30});
    table.setTotalWidth(520);
    table.setPaddingTop(500);
    table.setLockedWidth(true);
    table.setHorizontalAlignment(Element.ALIGN_CENTER);//居中
    table.writeSelectedRows(0, -1, 500, 800, writer.getDirectContentUnder());
    Font textFont = new Font(font, 10, Font.NORMAL);
    PdfPCell cell = new PdfPCell(new Paragraph(" ", textFont));

    cell.setHorizontalAlignment(Element.ALIGN_LEFT);

    cell.setVerticalAlignment(Element.ALIGN_BOTTOM);

    cell.setBorder(Rectangle.NO_BORDER);
    cell.setFixedHeight(height);

    cell.setColspan(1);

    table.addCell(cell);
    document.add(table);
  }
}

