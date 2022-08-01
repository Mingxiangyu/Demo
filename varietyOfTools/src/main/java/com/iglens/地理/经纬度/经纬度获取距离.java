package com.iglens.地理.经纬度;

public class 经纬度获取距离 {

  /**
   * 地球半径,单位 km
   * 常用值：
   * 极半径，从地球中心至南极或北极的距离， 相当于6356.7523km；
   * 赤道半径，从地球中心到赤道的距离，大约6378.137km；
   * 平均半径，6371.393km，表示地球中心到地球表面所有各点距离的平均值；
   * RE，地球半径，有时被使用作为距离单位, 特别是在天文学和地质学中常用，大概距离是6370.856km；
   */
  private static final double EARTH_RADIUS = 6378.137;


  /**
   * 根据经纬度，计算两点间的距离
   *
   * @param longitude1 第一个点的经度
   * @param latitude1  第一个点的纬度
   * @param longitude2 第二个点的经度
   * @param latitude2  第二个点的纬度
   * @return 返回距离 单位千米
   */
  public static double getDistance(double longitude1, double latitude1, double longitude2, double latitude2) {
    // 纬度
    double lat1 = Math.toRadians(latitude1);
    double lat2 = Math.toRadians(latitude2);
    // 经度
    double lng1 = Math.toRadians(longitude1);
    double lng2 = Math.toRadians(longitude2);
    // 纬度之差
    double a = lat1 - lat2;
    // 经度之差
    double b = lng1 - lng2;
    /*
    Math.pow(x,y)      //这个函数是求x的y次方
    Math.toRadians     //将一个角度测量的角度转换成以弧度表示的近似角度
    Math.sin           //正弦函数
    Math.cos           //余弦函数
    Math.sqrt          //求平方根函数
    Math.asin          //反正弦函数
     */
    // 计算两点距离的公式
    double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) +
        Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(b / 2), 2)));
    // 弧长乘地球半径,
    s =  s * EARTH_RADIUS;

    /*
      如果想返回米，可以修改地球半径的单位从千米到米
      并且由于该结果是double类型的，所以还可以借助Math.round方法进行四舍五入为long类型，然后精确到米：
     */
    // 返回类型: long，单位: 米
    // return Math.round(s * 10000) / 10000;
    //返回单位: 千米
    return s;
  }

  /**
   * 由于三角函数中特定的关联关系，Haversine公式的最终实现方式可以有多种，比如借助转角度的函数atan2：
   * @param longitude1 第一个点的经度
   * @param latitude1  第一个点的纬度
   * @param longitude2 第二个点的经度
   * @param latitude2  第二个点的纬度
   * @return
   */
  public static double getDistance2(double longitude1, double latitude1,
      double longitude2, double latitude2) {

    double latDistance = Math.toRadians(longitude1 - longitude2);
    double lngDistance = Math.toRadians(latitude1 - latitude2);

    double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
        + Math.cos(Math.toRadians(longitude1)) * Math.cos(Math.toRadians(longitude2))
        * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);

    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    return c * EARTH_RADIUS;
  }


  public static void main(String[] args) {
    double d = getDistance(116.308479, 39.983171, 116.353454, 39.996059);
    System.out.println(d);
  }
}
