package org.demo.地理;

import java.text.DecimalFormat;

public class 计算对应方位角的经纬度 {
  /** 地球半径 * */
  private static final double R = 6371e3;
  /** 180° * */
  private static final DecimalFormat df = new DecimalFormat("0.000000");

  public static void main(String[] args) {
    String[] result = calLocationByDistanceAndLocationAndDirection(0, 119.010212, 32.147982, 1000);
    String[] result2 =
        calLocationByDistanceAndLocationAndDirection(90, 119.010212, 32.147982, 1000);
    String[] result3 = calLocationByDistanceAndLocationAndDirection(360, 119.010212, 32.147982, 0);
    System.out.print(result[0] + ",");
    System.out.println(result[1]);
    System.out.print(result2[0] + ",");
    System.out.println(result2[1]);
    System.out.print(result3[0] + ",");
    System.out.println(result3[1]);
  }

  /**
   * 根据一点的坐标与距离，以及方向，计算另外一点的位置
   *
   * @param angle 角度，从正北顺时针方向开始计算
   * @param startLong 起始点经度
   * @param startLat 起始点纬度
   * @param distance 距离，单位m
   * @return
   */
  public static String[] calLocationByDistanceAndLocationAndDirection(
      double angle, double startLong, double startLat, double distance) {
    // 将距离转换成经度的计算公式
    double δ = distance / R;
    // 转换为radian，否则结果会不正确
    angle = Math.toRadians(angle);
    startLong = Math.toRadians(startLong);
    startLat = Math.toRadians(startLat);
    double lat =
        Math.asin(
            Math.sin(startLat) * Math.cos(δ) + Math.cos(startLat) * Math.sin(δ) * Math.cos(angle));
    double lon =
        startLong
            + Math.atan2(
                Math.sin(angle) * Math.sin(δ) * Math.cos(startLat),
                Math.cos(δ) - Math.sin(startLat) * Math.sin(lat));
    // 转为正常的10进制经纬度
    lon = Math.toDegrees(lon);
    lat = Math.toDegrees(lat);
    String[] result = new String[2];
    result[0] = df.format(lon);
    result[1] = df.format(lat);
    return result;
  }

  /**
   * 四点坐标计算
   *
   * @param cx：中心点x像素坐标
   * @param cy：中心点y像素坐标
   * @param w：矩形宽
   * @param h：矩形高
   * @param angle：矩形角度
   * @return
   */
  public static void get(double cx, double cy, int w, int h, int angle) {
    double x1 = cx + Math.cos(-angle) * (-(w >> 1)) + Math.sin(-angle) * (-(h >> 1));
    double y1 = cy - Math.sin(-angle) * (-(w >> 1)) + Math.cos(-angle) * (-(h >> 1));

    double x2 = cx + Math.cos(-angle) * ((w >> 1)) + Math.sin(-angle) * (-(h >> 1));
    double y2 = cy - Math.sin(-angle) * ((w >> 1)) + Math.cos(-angle) * (-(h >> 1));

    double x3 = cx + Math.cos(-angle) * ((w >> 1)) + Math.sin(-angle) * (h >> 1);
    double y3 = cy - Math.sin(-angle) * ((w >> 1)) + Math.cos(-angle) * ((h >> 1));

    double x4 = cx + Math.cos(-angle) * (-(w >> 1)) + Math.sin(-angle) * ((h >> 1));
    double y4 = cy - Math.sin(-angle) * (-(w >> 1)) + Math.cos(-angle) * ((h >> 1));
  }
}
