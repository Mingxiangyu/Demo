package org.demo.数据库;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JDBC连接获取Mysql信息 {
  private static final Logger log = LoggerFactory.getLogger(JDBC连接获取Mysql信息.class);

  private static final String URL =
      "jdbc:mysql://%s:%s/%s?characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai";

  private static final String driver = "com.mysql.cj.jdbc.Driver";

  private static final String SELECT_SQL = "SELECT * FROM %s";

  public static void main(String[] args) {
    try {
      // 从配置文件中加载数据
      InputStream is = JDBC连接获取Mysql信息.class.getClassLoader().getResourceAsStream("jdbc.properties");
      Properties properties = new Properties();
      properties.load(is);
      String username = properties.getProperty("jdbc.username"); // 用户名
      String password = properties.getProperty("jdbc.password"); // 用户密码
      String database = properties.getProperty("jdbc.exportDatabaseName"); // 需要导出的数据库名
      String ip = properties.getProperty("jdbc.host"); // 从 哪个主机导出数据库，如果没有指定这个值，则默认取localhost
      String port = properties.getProperty("jdbc.port"); // 使用的端口号

      getTableDataForDataBase(ip, port, database, username, password);
    } catch (SQLException | IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * 将数据库中的数据导出到excel文件中 导出文件名为: 数据库名_export.xls
   *
   * @param ip 数据库ip地址
   * @param port 数据库端口
   * @param database 数据库名
   * @param username 连接用户名
   * @param password 连接密码
   * @throws SQLException
   */
  public static void getTableDataForDataBase(
      String ip, String port, String database, String username, String password)
      throws SQLException {
    // 获取数据库连接
    Connection connection = getConnection(ip, port, database, username, password);
    if (connection == null) {
      log.error("获取数据库连接失败");
      throw new RuntimeException("数据库连接失败");
    }

    /*
    DatabaseMetaData getTables()函数
    catalog-  字符串参数，代表表（通常是数据库）的名称（或名称模式），表（其中包含您需要检索其描述的列）存在于其中。
    传递“”以获取没有目录的表中列的描述，如果不想使用目录，则传递null，从而缩小搜索范围。

    schemaPattern- 一个String参数，表示表的模式名称（或名称模式），如果表中的列没有模式，则传递“”，如果您不想使用模式，则传递null。

    tableNamePattern- 一个String参数，代表表的名称（或名称模式）。

    types-一个String参数，表示列的名称（或名称模式）。
     */
    // 获取数据库所有表信息
    // 检索元数据对象
    DatabaseMetaData metaData = connection.getMetaData();
    // 检索数据库中的列
    ResultSet rs = metaData.getTables(database, null, null, new String[] {"TABLE"});
    // 打印列名称和大小
    while (rs.next()) {
      String tableName = rs.getString("Table_NAME");
      System.out.println("Table name: " + tableName);
      System.out.println("Table type: " + rs.getString("TABLE_TYPE"));
      System.out.println("Table schema: " + rs.getString("TABLE_SCHEM"));
      System.out.println("Table catalog: " + rs.getString("TABLE_CAT"));
      System.out.println(" ");

      getDataNameAndType(tableName, connection);
      getAllDataFromTable(tableName, connection);
    }
  }

  /**
   * 获取连接
   *
   * @param ip 数据库IP
   * @param port 端口
   * @param database 库
   * @param username 用户名
   * @param password 密码
   * @return 数据库连接
   */
  static Connection getConnection(
      String ip, String port, String database, String username, String password) {
    String url = String.format(URL, ip, port, database);
    System.out.println(url);
    try {
      Class.forName(driver);
      // 获得连接
      return DriverManager.getConnection(url, username, password);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
      log.error("找不到MySQL数据库驱动类");
      return null;
    } catch (SQLException e) {
      e.printStackTrace();
      log.error("获取数据库连接失败");
      return null;
    }
  }

  /**
   * 获取表中所有数据
   *
   * @param tableName 表名
   * @param conn 连接
   * @return 表数据
   */
  private static Map<Integer, Map<String, Object>> getAllDataFromTable(
      String tableName, Connection conn) {
    LinkedHashMap<Integer, Map<String, Object>> res;
    PreparedStatement statement = null;

    try {
      res = new LinkedHashMap<>();

      // 根据表明查询表中所有数据
      String format = String.format(SELECT_SQL, tableName);
      statement = conn.prepareStatement(format);
      ResultSet rs = statement.executeQuery();

      // 获取表中字段总数
      ResultSetMetaData metaData = rs.getMetaData();
      int columnCount = metaData.getColumnCount();
      int count = 0;

      while (rs.next()) {
        LinkedHashMap<String, Object> tmp = new LinkedHashMap<>();
        for (int i = 1; i <= columnCount; i++) {
          String columnName = metaData.getColumnName(i);
          Object obj = rs.getObject(i);
          tmp.put(columnName, obj);
        }
        res.put(count++, tmp);
      }
    } catch (SQLException e) {
      e.printStackTrace();
      log.error("查询数据库表: " + tableName + " 信息失败");
      log.error(e.getMessage());
      return null;
    } finally {
      close(statement);
    }
    return res;
  }

  /**
   * 获取所有字段名
   *
   * @param tableName 表名
   * @param conn 连接
   * @return 表数据
   */
  private static Map<String, String> getDataNameAndType(String tableName, Connection conn) {
    LinkedHashMap<String, String> res;
    PreparedStatement statement = null;

    try {
      statement = conn.prepareStatement(String.format(SELECT_SQL, tableName));
      ResultSet rs = statement.executeQuery();

      res = new LinkedHashMap<>();

      ResultSetMetaData metaData = rs.getMetaData();
      int count = metaData.getColumnCount();

      for (int i = 1; i <= count; i++) {
        res.put(metaData.getColumnName(i), metaData.getColumnClassName(i));
      }
    } catch (SQLException e) {
      e.printStackTrace();
      log.error("查询数据库表: " + tableName + " 信息失败");
      log.error(e.getMessage());
      return null;
    } finally {
      close(statement);
    }
    return res;
  }

  static void close(Statement statement) {
    if (statement != null) {
      try {
        statement.close();
      } catch (SQLException e) {
        e.printStackTrace();
        log.error("关闭statement失败");
        log.error(e.getMessage());
      }
    }
  }

  static void close(Connection connection) {
    if (connection != null) {
      try {
        connection.close();
      } catch (SQLException e) {
        log.error("关闭数据库连接失败");
        log.error(e.getMessage());
        e.printStackTrace();
      }
    }
  }
}
