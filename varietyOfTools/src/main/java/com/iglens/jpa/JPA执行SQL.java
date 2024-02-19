package com.iglens.jpa;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

/** https://zhuanlan.zhihu.com/p/640897021*/
public class JPA执行SQL {

  // 注入EntityManager
  @PersistenceContext private EntityManager entityManager;

  /** 使用纯SQL插入数据 */
  @Transactional(rollbackOn = Exception.class)
  public void insertData() {
    // 创建SQL语句
    String sql = "INSERT INTO table_name (column1, column2) VALUES (?, ?)";

    // 创建Query对象
    Query query = entityManager.createNativeQuery(sql);

    // 设置参数
    query.setParameter(1, "value1");
    query.setParameter(2, "value2");

    // 执行SQL语句
    query.executeUpdate();
  }

  // 使用纯SQL更新数据
  @Transactional(rollbackOn = Exception.class)
  public void updateData() {
    // 创建SQL语句
    String sql = "UPDATE table_name SET column1 = ? WHERE column2 = ?";

    // 创建Query对象
    Query query = entityManager.createNativeQuery(sql);

    // 设置参数
    query.setParameter(1, "new_value1");
    query.setParameter(2, "value2");

    // 执行SQL语句
    query.executeUpdate();
  }

  // 使用纯SQL删除数据
  @Transactional(rollbackOn = Exception.class)
  public void deleteData() {
    // 创建SQL语句
    String sql = "DELETE FROM table_name WHERE column1 = ?";

    // 创建Query对象
    Query query = entityManager.createNativeQuery(sql);

    // 设置参数
    query.setParameter(1, "value1");

    // 执行SQL语句
    query.executeUpdate();
  }

  // 使用纯SQL查询数据
  public void selectData() {
    // 创建SQL语句
    String sql = "SELECT * FROM table_name WHERE column1 = ?";

    // 创建Query对象
    Query query = entityManager.createNativeQuery(sql);

    // 设置参数
    query.setParameter(1, "value1");

    // 执行查询并获取结果列表
    List resultList = query.getResultList();

    // 遍历结果
    for (Object result : resultList) {
      // 处理查询结果
    }
  }
}
