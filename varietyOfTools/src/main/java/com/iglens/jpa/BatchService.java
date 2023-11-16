package com.iglens.jpa;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @desc 批量处理
 *     <p>https://www.yii666.com/blog/490361.html
 */
@Service
@Transactional
public class BatchService {

  @PersistenceContext private EntityManager entityManager;

  // 配置文件中每次批量提交的数量
  // @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
  private long batchSize;

  /**
   * 批量插入
   *
   * <p>将原有的saveAll()，改为调用自定义的batchInsert（）方法
   *
   * @param list 实体类集合
   * @param <T> 表对应的实体类
   */
  public <T> void batchInsert(List<T> list) {
    if (!ObjectUtils.isEmpty(list)) {
      for (int i = 0; i < list.size(); i++) {
        entityManager.persist(list.get(i));
        if (i % batchSize == 0) {
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
        if (i % batchSize == 0) {
          entityManager.flush();
          entityManager.clear();
        }
      }
      entityManager.flush();
      entityManager.clear();
    }
  }
}
