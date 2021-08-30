package org.demo.地理.经纬度;

import java.util.HashMap;
import java.util.Map;

public class 获取每个像素代表的经纬度 {

  /**
   * 获取每个像素代表的经纬度
   *
   * @param maxLongitude 最大经度
   * @param minLongitude 最小经度
   * @param maxLatitude 最大纬度
   * @param minLatitude 最大纬度
   * @param width 图片宽度
   * @param height 图片高度
   * @return xy值
   */
  public static Map<String, Double> newCalculate(
      double maxLongitude,
      double minLongitude,
      double maxLatitude,
      double minLatitude,
      int width,
      int height) {

    // 获取当前图片下每个像素代表的经纬度值
    double yPx = (maxLatitude - minLatitude) / height;
    double xPx = (maxLongitude - minLongitude) / width;

    Map<String, Double> xyMap = new HashMap<>(4);
    xyMap.put("xPx", xPx);
    xyMap.put("yPx", yPx);
    return xyMap;
  }
}
