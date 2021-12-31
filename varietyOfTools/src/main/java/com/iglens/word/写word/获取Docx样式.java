package com.iglens.word.写word;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFFooter;
import org.apache.poi.xwpf.usermodel.XWPFHeader;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

/** @author T480S */
public class 获取Docx样式 {
  private static Map<String, Map<String, Object>> orderMap = new HashMap<>();

  public static void main(String[] args) throws Exception {
    String filePath = "C:\\Users\\T480S\\Desktop\\ces.docx";
    if (filePath.endsWith("doc")) {
    } else if (filePath.endsWith("docx")) {
      List<Map<String, Object>> all = new ArrayList<>();
      InputStream is = new FileInputStream(filePath);
      XWPFDocument document = new XWPFDocument(is);
      List<Map<String, Object>> header = getHeader(document);
      all.addAll(header);

      List<Map<String, Object>> footer = getFooter(document);
      all.addAll(footer);

      List<Map<String, Object>> xParagraph = getXParagraph(document);
      all.addAll(xParagraph);

      List<XWPFParagraph> paragraphs = document.getParagraphs();
      for (XWPFParagraph paragraphsIterator : paragraphs) {
        String titleLvl = getTitleLvl(document, paragraphsIterator);
      }
      for (Map<String, Object> stringObjectMap : all) {
        System.out.println(stringObjectMap);
      }
    }

  }

  /**
   * 获取页眉信息
   *
   * @param doc XWPFDocument对象
   * @return
   */
  public static List<Map<String, Object>> getHeader(XWPFDocument doc) {
    List<Map<String, Object>> list = new ArrayList();
    List<XWPFHeader> headerList = doc.getHeaderList(); // 获取页眉对象集合
    for (XWPFHeader xwpfHeader : headerList) {
      Map<String, Object> map = new HashMap<>();
      map.put("页眉", xwpfHeader.getText()); // 获取页眉文本
      List<XWPFParagraph> listParagraph = xwpfHeader.getListParagraph(); // 获取所有段落的列表
      for (XWPFParagraph xwpfParagraph : listParagraph) {
        List<XWPFRun> runs = xwpfParagraph.getRuns(); // 获取公共属性集的文本区域
        for (XWPFRun xwpfRun : runs) {
          map.put("页眉字体颜色", xwpfRun.getColor()); // 获取页眉字体颜色
          map.put("页眉字体大小", xwpfRun.getFontSize()); // 获取页眉字体大小
          map.put("页眉字体", xwpfRun.getFontName()); // 获取页眉字体
        }
      }
      list.add(map);
    }

    return list;
  }

  /**
   * 获取页脚信息
   *
   * @param doc
   * @return
   */
  public static List<Map<String, Object>> getFooter(XWPFDocument doc) {
    List<Map<String, Object>> list = new ArrayList();
    List<XWPFFooter> footerList = doc.getFooterList(); // 获取页脚对象集合
    for (XWPFFooter xwpfFooter : footerList) {
      Map<String, Object> map = new HashMap<>();
      map.put("页脚", xwpfFooter.getText()); // 获取页脚文本
      List<XWPFParagraph> listParagraph = xwpfFooter.getListParagraph();
      for (XWPFParagraph xwpfParagraph : listParagraph) {
        List<XWPFRun> runs = xwpfParagraph.getRuns();
        for (XWPFRun xwpfRun : runs) {
          map.put("页脚字体颜色", xwpfRun.getColor()); // 获取页脚字体颜色
          map.put("页脚字体大小", xwpfRun.getFontSize()); // 获取页脚字体大小
          map.put("页脚字体", xwpfRun.getFontName()); // 获取页脚字体
        }
      }
      list.add(map);
    }
    return list;
  }

  /**
   * 获取段落信息
   *
   * @param doc
   * @return
   */
  public static List<Map<String, Object>> getXParagraph(XWPFDocument doc) {
    List<Map<String, Object>> list = new ArrayList();

    List<XWPFParagraph> paragraphs2 = doc.getParagraphs();
    for (XWPFParagraph xwpfParagraph : paragraphs2) {
      Map<String, Object> map = new HashMap<>();
      // System.out.println("段落级别："+xwpfParagraph.getStyleID());// 段落级别
      map.put("段落内容", xwpfParagraph.getParagraphText()); // 段落内容
      map.put("段落对齐方式", xwpfParagraph.getAlignment()); // 段落对齐方式
      //      double spacingBetween = xwpfParagraph.getSpacingBetween(); // 返回段落行之间的间距。
      //      map.put("段落行间距",spacingBetween );
      List<XWPFRun> runs = xwpfParagraph.getRuns();
      for (XWPFRun xwpfRun : runs) {
        map.put("段落字体颜色", xwpfRun.getColor()); // 获取段落字体颜色
        map.put("段落字体大小", xwpfRun.getFontSize()); // 获取段落字体大小
        map.put("段落字体 ", xwpfRun.getFontName()); // 获取段落字体
        map.put("段落下划线设置", xwpfRun.getUnderline()); // 获取段落字体下划线设置
        //        map.put("段落下划线颜色", xwpfRun.getUnderlineColor()); // 获取段落字体下划线颜色
      }
      String titleLvl = getTitleLvl(doc, xwpfParagraph); // 获取段落级别
      if (!titleLvl.equals("")) {
        String orderCode = getOrderCode(titleLvl); // 获取编号
        map.put("段落编号", orderCode);
      }
      list.add(map);
    }

    return list;
  }

