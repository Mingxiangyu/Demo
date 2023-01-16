package com.iglens.csv;

import cn.hutool.core.collection.CollectionUtil;
import com.google.common.collect.Lists;
import com.iglens.数据库.MySql.JDBC连接获取Mysql信息;
import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/** @author xming */
@Slf4j
public class CSV导入到MySQL {
  private static Connection conn;
  public static String SQL_INSTALL_IDNO_THIRD = null;

  public static void main(String[] args) {
    List<List<String>> csvList =
        readCsv("E:\\WorkSpace\\pyWorkSpace\\noaa\\tar_gz\\2022\\47108099999.csv", 1);
    insert(csvList);
  }

  /**
   * 把数据从csv中读取到list
   *
   * @param filePath csv路径
   * @param ignoreRows 忽略行数
   * @return csv中所有数据
   */
  public static List<List<String>> readCsv(String filePath, int ignoreRows) {
    String line;
    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
      // 忽略前几行标题
      if (ignoreRows > 0) {
        for (int i = 0; i < ignoreRows; i++) {
          String columnNameLine = reader.readLine();
          SQL_INSTALL_IDNO_THIRD = buildInsertSql("hydrology", columnNameLine);
        }
      }
      List<List<String>> csvList = new ArrayList<>();
      while ((line = reader.readLine()) != null) {
        if (StringUtils.isEmpty(line.trim())) {
          continue;
        }

        // 双引号内的逗号不分割  双引号外的逗号进行分割 @link：https://blog.csdn.net/xyr05288/article/details/53696464/
        String[] lineArray = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)", -1);
        // CSV格式文件为逗号分隔符文件，这里根据逗号切分 toDo 无法对带有,数据进行切分
        // String[] lineArray = line.split(",");
        System.out.println("lineArray.length=" + lineArray.length);

        // 获取到的数据单行数据
        ArrayList<String> lineList = new ArrayList<>();
        // 替换数据中的引号
        for (String item : lineArray) {
          item = item.replace("\"", "");
          lineList.add(item);
        }
        // 添加进总数据
        csvList.add(lineList);
      }
      System.out.println("从CSV中读取到的数据：" + csvList);
      return csvList;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 插入到数据库
   *
   * @param tableName 表名
   * @param columnNameLine 列名行
   */
  public static String buildInsertSql(String tableName, String columnNameLine) {
    // CSV格式文件为逗号分隔符文件，这里根据逗号切分 toDo 如果标题中数据也带有,则需要正则匹配
    String[] lineArray = columnNameLine.split(",");
    System.out.println("columnNameLine.length=" + lineArray.length);

    StringBuilder columnNames = new StringBuilder();
    StringBuilder values = new StringBuilder();
    for (String s : lineArray) {
      // 获取表中列名串 id,name,age...
      // 替换数据中的引号
      String columnName = s.replace("\"", "");
      columnNames.append(columnName).append(",");
      values.append("?").append(",");
    }
    columnNames.deleteCharAt(columnNames.length() - 1);
    values.deleteCharAt(values.length() - 1);
    // 获取到的数据单行数据
    return "INSERT INTO "
        + tableName
        + " ("
        + columnNames.toString()
        + ") VALUES ("
        + values.toString()
        + ")";
  }

  /**
   * 插入到数据库
   *
   * @param csvList csv集合
   */
  public static void insert(List<List<String>> csvList) {
    try {
      // 批处理数，避免一条一次请求
      int batchNum = 100;
      // 将大数据集合进行切分，避免内存溢出
      List<List<List<String>>> subLists = Lists.partition(csvList, batchNum);
      for (List<List<String>> childCsvList : subLists) {
        executeUpate(SQL_INSTALL_IDNO_THIRD, childCsvList, "yyyy-MM-dd");
      }
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  /**
   * 批量执行增删改操作
   *
   * @param sql 语句
   * @param csvList 封装bean的集合
   * @param pattern 日期格式
   * @return int 影响行数
   * @throws SQLException 异常
   */
  public static int[] executeUpate(String sql, List<List<String>> csvList, String pattern)
      throws SQLException {
    SimpleDateFormat format;
    if (pattern != null && pattern.length() > 0) {
      format = new SimpleDateFormat(pattern);
    } else {
      format = new SimpleDateFormat("yyyy-MM-dd");
    }
    if (conn == null) {
      conn = JDBC连接获取Mysql信息.getConnection(ip, port, database, username, password);
      if (conn == null) {
        log.error("获取数据库连接失败");
        System.out.println("获取数据库连接失败");
        throw new RuntimeException("数据库连接失败");
      }
    }
    // 关闭事物自动提交
    conn.setAutoCommit(false);
    PreparedStatement ps = conn.prepareStatement(sql);
    try {
      // 注入参数
      if (CollectionUtil.isEmpty(csvList)) {
        return new int[] {0};
      }
      for (List<String> line : csvList) {
        int k = 1;
        // 获取每行下的每项数据
        for (int i = 0; i < line.size(); i++) {
          String source = line.get(i);
          if (i == 0) {
            ps.setString(k++, source);
            continue;
          } else if (i == 1) {
            Date parse = format.parse(source);
            Timestamp item = new Timestamp(parse.getTime());
            ps.setTimestamp(k++, item);
            continue;
          } else if (i == 5) {
            ps.setString(k++, source);
            continue;
          } else if (i == 7) {
            int item = Integer.parseInt(source);
            ps.setInt(k++, item);
            continue;
          } else if (i == 9) {
            int item = Integer.parseInt(source);
            ps.setInt(k++, item);
            continue;
          } else if (i == 11) {
            int item = Integer.parseInt(source);
            ps.setInt(k++, item);
            continue;
          } else if (i == 13) {
            int item = Integer.parseInt(source);
            ps.setInt(k++, item);
            continue;
          } else if (i == 15) {
            int item = Integer.parseInt(source);
            ps.setInt(k++, item);
            continue;
          } else if (i == 17) {
            int item = Integer.parseInt(source);
            ps.setInt(k++, item);
            continue;
          } else if (i == 21) {
            ps.setString(k++, source);
            continue;
          } else if (i == 23) {
            ps.setString(k++, source);
            continue;
          } else if (i == 25) {
            ps.setString(k++, source);
            continue;
          } else if (i == 27) {
            ps.setString(k++, source);
            continue;
          }
          float item = Float.parseFloat(source);
          ps.setFloat(k++, item);
        }
        // 添加到批处理
        ps.addBatch();
      }
      String rsq = ps.toString();
      System.out.println(rsq);
      int[] res = ps.executeBatch();
      conn.commit();
      System.out.println("成功了插入了" + res.length + "行");
      return res;
    } catch (SQLException e) {
      conn.rollback();
      log.error("部分数据导入失败", e);
    } catch (ParseException e) {
      e.printStackTrace();
    } finally {
      ps.clearBatch();
      ps.close();
    }
    return new int[] {0};
  }
}
