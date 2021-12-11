package com.iglens.数据库.MongoDB;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.ListIndexesIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

/**
 * mongoTemplate应用
 *
 * @author ming
 * @date 27/12/2019 上午 11:21
 * @desc MongoDB 操作工具类
 */
@Component
public class MongoDBHelper<T> {

  /** 注入template */
  @Autowired private MongoTemplate mongoTemplate;

  public void importExcel(String filePath) {
    // 检查文件
    Workbook workbook = getWorkBook(new File(filePath));
    if (workbook == null) {
      return;
    }
    for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {
      // 获取当前sheet工作表
      Sheet sheet = workbook.getSheetAt(sheetNum);
      if (sheet == null) {
        continue;
      }
      String sheetName = sheet.getSheetName();
      System.out.println("sheetName: " + sheetName);
      // 连接文档
      MongoCollection<Document> collection = mongoTemplate.getCollection(sheetName);
      System.out.println("连接成功");

      List<Map<String, Object>> dataList = new ArrayList<>();
      List<String> fieldList = new ArrayList<String>();
      // 获取Excel第一行名称
      Row row0 = sheet.getRow(0);
      for (Cell cell : row0) {
        fieldList.add(cell.toString());
      }
      int rows = sheet.getLastRowNum() + 1;
      int cells = fieldList.size();
      for (int i = 1; i < rows; i++) {
        Row row = sheet.getRow(i);
        Map<String, Object> paraMap = new HashMap<>();
        for (int j = 0; j < cells; j++) {
          Cell cell = row.getCell(j);
          if (cell != null && !"".equals(cell.toString())) {
            paraMap.put(fieldList.get(j), cell.toString());
          }
        }
        dataList.add(paraMap);
      }
      // 封装数据
      List<Document> documents = new ArrayList<>();
      if (dataList.size() != 0) {
        for (Map<String, Object> map : dataList) {
          Document document = new Document();
          for (String s : fieldList) {
            document.append(s, map.get(s));
          }
          collection.insertOne(document);
          documents.add(document);
        }
      }
      //      collection.insertMany(documents);
    }
  }

