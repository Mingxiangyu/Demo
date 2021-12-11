package com.iglens.数据库.MongoDB.导出导入;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.bson.Document;

/**
 * TODO mongoClient连接总是报错，连接不上，后更换为使用mongoTemplate实现 <br>
 * {@link com.iglens.数据库.MongoDB.MongoDBHelper} 实现
 *
 * @author T480S
 */
@Slf4j
public class MongoToExcel {
  private static Integer PORT = 27017; // 端口号
  private static String IP = "localhost"; // IP
  private static String DATABASE = "admin"; // 数据库名
  private static String USERNAME = "root"; // 用户名
  private static String PASSWORD = "123456"; // 密码
  private static String COLLECTION = "mongoTestUser"; // 文档
  private static String ADDRESS = "d:\\workbook.xls"; // 导出Excel文件的路径

  public static void main(String[] args) {
    try {
      //      // 连接池选项
      //
      //      // 选项构建者
      //      com.mongodb.MongoClientOptions.Builder builder = new MongoClientOptions.Builder();
      //
      //      // 每个地址最大请求数
      //      builder.connectionsPerHost(8);
      //      builder.threadsAllowedToBlockForConnectionMultiplier(4);
      //
      //      // 设置连接超时时间
      //      builder.connectTimeout(10000);
      //
      //      // 设置最大等待时间
      //      builder.maxWaitTime(120000);
      //
      //      // 读取数据的超时时间
      //      builder.socketTimeout(1500);
      //
      //      MongoClientOptions options = builder.build();

      // IP，端口
      ServerAddress serverAddress = new ServerAddress(IP, PORT);
      List<ServerAddress> address = new ArrayList<>();
      address.add(serverAddress);
      // 用户名，数据库，密码
      MongoCredential credential =
          MongoCredential.createCredential(USERNAME, DATABASE, PASSWORD.toCharArray());
      List<MongoCredential> credentials = new ArrayList<>();
      credentials.add(credential);
      // 通过验证获取连接
      MongoClient mongoClient = new MongoClient(address, credentials);
      // 连接到数据库
      MongoDatabase mongoDatabase = mongoClient.getDatabase(DATABASE);
      // 连接文档
      MongoCollection<Document> collection = mongoDatabase.getCollection(COLLECTION);
      // 检索所有文档
      FindIterable<Document> findIterable = collection.find();
      MongoCursor<Document> mongoCursor = findIterable.iterator();
      List<Document> documents = new ArrayList<>();
      while (mongoCursor.hasNext()) {
        documents.add(mongoCursor.next());
      }
      // 表头
      List<String> stringList = new ArrayList<>();
      for (Entry<String, Object> entry : documents.get(0).entrySet()) {
        stringList.add(entry.getKey());
      }
      // 创建HSSFWorkbook对象
      HSSFWorkbook workbook = new HSSFWorkbook();
      // 创建HSSFSheet对象
      HSSFSheet sheet = workbook.createSheet("sheet");
      // Excel表头
      HSSFRow row0 = sheet.createRow(0);
      for (int i = 0; i < stringList.size(); i++) {
        HSSFCell cell0 = row0.createCell(i);
        cell0.setCellValue(stringList.get(i));
      }
      // Excel数据
      for (int i = 0; i < documents.size(); i++) {
        HSSFRow rows = sheet.createRow(i + 1);
        for (int j = 0; j < stringList.size(); j++) {
          HSSFCell cells = rows.createCell(j);
          cells.setCellValue(documents.get(i).get(stringList.get(j)).toString());
        }
      }
      // 输出文件
      FileOutputStream outputStream = new FileOutputStream(ADDRESS);
      workbook.write(outputStream);
      outputStream.flush();
      System.out.println("导出成功");
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    //    MongoDatabase connect = getConnect();
    //    MongoIterable<String> strings = connect.listCollectionNames();
    //    System.out.println(strings);
  }
  // 不通过认证获取连接数据库对象
  public static MongoDatabase getConnect() {
    // 连接到 mongodb 服务
    MongoClient mongoClient = new MongoClient("10.58.225.161", 27017);

    // 连接到数据库
    MongoDatabase mongoDatabase = mongoClient.getDatabase("test");

    // 返回连接数据库对象
    return mongoDatabase;
  }
  //  原文链接：https://blog.csdn.net/weixin_48469895/article/details/118304080
}
