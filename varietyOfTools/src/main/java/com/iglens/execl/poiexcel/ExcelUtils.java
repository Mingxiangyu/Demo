package com.iglens.execl.poiexcel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * @描述：测试excel读取
 *
 * <p>导入的jar包
 *
 * <p>poi-3.8-beta3-20110606.jar
 *
 * <p>poi-ooxml-3.8-beta3-20110606.jar
 *
 * <p>poi-examples-3.8-beta3-20110606.jar
 *
 * <p>poi-excelant-3.8-beta3-20110606.jar
 *
 * <p>poi-ooxml-schemas-3.8-beta3-20110606.jar
 *
 * <p>poi-scratchpad-3.8-beta3-20110606.jar
 *
 * <p>xmlbeans-2.3.0.jar
 *
 * <p>dom4j-1.6.1.jar
 *
 * <p>jar包官网下载地址：http://poi.apache.org/download.html
 *
 * <p>下载poi-bin-3.8-beta3-20110606.zipp @时间：2012-08-29 下午16:27:15
 */
public class ExcelUtils {

  /** 总行数 */
  private int totalRows = 0;

  /** 总列数 */
  private int totalCells = 0;

  /** 错误信息 */
  private String errorInfo;

  /** 构造方法 */
  public ExcelUtils() {}

  /** @描述：得到总行数 @时间：2012-08-29 下午16:27:15 @参数：@return @返回值：int */
  public int getTotalRows() {

    return totalRows;
  }

  /** @描述：得到总列数 @时间：2012-08-29 下午16:27:15 @参数：@return @返回值：int */
  public int getTotalCells() {
    return totalCells;
  }

  /** @描述：得到错误信息 @时间：2012-08-29 下午16:27:15 @参数：@return @返回值：String */
  public String getErrorInfo() {

    return errorInfo;
  }

  /** @描述：main测试方法 @时间：2012-08-29 下午17:12:15 @参数：@param args @参数：@throws Exception @返回值：void */
  public static void main(String[] args) throws Exception {

    ExcelUtils poi = new ExcelUtils();
    List<List<String>> read = poi.read("C:\\Users\\T480S\\Desktop\\模板数据结构.xlsx");
    //    Map<String, List<String>> map =
    // poi.readByColumn("C:\\Users\\T480S\\Desktop\\模板数据结构.xlsx");
    for (List<String> strings : read) {
      for (String string : strings) {
        //        System.out.println(string);
      }
    }
    //    Set<Map.Entry<String, List<String>>> entys = map.entrySet();
    //    for (Map.Entry<String, List<String>> li : entys) {
    //      System.out.println(li);
    //    }

    /*List<List<String>> list = poi.read("C:\\Users\\Administrator\\Desktop\\test.xls");
    if (list != null)
    {
    	for (int i = 0; i < list.size(); i++)
    	{
    		System.out.print("第" + (i) + "行");
    		List<String> cellList = list.get(i);
    		for (int j = 0; j < cellList.size(); j++)
    		{
    			// System.out.print("    第" + (j + 1) + "列值：");
    			System.out.print("    " + cellList.get(j));
    		}
    		System.out.println();
    	}
    }*/

  }

