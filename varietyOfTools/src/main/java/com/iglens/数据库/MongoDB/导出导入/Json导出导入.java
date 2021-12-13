package com.iglens.数据库.MongoDB.导出导入;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.iglens.txt.一次读取txt内所有内容;
import com.iglens.字符串.字符串写入到文件中;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class Json导出导入 {

  /** 注入template */
  @Autowired private MongoTemplate mongoTemplate;

  public void exportJson(String filePath) {
    // 获取库中所有集合名称
    Set<String> collectionNames = mongoTemplate.getCollectionNames();

    JSONArray jsonArray = new JSONArray();

    for (String collectionName : collectionNames) {
      // 添加查询条件，可以不用
      Query query =
          new Query(
              Criteria.where("createTime")
                  .is(new Date())
                  .andOperator(Criteria.where("createTime").is(new Date())));

      // 通过{@link org.bson.Document}接收，然后转成json，如果直接用JSONObject接收则mongo中对象字段会变成很奇怪的东西
      List<Document> documentList = mongoTemplate.find(query, Document.class, collectionName);
      //      List<Document> documentList = mongoTemplate.findAll(Document.class); //可以不添加查询条件直接查询所有

      if (CollectionUtils.isEmpty(documentList)) {
        continue;
      }

      List<JSONObject> collect =
          documentList.stream()
              .map(o -> JSONObject.parseObject(o.toJson()))
              .collect(Collectors.toList());
      JSONObject jsonObject = new JSONObject();
      jsonObject.put(collectionName, collect);
      jsonArray.add(jsonObject);
    }
    // 将json数组转换成字符串然后写入到json文件中
    String string = JSONObject.toJSONString(jsonArray, SerializerFeature.PrettyFormat);

    字符串写入到文件中.writerFile(filePath, string);
  }

  public void importJson(String filePath) {
    String string = 一次读取txt内所有内容.readToString(filePath);

    // 将导出的文件解析成集合数组
    JSONArray collectionArray = JSONArray.parseArray(string);
    for (Object item : collectionArray) {
      // 导出时的集合对象 对应为（集合名称：数据对象List）
      JSONObject jsonObject = (JSONObject) item;
      for (Entry<String, Object> stringObjectEntry : jsonObject.entrySet()) {
        // 集合名称
        String collectionName = stringObjectEntry.getKey();
        // 数据对象List
        Object value = stringObjectEntry.getValue();
        JSONArray jsonArray = JSONArray.parseArray(value.toString());
        for (Object o : jsonArray) {
          // 转换为org.bson.Document
          Document parse = Document.parse(o.toString());
          mongoTemplate.save(parse, collectionName);
        }
      }
    }
  }
}
