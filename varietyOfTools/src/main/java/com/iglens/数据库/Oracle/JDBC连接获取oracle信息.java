package com.iglens.数据库.Oracle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class JDBC连接获取oracle信息 {

  /**
   * 获取Oracle连接对象
   *
   * <p>toDO 待添加配置文件
   *
   * @return 连接对象
   * @throws ClassNotFoundException, SQLException 连接异常
   */
  private static Connection getConnection() throws ClassNotFoundException, SQLException {
    // 在项目根目录下配置oracle.properties文件， 配置url，username，password； ResourceBundle jdk1.7中可以用来读取配置文件的类
    ResourceBundle rb = ResourceBundle.getBundle("oracle");
    Class.forName("oracle.jdbc.driver.OracleDriver");
    String url = rb.getString("url");
    return DriverManager.getConnection(url, rb.getString("username"), rb.getString("password"));
  }
}
