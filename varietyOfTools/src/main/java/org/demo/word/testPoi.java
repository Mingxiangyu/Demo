package org.demo.word;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFStyle;
import org.apache.poi.xwpf.usermodel.XWPFStyles;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTOnOff;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTString;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTStyle;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STStyleType;


public class testPoi {
  private static Map<String,Map<String,Object>> orderMap =new HashMap<String, Map<String,Object>>();

  public void init(String targetPath,String sourcePath){
    InputStream is = null;
    XWPFDocument doc=null;
    OutputStream out=null;
    try {
      XWPFDocument createDoc = new XWPFDocument();

      is = new FileInputStream(sourcePath);
      doc = new XWPFDocument(is);
      //获取段落
      List<XWPFParagraph> paras=doc.getParagraphs();

      for (XWPFParagraph para : paras){
        //             System.out.println(para.getCTP());//得到xml格式
        System.out.println(para.getStyleID());//段落级别
        System.out.println(para.getParagraphText());//段落内容

        String titleLvl = getTitleLvl(doc,para);//获取段落级别
        if("a5".equals(titleLvl)||"HTML".equals(titleLvl)||"".equals(titleLvl)||null==titleLvl){
          titleLvl = "8";
        }
        System.out.println(titleLvl+"-----");//0,1,2
        if(!"8".equals(titleLvl)){
          System.out.println(titleLvl+"===="+para.getParagraphText());
        }


        XWPFParagraph ctPara = createDoc.createParagraph();
        //一个XWPFRun代表具有相同属性的一个区域。
        XWPFRun ctRun = ctPara.createRun();
        String ctText = para.getParagraphText();
        ctRun.setFontFamily("宋体");//字体
        ctRun.setFontSize(12);

        if(null!=titleLvl&&!"".equals(titleLvl)&&!"8".equals(titleLvl)){
          addCustomHeadingStyle(createDoc,titleLvl,Integer.parseInt(titleLvl));
          String orderCode = getOrderCode(titleLvl);//获取编号
          ctText = orderCode+" "+ctText;
          ctRun.setBold(true);//标题加粗
          ctRun.setFontSize(14);

          ctPara.setStyle(titleLvl);

        }else{//正文
          ctPara.setIndentationFirstLine(567);//首行缩进：567==1厘米
//                  ctRun.setTextPosition(6);//设置行间距
        }

        ctRun.setText(ctText);//内容
      }
      out=new FileOutputStream(targetPath);
      createDoc.write(out);
    } catch (Exception e) {
      e.printStackTrace();
    } finally{
      try {
        if(null!=out){
          out.close();
        }
        if(null!=is){
          is.close();
        }
      }catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Word中的大纲级别，可以通过getPPr().getOutlineLvl()直接提取，但需要注意，Word中段落级别，通过如下三种方式定义：
   *  1、直接对段落进行定义；
   *  2、对段落的样式进行定义；
   *  3、对段落样式的基础样式进行定义。
   *  因此，在通过“getPPr().getOutlineLvl()”提取时，需要依次在如上三处读取。
   * @param doc
   * @param para
   * @return
   */
  private static String getTitleLvl(XWPFDocument doc, XWPFParagraph para) {
    String titleLvl = "";
    try {
      //判断该段落是否设置了大纲级别
      if (para.getCTP().getPPr().getOutlineLvl() != null) {
        // System.out.println("getCTP()");
//              System.out.println(para.getParagraphText());
//              System.out.println(para.getCTP().getPPr().getOutlineLvl().getVal());

        return String.valueOf(para.getCTP().getPPr().getOutlineLvl().getVal());
      }
    } catch (Exception e) {

    }

    try {
      //判断该段落的样式是否设置了大纲级别
      if (doc.getStyles().getStyle(para.getStyle()).getCTStyle().getPPr().getOutlineLvl() != null) {

        // System.out.println("getStyle");
//              System.out.println(para.getParagraphText());
//              System.out.println(doc.getStyles().getStyle(para.getStyle()).getCTStyle().getPPr().getOutlineLvl().getVal());

        return String.valueOf(doc.getStyles().getStyle(para.getStyle()).getCTStyle().getPPr().getOutlineLvl().getVal());
      }
    } catch (Exception e) {

    }

    try {
      //判断该段落的样式的基础样式是否设置了大纲级别
      if (doc.getStyles().getStyle(doc.getStyles().getStyle(para.getStyle()).getCTStyle().getBasedOn().getVal())
          .getCTStyle().getPPr().getOutlineLvl() != null) {
        // System.out.println("getBasedOn");
//              System.out.println(para.getParagraphText());
        String styleName = doc.getStyles().getStyle(para.getStyle()).getCTStyle().getBasedOn().getVal();
//              System.out.println(doc.getStyles().getStyle(styleName).getCTStyle().getPPr().getOutlineLvl().getVal());

        return String.valueOf(doc.getStyles().getStyle(styleName).getCTStyle().getPPr().getOutlineLvl().getVal());
      }
    } catch (Exception e) {

    }

    try {
      if(para.getStyleID()!=null){
        return para.getStyleID();
      }
    } catch (Exception e) {

    }

    return titleLvl;
  }

  /**
   * 增加自定义标题样式。这里用的是stackoverflow的源码
   *
   * @param docxDocument 目标文档
   * @param strStyleId 样式名称
   * @param headingLevel 样式级别
   */
  private static void addCustomHeadingStyle(XWPFDocument docxDocument, String strStyleId, int headingLevel) {

    strStyleId = String.valueOf(Integer.parseInt(strStyleId)+1);
    CTStyle ctStyle = CTStyle.Factory.newInstance();
    ctStyle.setStyleId(strStyleId);

    CTString styleName = CTString.Factory.newInstance();
    styleName.setVal(strStyleId);
    ctStyle.setName(styleName);

    CTDecimalNumber indentNumber = CTDecimalNumber.Factory.newInstance();
    indentNumber.setVal(BigInteger.valueOf(headingLevel));

    // lower number > style is more prominent in the formats bar
    ctStyle.setUiPriority(indentNumber);

    CTOnOff onoffnull = CTOnOff.Factory.newInstance();
    ctStyle.setUnhideWhenUsed(onoffnull);

    // style shows up in the formats bar
    ctStyle.setQFormat(onoffnull);

    // style defines a heading of the given level
    CTPPr ppr = CTPPr.Factory.newInstance();
    ppr.setOutlineLvl(indentNumber);
    ctStyle.setPPr(ppr);

    XWPFStyle style = new XWPFStyle(ctStyle);

    // is a null op if already defined
    XWPFStyles styles = docxDocument.createStyles();

    style.setType(STStyleType.PARAGRAPH);
    styles.addStyle(style);

  }
  /**
   * 获取标题编号
   * @param titleLvl
   * @return
   */
  private static String getOrderCode(String titleLvl) {
    String order = "";

    if("0".equals(titleLvl)||Integer.parseInt(titleLvl)==8){//文档标题||正文
      return "";
    }else if(Integer.parseInt(titleLvl)>0&&Integer.parseInt(titleLvl)<8){//段落标题

      //设置最高级别标题
      Map<String,Object> maxTitleMap = orderMap.get("maxTitleLvlMap");
      if(null==maxTitleMap){//没有，表示第一次进来
        //最高级别标题赋值
        maxTitleMap = new HashMap<String, Object>();
        maxTitleMap.put("lvl", titleLvl);
        orderMap.put("maxTitleLvlMap", maxTitleMap);
      }else{
        String maxTitleLvl = maxTitleMap.get("lvl")+"";//最上层标题级别(0,1,2,3)
        if(Integer.parseInt(titleLvl)<Integer.parseInt(maxTitleLvl)){//当前标题级别更高
          maxTitleMap.put("lvl", titleLvl);//设置最高级别标题
          orderMap.put("maxTitleLvlMap", maxTitleMap);
        }
      }

      //查父节点标题
      int parentTitleLvl = Integer.parseInt(titleLvl)-1;//父节点标题级别
      Map<String,Object> cMap = orderMap.get(titleLvl);//当前节点信息
      Map<String,Object> pMap = orderMap.get(parentTitleLvl+"");//父节点信息

      if(0==parentTitleLvl){//父节点为文档标题，表明当前节点为1级标题
        int count= 0;
        //最上层标题，没有父节点信息
        if(null==cMap){//没有当前节点信息
          cMap = new HashMap<String, Object>();
        }else{
          count = Integer.parseInt(String.valueOf(cMap.get("cCount")));//当前序个数
        }
        count++;
        order = count+"";
        cMap.put("cOrder", order);//当前序
        cMap.put("cCount", count);//当前序个数
        orderMap.put(titleLvl, cMap);

      }else{//父节点为非文档标题
        int count= 0;
        //如果没有相邻的父节点信息，当前标题级别自动升级
        if(null==pMap){
          return getOrderCode(String.valueOf(parentTitleLvl));
        }else{
          String pOrder = String.valueOf(pMap.get("cOrder"));//父节点序
          if(null==cMap){//没有当前节点信息
            cMap = new HashMap<String, Object>();
          }else{
            count = Integer.parseInt(String.valueOf(cMap.get("cCount")));//当前序个数
          }
          count++;
          order = pOrder+"."+count;//当前序编号
          cMap.put("cOrder", order);//当前序
          cMap.put("cCount", count);//当前序个数
          orderMap.put(titleLvl, cMap);
        }
      }

      //字节点标题计数清零
      int childTitleLvl = Integer.parseInt(titleLvl)+1;//子节点标题级别
      Map<String,Object> cdMap = orderMap.get(childTitleLvl+"");//
      if(null!=cdMap){
        cdMap.put("cCount", 0);//子节点序个数
        orderMap.get(childTitleLvl+"").put("cCount", 0);
      }
    }
    return order;
  }

  public static void main(String[] args) {
    String docFilePath = "C:\\Users\\T480S\\Desktop\\新建 DOCX 文档.docx";

    OutputStream out=null;
    String[] title= new String [276];
    String[] concent= new String [276];
    String[] type= new String [276];
    int i=0;
    try( InputStream is  = new FileInputStream(docFilePath);
        XWPFDocument doc = new XWPFDocument(is);) {
      XWPFDocument createDoc = new XWPFDocument();

      //获取段落
      List<XWPFParagraph> paras=doc.getParagraphs();

      for (XWPFParagraph para : paras){
        System.out.println(para.getCTP());//得到xml格式
        System.out.println(para.getStyleID());//段落级别
         System.out.println(para.getParagraphText());//段落内容
        String titleLvl = getTitleLvl(doc,para);//获取段落级别
        if("a5".equals(titleLvl)||"HTML".equals(titleLvl)||"".equals(titleLvl)||null==titleLvl){
          titleLvl = "8";
        }
        System.out.println(titleLvl+"-----");//0,1,2
        if(!"8".equals(titleLvl)){
          System.out.println(titleLvl+"===="+para.getParagraphText());

          if("3".equals(titleLvl)) {

            if(concent[i]!=null) {
              concent[i]=concent[i]+para.getParagraphText();
            } else {
              concent[i]=para.getParagraphText();
            }
            System.out.println(concent[i]);
          }
          if("2".equals(titleLvl)) {
            i++;
            title[i]=para.getParagraphText();
            type[i]=type[i-1];
            System.out.println(title[i]);
          }
          if("1".equals(titleLvl)) {
            i++;
            type[i]=para.getParagraphText();
            System.out.println(title[i]);
          }
        }


      }
      System.out.println("type: "+Arrays.toString(type));
      System.out.println("title: "+Arrays.toString(title));
      System.out.println("concent: "+Arrays.toString(concent));
//      for(int j=2;j<title.length;j++) {
//        if(title[j]!=null) {
//          String sql = "INSERT INTO shuju (title,concent,type)VALUES ('"+title[j]+"','"+concent[j]+"','"+type[j]+"')";
//          DBUtil jdbc=new DBUtil();
//          int result=jdbc.executeUpdate(sql);
//          jdbc.close();
//          //System.out.println(type[j]);
//          //System.out.println(title[j]);
//          //System.out.println(concent[j]);
//        }
//      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally{
      try {
        if(null!=out){
          out.close();
        }
      }catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}