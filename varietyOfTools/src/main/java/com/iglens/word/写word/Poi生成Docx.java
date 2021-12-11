package com.iglens.word.写word;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.LocaleUtil;
import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.Borders;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.apache.xmlbeans.impl.xb.xmlschema.SpaceAttribute;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTFonts;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHpsMeasure;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP.Factory;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtBlock;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtContentBlock;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtEndPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSdtPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShd;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSpacing;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTabStop;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTabs;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STFldCharType;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STLineSpacingRule;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STOnOff;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STShd;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTabJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTabTlc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTheme;

public class Poi生成Docx {

  private CTSdtBlock block;

  public static void main(String[] args) throws IOException, InvalidFormatException {

    XWPFDocument document = new XWPFDocument();
    String pathname = "C:\\Users\\T480S\\Desktop\\spire获取word自动生产的序号.docx";
    FileOutputStream out = new FileOutputStream(new File(pathname));

    // 添加标题
    XWPFParagraph titleParagraph = document.createParagraph();
    // 设置段落居中
    titleParagraph.setAlignment(ParagraphAlignment.CENTER);

    XWPFRun titleParagraphRun = titleParagraph.createRun();
    //    解决 poi 中 转换仿宋字体的时候中文转换失败的问题
    CTRPr rpr =
        titleParagraphRun.getCTR().isSetRPr()
            ? titleParagraphRun.getCTR().getRPr()
            : titleParagraphRun.getCTR().addNewRPr();
    CTFonts fonts = rpr.isSetRFonts() ? rpr.getRFonts() : rpr.addNewRFonts();
    fonts.setAscii("仿宋");
    fonts.setEastAsia("仿宋");
    fonts.setHAnsi("仿宋");

    titleParagraphRun.setText("Java PoI");
    titleParagraphRun.setColor("000000");
    titleParagraphRun.setFontSize(20);
    // 段落
    XWPFParagraph firstParagraph = document.createParagraph();
    firstParagraph.setAlignment(ParagraphAlignment.CENTER); // 设置段落的对齐方式
    firstParagraph.setBorderBottom(Borders.DOUBLE); // 设置下边框
    firstParagraph.setBorderTop(Borders.DOUBLE); // 设置上边框
    firstParagraph.setBorderRight(Borders.DOUBLE); // 设置右边框
    firstParagraph.setBorderLeft(Borders.DOUBLE); // 设置左边框
//    firstParagraph.setPageBreak(true); // 插入分页符
    XWPFRun run = firstParagraph.createRun(); // 段落末尾创建XWPFRun
    run.setText("Java POI 生成word文件。");
    run.setBold(true); // 设置为粗体
    run.setColor("696969");
    run.setFontSize(16); // 设置字体大小
    run.setBold(true); // 设置为粗体
    run.setItalic(true); // 斜体
    run.setFontFamily("Courier"); // 字体
    run.setUnderline(UnderlinePatterns.DOT_DOT_DASH); // 下划线
//    run.addCarriageReturn(); // 文本换行

    // 段落末尾创建XWPFRun
    XWPFRun endRun = firstParagraph.createRun();
    endRun.setText("为这个段落追加文本");

    // 段落起始插入XWPFRun
    XWPFRun insertNewRun = firstParagraph.insertNewRun(0);
    insertNewRun.setText("在段落起始位置插入这段文本");

    // 设置段落背景颜色
    CTShd cTShd = run.getCTR().addNewRPr().addNewShd();
    cTShd.setVal(STShd.CLEAR);
    cTShd.setFill("97FFFF");

    // 换行
    XWPFParagraph paragraph1 = document.createParagraph();
    XWPFRun paragraphRun1 = paragraph1.createRun();
//    paragraphRun1.setText("\r");
//    paragraphRun1.addCarriageReturn(); // 回车换行

    // 基本信息表格
    XWPFTable infoTable = document.createTable(); // 创建一个表格
    // 去表格边框
    infoTable.getCTTbl().getTblPr().unsetTblBorders();

    // 列宽自动分割
    CTTblWidth infoTableWidth = infoTable.getCTTbl().addNewTblPr().addNewTblW();
    infoTableWidth.setType(STTblWidth.DXA);
    infoTableWidth.setW(BigInteger.valueOf(9072));

    // 表格第一行
    XWPFTableRow infoTableRowOne = infoTable.getRow(0);
    infoTableRowOne.getCell(0).setText("职位");
    infoTableRowOne.addNewTableCell().setText(": Java 开发工程师");
    //    上面这一段代码和下面这一段代码是等价的
    //    XWPFParagraph p1 = infoTable.getRow(0).getCell(0).addParagraph();
    //    XWPFRun r1 = p1.createRun();
    //    r1.setText(": Java 开发工程师");

    // 表格第二行
    XWPFTableRow infoTableRowTwo = infoTable.createRow();
    infoTableRowTwo.getCell(0).setText("姓名");
    XWPFTableCell cell = infoTableRowTwo.getCell(1);
    cell.setText(": 测试");
    // 背景色、对齐方式
    cell.setColor("696969");
    // 获取单元格段落后设置对齐方式
    XWPFParagraph addParagraph = cell.addParagraph();
    addParagraph.setAlignment(ParagraphAlignment.RIGHT);//

    // 表格第三行
    XWPFTableRow infoTableRowThree = infoTable.createRow();
    infoTableRowThree.getCell(0).setText("生日");
    infoTableRowThree.getCell(1).setText(": xxx-xx-xx");

    // 表格第四行
    XWPFTableRow infoTableRowFour = infoTable.createRow();
    infoTableRowFour.getCell(0).setText("性别");
    infoTableRowFour.getCell(1).setText(": 男");

    // 表格第五行
    XWPFTableRow infoTableRowFive = infoTable.createRow();
    infoTableRowFive.getCell(0).setText("现居地");
    infoTableRowFive.getCell(1).setText(": xx");

    CTSectPr sectPr = document.getDocument().getBody().addNewSectPr();
    XWPFHeaderFooterPolicy policy = new XWPFHeaderFooterPolicy(document, sectPr);

    // 写入图片
    XWPFParagraph pic = document.createParagraph();
    pic.setAlignment(ParagraphAlignment.CENTER);
    XWPFRun picRun = pic.createRun();
    List<String> filePath = new ArrayList<String>();
    filePath.add("C:\\Users\\T480S\\Desktop\\file1.jpg");
    for (String str : filePath) {
      picRun.setText(str);
//      picRun.addPicture(
//          new FileInputStream(str),
//          XWPFDocument.PICTURE_TYPE_JPEG,
//          str,
//          Units.toEMU(450),
//          Units.toEMU(300));
    }
    // 添加页眉
    CTP ctpHeader = Factory.newInstance();
    CTR ctrHeader = ctpHeader.addNewR();
    CTText ctHeader = ctrHeader.addNewT();
    // 页眉内容
    String headerText = "页眉内容";
    ctHeader.setStringValue(headerText);
    XWPFParagraph headerParagraph = new XWPFParagraph(ctpHeader, document);
    // 设置为右对齐
    headerParagraph.setAlignment(ParagraphAlignment.RIGHT);
    XWPFParagraph[] parsHeader = new XWPFParagraph[1];
    parsHeader[0] = headerParagraph;
    policy.createHeader(XWPFHeaderFooterPolicy.DEFAULT, parsHeader);

    // 添加页脚
    CTP ctpFooter = Factory.newInstance();
    CTR ctrFooter = ctpFooter.addNewR();
    CTText ctFooter = ctrFooter.addNewT();
    // 页脚内容
    String footerText = "页脚内容";
    ctFooter.setStringValue(footerText);
    XWPFParagraph footerParagraph = new XWPFParagraph(ctpFooter, document);
    headerParagraph.setAlignment(ParagraphAlignment.CENTER);
    XWPFParagraph[] parsFooter = new XWPFParagraph[1];
    parsFooter[0] = footerParagraph;
    policy.createFooter(XWPFHeaderFooterPolicy.DEFAULT, parsFooter);
    document.write(out);
    out.close();
  }

