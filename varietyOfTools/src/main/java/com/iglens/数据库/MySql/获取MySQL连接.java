package com.iglens.数据库.MySql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 获取MySQL连接 */
@Slf4j
final class 获取MySQL连接 {

  private static final Logger logger = LoggerFactory.getLogger(获取MySQL连接.class);

  private static final String URL = "jdbc:mysql://%s:%s/%s?characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai";

  static Connection getConnection(
      String ip, String port, String database, String username, String password) {
    String driver = "com.mysql.cj.jdbc.Driver";
    String url = String.format(URL, ip, port, database);
    System.out.println(url);
    try {
      Class.forName(driver);
      return DriverManager.getConnection(url, username, password);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
      logger.error("找不到MySQL数据库驱动类");
      return null;
    } catch (SQLException e) {
      e.printStackTrace();
      logger.error("获取数据库连接失败");
      return null;
    }
  }

  static void close(Statement statement) {
    if (statement != null) {
      try {
        statement.close();
      } catch (SQLException e) {
        e.printStackTrace();
        logger.error("关闭statement失败");
        logger.error(e.getMessage());
      }
    }
  }

  static void close(Connection connection) {
    if (connection != null) {
      try {
        connection.close();
      } catch (SQLException e) {
        logger.error("关闭数据库连接失败");
        logger.error(e.getMessage());
        e.printStackTrace();
      }
    }
  }

  static void close(Statement statement, Connection connection) {
    close(statement);
    close(connection);
  }
}
