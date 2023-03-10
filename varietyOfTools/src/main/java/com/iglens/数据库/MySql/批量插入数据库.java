package com.iglens.数据库.MySql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class 批量插入数据库 {

  public static void main(String[] args) throws ClassNotFoundException, SQLException {
    final String url =
        "jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai";
    final String name = "com.mysql.cj.jdbc.Driver";
    final String user = "root";
    final String password = "mingxy925871";
    Connection conn;
    Class.forName(name); // 指定连接类型
    conn = DriverManager.getConnection(url, user, password); // 获取连接
    if (conn != null) {
      System.out.println("获取连接成功");
      insert(conn);
    } else {
      System.out.println("获取连接失败");
    }
  }

  public static void insert(Connection conn) {
    // 开始时间
    Long begin = System.currentTimeMillis();
    // sql前缀
    String prefix =
        "INSERT INTO `test`.`sys_job`(`id`, `jobId`, `beanName`, `methodParams`, `cronExpression`, `jobStatus`, `remark`, `createTime`, `updateTime`, `methodName`) VALUES ";
    int count = 1;
    try {
      // 保存sql后缀
      StringBuffer suffix;
      // 设置事务为非自动提交
      conn.setAutoCommit(false);
      // 比起st，pst会更好些
      PreparedStatement pst = conn.prepareStatement(" "); // 准备执行语句
      // 外层循环，总提交事务次数
      for (int i = 1; i <= 100; i++) {
        suffix = new StringBuffer();
        // 第j次提交步长
        for (int j = 1; j <= 1000; j++) {
          // 构建SQL后缀
          suffix
              .append("(")
              .append(count)
              .append(", 4, '5', '6', '7', '7', '7', '2021-11-17', '2021-11-10', '7'),");
          count++;
        }
        // 构建完整SQL
        String sql = prefix + suffix.substring(0, suffix.length() - 1);
        System.out.println(sql);
        // 添加执行SQL
        pst.addBatch(sql);
        // 执行操作
        pst.executeBatch();
        // 提交事务
        conn.commit();
      }
      // 关闭连接
      pst.close();
      conn.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    // 结束时间
    Long end = System.currentTimeMillis();
    // 耗时
    System.out.println(count + "条数据插入花费时间 : " + (end - begin) / 1000 + " s");
    System.out.println("插入完成");
  }
}
