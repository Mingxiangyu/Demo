package com.iglens.数据库.MySql.导入导出;

import com.iglens.数据库.MySql.JDBC连接获取Mysql信息;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** 将MySQL中的数据导出到Excel表格中 */
public class MySQLExcelUtil {

  private static final Logger log = LoggerFactory.getLogger(MySQLExcelUtil.class);

  private static final String SELECT_SQL = "SELECT * FROM %s";

  public static void main(String[] args) {
    String ip = "localhost";
    String port = "3306";
    String username = "root";
    String password = "mingxy925871";
    String database = "test";
    String filePath = "C:\\Users\\T480S\\Desktop\\mysql-excel";
    //    try {
    //      MySQLExcelUtil.exportFromMySQLToExcel(ip, port, database, username, password, filePath);
    //    } catch (SQLException e) {
    //      e.printStackTrace();
    //    }

    importFromExcelToMySQL(
        ip,
        port,
        database,
        username,
        password,
        "C:\\Users\\T480S\\Desktop\\mysql-excel\\test_export.xlsx");
  }

  /**
   * 将数据库中的数据导出到excel文件中 导出文件名为: 数据库名_export.xls
   *
   * @param ip 数据库ip地址
   * @param port 数据库端口
   * @param database 数据库名
   * @param username 连接用户名
   * @param password 连接密码
   * @param filePath 导出文件保存路径 -- 不含文件名
   * @param excludeTables 不作导出的数据库表名
   * @throws SQLException
   */
  public static void exportFromMySQLToExcel(
      String ip,
      String port,
      String database,
      String username,
      String password,
      String filePath,
      String... excludeTables)
      throws SQLException {

    Connection connection = JDBC连接获取Mysql信息.getConnection(ip, port, database, username, password);
    if (connection == null) {
      log.error("获取数据库连接失败");
      System.out.println("获取数据库连接失败");
      throw new RuntimeException("数据库连接失败");
    }

    if (filePath == null) {
      log.error("文件导出路径不能为空");
      System.out.println("文件导出路径不能为空");
      throw new RuntimeException("文件导出路径不能为空");
    }

    ArrayList<String> excludes = new ArrayList<>(Arrays.asList(excludeTables));

    // 获取数据库所有表信息
    DatabaseMetaData metaData = connection.getMetaData();
    ResultSet rs = metaData.getTables(database, null, null, new String[] {"TABLE"});

    // 创建工作簿
    //    HSSFWorkbook workbook = new HSSFWorkbook();
    XSSFWorkbook workbook = new XSSFWorkbook();

    while (rs.next()) {
      String tableName = rs.getString("TABLE_NAME");
      System.out.println(String.format("开始执行：{}", tableName));
      if (!excludes.contains(tableName)) {
        // 获取表中的数据
        Map<Integer, Map<String, Object>> datas = getAllDataFromTable(tableName, connection);
        if (datas == null) break;

        // 获取字段名和类型
        Map<String, String> nameType = getDataNameAndType(tableName, connection);
        if (nameType == null) break;

        // 创建一张表
        XSSFSheet sheet = workbook.createSheet(tableName);
        //        HSSFSheet sheet = workbook.createSheet(tableName);
        // 初始化第一行 显示表中的列
        Object[] columnNames = nameType.keySet().toArray();
        XSSFRow firstRow = sheet.createRow(0);
        //        HSSFRow firstRow = sheet.createRow(0);
        for (int i = 0; i < columnNames.length; i++) {
          firstRow.createCell(i + 1).setCellValue((String) columnNames[i]);
        }

        int count = 1;
        // 向工作簿写入数据
        for (int row = 0; row < datas.size(); row++) {
          Map<String, Object> data = datas.get(row);
          XSSFRow rows = sheet.createRow(row + 1);
          //          HSSFRow rows = sheet.createRow(row + 1);
          // 设置第一列数据为数据行数
          rows.createCell(0).setCellValue(row + 1);
          for (int col = 0; col < data.size(); col++) {
            Object value = data.get((String) columnNames[col]);
            if (value == null) break;
            rows.createCell(col + 1).setCellValue(value.toString());
          }
          count++;
        }
        System.out.println(String.format("该表存在：{} 行", count));
      }
    }
    // 保存工作簿
    try {
      File file = new File(filePath);
      if (!file.exists()) file.mkdirs();
      filePath = filePath + "\\" + database + "_export.xls";
      FileOutputStream fos = new FileOutputStream(filePath);
      workbook.write(fos);
      System.out.println(String.format("文件路径为 {}", filePath));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      log.error(e.getLocalizedMessage());
    } catch (IOException e) {
      log.error(e.getMessage());
      e.printStackTrace();
    }
  }

