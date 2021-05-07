package cn.demo.dao;

import cn.demo.entity.SmbLasttime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * (SmbLasttime)表数据库访问层
 *
 * @author Ray。
 * @date 2020-07-22 14:06:51
 */
@Repository
public interface SmbDao extends JpaRepository<SmbLasttime, Integer> {
  @Deprecated
  SmbLasttime findByMonthAndDayAndIsNew(Integer month, Integer day, boolean isNew);

  List<SmbLasttime> findByMonthAndDay(Integer month, Integer day);

  List<SmbLasttime> findByMonth(Integer month);
}