  /**
   * Word中的大纲级别，可以通过getPPr().getOutlineLvl()直接提取，但需要注意，Word中段落级别，通过如下三种方式定义： 1、直接对段落进行定义；
   * 2、对段落的样式进行定义； 3、对段落样式的基础样式进行定义。 因此，在通过“getPPr().getOutlineLvl()”提取时，需要依次在如上三处读取。
   *
   * @param doc
   * @param para
   * @return
   */
  private static String getTitleLvl(XWPFDocument doc, XWPFParagraph para) {
    String titleLvl = "";
    try {
      // 判断该段落是否设置了大纲级别
      if (para.getCTP().getPPr().getOutlineLvl() != null) {
        // System.out.println("getCTP()");
        //             System.out.println(para.getParagraphText());
        //             System.out.println(para.getCTP().getPPr().getOutlineLvl().getVal());

        return String.valueOf(para.getCTP().getPPr().getOutlineLvl().getVal());
      }
    } catch (Exception e) {

    }

    try {
      // 判断该段落的样式是否设置了大纲级别
      if (doc.getStyles().getStyle(para.getStyle()).getCTStyle().getPPr().getOutlineLvl() != null) {

        // System.out.println("getStyle");
        //             System.out.println(para.getParagraphText());
        //
        // System.out.println(doc.getStyles().getStyle(para.getStyle()).getCTStyle().getPPr().getOutlineLvl().getVal());

        return String.valueOf(
            doc.getStyles()
                .getStyle(para.getStyle())
                .getCTStyle()
                .getPPr()
                .getOutlineLvl()
                .getVal());
      }
    } catch (Exception e) {

    }

    try {
      // 判断该段落的样式的基础样式是否设置了大纲级别
      if (doc.getStyles()
              .getStyle(
                  doc.getStyles().getStyle(para.getStyle()).getCTStyle().getBasedOn().getVal())
              .getCTStyle()
              .getPPr()
              .getOutlineLvl()
          != null) {
        // System.out.println("getBasedOn");
        //             System.out.println(para.getParagraphText());
        String styleName =
            doc.getStyles().getStyle(para.getStyle()).getCTStyle().getBasedOn().getVal();
        //
        // System.out.println(doc.getStyles().getStyle(styleName).getCTStyle().getPPr().getOutlineLvl().getVal());

        return String.valueOf(
            doc.getStyles().getStyle(styleName).getCTStyle().getPPr().getOutlineLvl().getVal());
      }
    } catch (Exception e) {

    }

    try {
      if (para.getStyleID() != null) {
        return para.getStyleID();
      }
    } catch (Exception e) {

    }

    return titleLvl;
  }

  /**
   * 获取标题编号
   *
   * @param titleLvl
   * @return
   */
  private static String getOrderCode(String titleLvl) {
    String order = "";

    if ("0".equals(titleLvl) || Integer.parseInt(titleLvl) == 8) { // 文档标题||正文
      return "";
    } else if (Integer.parseInt(titleLvl) > 0 && Integer.parseInt(titleLvl) < 8) { // 段落标题

      // 设置最高级别标题
      Map<String, Object> maxTitleMap = orderMap.get("maxTitleLvlMap");
      if (null == maxTitleMap) { // 没有，表示第一次进来
        // 最高级别标题赋值
        maxTitleMap = new HashMap<String, Object>();
        maxTitleMap.put("lvl", titleLvl);
        orderMap.put("maxTitleLvlMap", maxTitleMap);
      } else {
        String maxTitleLvl = maxTitleMap.get("lvl") + ""; // 最上层标题级别(0,1,2,3)
        if (Integer.parseInt(titleLvl) < Integer.parseInt(maxTitleLvl)) { // 当前标题级别更高
          maxTitleMap.put("lvl", titleLvl); // 设置最高级别标题
          orderMap.put("maxTitleLvlMap", maxTitleMap);
        }
      }

      // 查父节点标题
      int parentTitleLvl = Integer.parseInt(titleLvl) - 1; // 父节点标题级别
      Map<String, Object> cMap = orderMap.get(titleLvl); // 当前节点信息
      Map<String, Object> pMap = orderMap.get(parentTitleLvl + ""); // 父节点信息

      if (0 == parentTitleLvl) { // 父节点为文档标题，表明当前节点为1级标题
        int count = 0;
        // 最上层标题，没有父节点信息
        if (null == cMap) { // 没有当前节点信息
          cMap = new HashMap<String, Object>();
        } else {
          count = Integer.parseInt(String.valueOf(cMap.get("cCount"))); // 当前序个数
        }
        count++;
        order = count + "";
        cMap.put("cOrder", order); // 当前序
        cMap.put("cCount", count); // 当前序个数
        orderMap.put(titleLvl, cMap);

      } else { // 父节点为非文档标题
        int count = 0;
        // 如果没有相邻的父节点信息，当前标题级别自动升级
        if (null == pMap) {
          return getOrderCode(String.valueOf(parentTitleLvl));
        } else {
          String pOrder = String.valueOf(pMap.get("cOrder")); // 父节点序
          if (null == cMap) { // 没有当前节点信息
            cMap = new HashMap<String, Object>();
          } else {
            count = Integer.parseInt(String.valueOf(cMap.get("cCount"))); // 当前序个数
          }
          count++;
          order = pOrder + "." + count; // 当前序编号
          cMap.put("cOrder", order); // 当前序
          cMap.put("cCount", count); // 当前序个数
          orderMap.put(titleLvl, cMap);
        }
      }

      // 字节点标题计数清零
      int childTitleLvl = Integer.parseInt(titleLvl) + 1; // 子节点标题级别
      Map<String, Object> cdMap = orderMap.get(childTitleLvl + ""); //
      if (null != cdMap) {
        cdMap.put("cCount", 0); // 子节点序个数
        orderMap.get(childTitleLvl + "").put("cCount", 0);
      }
    }
    return order;
  }
}