  // ============================以下为工具======================================================

  /**
   * 自定义的生成目录
   *
   * @param block
   */
  public void CustomTOC(CTSdtBlock block) {
    this.block = block;
    CTSdtPr sdtPr = block.addNewSdtPr();
    CTDecimalNumber id = sdtPr.addNewId();
    id.setVal(new BigInteger("4844945"));
    sdtPr.addNewDocPartObj().addNewDocPartGallery().setVal("Table of contents");
    CTSdtEndPr sdtEndPr = block.addNewSdtEndPr();
    CTRPr rPr = sdtEndPr.addNewRPr();
    CTFonts fonts = rPr.addNewRFonts();
    fonts.setAsciiTheme(STTheme.MINOR_H_ANSI);
    fonts.setEastAsiaTheme(STTheme.MINOR_H_ANSI);
    fonts.setHAnsiTheme(STTheme.MINOR_H_ANSI);
    fonts.setCstheme(STTheme.MINOR_BIDI);
    rPr.addNewB().setVal(STOnOff.OFF);
    rPr.addNewBCs().setVal(STOnOff.OFF);
    rPr.addNewColor().setVal("auto");
    rPr.addNewSz().setVal(new BigInteger("24"));
    rPr.addNewSzCs().setVal(new BigInteger("24"));
    CTSdtContentBlock content = block.addNewSdtContent();
    CTP p = content.addNewP();
    p.setRsidR("00EF7E24".getBytes(LocaleUtil.CHARSET_1252));
    p.setRsidRDefault("00EF7E24".getBytes(LocaleUtil.CHARSET_1252));
    p.addNewPPr().addNewPStyle().setVal("TOCHeading");
    p.addNewR().addNewT().setStringValue("目     录"); // 源码中为"Table of contents"
    // 设置段落对齐方式，即将“目录”二字居中
    CTPPr pr = p.getPPr();
    CTJc jc = pr.isSetJc() ? pr.getJc() : pr.addNewJc();
    STJc.Enum en = STJc.Enum.forInt(ParagraphAlignment.CENTER.getValue());
    jc.setVal(en);
    // "目录"二字的字体
    CTRPr pRpr = p.getRArray(0).addNewRPr();
    fonts = pRpr.isSetRFonts() ? pRpr.getRFonts() : pRpr.addNewRFonts();
    fonts.setAscii("Times New Roman");
    fonts.setEastAsia("华文中宋");
    fonts.setHAnsi("华文中宋");
    // "目录"二字加粗
    CTOnOff bold = pRpr.isSetB() ? pRpr.getB() : pRpr.addNewB();
    bold.setVal(STOnOff.TRUE);
    // 设置“目录”二字字体大小为24号
    CTHpsMeasure sz = pRpr.isSetSz() ? pRpr.getSz() : pRpr.addNewSz();
    sz.setVal(new BigInteger("36"));
  }

