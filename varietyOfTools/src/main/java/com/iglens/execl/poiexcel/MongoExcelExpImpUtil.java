package com.iglens.execl.poiexcel;

import cn.hutool.core.io.FileUtil;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class MongoExcelExpImpUtil {

  /**
   * 方法描述:将dataList数据保存到Excel中
   *
   * @param excelName 文件名
   * @param path 文件路径
   * @param headList 表头
   * @param fieldList 表头对应字段
   * @param dataList 数据
   * @author wangcy
   * @date 2019年6月26日 下午4:35:38
   */
  public static void downloadExcel(
      String excelName,
      String path,
      List<String> headList,
      List<String> fieldList,
      List<Map<String, Object>> dataList) {
    Workbook workbook = null;
    if (path.endsWith(".xls")) {
      workbook = new HSSFWorkbook();
    } else if (path.endsWith(".xlsx")) {
      workbook = new XSSFWorkbook();
    }
    try (FileOutputStream outputStream = new FileOutputStream(path); ) {
      FileUtil.mkdir(path);
      Sheet sheet = workbook.createSheet(excelName);
      // 表头
      Row row0 = sheet.createRow(0);
      for (int i = 0; i < headList.size(); i++) {
        Cell cellI = row0.createCell(i);
        cellI.setCellValue(headList.get(i));
      }
      // 数据
      if (dataList != null && dataList.size() != 0) {
        for (int i = 0; i < dataList.size(); i++) {
          Row row = sheet.createRow(i + 1);
          for (int j = 0; j < fieldList.size(); j++) {
            Cell cell = row.createCell(j);
            cell.setCellValue(castString(dataList.get(i).get(fieldList.get(j)), ""));
          }
        }
      }

      workbook.write(outputStream);
      outputStream.flush();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (workbook != null) {
        try {
          workbook.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  /**
   * 方法描述:获取Excel中的数据
   *
   * @param path Excel路径
   * @return
   * @author wangcy
   * @date 2019年6月26日 下午5:58:51
   */
  public static List<Map<String, Object>> getExcelData(String path) {
    List<Map<String, Object>> dataList = new ArrayList<>();
    Workbook workbook = null;
    try {
      // 输入文件
      FileInputStream inputStream = new FileInputStream(path);
      if (path.endsWith(".xls")) {
        workbook = new HSSFWorkbook(inputStream);
      } else if (path.endsWith(".xlsx")) {
        workbook = new XSSFWorkbook(inputStream);
      }
      List<String> fieldList = new ArrayList<String>();
      // 获取Excel文档中第一个表单
      Sheet sheet = workbook.getSheetAt(0);
      // 获取Excel第一行名称
      Row row0 = sheet.getRow(0);
      for (Cell cell : row0) {
        fieldList.add(cell.toString());
      }
      int rows = sheet.getLastRowNum() + 1;
      int cells = fieldList.size();
      for (int i = 1; i < rows; i++) {
        Row row = sheet.getRow(i);
        Map<String, Object> paraMap = new HashMap<>();
        for (int j = 0; j < cells; j++) {
          Cell cell = row.getCell(j);
          if (cell != null && !"".equals(cell.toString())) {
            paraMap.put(fieldList.get(j), cell.toString());
          }
        }
        dataList.add(paraMap);
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (workbook != null) {
        try {
          workbook.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    return dataList;
  }

  public static String castString(Object o, String defaultValue) {
    if (o == null) {
      return defaultValue;
    }
    return o.toString();
  }
}