  /**
   * 获得工作簿对象
   *
   * @param excelFile excel文件
   * @return 工作簿对象
   */
  public static Workbook getWorkBook(File excelFile) {
    // 获得文件名
    String fileName = excelFile.getName();
    // 创建Workbook工作簿对象，表示整个excel
    Workbook workbook = null;
    try {
      // 获得excel文件的io流
      InputStream is = new FileInputStream(excelFile);
      // 根据文件后缀名不同(xls和xlsx)获得不同的workbook实现类对象
      if (fileName.endsWith(".xls")) {
        // 2003版本
        workbook = new HSSFWorkbook(new POIFSFileSystem(is));
      } else if (fileName.endsWith(".xlsx")) {
        // 2007版本
        workbook = new XSSFWorkbook(is);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return workbook;
  }

  /**
   * 导出库中所有集合到Excel
   *
   * @param filePath excel文件目录
   */
  public void exportExcel(String filePath) {
    // 创建HSSFWorkbook对象
    XSSFWorkbook workbook = new XSSFWorkbook();
    // 获取库中所有集合名称
    Set<String> collectionNames = mongoTemplate.getCollectionNames();
    System.out.println("collectionNames: " + collectionNames.toString());

    // 循环每一个集合
    for (String collectionName : collectionNames) {
      List<Map> x =
          mongoTemplate.find(
              new Query().with(Sort.by(Direction.DESC, "x")), Map.class, collectionName);
      for (Map map : x) {
        System.out.println(map);
      }
      MongoCollection<Document> collection = mongoTemplate.getCollection(collectionName);
      // 检索所有文档
      FindIterable<Document> findIterable = collection.find();
      MongoCursor<Document> mongoCursor = findIterable.iterator();
      List<Document> documents = new ArrayList<>();
      while (mongoCursor.hasNext()) {
        documents.add(mongoCursor.next());
      }
      // 表头(属性字段名）
      List<String> stringList = new ArrayList<>();
      for (Entry<String, Object> entry : documents.get(0).entrySet()) {
        stringList.add(entry.getKey());
      }

      // 创建HSSFSheet对象
      XSSFSheet sheet = workbook.createSheet(collectionName);
      // Excel表头
      XSSFRow row0 = sheet.createRow(0);
      for (int i = 0; i < stringList.size(); i++) {
        XSSFCell cell0 = row0.createCell(i);
        cell0.setCellValue(stringList.get(i));
      }

      // Excel数据
      for (int i = 0; i < documents.size(); i++) {
        XSSFRow rows = sheet.createRow(i + 1);
        for (int j = 0; j < stringList.size(); j++) {
          XSSFCell cells = rows.createCell(j);
          Object obj = documents.get(i).get(stringList.get(j));
          cells.setCellValue(obj == null ? null : obj.toString());
        }
      }
    }
    try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
      workbook.write(outputStream);
      outputStream.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }
    System.out.println("导出完成");
  }

  /**
   * 功能描述: 创建一个集合 同一个集合中可以存入多个不同类型的对象，我们为了方便维护和提升性能， 后续将限制一个集合中存入的对象类型，即一个集合只能存放一个类型的数据
   *
   * @param name 集合名称，相当于传统数据库的表名
   * @return:void
   */
  public void createCollection(String name) {
    mongoTemplate.createCollection(name);
  }

  /**
   * 功能描述: 创建索引 索引是顺序排列，且唯一的索引
   *
   * @param collectionName 集合名称，相当于关系型数据库中的表名
   * @param filedName 对象中的某个属性名
   * @return:java.lang.String
   */
  public String createIndex(String collectionName, String filedName) {
    // 配置索引选项
    IndexOptions options = new IndexOptions();
    // 设置为唯一
    options.unique(true);
    // 创建按filedName升序排的索引
    return mongoTemplate
        .getCollection(collectionName)
        .createIndex(Indexes.ascending(filedName), options);
  }

  /**
   * 功能描述: 获取当前集合对应的所有索引的名称
   *
   * @param collectionName
   * @return:java.util.List<java.lang.String>
   */
  public List<String> getAllIndexes(String collectionName) {
    ListIndexesIterable<Document> list = mongoTemplate.getCollection(collectionName).listIndexes();
    // 上面的list不能直接获取size，因此初始化arrayList就不设置初始化大小了
    List<String> indexes = new ArrayList<>();
    for (Document document : list) {
      document
          .entrySet()
          .forEach(
              (key) -> {
                // 提取出索引的名称
                if (key.getKey().equals("name")) {
                  indexes.add(key.getValue().toString());
                }
              });
    }
    return indexes;
  }

  /**
   * 功能描述: 往对应的集合中插入一条数据
   *
   * @param info 存储对象
   * @param collectionName 集合名称
   * @return:void
   */
  public void insert(T info, String collectionName) {
    mongoTemplate.insert(info, collectionName);
  }

  /**
   * 功能描述: 往对应的集合中批量插入数据，注意批量的数据中不要包含重复的id
   *
   * @param infos 对象列表
   * @return:void
   */
  public void insertMulti(List<T> infos, String collectionName) {
    mongoTemplate.insert(infos, collectionName);
  }

  /**
   * 功能描述: 使用索引信息精确更改某条数据
   *
   * @param id 唯一键
   * @param collectionName 集合名称
   * @param info 待更新的内容
   * @return:void
   */
  public void updateById(String id, String collectionName, T info) {
    Query query = new Query(Criteria.where("id").is(id));
    Update update = new Update();
    String str = JSON.toJSONString(info);
    JSONObject jQuery = JSON.parseObject(str);
    jQuery.forEach(
        (key, value) -> {
          // 因为id相当于传统数据库中的主键，这里使用时就不支持更新，所以需要剔除掉
          if (!key.equals("id")) {
            update.set(key, value);
          }
        });
    mongoTemplate.updateMulti(query, update, info.getClass(), collectionName);
  }

  /**
   * 功能描述: 根据id删除集合中的内容
   *
   * @param id 序列id
   * @param collectionName 集合名称
   * @param clazz 集合中对象的类型
   * @return:void
   */
  public void deleteById(String id, Class<T> clazz, String collectionName) {
    // 设置查询条件，当id=#{id}
    Query query = new Query(Criteria.where("id").is(id));
    // mongodb在删除对象的时候会判断对象类型，如果你不传入对象类型，只传入了集合名称，它是找不到的
    // 上面我们为了方便管理和提升后续处理的性能，将一个集合限制了一个对象类型，所以需要自行管理一下对象类型
    // 在接口传入时需要同时传入对象类型
    mongoTemplate.remove(query, clazz, collectionName);
  }

  /**
   * 功能描述: 根据id查询信息
   *
   * @param id 注解
   * @param clazz 类型
   * @param collectionName 集合名称
   * @return:java.util.List<T>
   */
  public T selectById(String id, Class<T> clazz, String collectionName) {
    // 查询对象的时候，不仅需要传入id这个唯一键，还需要传入对象的类型，以及集合的名称
    return mongoTemplate.findById(id, clazz, collectionName);
  }

  /**
   * 功能描述: 查询列表信息 将集合中符合对象类型的数据全部查询出来
   *
   * @param collectName 集合名称
   * @param clazz 类型
   * @return:java.util.List<T>
   */
  public List<T> selectList(String collectName, Class<T> clazz) {
    return selectList(collectName, clazz, null, null);
  }

  /**
   * 功能描述: 分页查询列表信息
   *
   * @param collectName 集合名称
   * @param clazz 对象类型
   * @param currentPage 当前页码
   * @param pageSize 分页大小
   * @return:java.util.List<T>
   */
  public List<T> selectList(
      String collectName, Class<T> clazz, Integer currentPage, Integer pageSize) {
    // 设置分页参数
    Query query = new Query();
    // 设置分页信息
    if (!ObjectUtils.isEmpty(currentPage) && ObjectUtils.isEmpty(pageSize)) {
      query.limit(pageSize);
      query.skip(pageSize * (currentPage - 1));
    }
    return mongoTemplate.find(query, clazz, collectName);
  }

  /**
   * 功能描述: 根据条件查询集合
   *
   * @param collectName 集合名称
   * @param conditions 查询条件，目前查询条件处理的比较简单，仅仅做了相等匹配，没有做模糊查询等复杂匹配
   * @param clazz 对象类型
   * @param currentPage 当前页码
   * @param pageSize 分页大小
   * @return:java.util.List<T>
   */
  public List<T> selectByCondition(
      String collectName,
      Map<String, String> conditions,
      Class<T> clazz,
      Integer currentPage,
      Integer pageSize) {
    if (ObjectUtils.isEmpty(conditions)) {
      return selectList(collectName, clazz, currentPage, pageSize);
    } else {
      // 设置分页参数
      Query query = new Query();
      query.limit(pageSize);
      query.skip(currentPage);
      // 往query中注入查询条件
      conditions.forEach((key, value) -> query.addCriteria(Criteria.where(key).is(value)));
      return mongoTemplate.find(query, clazz, collectName);
    }
  }

  /**
   * 功能描述: 根据条件查询集合
   *
   * @param collectName 集合名称
   * @param query 查询条件
   * @param clazz 对象类型
   * @return
   */
  public List<T> selectByQuery(String collectName, Query query, Class<T> clazz) {
    List<T> result = mongoTemplate.find(query, clazz, collectName);
    return result;
  }

  /**
   * 功能描述: 根据开始结束时间查询集合
   *
   * @param collectName 集合名称
   * @param startTime 开始时间
   * @param endTime 结束时间
   * @param clazz 对象类型
   * @return
   */
  public List<T> selectByTime(
      String collectName, String startTime, String endTime, Class<T> clazz) {
    Query query = new Query();
    try {
      Date start = new SimpleDateFormat().parse(startTime);
      Date end = new SimpleDateFormat().parse(endTime);
      //gt 大于  lt小于 加e后为或等于，
      // 时间需要转换为date
      Criteria criteria =
          Criteria.where("createTime").gt(start).andOperator(Criteria.where("createTime").lt(end));
      query.addCriteria(criteria);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return mongoTemplate.find(query, clazz, collectName);
  }
}
