package com.iglens.nc;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.io.FileWriter;
import java.io.IOException;
import org.junit.Test;
import ucar.ma2.Array;
import ucar.ma2.Index;
import ucar.nc2.NetcdfFile;
import ucar.nc2.Variable;

/**
 * https://www.cnblogs.com/fangts/p/17654086.html
 *
 * <p>https://blog.csdn.net/python113/article/details/125410208
 */
public class NetCDF2JSON {

  String path =
      // "C:\\Users\\zhouhuilin\\Desktop\\testNc\\modis_遥感_&_&_19_20230530175711_&#CLDPROP_D3_MODIS_Aqua.A2010001.011.2021147232558.nc";
      // "C:\\Users\\zhouhuilin\\Desktop\\modis_遥感_&_&_19_20230601135627_&#MOD14.A2010001.0420.061.2021148110852.hdf";
      "E:\\WorkSpace\\J2WorkSpace\\daoda-zhongbao-meta\\target\\modis_遥感_&_&_19_20231016040645_&#CLDPROP_D3_MODIS_Aqua.A2023235.011.2023254033051.nc";

  @Test
  public void test() {
    cut2Json(path, 0, 70, 70, 150);
  }

  public void cut2Json(String ncPath, double minLat, double maxLat, double minLon, double maxLon) {
    try {
      // 打开 NetCDF 文件
      NetcdfFile inputFile = NetcdfFile.open(ncPath);
      // 根据代码，将这个二进制文件里面的内容读取出来，我们看看使用代码读取到的东西是什么。把读取到的东西进行控制台输出，和读取TXT文件一样，
      System.out.println(inputFile);

      // 获取经纬度变量
      Variable latitude = inputFile.findVariable("latitude");
      Array latArray = latitude.read();
      Variable longitude = inputFile.findVariable("longitude");
      Array lonArray = longitude.read();
      Array dataArray = inputFile.findVariable("V").read();

      // 获取经纬度索引范围
      int minLatIndex = findIndex(latArray, minLat);
      int maxLatIndex = findIndex(latArray, maxLat);
      int minLonIndex = findIndex(lonArray, minLon);
      int maxLonIndex = findIndex(lonArray, maxLon);

      // 计算 dx 和 dy
      double dx = (maxLat - minLat) / (minLatIndex - maxLatIndex);
      double dy = (maxLon - minLon) / (maxLonIndex - minLonIndex);

      // 创建 Index 对象
      Index index = dataArray.getIndex();
      JSONArray jsonArray = new JSONArray();
      for (int i = maxLatIndex; i <= minLatIndex; i++) {
        JSONArray rowArray = new JSONArray();
        for (int j = minLonIndex; j <= maxLonIndex; j++) {
          rowArray.add(dataArray.getDouble(index.set(0, i, j)));
        }
        jsonArray.add(rowArray);
      }

      JSONObject jsonObject = new JSONObject(true);
      jsonObject.put("minLat", minLat);
      jsonObject.put("maxLat", maxLat);
      jsonObject.put("minLon", minLon);
      jsonObject.put("maxLon", maxLon);
      jsonObject.put("dx", dx);
      jsonObject.put("dy", dy);
      jsonObject.put("data", jsonArray);

      // 将 JSON 数组写入到文件
      try (FileWriter fileWriter = new FileWriter("aaa.json")) {
        fileWriter.write(jsonObject.toJSONString());
      } catch (IOException e) {
        e.printStackTrace();
      }

      // 关闭文件
      inputFile.close();

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // 查找最接近的索引
  public int findIndex(Array array, double target) {
    double[] values = (double[]) array.get1DJavaArray(double.class);
    int index = 0;
    double minDifference = Double.MAX_VALUE;

    for (int i = 0; i < values.length; i++) {
      double difference = Math.abs(values[i] - target);
      if (difference < minDifference) {
        minDifference = difference;
        index = i;
      }
    }
    return index;
  }
}
