package com.iglens.数据库.MongoDB.JPA;

import java.io.Serializable;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "statistics_wfprocess") // 声明集合名称（即表名）
public class MongoTestUser implements Serializable {
  private static final long serialVersionUID = 1L; // 序列号版本id
  @Id private String id;
  private String userName;
  private String password;
  private int age;
  private long createTime;
}