  /** @描述：验证excel文件 @时间：2012-08-29 下午16:27:15 @参数：@param filePath　文件完整路径 @参数：@return @返回值：boolean */
  public boolean validateExcel(String filePath) {

    /** 检查文件名是否为空或者是否是Excel格式的文件 */
    if (filePath == null || !(isExcel2003(filePath) || isExcel2007(filePath))) {
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

  /**
   * @描述：根据文件名读取excel文件 @时间：2012-08-29 下午16:27:15 @参数：@param filePath 文件完整路径 @参数：@return @返回值：List
   */
  public List<List<String>> read(String filePath) {
    // 数据结果
    List<List<String>> dataLst = new ArrayList<>();

    /* 调用本类提供的根据流读取的方法 */
    File file = new File(filePath);
    try (InputStream is = new FileInputStream(file); ) {

      /* 验证文件是否合法 */
      if (!validateExcel(filePath)) {
        System.out.println(errorInfo);
        return null;
      }

      /* 判断文件的类型，是2003还是2007 */
      boolean isExcel2003 = true;
      if (isExcel2007(filePath)) {
        isExcel2003 = false;
      }

      dataLst = read(is, isExcel2003);
    } catch (Exception ex) {
      ex.printStackTrace();
    }

    /** 返回最后读取的结果 */
    return dataLst;
  }

  /**
   * @描述：根据文件名读取excel文件，以列讀取 @时间：2012-08-29 下午16:27:15 @参数：@param filePath
   * 文件完整路径 @参数：@return @返回值：List
   */
  public Map<String, List<String>> readByColumn(String filePath) {
    Map<String, List<String>> dataLst = new HashMap<String, List<String>>();
    /** 调用本类提供的根据流读取的方法 */
    File file = new File(filePath);
    try (InputStream is = new FileInputStream(file); ) {

      /** 验证文件是否合法 */
      if (!validateExcel(filePath)) {

        System.out.println(errorInfo);

        return null;
      }

      /* 判断文件的类型，是2003还是2007 */
      boolean isExcel2003 = true;
      if (isExcel2007(filePath)) {
        isExcel2003 = false;
      }
      dataLst = readByColumn(is, isExcel2003);
    } catch (Exception ex) {
      ex.printStackTrace();
    }

    /* 返回最后读取的结果 */
    return dataLst;
  }

  /**
   * @描述：根据流读取Excel文件 @时间：2012-08-29 下午16:40:15 @参数：@param inputStream @参数：@param isExcel2003
   *
   * @return @返回值：List
   */
  public List<List<String>> read(InputStream inputStream, boolean isExcel2003) {
    List<List<String>> dataLst = null;
    try {
      /** 根据版本选择创建Workbook的方式 */
      Workbook wb;
      if (isExcel2003) {
        wb = new HSSFWorkbook(inputStream);
      } else {
        wb = new XSSFWorkbook(inputStream);
      }
      dataLst = read(wb);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return dataLst;
  }

  /**
   * @描述：根据流读取Excel文件，以列讀取 @时间：2012-08-29 下午16:40:15 @参数：@param inputStream @参数：@param
   * isExcel2003 @参数：@return @返回值：List
   */
  public Map<String, List<String>> readByColumn(InputStream inputStream, boolean isExcel2003) {

    Map<String, List<String>> dataLst = null;

    try {

      /** 根据版本选择创建Workbook的方式 */
      Workbook wb = null;

      if (isExcel2003) {
        wb = new HSSFWorkbook(inputStream);
      } else {
        wb = new XSSFWorkbook(inputStream);
      }
      dataLst = readByColumn(wb);

    } catch (IOException e) {

      e.printStackTrace();
    }

    return dataLst;
  }

  /**
   * @描述：读取数据，以行读取 @时间：2012-08-29 下午16:50:15 @参数：@param Workbook @参数：@return @返回值：List<List<String>>
   */
  private List<List<String>> readold(Workbook wb) {
    List<List<String>> dataLst = new ArrayList<List<String>>();
    int sheetConut = wb.getNumberOfSheets();
    /** 得到第一个shell */
    Sheet sheet = wb.getSheetAt(0);

    /** 得到Excel的行数 */
    this.totalRows = sheet.getPhysicalNumberOfRows();
    /** 得到Excel的列数 */
    if (this.totalRows >= 1 && sheet.getRow(0) != null) {
      this.totalCells = sheet.getRow(0).getLastCellNum();
    }

    /** 循环Excel的行 */
    for (int r = 0; r < this.totalRows; r++) {

      Row row = sheet.getRow(r);

      if (row == null) {

        continue;
      }

      List<String> rowLst = new ArrayList<String>();

      /** 循环Excel的列 */
      for (int c = 0; c < this.getTotalCells(); c++) {

        Cell cell = row.getCell(c);

        String cellValue = "";

        if (null != cell) {
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
        }

        rowLst.add(cellValue);
      }

      /** 保存第r行的第c列 */
      dataLst.add(rowLst);
    }

    return dataLst;
  }

  private List<List<String>> read(Workbook wb) {
    List<List<String>> dataLst = new ArrayList<>();

    // 从第几个sheet开始 TOdo
    int startSheet = 0;
    System.out.println("StartSheet: " + startSheet);
    for (int i = startSheet; i < wb.getNumberOfSheets(); i++) {
      /* 得到shell */
      Sheet sheet = wb.getSheetAt(i);

      /* 得到Excel的行数 */
      this.totalRows = sheet.getPhysicalNumberOfRows();
      System.out.println("getPhysicalNumberOfRows: " + totalRows);
      int lastRowNum = sheet.getLastRowNum();
      System.out.println("getLastRowNum: " + lastRowNum);

      /* 循环Excel的行 */
      for (int r = 0; r < this.totalRows; r++) {
        Row row = sheet.getRow(r);
        if (row == null) {
          continue;
          // 同时输出该条sql
        }
        // 拼接sql 拼接从配置文件中取 替换

        List<String> rowLst = new ArrayList<String>();
        String tableName = "";
        String filedValue = "";
        String filedTyep = "";
        String filedComment = "";

        /* 循环Excel的列 */
        short lastCellNum = row.getLastCellNum();
        System.out.println("lastCellNum: " + lastCellNum);
        int physicalNumberOfCells = row.getPhysicalNumberOfCells();
        System.out.println("physicalNumberOfCells: " + physicalNumberOfCells);

        for (int cellIndex = 0; cellIndex < lastCellNum; cellIndex++) {
          Cell cell = row.getCell(cellIndex);
          if (cell == null) {
            continue;
          }
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

          // 字段列有多少 TODO
          int field = 3;
          if (lastCellNum < field) {
            // 如果小于字段列,则认为该行为表名行
            if (lastCellNum < 2) {
              tableName = cellValue;
            } else if (cellIndex == lastCellNum - 1) {
              tableName = cellValue;
            }
          } else {
            if (cellIndex == 0) {
              filedValue = cellValue;
            } else if (cellIndex == 1) {
              filedTyep = cellValue;
            } else if (cellIndex == 2) {
              filedComment = cellValue;
            }
          }

          rowLst.add(cellValue);
        }

        System.out.println("tableName: " + tableName);
        System.out.println("filedValue: " + filedValue);
        System.out.println("filedTyep: " + filedTyep);
        System.out.println("filedComment: " + filedComment);
        System.out.println("row " + r + " 已读取完\n");
        /** 保存第r行的第c列 */
        dataLst.add(rowLst);
      }
      System.out.println("sheet " + i + " 已读取完\n");
    }

    return dataLst;
  }

  /**
   * @描述：读取数据，以列读取 @时间：2012-08-29 下午16:50:15 @参数：@param Workbook @参数：@return @返回值：List<List<String>>
   */
  private Map<String, List<String>> readByColumn(Workbook wb) {

    Map<String, List<String>> dataMap = new HashMap<String, List<String>>();

    /** 得到第一个shell */
    Sheet sheet = wb.getSheetAt(0);

    /** 得到Excel的行数 */
    this.totalRows = sheet.getPhysicalNumberOfRows();

    /** 得到Excel的列数 */
    if (this.totalRows >= 1 && sheet.getRow(0) != null) {

      this.totalCells = sheet.getRow(0).getPhysicalNumberOfCells();
    }

    for (int lie = 1; lie <= totalCells; lie++) {
      List<String> list = new ArrayList<String>();
      dataMap.put("第" + lie + "列", list);
    }

    /** 循环Excel的行 */
    for (int r = 0; r < this.totalRows; r++) {

      Row row = sheet.getRow(r);

      if (row == null) {

        continue;
      }

      List<String> rowLst = new ArrayList<String>();

      /* 循环Excel的列 */
      for (int c = 0; c < this.getTotalCells(); c++) {

        Cell cell = row.getCell(c);
        String cellValue = "";

        if (null != cell) {
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
        }

        dataMap.get("第" + (c + 1) + "列").add(cellValue);
        //				rowLst.add(cellValue);

      }
    }

    return dataMap;
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
