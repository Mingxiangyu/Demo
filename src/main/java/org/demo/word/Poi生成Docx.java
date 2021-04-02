package org.demo.word;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTR;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSectPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShd;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STShd;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;

public class Poi生成Docx {

  public static void main(String[] args) throws IOException, InvalidFormatException {

    XWPFDocument document = new XWPFDocument();
    FileOutputStream out =
        new FileOutputStream(new File("C:\\Users\\T480S\\Desktop\\新建 Microsoft Word 文档.docx"));

    // 添加标题
    XWPFParagraph titleParagraph = document.createParagraph();
    // 设置段落居中
    titleParagraph.setAlignment(ParagraphAlignment.CENTER);

    XWPFRun titleParagraphRun = titleParagraph.createRun();

    titleParagraphRun.setText("Java PoI");
    titleParagraphRun.setColor("000000");
    titleParagraphRun.setFontSize(20);
    // 段落
    XWPFParagraph firstParagraph = document.createParagraph();
    XWPFRun run = firstParagraph.createRun();
    run.setText("Java POI 生成word文件。");
    run.setColor("696969");
    run.setFontSize(16); //设置字体大小
    run.setBold(true);//设置为粗体

    // 设置段落背景颜色
    CTShd cTShd = run.getCTR().addNewRPr().addNewShd();
    cTShd.setVal(STShd.CLEAR);
    cTShd.setFill("97FFFF");

    // 换行
    XWPFParagraph paragraph1 = document.createParagraph();
    XWPFRun paragraphRun1 = paragraph1.createRun();
    paragraphRun1.setText("\r");
    paragraphRun1.addCarriageReturn();//回车换行

    // 基本信息表格
    XWPFTable infoTable = document.createTable();
    // 去表格边框
//    infoTable.getCTTbl().getTblPr().unsetTblBorders();

    // 列宽自动分割
    CTTblWidth infoTableWidth = infoTable.getCTTbl().addNewTblPr().addNewTblW();
    infoTableWidth.setType(STTblWidth.DXA);
    infoTableWidth.setW(BigInteger.valueOf(9072));

    // 表格第一行
    XWPFTableRow infoTableRowOne = infoTable.getRow(0);
    infoTableRowOne.getCell(0).setText("职位");
    infoTableRowOne.addNewTableCell().setText(": Java 开发工程师");

    // 表格第二行
    XWPFTableRow infoTableRowTwo = infoTable.createRow();
    infoTableRowTwo.getCell(0).setText("姓名");
    infoTableRowTwo.getCell(1).setText(": seawater");

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

    XWPFParagraph pic = document.createParagraph();
    pic.setAlignment(ParagraphAlignment.CENTER);
    XWPFRun picRun = pic.createRun();
    List<String> filePath = new ArrayList<String>();
    filePath.add("C:\\Users\\T480S\\Desktop\\file1.jpg");

    for (String str : filePath) {
      picRun.setText(str);
      picRun.addPicture(
          new FileInputStream(str),
          XWPFDocument.PICTURE_TYPE_JPEG,
          str,
          Units.toEMU(450),
          Units.toEMU(300));
    }
    // 添加页眉
    CTP ctpHeader = CTP.Factory.newInstance();
    CTR ctrHeader = ctpHeader.addNewR();
    CTText ctHeader = ctrHeader.addNewT();
    String headerText = "ctpHeader";
    ctHeader.setStringValue(headerText);
    XWPFParagraph headerParagraph = new XWPFParagraph(ctpHeader, document);
    // 设置为右对齐
    headerParagraph.setAlignment(ParagraphAlignment.RIGHT);
    XWPFParagraph[] parsHeader = new XWPFParagraph[1];
    parsHeader[0] = headerParagraph;
    policy.createHeader(XWPFHeaderFooterPolicy.DEFAULT, parsHeader);

    // 添加页脚
    CTP ctpFooter = CTP.Factory.newInstance();
    CTR ctrFooter = ctpFooter.addNewR();
    CTText ctFooter = ctrFooter.addNewT();
    String footerText = "ctpFooter";
    ctFooter.setStringValue(footerText);
    XWPFParagraph footerParagraph = new XWPFParagraph(ctpFooter, document);
    headerParagraph.setAlignment(ParagraphAlignment.CENTER);
    XWPFParagraph[] parsFooter = new XWPFParagraph[1];
    parsFooter[0] = footerParagraph;
    policy.createFooter(XWPFHeaderFooterPolicy.DEFAULT, parsFooter);
    document.write(out);
    out.close();
  }
}
