package com.iglens.数据库.MongoDB.导出导入;

import com.iglens.execl.poiexcel.MongoExcelExpImpUtil;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.bson.Document;

/**
 * Excel数据导入MongoDB
 *
 * @version 1.0
 * @author wangcy
 * @date 2019年6月26日 下午5:41:38
 */
public class ExcelToMongo {

  private static Integer PORT = 27017; // 端口号
  private static String IP = "localhost"; // Ip
  private static String DATABASE = "database"; // 数据库名称
  private static String USERNAME = "username"; // 用户名
  private static String PASSWORD = "password"; // 密码
  private static String COLLECTION = "calendar"; // 文档名称
  private static String PATH = "d:\\2019日历.xls"; // Excel文件所在的路径
  private static List<String> FIELDLIST = Arrays.asList("data", "type"); // 数据库字段

  public static void main(String[] args) {
    MongoClient mongoClient = null;
    try {
      // IP，端口
      ServerAddress serverAddress = new ServerAddress(IP, PORT);
      List<ServerAddress> address = new ArrayList<ServerAddress>();
      address.add(serverAddress);
      // 用户名，数据库，密码
      MongoCredential credential =
          MongoCredential.createCredential(USERNAME, DATABASE, PASSWORD.toCharArray());
      List<MongoCredential> credentials = new ArrayList<MongoCredential>();
      credentials.add(credential);
      // 通过验证获取连接
      mongoClient = new MongoClient(address, credentials);
      // 连接到数据库
      MongoDatabase mongoDatabase = mongoClient.getDatabase(DATABASE);
      // 连接文档
      MongoCollection<Document> collection = mongoDatabase.getCollection(COLLECTION);
      System.out.println("连接成功");
      // 封装数据
      List<Document> documents = new ArrayList<>();
      List<Map<String, Object>> dataList = MongoExcelExpImpUtil.getExcelData(PATH);
      if (dataList != null && dataList.size() != 0) {
        Document document = new Document();
        for (Map<String, Object> map : dataList) {
          for (int i = 0; i < FIELDLIST.size(); i++) {
            document.append(FIELDLIST.get(i), map.get(FIELDLIST.get(i)));
          }
        }
        documents.add(document);
      }
      collection.insertMany(documents);
      System.out.println("导入成功");
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (mongoClient != null) {
        mongoClient.close();
      }
    }
  }
}
