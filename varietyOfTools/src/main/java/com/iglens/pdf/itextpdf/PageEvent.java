package com.iglens.pdf.itextpdf;


import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.IOException;

public class PageEvent extends PdfPageEventHelper {
  /**
   * 页眉
   */
  //public String header = "itext测试页眉";

  /**
   * 文档字体大小，页脚页眉最好和文本大小一致
   */
  public int presentFontSize = 10;

  /**
   * 文档页面大小，最好前面传入，否则默认为A4纸张
   */
  public Rectangle pageSize = PageSize.A4;

  // 模板
  public PdfTemplate total;

  // 基础字体对象
  public BaseFont bf = null;

  // 利用基础字体生成的字体对象，一般用于生成中文文字
  public Font fontDetail = null;


  /**
   *
   * Creates a new instance of PdfReportM1HeaderFooter 无参构造方法.
   *
   */
  public PageEvent() {

  }

  /**
   *
   * Creates a new instance of PdfReportM1HeaderFooter 构造方法.
   *
   * @param
   *
   * @param presentFontSize
   *            数据体字体大小
   * @param pageSize
   *            页面文档大小，A4，A5，A6横转翻转等Rectangle对象
   */
  //    public PDFBuilder(String yeMei, int presentFontSize, Rectangle pageSize) {
  //        this.header = yeMei;
  //        this.presentFontSize = presentFontSize;
  //        this.pageSize = pageSize;
  //    }
  public PageEvent( int presentFontSize, Rectangle pageSize) {
    this.presentFontSize = presentFontSize;
    this.pageSize = pageSize;
  }

  //    public void setHeader(String header) {
  //        this.header = header;
  //    }

  public void setPresentFontSize(int presentFontSize) {
    this.presentFontSize = presentFontSize;
  }


  /**
   *
   * TODO 文档打开时创建模板
   *
   * @see com.itextpdf.text.pdf.PdfPageEventHelper#onOpenDocument(com.itextpdf.text.pdf.PdfWriter,
   *      com.itextpdf.text.Document)
   */
  @Override
  public void onOpenDocument(PdfWriter writer, Document document) {
    total = writer.getDirectContent().createTemplate(50, 50);// 共 页 的矩形的长宽高
  }

  /**
   *
   * TODO 关闭每页的时候，写入页眉，写入'第几页共'这几个字。
   *
   * @see com.itextpdf.text.pdf.PdfPageEventHelper#onEndPage(com.itextpdf.text.pdf.PdfWriter,
   *      com.itextpdf.text.Document)
   */
  @Override
  public void onEndPage(PdfWriter writer, Document document) {
    this.addPage(writer, document);
    //加水印
    this.addWatermark(writer);
  }