  public static void importFromExcelToMySQL(
      String ip, String port, String database, String username, String password, String xlsFile) {
    Connection conn = JDBC连接获取Mysql信息.getConnection(ip, port, database, username, password);
    if (conn == null) {
      log.error("连接数据库失败");
      throw new RuntimeException("连接数据库失败");
    }

    if (!(xlsFile.endsWith(".xls") || xlsFile.endsWith(".xlsx"))) {
      log.error("文件格式错误，应为.xls 或 .xlsx，你的: " + xlsFile);
      throw new RuntimeException("文件格式错误，应为.xls 或 .xlsx，你的: " + xlsFile);
    }

    try {
      InputStream in = new FileInputStream(xlsFile);
      XSSFWorkbook workbook = new XSSFWorkbook(in);
      //      HSSFWorkbook workbook = new HSSFWorkbook(in);

      // 获取工作簿中表的数量
      int sheetNum = workbook.getNumberOfSheets();

      // 遍历每一张表
      for (int i = 0; i < sheetNum; i++) {
        // 获取当前操作的表
        XSSFSheet sheet = workbook.getSheetAt(i);
        //        HSSFSheet sheet = workbook.getSheetAt(i);
        // 获取当前操作表的名称 -- 数据库表名
        String sheetName = sheet.getSheetName();
        // 获取当前操作表的数据行数
        int rowNum = sheet.getLastRowNum();
        // 获取第一行 -- 表中的列名
        XSSFRow firstRow = sheet.getRow(0);
        //        HSSFRow firstRow = sheet.getRow(0);
        // 获取每一行的列数
        int colNum = firstRow.getLastCellNum();

        // 获取表中列名串 id,name,age...
        StringBuffer columnNames = new StringBuffer();
        for (int j = 1; j < colNum; j++) {
          columnNames.append(firstRow.getCell(j).getStringCellValue());
          columnNames.append(",");
        }
        columnNames.deleteCharAt(columnNames.length() - 1);

        // 遍历除第一行以外的其它行 -- 数据
        for (int j = 1; j <= rowNum; j++) {
          // 获取当前操作的行
          XSSFRow currRow = sheet.getRow(j);
          //          HSSFRow currRow = sheet.getRow(j);
          // 遍历当前操作行的每一列
          StringBuilder values = new StringBuilder();
          for (int col = 1; col < colNum; col++) {
            XSSFCell cell = currRow.getCell(col);
            //            HSSFCell cell = currRow.getCell(col);
            if (cell == null) {
              values.append("''");
            } else {
              cell.setCellType(CellType.STRING);
              values.append("'").append(cell.getStringCellValue()).append("'");
            }
            values.append(",");
          }
          values.deleteCharAt(values.length() - 1);
          String sql =
              "INSERT INTO "
                  + sheetName
                  + " ("
                  + columnNames.toString()
                  + ") VALUES ("
                  + values.toString()
                  + ")";

          if (isTableExist(conn, sheetName)) {
            // 数据库中存在表
            PreparedStatement statement = null;
            try {
              statement = conn.prepareStatement(sql);
              statement.execute();
            } catch (SQLException e) {
              e.printStackTrace();
              log.error("执行SQL语句出错: " + sql);
              log.error(e.getMessage());
            } finally {
              JDBC连接获取Mysql信息.close(statement);
            }
          } else {
            log.error("数据库中不存在表: " + sheetName + "，请检查数据库以及xls文件中的sheet名称是否和数据库中表名一致");
            throw new RuntimeException(
                "数据库中不存在表: " + sheetName + "，请检查数据库以及xls文件中的sheet名称是否和数据库中表名一致");
          }
        }
      }
    } catch (FileNotFoundException e) {
      log.error("找不到目标文件: " + xlsFile);
      log.error(e.getMessage());
    } catch (IOException e) {
      e.printStackTrace();
      log.error("加载Excel文件失败");
      log.error(e.getMessage());
    }
  }

  private static Map<Integer, Map<String, Object>> getAllDataFromTable(
      String tableName, Connection conn) {
    LinkedHashMap<Integer, Map<String, Object>> res = null;
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
      JDBC连接获取Mysql信息.close(statement);
    }

    return res;
  }

  private static Map<String, String> getDataNameAndType(String tableName, Connection conn) {
    LinkedHashMap<String, String> res = null;
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
      JDBC连接获取Mysql信息.close(statement);
    }

    return res;
  }

  private static boolean isTableExist(Connection conn, String tableName) {
    try {
      ResultSet rs = conn.getMetaData().getTables(null, null, tableName, null);
      return rs.next();
    } catch (SQLException e) {
      e.printStackTrace();
      log.error("获取数据库metaData失败");
      log.error(e.getMessage());
      return false;
    }
  }
}
