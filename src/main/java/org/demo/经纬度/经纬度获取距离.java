package org.demo.经纬度;

public class 经纬度获取距离 {

  /**
   * 经纬度获取距离，单位为米
   *
   * @param lat1 最大纬度
   * @param lng1 最大经度
   * @param lat2 最小纬度
   * @param lng2 最小经度
   * @return 米
   */
  public static double newGetDistance(double lat1, double lng1, double lat2, double lng2) {
    double radLat1 = Math.toRadians(lat1);
    double radLat2 = Math.toRadians(lat2);
    double a = Math.toRadians(lat1) - Math.toRadians(lat2);
    double b = Math.toRadians((lng1)) - Math.toRadians((lng2));
    double s =
        2
            * Math.asin(
                Math.sqrt(
                    Math.pow(Math.sin(a / 2), 2)
                        + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
    // 地球赤道半径
    double earthRadius = 6378.137;
    s = s * earthRadius;
    s = Math.round(s * 10000d) / 10000d;
    s = s * 1000;
    System.out.println("直径距离为 " + s + " 米");
    return s;
  }
}