  //加分页
  public void addPage(PdfWriter writer, Document document){
    //设置分页页眉页脚字体
    try {
      if (bf == null) {
        bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", false);
      }
      if (fontDetail == null) {
        fontDetail = new Font(bf, presentFontSize, Font.NORMAL);// 数据体字体
      }
    } catch (DocumentException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    // 1.写入页眉
    //        ColumnText.showTextAligned(writer.getDirectContent(),
    //                Element.ALIGN_LEFT, new Phrase(header, fontDetail),
    //                document.left(), document.top() + 20, 0);
    //		页眉添加图片
    //		String path = ResourceUtils.getFile("classpath:").getPath();
    //      Image img = Image.getInstance(path + "/pdfTemplates/logo.jpg");
    //      img.setAlignment(Image.MIDDLE);
    //      img.setWidthPercentage(80);
    //      img.scaleToFit(50,40);
    //      img.setAbsolutePosition(document.left(),document.top());
    //      writer.getDirectContent().addImage(img);
    //页眉加下划线
    //        PdfPTable tableHeader = new PdfPTable(1);
    //        tableHeader.setTotalWidth(PageSize.A4.getWidth() - 60);
    //        PdfPCell pCell = new PdfPCell();
    //        pCell.setBorderWidthBottom(0.3f);
    //        tableHeader.addCell(pCell);
    //        tableHeader.writeSelectedRows(0, -1, 30, 805, writer.getDirectContent());

    // 2.写入前半部分的 第 X页/共
    int pageS = writer.getPageNumber();
    String foot1 = "第 " + pageS + " 页 /共";
    //        String foot1 = pageS  +"/";
    Phrase footer = new Phrase(foot1, fontDetail);

    // 3.计算前半部分的foot1的长度，后面好定位最后一部分的'Y页'这俩字的x轴坐标，字体长度也要计算进去 = len
    float len = bf.getWidthPoint(foot1, presentFontSize);

    // 4.拿到当前的PdfContentByte
    PdfContentByte cb = writer.getDirectContent();

    // 5.写入页脚1，x轴就是(右margin+左margin + right() -left()- len)/2.0F
    // 再给偏移20F适合人类视觉感受，否则肉眼看上去就太偏左了
    // ,y轴就是底边界-20,否则就贴边重叠到数据体里了就不是页脚了；注意Y轴是从下往上累加的，最上方的Top值是大于Bottom好几百开外的。
    ColumnText
        .showTextAligned(
            cb,
            Element.ALIGN_CENTER,
            footer,
            (document.rightMargin() + document.right()
                + document.leftMargin() - document.left() - len) / 2.0F ,
            document.bottom() - 20, 0);

    // 6.写入页脚2的模板（就是页脚的Y页这俩字）添加到文档中，计算模板的和Y轴,X=(右边界-左边界 - 前半部分的len值)/2.0F +
    // len ， y 轴和之前的保持一致，底边界-20
    cb.addTemplate(total, (document.rightMargin() + document.right()
            + document.leftMargin() - document.left()) / 2.0F ,
        document.bottom() - 20); // 调节模版显示的位置

  }

  //加水印
  public void addWatermark(PdfWriter writer) {
    // 水印图片
    //        Image image;
    //        try {
    //            image = Image.getInstance("./web/images/001.jpg");
    //            PdfContentByte content = writer.getDirectContentUnder();
    //            content.beginText();
    //            // 开始写入水印
    //            for(int k=0;k<5;k++){
    //                for (int j = 0; j <4; j++) {
    //                    image.setAbsolutePosition(150*j,170*k);
    //                    content.addImage(image);
    //                }
    //            }
    //            content.endText();
    //        } catch (IOException | DocumentException e) {
    //            // TODO Auto-generated catch block
    //            e.printStackTrace();
    //        }
    BaseFont font = null;
    try {
      font = BaseFont.createFont("STSong-Light","UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
    } catch (DocumentException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    PdfGState gs = new PdfGState();
    //添加透明度
    gs.setFillOpacity(0.4f);
    PdfContentByte content = writer.getDirectContentUnder();
    content.beginText();
    //水印颜色
    content.setColorFill(BaseColor.DARK_GRAY);
    content.setGState(gs);
    //水印字体样式和大小
    content.setFontAndSize(font, 35);
    //插入水印  循环每页插入的条数
    for (int j = 0; j < 3; j++) {
      content.showTextAligned(Element.ALIGN_CENTER, "锦鲤飞上天测试水印", 300, 200 * (j + 1), 30);
    }
    content.endText();
  }

  /**
   *
   * TODO 关闭文档时，替换模板，完成整个页眉页脚组件
   *
   * @see com.itextpdf.text.pdf.PdfPageEventHelper#onCloseDocument(com.itextpdf.text.pdf.PdfWriter,
   *      com.itextpdf.text.Document)
   */
  @Override
  public void onCloseDocument(PdfWriter writer, Document document) {
    // 7.最后一步了，就是关闭文档的时候，将模板替换成实际的 Y 值,至此，page x of y 制作完毕，完美兼容各种文档size。
    total.beginText();
    total.setFontAndSize(bf, presentFontSize);// 生成的模版的字体、颜色
    String foot2 = " " + (writer.getPageNumber()) + " 页"; //页脚内容拼接  如  第1页/共2页
    //        String foot2 = String.valueOf(writer.getPageNumber()); //页脚内容拼接  如  1/2
    total.showText(foot2);// 模版显示的内容
    total.endText();
    total.closePath();
  }
}

