package com.iglens;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.iglens.txt.获取TXT文本编码格式;
import org.apache.commons.lang3.math.NumberUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class 钱磊项目中风场转向量 {

  public static void main(String[] args) {
    getWindField();
  }

  private static JSONArray getWindField() {
    String windDirectionPath =
        "G://软件备份//Project//J2//国遥//气象数据抓取//样例数据//windDirection_2022071623.txt";
    String windDirectionString = readToString(windDirectionPath);
    String[] windDirectionSplit = windDirectionString.split("\\s+");
    List<String> windDirectionList =
        new ArrayList<>(Arrays.asList(windDirectionSplit)).subList(12, windDirectionSplit.length);
    // 文件多少列
    double uNcols = NumberUtils.toDouble(windDirectionSplit[1]);
    // 文件多少行
    double u_nrows = NumberUtils.toDouble(windDirectionSplit[3]);
    // 起始经度点
    double u_xllcorner = NumberUtils.toDouble(windDirectionSplit[5]);
    // 起始纬度点
    double u_yllcorner = NumberUtils.toDouble(windDirectionSplit[7]);
    // 步进值
    double u_cellsize = NumberUtils.toDouble(windDirectionSplit[9]);
    JSONObject uHeader = new JSONObject();
    uHeader.put("parameterCategory", 2);
    uHeader.put("parameterCategoryName", "Momentum");
    uHeader.put("parameterNumber", 2);
    uHeader.put("parameterNumberName", "U-component_of_wind");
    uHeader.put("numberPoints", uNcols * u_nrows);
    uHeader.put("dx", u_cellsize);
    uHeader.put("dy", u_cellsize);
    uHeader.put("nx", uNcols);
    uHeader.put("ny", u_nrows);
    uHeader.put("lo1", u_xllcorner);
    uHeader.put("la1", u_yllcorner + (u_cellsize * u_nrows));
    uHeader.put("lo2", u_xllcorner + (u_cellsize * uNcols));
    uHeader.put("la2", u_yllcorner);

    String windSpeedPath = "G://软件备份//Project//J2//国遥//气象数据抓取//样例数据//windSpeed_2022071623.txt";
    String windSpeedString = readToString(windSpeedPath);
    String[] windSpeedSplit = windSpeedString.split("\\s+");
    List<String> windSpeedList =
        new ArrayList<>(Arrays.asList(windSpeedSplit)).subList(12, windSpeedSplit.length);
    // 文件多少列
    double ncols = NumberUtils.toDouble(windSpeedSplit[1]);
    // 文件多少行
    double nrows = NumberUtils.toDouble(windSpeedSplit[3]);
    // 起始经度点
    double xllcorner = NumberUtils.toDouble(windSpeedSplit[5]);
    // 起始纬度点
    double yllcorner = NumberUtils.toDouble(windSpeedSplit[7]);
    // 步进值
    double cellsize = NumberUtils.toDouble(windSpeedSplit[9]);
    JSONObject vHeader = new JSONObject();
    vHeader.put("parameterCategory", 2);
    vHeader.put("parameterCategoryName", "Momentum");
    vHeader.put("parameterNumber", 2);
    vHeader.put("parameterNumberName", "V-component_of_wind");
    vHeader.put("numberPoints", ncols * nrows);
    vHeader.put("dx", cellsize);
    vHeader.put("dy", cellsize);
    vHeader.put("nx", ncols);
    vHeader.put("ny", nrows);
    vHeader.put("lo1", xllcorner);
    vHeader.put("la1", yllcorner + (cellsize * nrows));
    vHeader.put("lo2", xllcorner + (cellsize * ncols));
    vHeader.put("la2", yllcorner);

    List<Double> uList = new ArrayList<>();
    List<Double> vList = new ArrayList<>();
    // 计算uv向量
    for (int index = 0; index < windSpeedList.size(); index++) {
      String str = windSpeedList.get(index);
      double speed = NumberUtils.toDouble(str);
      String str1 = windDirectionList.get(index);
      double direction = NumberUtils.toDouble(str1);
      double u = Math.abs(Math.cos(270 - direction) * speed);
      double v = Math.abs(Math.sin(270 - direction) * speed);
      if ((direction > 0 && direction < 90) || (direction > 270)) {
        u = -u;
      }
      if ((direction > 0 && direction < 180)) {
        v = -v;
      }
      uList.add(u);
      vList.add(v);
    }
    JSONArray uData = JSONArray.parseArray(JSONObject.toJSONString(uList));
    JSONArray vData = JSONArray.parseArray(JSONObject.toJSONString(vList));

    JSONObject uRes = new JSONObject();
    uRes.put("header", uHeader);
    uRes.put("data", uData);

    JSONObject vRes = new JSONObject();
    vRes.put("header", vHeader);
    vRes.put("data", vData);

    JSONArray res = new JSONArray();
    res.add(uRes);
    res.add(vRes);
    return res;
  }

  public static JSONObject getJsonData(String type, String date) {
    String rootPath = "";
    String year = ""; // date.slice(0,4)
    String month = ""; // date.slice(4,6)
    String day = ""; // date.slice(6,8)
    // String year_path = rootPath + File.separator + year;
    // String month_path = year_path +  File.separator  + month;
    // String day_path = month_path +  File.separator  + day;

    String day_path = "G://软件备份//Project//J2//国遥//气象数据抓取//样例数据//windDirection_2022071623.txt";
    String dataString = readToString(day_path);
    String[] dataSplit = dataString.split("\\s+");

    // 文件多少列
    double ncols = NumberUtils.toDouble(dataSplit[1]);
    // 文件多少行
    double nrows = NumberUtils.toDouble(dataSplit[3]);
    // 起始经度点
    double xllcorner = NumberUtils.toDouble(dataSplit[5]);
    // 起始纬度点
    double yllcorner = NumberUtils.toDouble(dataSplit[7]);
    // 步进值
    double cellsize = NumberUtils.toDouble(dataSplit[9]);
    JSONObject header = new JSONObject();
    header.put("parameterCategory", 2);
    header.put("parameterCategoryName", "Momentum");
    header.put("parameterNumber", 2);
    header.put("parameterNumberName", "U-component_of_wind");
    header.put("numberPoints", ncols * nrows);
    header.put("dx", cellsize);
    header.put("dy", cellsize);
    header.put("nx", ncols);
    header.put("ny", nrows);
    header.put("lo1", xllcorner);
    header.put("la1", yllcorner + (cellsize * nrows));
    header.put("lo2", xllcorner + (cellsize * ncols));
    header.put("la2", yllcorner);

    List<String> dataList = new ArrayList<>(Arrays.asList(dataSplit)).subList(12, dataSplit.length);
    JSONArray data = JSONArray.parseArray(JSONObject.toJSONString(dataList));

    JSONObject res = new JSONObject();
    res.put("header", header);
    res.put("data", data);
    return res;
  }

  public static String readToString(String filePath) {
    File file = new File(filePath);
    String encoding = 获取TXT文本编码格式.getFilecharset(file);
    long filelength = file.length();
    byte[] filecontent = new byte[(int) filelength];
    try {
      FileInputStream in = new FileInputStream(file);
      in.read(filecontent);
      in.close();
      return new String(filecontent, encoding);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }
}