  /**
   * 添加不带页数的大字栏目
   *
   * @param level
   * @param title
   */
  public void addRowOnlyTitle(int level, String title) {
    CTSdtContentBlock contentBlock = this.block.getSdtContent();
    CTP p = contentBlock.addNewP();
    p.setRsidR("00EF7E24".getBytes(LocaleUtil.CHARSET_1252));
    p.setRsidRDefault("00EF7E24".getBytes(LocaleUtil.CHARSET_1252));
    CTPPr pPr = p.addNewPPr();
    pPr.addNewPStyle().setVal("TOC" + level);
    CTTabs tabs = pPr.addNewTabs(); // Set of Custom Tab Stops自定义制表符集合
    CTTabStop tab = tabs.addNewTab(); // Custom Tab Stop自定义制表符
    tab.setVal(STTabJc.RIGHT);
    tab.setLeader(STTabTlc.DOT);
    tab.setPos(new BigInteger("9190")); // 默认为8290，因为调整过页边距，所有需要调整，手动设置找出最佳值
    pPr.addNewRPr().addNewNoProof(); // 不检查语法
    CTR run = p.addNewR();
    run.addNewRPr().addNewNoProof();
    run.addNewT().setStringValue(title);
    // 设置行间距
    CTSpacing pSpacing = pPr.getSpacing() != null ? pPr.getSpacing() : pPr.addNewSpacing();
    pSpacing.setLineRule(STLineSpacingRule.AUTO); // 行间距类型：多倍
    pSpacing.setLine(new BigInteger("360")); // 此处1.5倍行间距
    pSpacing.setBeforeLines(new BigInteger("20")); // 段前0.2
    pSpacing.setAfterLines(new BigInteger("10")); // 段后0.1
    // 设置字体
    CTRPr pRpr = run.getRPr();
    CTFonts fonts = pRpr.isSetRFonts() ? pRpr.getRFonts() : pRpr.addNewRFonts();
    fonts.setAscii("Times New Roman");
    fonts.setEastAsia("黑体");
    fonts.setHAnsi("黑体");
    // 设置字体大小
    CTHpsMeasure sz = pRpr.isSetSz() ? pRpr.getSz() : pRpr.addNewSz();
    sz.setVal(new BigInteger("24"));

    CTHpsMeasure szCs = pRpr.isSetSzCs() ? pRpr.getSzCs() : pRpr.addNewSzCs();
    szCs.setVal(new BigInteger("24"));
  }

