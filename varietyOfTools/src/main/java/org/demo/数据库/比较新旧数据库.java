package org.demo.数据库;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 比较新旧数据库
 *
 * <p>显示新增的表名称 显示被修改的列(名称和类型)
 */
public class 比较新旧数据库 {
  public static void main(String[] args) {
    // compare from DB
    String driveName = "org.gjt.mm.mysql.Driver";
    String userName = "root";
    String password = "yi.gong";
    String url = "jdbc:mysql://db_IP1:3306/db_name1?useUnicode=true&amp;characterEncoding=GBK";
    DataBase db1 = new DataBase(driveName, userName, password, url);
    Map map1 = db1.getTableData();

    // compare to DB
    password = "root";
    url = "jdbc:mysql://db_IP2:3306/db_name2?useUnicode=true&amp;characterEncoding=GBK";
    DataBase db2 = new DataBase(driveName, userName, password, url);
    Map map2 = db2.getTableData();

    List newTableList = new ArrayList(); // 新增的表
    List updateColList = new ArrayList(); // 修改已有表的列
    // compare with
    for (Object object1 : map1.keySet()) {
      if (map2.containsKey(object1)) {
        List<String> list1 = (List) map1.get(object1);
        List<String> list2 = (List) map2.get(object1);
        for (String col1 : list1) {
          if (!list2.contains(col1)) {
            updateColList.add(object1 + "中的【" + col1 + "】不存在 ");
          }
        }
      } else {
        newTableList.add("新增" + object1);
      }
      // show result
      System.out.println("新增的表----------");
      for (Object addNewTable : newTableList) {
        System.out.println(addNewTable);
      }
      System.out.println("表更改过的列------------");
      for (Object updateCol : updateColList) {
        System.out.println(updateCol);
      }
    }
  }

  // 数据源
 static class DataBase {
    private String driveName = null;
    private String userName = null;
    private String password = null;
    private String url = null;
    private Connection con = null;

    public DataBase(String driveName, String userName, String password, String url) {
      linkDataBase(driveName);
      createConnection(userName, password, url);
    }

    /** 加载数据库的驱动程 */
    private void linkDataBase(String driveName) {
      try {
        Class.forName(driveName);
      } catch (ClassNotFoundException e) {
        System.out.println("Link DataBase Fail!");
      }
    }

    /** 建立一个Connection对象 */
    private void createConnection(String userName, String password, String url) {
      try {
        con = DriverManager.getConnection(url, userName, password);
      } catch (SQLException e) {
        System.out.println("Connection Opened Fail!!");
        e.printStackTrace();
      }
    }

    /** 关闭 */
    private void closeConnection() {
      if (con != null) {
        try {
          con.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
        con = null;
      }
    }

    /**
     * 获取表列数据
     *
     * @return
     * @throws SQLException
     */
    public Map getTableData() {
      Map map = new HashMap();
      try {
        DatabaseMetaData dbmd = (DatabaseMetaData) con.getMetaData();
        // 所有的表名称
        ResultSet tableRet = dbmd.getTables(null, "%", "%", new String[] {"TABLE"});
        String tableName = "";
        while (tableRet.next()) {
          List list = new ArrayList();
          // 所有的列
          tableName = tableRet.getString("TABLE_NAME");
          ResultSet colRet = dbmd.getColumns(null, "%", tableName, "%");
          while (colRet.next()) {
            list.add(colRet.getString("COLUMN_NAME") + "_" + colRet.getString("TYPE_NAME"));
          }
          map.put(tableName, list);
        }
      } catch (SQLException e) {
        e.printStackTrace();
      } finally {
        this.closeConnection();
      }
      return map;
    }
  }
}
