package com.iglens.csv;

import com.csvreader.CsvReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** @author xming 原文链接：https://blog.csdn.net/tanqingfu1/article/details/124632814 */
public class CSVReader读取CSV {

  public static void main(String[] args) {
    String filepath ="C:\\Users\\zhouhuilin\\Downloads\\54511099999 (2).csv";

    File csvFile = new File(filepath);
    readCsvByCsvReader(csvFile);
    readCsvByBufferedReader(csvFile.getPath());
  }

  public static Map<String, Object> readCsvByCsvReader(File file) {
    Map<String, Object> mapData = new HashMap<>();
    String fileName = file.getName();
    fileName = fileName.substring(0, fileName.lastIndexOf("."));
    mapData.put("sheetName", fileName);

    ArrayList<String> strList = new ArrayList<>();
    List<Map<String, Object>> list = new ArrayList<>();
    try {
      ArrayList<String[]> arrList = new ArrayList<String[]>();
      // 如果生产文件乱码，windows下用gbk，linux用UTF-8
      CsvReader reader = new CsvReader(file.getPath(), ',', Charset.forName("UTF-8"));
      // 读取表头
      reader.readHeaders();
      String[] headArray = reader.getHeaders(); // 获取标题
      // System.out.println(headArray);
      while (reader.readRecord()) {
        // System.out.println(Arrays.asList(reader.getValues()));
        // 按行读取，并把每一行的数据添加到list集合
        arrList.add(reader.getValues());
      }
      reader.close();
      // System.out.println("读取的行数：" + arrList.size());
      // 如果要返回 String[] 类型的 list 集合，则直接返回 arrList
      // 以下步骤是把 String[] 类型的 list 集合转化为 String 类型的 list 集合
      for (int i = 1; i < arrList.size(); i++) {
        // 组装String字符串
        // 如果不知道有多少列，则可再加一个循环
        Map<String, Object> map = new HashMap<>();
        for (int j = 0; j < arrList.get(0).length; j++) {
          map.put("" + headArray[j] + "", arrList.get(i)[j]);
        }
        // 返回的数格为拼接
        /*String ele = arrList.get(i)[0] + "," + arrList.get(i)[1] + "," + arrList.get(i)[2];
        System.out.println(ele);
        strList.add(ele);*/
        list.add(map);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    mapData.put("data", list);
    return mapData;
  }

  /**
   * BufferedReader 读取
   *
   * @param filePath
   * @return
   */
  public static ArrayList<String> readCsvByBufferedReader(String filePath) {
    File csv = new File(filePath);
    csv.setReadable(true);
    csv.setWritable(true);
    InputStreamReader isr = null;
    BufferedReader br = null;
    try {
      isr = new InputStreamReader(new FileInputStream(csv), "UTF-8");
      br = new BufferedReader(isr);
    } catch (Exception e) {
      e.printStackTrace();
    }
    String line = "";
    ArrayList<String> records = new ArrayList<>();
    try {
      while ((line = br.readLine()) != null) {
        System.out.println(line);
        records.add(line);
      }
      System.out.println("csv表格读取行数：" + records.size());
    } catch (IOException e) {
      e.printStackTrace();
    }
    System.out.println(records);
    return records;
  }
}