  /**
   * 带页数的详细小字栏目
   *
   * @param level
   * @param title
   * @param page
   * @param bookmarkRef
   */
  public void addRow(int level, String title, int page, String bookmarkRef) {
    CTSdtContentBlock contentBlock = this.block.getSdtContent();
    CTP p = contentBlock.addNewP();
    p.setRsidR("00EF7E24".getBytes(LocaleUtil.CHARSET_1252));
    p.setRsidRDefault("00EF7E24".getBytes(LocaleUtil.CHARSET_1252));
    CTPPr pPr = p.addNewPPr();
    pPr.addNewPStyle().setVal("TOC" + level);
    CTTabs tabs = pPr.addNewTabs(); // Set of Custom Tab Stops自定义制表符集合
    CTTabStop tab = tabs.addNewTab(); // Custom Tab Stop自定义制表符
    tab.setVal(STTabJc.RIGHT);
    tab.setLeader(STTabTlc.DOT);
    tab.setPos(new BigInteger("9100")); // 默认为8290，因为调整过页边距，所有需要调整，手动设置找出最佳值
    pPr.addNewRPr().addNewNoProof(); // 不检查语法
    CTR run = p.addNewR();
    run.addNewRPr().addNewNoProof();
    run.addNewT().setStringValue(title); // 添加标题文字
    // 设置标题字体
    CTRPr pRpr = run.getRPr();
    CTFonts fonts = pRpr.isSetRFonts() ? pRpr.getRFonts() : pRpr.addNewRFonts();
    fonts.setAscii("Times New Roman");
    fonts.setEastAsia("楷体");
    fonts.setHAnsi("楷体");
    // 设置标题字体大小
    CTHpsMeasure sz = pRpr.isSetSz() ? pRpr.getSz() : pRpr.addNewSz();
    sz.setVal(new BigInteger("21"));
    CTHpsMeasure szCs = pRpr.isSetSzCs() ? pRpr.getSzCs() : pRpr.addNewSzCs();
    szCs.setVal(new BigInteger("21"));
    // 添加制表符
    run = p.addNewR();
    run.addNewRPr().addNewNoProof();
    run.addNewTab();
    // 添加页码左括号
    p.addNewR().addNewT().setStringValue("(");
    // STFldCharType.BEGIN标识与结尾处STFldCharType.END相对应
    run = p.addNewR();
    run.addNewRPr().addNewNoProof();
    run.addNewFldChar().setFldCharType(STFldCharType.BEGIN); // Field Character Type
    // pageref run
    run = p.addNewR();
    run.addNewRPr().addNewNoProof();
    CTText text = run.addNewInstrText(); // Field Code 添加域代码文本控件
    text.setSpace(SpaceAttribute.Space.PRESERVE);
    // bookmark reference
    // 源码的域名为" PAGEREF _Toc","\h"含义为在目录内建立目录项与页码的超链接
    text.setStringValue(" PAGEREF " + bookmarkRef + " \\h ");
    p.addNewR().addNewRPr().addNewNoProof();
    run = p.addNewR();
    run.addNewRPr().addNewNoProof();
    run.addNewFldChar().setFldCharType(STFldCharType.SEPARATE);
    // page number run
    run = p.addNewR();
    run.addNewRPr().addNewNoProof();
    run.addNewT().setStringValue(Integer.toString(page));
    run = p.addNewR();
    run.addNewRPr().addNewNoProof();
    // STFldCharType.END标识与上面STFldCharType.BEGIN相对应
    run.addNewFldChar().setFldCharType(STFldCharType.END);
    // 添加页码右括号
    p.addNewR().addNewT().setStringValue(")");
    // 设置行间距
    CTSpacing pSpacing = pPr.getSpacing() != null ? pPr.getSpacing() : pPr.addNewSpacing();
    pSpacing.setLineRule(STLineSpacingRule.AUTO); // 行间距类型：多倍
    pSpacing.setLine(new BigInteger("360")); // 此处1.5倍行间距
  }
}
