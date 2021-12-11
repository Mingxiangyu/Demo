package com.iglens.ofd;

import java.io.File;
import java.util.List;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;


//根据上篇博客，先将ofd文件解压后在对xml文件解析
public class OfdRead {
  //适合于xml文件中标签不同，可根据标签获取信息
    /*public static void main(String[] args) {
        try {
            SAXReader reader = new SAXReader();//C:\\Users\\Gean_2016\\Desktop\\ofd123\\3\\Doc_0\\Attachs\\original_invoice.xml
            Document doc = reader.read(new File("C:\\Users\\Gean_2016\\Desktop\\ofd123\\3\\Doc_0\\Attachs\\original_invoice.xml"));        //读取本地xml文件
            Element rootElement = doc.getRootElement();        // 获取document对象根节点，即最外层节点下的内容

            String InvoiceCodeTagName="InvoiceCode",
                    InvoiceNoTagName="InvoiceNo",
                    TaxTotalAmountTagName="TaxTotalAmount",
                    TaxExclusiveTotalAmountTagName="TaxExclusiveTotalAmount",
                    BuyerName="Buyer",
                    BuyerTaxIDName="BuyerTaxID";

            Element InvoiceCodeElement = rootElement.element(InvoiceCodeTagName),
                    InvoiceNoElement = rootElement.element(InvoiceNoTagName),
                    TaxTotalAmountElement = rootElement.element(TaxTotalAmountTagName),
                    TaxExclusiveTotalAmountElement = rootElement.element(TaxExclusiveTotalAmountTagName),
                    BuyerNameElement=rootElement.element(BuyerName);


            System.out.println(InvoiceCodeTagName+":"+ InvoiceCodeElement.getText());        //获取xml文档中标签为<ItemType>的内容
            System.out.println(InvoiceNoTagName+":"+ InvoiceNoElement.getText());        //获取xml文档中标签为<ItemType>的内容
            System.out.println(TaxTotalAmountTagName+":"+ TaxTotalAmountElement.getText());        //获取xml文档中标签为<ItemType>的内容
            System.out.println(TaxExclusiveTotalAmountTagName+":"+ TaxExclusiveTotalAmountElement.getText());        //获取xml文档中标签为<ItemType>的内容

            List<Element> elList = BuyerNameElement.elements(BuyerTaxIDName);
            for (Element element : elList) {
                System.out.println(BuyerTaxIDName+":"+ element.getText());
            }
            BigDecimal TaxTotalAmountBigDecimal = new BigDecimal(TaxTotalAmountElement.getText());
            BigDecimal TaxExclusiveTotalAmountBigDecimal = new BigDecimal(TaxExclusiveTotalAmountElement.getText());
            BigDecimal add = TaxTotalAmountBigDecimal.add(TaxExclusiveTotalAmountBigDecimal);
            System.out.println("gold:"+add.toString());

        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }*/

  /*
   *适合于xml文件中标签相同，获取标签的Name属性和值 根据Name判断需要的信息
   **/
  public static void main(String[] args) {
    try {
      SAXReader reader = new SAXReader();
      Document doc =
          reader.read(
              new File("F:\\gitWorkSpace\\ofdparser\\ofdunzipfiles\\1\\OFD.xml")); // 读取本地xml文件
      Element rootElement = doc.getRootElement();        // 获取document对象根节点，即最外层节点下的内容
      Element bodyElement = rootElement.element("DocBody");
      List<Element> elList = ((Element) bodyElement.elements("DocInfo").get(0)).elements("CustomDatas");
      for (Element element : elList) {
        List<Element> elements = element.elements("CustomData");
        for (Element element3 : elements) {
          List attrList = element3.attributes();
          String tagName="";
          for (int i = 0; i < attrList.size(); i++) {
            //属性的取得bai
            Attribute item = (Attribute)attrList.get(i);
            tagName = item.getValue();
            System.out.println(item.getName() + "=" + item.getValue());
          }
          System.out.println(tagName+":"+ element3.getText());        //获取xml文档中标签为<ItemType>的内容
        }
      }
    } catch (DocumentException e) {
      e.printStackTrace();
    }
  }
}
