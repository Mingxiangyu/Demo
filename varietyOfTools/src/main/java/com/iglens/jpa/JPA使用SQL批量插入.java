package com.iglens.jpa;

import com.alibaba.fastjson.JSON;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
/** https://dandelioncloud.cn/article/details/1602120102621429762 */
@Service
@Transactional
@Slf4j
public class JPA使用SQL批量插入 {
  @PersistenceContext private EntityManager entityManager;
  @Autowired private JdbcTemplate jdbcTemplate;
  /**
   * 批量插入
   *
   * @param list 实体类集合
   * @param <T> 表对应的实体类
   */
  public <T> void batchInsert(List<T> list) {
    if (!ObjectUtils.isEmpty(list)) {
      for (int i = 0; i < list.size(); i++) {
        entityManager.persist(list.get(i));
        if (i % 50 == 0) {
          entityManager.flush();
          entityManager.clear();
        }
      }
      entityManager.flush();
      entityManager.clear();
    }
  }
  /**
   * 批量更新
   *
   * @param list 实体类集合
   * @param <T> 表对应的实体类
   */
  public <T> void batchUpdate(List<T> list) {
    if (!ObjectUtils.isEmpty(list)) {
      for (int i = 0; i < list.size(); i++) {
        entityManager.merge(list.get(i));
        if (i % 50 == 0) {
          entityManager.flush();
          entityManager.clear();
        }
      }
      entityManager.flush();
      entityManager.clear();
    }
  }

  public void saveBatch(List<User> list) {
    /*String sql="insert into user " +
            "(user_name)" +
            " values (?)";
    List<Object[]> batchArgs=new ArrayList<Object[]>();
    for (int i = 0; i < list.size(); i++) {
        batchArgs.add(new Object[]{list.get(i)});
    }
    jdbcTemplate.batchUpdate(sql, batchArgs);*/
    StringBuilder insert =
        new StringBuilder(
            "INSERT INTO `user` (`user_name`, `pass_word`, `nick_name`,"
                + "`email`,`reg_time`) VALUES ");
    for (int i = 0; i < list.size(); i++) {
      insert
          .append("(")
          .append("'")
          .append(list.get(i).getUserName())
          .append("'")
          .append(",")
          .append("'")
          .append(list.get(i).getPassWord())
          .append("'")
          .append(",")
          .append("'")
          .append(list.get(i).getNickName())
          .append("'")
          .append(",")
          .append("'")
          .append(list.get(i).getEmail())
          .append("'")
          .append(",")
          .append("'")
          .append(list.get(i).getRegTime())
          .append("'")
          .append(")");
      if (i < list.size() - 1) {
        insert.append(",");
      }
    }
    String sql = (String) JSON.toJSON(insert);
    log.info("SQL语句:{}", JSON.toJSON(insert));
    try {
      jdbcTemplate.execute(sql);
    } catch (Exception e) {
      log.error("sql解析错误", e.getMessage());
    }
  }
}

/**
 * @author 小石潭记
 * @date 2020/10/4 14:14 @Description: ${todo}
 */
// @Entity
@Data
class User implements Serializable {
  @Id @GeneratedValue private Long id;

  @Column(nullable = false, unique = true)
  private String userName;

  @Column(nullable = false)
  private String passWord;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = true, unique = true)
  private String nickName;

  @Column(nullable = false)
  private String regTime;

  public User(String userName, String passWord, String email, String nickName, String regTime) {
    this.userName = userName;
    this.passWord = passWord;
    this.email = email;
    this.nickName = nickName;
    this.regTime = regTime;
  }

  public User() {}
}
