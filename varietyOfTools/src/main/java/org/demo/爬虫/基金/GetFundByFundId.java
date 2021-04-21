package org.demo.爬虫.基金;

import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * java根据 url获取 xml 并解析xml获取所需对象
 * @author openks
 *	@since 2013-7-16
 *	需要添加 org.apache.commons.codec.jar
 *	和 commons-httpclient.jar
 * 借用甜菜萧提供的免费查询基金净值接口
 */
public class GetFundByFundId {


  /**
   * 根据基金id获取该基金信息（含当前单价，基金名称）
   * @param fid 基金id
   * @return 基金对象
   * @throws
   */
  public static void getFundByFid(String fid) {
    HttpClient client = new HttpClient();
    String ura="http://xiaoservices.sinaapp.com/fund.php?id="+fid;
    GetMethod get =new GetMethod();
    Document doc = null;
    try {
      URI uri = new URI(ura,false);
      get.setURI(uri);
      get.addRequestHeader("Content-Type",
          "application/x-www-form-urlencoded;charset=utf-8");
      client.executeMethod(get);
      InputStream inputStream = get.getResponseBodyAsStream();
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder db = factory.newDocumentBuilder();
      doc = db.parse(inputStream);
    } catch (URIException e) {
      e.printStackTrace();
    } catch (NullPointerException e) {
      e.printStackTrace();
    } catch (HttpException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ParserConfigurationException e) {
      e.printStackTrace();
    } catch (SAXException e) {
      e.printStackTrace();
    }
    get.releaseConnection();

    NodeList nodes1 =  doc.getElementsByTagName("fund");
    for (int i = 0; i < nodes1.getLength(); i++) {
      Node fund = nodes1.item(i);
      NodeList ns = fund.getChildNodes();
      for (int j = 0; j < ns.getLength(); j++){
        Node record = ns.item(j);
        if(record.getNodeName().equals("name")){
          System.out.println("基金名称：："+record.getTextContent());
        }
        if(record.getNodeName().equals("value")){
          System.out.println("基金净值：："+record.getTextContent());
        }
      }
    }
  }

  public static void main(String[] args) {
    getFundByFid("070023");
  }
}