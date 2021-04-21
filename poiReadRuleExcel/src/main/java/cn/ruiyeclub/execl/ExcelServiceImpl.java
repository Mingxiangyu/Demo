package cn.ruiyeclub.execl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ExcelServiceImpl {
  private String errorInfo;

  private static final Logger log = LoggerFactory.getLogger(ExcelServiceImpl.class.getName());

  /**
   * @描述：根据文件名读取excel文件 @时间：2012-08-29 下午16:27:15 @参数：@param filePath 文件完整路径 @参数：@return @返回值：List
   *
   * @return
   * @param startSheet
   * @param cellLastIndex
   * @param filePath
   * @param row
   * @param cell
   * @param tableNameSql
   * @param filedSql
   * @param endSql
   * @param tableNameFlag
   * @param commentTableSql
   * @param commentFiledSql
   */
  public List<List<String>> read(
      int startSheet,
      int cellLastIndex,
      String filePath,
      boolean row,
      boolean cell,
      String tableNameSql,
      String filedSql,
      String endSql,
      boolean tableNameFlag,
      String commentTableSql,
      String commentFiledSql) {
    // 数据结果
    List<List<String>> dataLst = new ArrayList<>();
    log.info("filePath: " + filePath);
    /* 调用本类提供的根据流读取的方法 */
    File file = new File(filePath);
    try (InputStream is = new FileInputStream(file); ) {

      /* 验证文件是否合法 */
      if (!validateExcel(filePath)) {
        log.info(errorInfo);
        return null;
      }

      /* 判断文件的类型，是2003还是2007 */
      boolean isExcel2003 = true;
      if (WDWUtil.isExcel2007(filePath)) {
        isExcel2003 = false;
      }

      dataLst =
          read(
              is,
              isExcel2003,
              startSheet,
              cellLastIndex,
              row,
              cell,
              tableNameSql,
              filedSql,
              endSql,
              tableNameFlag,
              commentTableSql,
              commentFiledSql);
    } catch (Exception ex) {
      ex.printStackTrace();
    }

    /** 返回最后读取的结果 */
    return dataLst;
  }

  /**
   * @描述：根据流读取Excel文件 @时间：2012-08-29 下午16:40:15 @参数：@param inputStream @参数：@param isExcel2003
   *
   * @return @返回值：List
   */
  public List<List<String>> read(
      InputStream inputStream,
      boolean isExcel2003,
      int startSheet,
      int cellLastIndex,
      boolean row,
      boolean cell,
      String tableNameSql,
      String filedSql,
      String endSql,
      boolean tableNameFlag,
      String commentTableSql,
      String commentFiledSql) {
    List<List<String>> dataLst = new ArrayList<>();
    try {
      /** 根据版本选择创建Workbook的方式 */
      Workbook wb;
      if (isExcel2003) {
        wb = new HSSFWorkbook(inputStream);
      } else {
        wb = new XSSFWorkbook(inputStream);
      }
      dataLst =
          read(
              wb,
              startSheet,
              cellLastIndex,
              row,
              cell,
              tableNameSql,
              filedSql,
              endSql,
              tableNameFlag,
              commentTableSql,
              commentFiledSql);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return dataLst;
  }

  private List<List<String>> read(
      Workbook wb,
      int startSheet,
      int cellLastIndex,
      boolean rowFlag,
      boolean cellFlag,
      String tableNameSql,
      String inputFiledSql,
      String endSql,
      boolean tableNameFlag,
      String commentTableSql,
      String commentFiledSql) {
    log.info("cellLastIndex: " + cellLastIndex);
    log.info("StartSheet: " + startSheet);
    log.info("tableNameSql: " + tableNameSql);
    log.info("filedSql: " + inputFiledSql);
    log.info("endSql: " + endSql);
    log.info("tableNameFlag: " + tableNameFlag);
    log.info("commentTableSql: " + commentTableSql);
    log.info("commentFiledSql: " + commentFiledSql);
    log.info("======================================\n");

    String empty = "\n";
    List<List<String>> sqlList = new ArrayList<>();
    for (int i = startSheet; i < wb.getNumberOfSheets(); i++) {
      List<String> sql = new LinkedList<>();
      /* 得到shell */
      Sheet sheet = wb.getSheetAt(i);
      sql.add("sheet名称为:" + sheet.getSheetName());
      sql.add(empty);

      /* 得到Excel的行数 */
      int physicalNumberOfRows = sheet.getPhysicalNumberOfRows();
      log.info("getPhysicalNumberOfRows: " + physicalNumberOfRows);
      int lastRowNum = sheet.getLastRowNum();
      log.info("getLastRowNum: " + lastRowNum);
      int rowNum;
      if (rowFlag) {
        rowNum = physicalNumberOfRows;
      } else {
        rowNum = lastRowNum;
      }
      log.info("最后确认rowNum为: " + rowNum);

      StringBuilder sqlBuilder = new StringBuilder();
      List<String> commentSqlList = new ArrayList<>();
      /* 循环Excel的行 */
      String tableName = "";
      String tableChineseName = "";
      for (int r = 0; r <= rowNum; r++) {
        Row row = sheet.getRow(r);
        // TODO 判断行为空!需要修改不能简单判断为null
        if (WDWUtil.isEmptyRow(row)) {
          log.info("该行为空,行数为: " + (r + 1));
          // 同时将该条sqlBuilder转换为字符串,然后将占位符替换为正确字段
          String string = sqlBuilder.toString();
          string = StringUtils.substringBeforeLast(string, ",");
          string = string + endSql;
          log.info("本次生成: " + string);
          log.info("本次生成注释为: \n"+StringUtils.join(commentSqlList, "\n")+"\n");
          sql.add(string);
          sql.addAll(commentSqlList);
          sql.add(empty);

          commentSqlList = new ArrayList<>();
          sqlBuilder = new StringBuilder();
          tableName = "";
          tableChineseName = "";
          continue;
        }
        // 拼接sql 拼接从配置文件中取 替换
        String filedValue = "";
        String filedType = "";
        String filedComment = "";

        short lastCellNum = row.getLastCellNum();
        log.info("lastCellNum: " + lastCellNum);
        int physicalNumberOfCells = row.getPhysicalNumberOfCells();
        log.info("physicalNumberOfCells: " + physicalNumberOfCells);

        if (lastCellNum != physicalNumberOfCells) {
          log.info("行数为: " + (r + 1) + " 缺失字段,Sql文件未添加该行! 请注意!!!!!!!!!!!!!!!!!!");
        }

        int cellNum;
        if (cellFlag) {
          cellNum = lastCellNum;
        } else {
          cellNum = physicalNumberOfCells;
        }
        log.info("最后确认的cellNum: " + cellNum);
        String filedSql = inputFiledSql;
        String tableCommentSql = commentTableSql;
        String filedCommentSql = commentFiledSql;

        /* 循环Excel的列 */
        for (int cellIndex = 0; cellIndex < cellNum; cellIndex++) {
          Cell cell = row.getCell(cellIndex);
          if (cell == null) {
            continue;
          }
          String cellValue = getCellValue(cell);

          // 如果小于字段列,则认为该行为表名行 TODO 判断cellNum等于两个还是三个需要获取真实的,因为可能有的单元格没有值只有格式也占一个单元格
          if (cellNum < cellLastIndex) {
            if (cellNum < 2) {
              tableName = cellValue;
            } else {
              if (tableNameFlag) {
                if (cellIndex == 0) {
                  tableChineseName = cellValue;
                  sql.add(tableChineseName);
                }
                if (cellIndex == cellNum - 1) {
                  tableName = cellValue;
                }
              } else {
                if (cellIndex == 0) {
                  tableName = cellValue;
                }
                if (cellIndex == cellNum - 1) {
                  tableChineseName = cellValue;
                  sql.add(tableChineseName);
                }
              }
            }
            if (!StringUtils.isEmpty(tableName)) {
              String replaceAll = tableNameSql.replace(":!{tableName}", tableName);
              sqlBuilder.append(replaceAll);
              if (StringUtils.isNotEmpty(tableChineseName)) {
                tableCommentSql = tableCommentSql.replace(":!{tableName}", tableName);
                tableCommentSql = tableCommentSql.replace(":!{tableComment}", tableChineseName);
                commentSqlList.add(tableCommentSql);
              }
            }
          } else {
            if (cellIndex == 0) {
              filedValue = cellValue;
              filedSql = filedSql.replace(":!{filedValue}", filedValue);
            } else if (cellIndex == 1) {
              filedType = cellValue;
              filedSql = filedSql.replace(":!{filedType}", filedType);
            } else if (cellIndex == 2) {
              filedComment = cellValue;
              filedCommentSql = filedCommentSql.replace(":!{tableName}", tableName);
              filedCommentSql = filedCommentSql.replace(":!{filedValue}", filedValue);
              filedCommentSql = filedCommentSql.replace(":!{filedComment}", filedComment);
              commentSqlList.add(filedCommentSql);
            }
          }
        }
        if (!filedSql.contains(":!{filedValue}") && !filedSql.contains(":!{filedType}")) {
          sqlBuilder.append(filedSql);
        }

        log.info("sheet: "+sheet.getSheetName()+" row: " + (r + 1) + " 已读取完\n");
      }
      String string = sqlBuilder.toString();
      if (!StringUtils.isEmpty(string)) {
        string = StringUtils.substringBeforeLast(string, ",");
        string = string + endSql ;
        sql.add(string);
        sql.addAll(commentSqlList);
        sql.add(empty);
        log.info("最后一次: " + string);
        log.info("最后一次生成注释为: \n"+StringUtils.join(commentSqlList, "\n")+"\n");
      }
      log.info("sheet " + i + " 已读取完\n");
      sqlList.add(sql);
    }
    return sqlList;
  }

  private String getCellValue(Cell cell) {
    String cellValue;
    // 以下是判断数据的类型
    switch (cell.getCellType()) {
      case HSSFCell.CELL_TYPE_NUMERIC: // 数字
        cellValue = cell.getNumericCellValue() + "";
        break;

      case HSSFCell.CELL_TYPE_STRING: // 字符串
        cellValue = cell.getStringCellValue();
        break;

      case HSSFCell.CELL_TYPE_BOOLEAN: // Boolean
        cellValue = cell.getBooleanCellValue() + "";
        break;

      case HSSFCell.CELL_TYPE_FORMULA: // 公式
        cellValue = cell.getCellFormula() + "";
        break;

      case HSSFCell.CELL_TYPE_BLANK: // 空值
        cellValue = "";
        break;

      case HSSFCell.CELL_TYPE_ERROR: // 故障
        cellValue = "非法字符";
        break;

      default:
        cellValue = "未知类型";
        break;
    }
    return cellValue;
  }

  public static void main(String[] args) {
    ExcelServiceImpl service = new ExcelServiceImpl();
    //    service.read();
    //    writeFile(sql);
  }

  public static void writeFile(List<List<String>> sqlList, String outFilePath) {
    for (int i = 0; i < sqlList.size(); i++) {
      String fileName = outFilePath + "out" + i + ".txt";
      File fout = new File(fileName);
      List<String> sql = sqlList.get(i);
      try {
        if (fout.exists()) {
          fout.createNewFile();
        }

        FileOutputStream fos = new FileOutputStream(fout);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
        for (String s : sql) {
          bw.write(s);
          bw.newLine();
        }
        bw.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /** @描述：验证excel文件 @时间：2012-08-29 下午16:27:15 @参数：@param filePath　文件完整路径 @参数：@return @返回值：boolean */
  public boolean validateExcel(String filePath) {

    /** 检查文件名是否为空或者是否是Excel格式的文件 */
    if (filePath == null || !(WDWUtil.isExcel2003(filePath) || WDWUtil.isExcel2007(filePath))) {

      errorInfo = "文件名不是excel格式";

      return false;
    }

    /** 检查文件是否存在 */
    File file = new File(filePath);

    if (file == null || !file.exists()) {

      errorInfo = "文件不存在";

      return false;
    }

    return true;
  }
}

/** @描述：工具类 @时间：2012-08-29 下午16:30:40 */
class WDWUtil {

  /**
   * 判断是否为空行
   *
   * @param row 如果为空行返回true
   * @return 是否为空行
   */
  public static boolean isRowEmpty(Row row) {
    if (row == null) {
      return true;
    }
    for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
      Cell cell = row.getCell(c);
      if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK) {
        return false;
      }
    }
    return true;
  }

  public static boolean isEmptyRow(Row row) {
    if (row == null || row.toString().isEmpty()) {
      return true;
    } else {
      boolean isEmpty = true;
      // 从第一个不为空的列开始 到 最后一个 不为空的列(有格式就算一列 )
      for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
        Cell cell = row.getCell(c);
        if (cell != null && cell.getCellTypeEnum() != CellType.BLANK) {
          isEmpty = false;
          break;
        }
      }
      return isEmpty;
    }
  }

  /**
   * @描述：是否是2003的excel，返回true是2003 @时间：2012-08-29 下午16:29:11 @参数：@param
   * filePath　文件完整路径 @参数：@return @返回值：boolean
   */
  public static boolean isExcel2003(String filePath) {

    return filePath.matches("^.+\\.(?i)(xls)$");
  }

  /**
   * @描述：是否是2007的excel，返回true是2007 @时间：2012-08-29 下午16:28:20 @参数：@param
   * filePath　文件完整路径 @参数：@return @返回值：boolean
   */
  public static boolean isExcel2007(String filePath) {

    return filePath.matches("^.+\\.(?i)(xlsx)$");
  }
}
