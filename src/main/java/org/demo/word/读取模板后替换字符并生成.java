//package org.demo.word;
//
//import java.awt.image.BufferedImage;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.math.BigInteger;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import java.util.Map.Entry;
//import javax.imageio.ImageIO;
//import org.apache.poi.POIXMLDocument;
//import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
//import org.apache.poi.openxml4j.opc.OPCPackage;
//import org.apache.poi.util.Units;
//import org.apache.poi.xwpf.converter.pdf.PdfConverter;
//import org.apache.poi.xwpf.converter.pdf.PdfOptions;
//import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
//import org.apache.poi.xwpf.usermodel.LineSpacingRule;
//import org.apache.poi.xwpf.usermodel.XWPFDocument;
//import org.apache.poi.xwpf.usermodel.XWPFParagraph;
//import org.apache.poi.xwpf.usermodel.XWPFRun;
//import org.apache.poi.xwpf.usermodel.XWPFTable;
//import org.apache.poi.xwpf.usermodel.XWPFTableCell;
//import org.apache.poi.xwpf.usermodel.XWPFTableRow;
//import org.apache.xmlbeans.XmlException;
//import org.apache.xmlbeans.XmlToken;
//import org.demo.word.写word.CustomXWPFDocument;
//import org.demo.word.写word.WordUtil;
//import org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps;
//import org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D;
//import org.openxmlformats.schemas.drawingml.x2006.wordprocessingDrawing.CTInline;
//import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
//import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSpacing;
//import org.openxmlformats.schemas.wordprocessingml.x2006.main.STLineSpacingRule;
//
//public class 读取模板后替换字符并生成 {
//  public static void main(String[] args) throws Exception {
//    Map<String, Object> param = new HashMap<String, Object>();
//    param.put("periodical", "1");
//    param.put("missileBatch", "missileBatch");
//    param.put("height", "height == null ? null : height.toString()");
//    param.put("longitude", "nuclearExplosion.getLongitude()");
//    param.put("latitude", "nuclearExplosion.getLatitude()");
//    // 吨转换为千吨
//    param.put("nuclearEquivalent", "nuclearEquivalent");
//    param.put("proName", "合规性分析测试");
//    param.put("proType", "总体规划");
//    param.put("nonConstrArea", "25438834.17");
//    param.put("constrArea", "0.00");
//
//    String fileUrl =
//        "http://localhost:9914/image/result/0b92382a-2698-499c-9622-01f61bba60ce-result.png";
//    URL url = new URL(fileUrl);
//    BufferedImage read = ImageIO.read(url);
//    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//    // 设置超时间为3秒
//    conn.setConnectTimeout(3 * 1000);
//    // 防止屏蔽程序抓取而返回403错误
//    conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
//    // 得到输入流
//    InputStream inputStream = conn.getInputStream();
//    //    BufferedImage read = ImageIO.read(inputStream);
//    //    int width = 110;
//    //    int height = 110;
//    int readWidth = read.getWidth();
//    int readHeight = read.getHeight();
//    // 修改为等比例图片，避免图片过大
//    int height = 180;
//    int width = height * readWidth / readHeight; // readWidth / height1为图片缩小比例
//    Map<String, Object> header = new HashMap<String, Object>();
//    header.put("width", width);
//    header.put("height", height);
//    header.put("type", "png");
//
//    header.put("content", inputStream); // 图片路径
//
//    param.put("tianjiImage", header);
//    XWPFDocument doc = 读取模板后替换字符并生成.generateWord(param, "E:\\DJDeploy\\打击效果评估报告.docx");
//    FileOutputStream fopts = new FileOutputStream("E:\\DJDeploy\\test.pdf");
//    FileOutputStream foptdocs = new FileOutputStream("E:\\DJDeploy\\test.docx");
//    PdfOptions options = PdfOptions.create();
//
//    doc.write(foptdocs);
//    PdfConverter.getInstance().convert(doc, fopts, options);
//    foptdocs.close();
//    fopts.close();
//  }
//
//  public static String getTextFromDocx(String filePath) throws IOException {
//    FileInputStream in = new FileInputStream(filePath);
//    XWPFDocument doc = new XWPFDocument(in);
//    XWPFWordExtractor extractor = new XWPFWordExtractor(doc);
//    String text = extractor.getText();
//    System.out.println(text);
//    in.close();
//    return text;
//  }
//
//  public static void replaceText(String text, Map<String, Object> param) {
//    if (text != null) {
//      boolean isSetText = false;
//      for (Entry<String, Object> entry : param.entrySet()) {
//        String key = "${" + entry.getKey() + "}";
//        if (text.contains(key)) {
//          isSetText = true;
//          Object value = entry.getValue();
//          if (value instanceof String) { // 文本替换
//            text = text.replace(key, value.toString());
//          }
//
//        }
//      }
//      if (isSetText) {
//        run.setText(text, 0);
//      }
//    }
//  }
//
//  /**
//   * 适用于word 2007
//   *
//   * @param param 需要替换的参数
//   * @param template 模板
//   */
//  public static XWPFDocument generateWord(Map<String, Object> param, String template) {
//    XWPFDocument doc = null;
//    try {
//      OPCPackage pack = POIXMLDocument.openPackage(template); // 通过路径获取word模板
//      doc = new CustomXWPFDocument(pack);
//      // 通过InputStream 获取模板，此方法适用于jar包部署
//      //  doc = new CustomXWPFDocument(template);
//      if (param != null && param.size() > 0) {
//        // 处理段落
//        List<XWPFParagraph> paragraphList = doc.getParagraphs();
//        processParagraphs(paragraphList, param, doc);
//      }
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//    return doc;
//  }
//
//  /**
//   * 处理段落
//   *
//   * @param paragraphList 段落
//   * @param param 替换的参数
//   * @param doc doc文档
//   * @throws InvalidFormatException
//   */
//  public static void processParagraphs(
//      List<XWPFParagraph> paragraphList, Map<String, Object> param, XWPFDocument doc)
//      throws InvalidFormatException {
//    if (paragraphList != null && paragraphList.size() > 0) {
//      for (XWPFParagraph paragraph : paragraphList) {
//        // TODO 设置行间距
//        paragraph.setSpacingLineRule(LineSpacingRule.AUTO);
//
//        List<XWPFRun> runs = paragraph.getRuns();
//        for (XWPFRun run : runs) {
//          run.setFontSize(16);
//          run.setFontFamily("宋体");
//          //          run.setBold(true);
//          String text = run.getText(0);
//        }
//      }
//    }
//  }
//
//  /**
//   * 获取图片对应类型代码
//   *
//   * @param picType 图片文件后缀
//   * @return int
//   */
//  private static int getPictureType(String picType) {
//    int res = CustomXWPFDocument.PICTURE_TYPE_PICT;
//    if (picType != null) {
//      if ("png".equalsIgnoreCase(picType)) {
//        res = CustomXWPFDocument.PICTURE_TYPE_PNG;
//      } else if ("dib".equalsIgnoreCase(picType)) {
//        res = CustomXWPFDocument.PICTURE_TYPE_DIB;
//      } else if ("emf".equalsIgnoreCase(picType)) {
//        res = CustomXWPFDocument.PICTURE_TYPE_EMF;
//      } else if ("jpg".equalsIgnoreCase(picType) || "jpeg".equalsIgnoreCase(picType)) {
//        res = CustomXWPFDocument.PICTURE_TYPE_JPEG;
//      } else if ("wmf".equalsIgnoreCase(picType)) {
//        res = CustomXWPFDocument.PICTURE_TYPE_WMF;
//      }
//    }
//    return res;
//  }
//
//  /**
//   * insert Picture
//   *
//   * @param document
//   * @param filePath
//   * @param inline
//   * @param width
//   * @param height
//   * @throws InvalidFormatException
//   * @throws FileNotFoundException
//   */
//  private static void insertPicture(
//      XWPFDocument document,
//      InputStream filePath,
//      CTInline inline,
//      int width,
//      int height,
//      int imgType)
//      throws InvalidFormatException, FileNotFoundException {
//    // 通过流获取图片，因本人项目中，是通过流获取
//    document.addPictureData(filePath, imgType);
//    //    document.addPictureData(new FileInputStream(filePath),imgType);
//    int id = document.getAllPictures().size() - 1;
//    final int EMU = 9525;
//    width *= EMU;
//    height *= EMU;
//    String blipId = document.getAllPictures().get(id).getPackageRelationship().getId();
//    String picXml = getPicXml(blipId, width, height);
//    XmlToken xmlToken = null;
//    try {
//      xmlToken = XmlToken.Factory.parse(picXml);
//    } catch (XmlException xe) {
//      xe.printStackTrace();
//    }
//    inline.set(xmlToken);
//    inline.setDistT(0);
//    inline.setDistB(0);
//    inline.setDistL(0);
//    inline.setDistR(0);
//    CTPositiveSize2D extent = inline.addNewExtent();
//    extent.setCx(width);
//    extent.setCy(height);
//    CTNonVisualDrawingProps docPr = inline.addNewDocPr();
//    docPr.setId(id);
//    docPr.setName("IMG_" + id);
//    docPr.setDescr("IMG_" + id);
//  }
//
//  /**
//   * get the xml of the picture
//   *
//   * @param blipId
//   * @param width
//   * @param height
//   * @return
//   */
//  private static String getPicXml(String blipId, int width, int height) {
//    String picXml =
//        ""
//            + "<a:graphic xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\">"
//            + "   <a:graphicData uri=\"http://schemas.openxmlformats.org/drawingml/2006/picture\">"
//            + "      <pic:pic xmlns:pic=\"http://schemas.openxmlformats.org/drawingml/2006/picture\">"
//            + "         <pic:nvPicPr>"
//            + "            <pic:cNvPr id=\""
//            + 0
//            + "\" name=\"Generated\"/>"
//            + "            <pic:cNvPicPr/>"
//            + "         </pic:nvPicPr>"
//            + "         <pic:blipFill>"
//            + "            <a:blip r:embed=\""
//            + blipId
//            + "\" xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\"/>"
//            + "            <a:stretch>"
//            + "               <a:fillRect/>"
//            + "            </a:stretch>"
//            + "         </pic:blipFill>"
//            + "         <pic:spPr>"
//            + "            <a:xfrm>"
//            + "               <a:off x=\"0\" y=\"0\"/>"
//            + "               <a:ext cx=\""
//            + width
//            + "\" cy=\""
//            + height
//            + "\"/>"
//            + "            </a:xfrm>"
//            + "            <a:prstGeom prst=\"rect\">"
//            + "               <a:avLst/>"
//            + "            </a:prstGeom>"
//            + "         </pic:spPr>"
//            + "      </pic:pic>"
//            + "   </a:graphicData>"
//            + "</a:graphic>";
//    return picXml;
//  }
//
//  /**
//   * 设置段落间距
//   *
//   * @param para
//   */
//  public static void setSingleLineSpacing(XWPFParagraph para) {
//    CTPPr ppr = para.getCTP().getPPr();
//    if (ppr == null) {
//      ppr = para.getCTP().addNewPPr();
//    }
//    CTSpacing spacing = ppr.isSetSpacing() ? ppr.getSpacing() : ppr.addNewSpacing();
//    spacing.setAfter(BigInteger.valueOf(0));
//    spacing.setBefore(BigInteger.valueOf(0));
//    spacing.setLineRule(STLineSpacingRule.AUTO);
//    spacing.setLine(BigInteger.valueOf(240));
//  }
//}
