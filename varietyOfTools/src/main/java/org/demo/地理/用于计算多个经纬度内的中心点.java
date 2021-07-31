package org.demo.地理;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用于计算多个经纬度内的中心点
 */
public class 用于计算多个经纬度内的中心点 {

  public static void main(String[] args) {
    List<GeoCoordinate> geoCoordinateList = new ArrayList<GeoCoordinate>();

    GeoCoordinate g1 = new GeoCoordinate();//31.245893,118.846010
    g1.setLongitude(118.846010);
    g1.setLatitude(31.245893);
    geoCoordinateList.add(g1);

    GeoCoordinate g2 = new GeoCoordinate();//31.326869,119.180899
    g2.setLongitude(119.180899);
    g2.setLatitude(31.326869);
    geoCoordinateList.add(g2);

    GeoCoordinate g3 = new GeoCoordinate();//31.292315,119.426963
    g3.setLongitude(119.426963);
    g3.setLatitude(31.292315);
    geoCoordinateList.add(g3);

    GeoCoordinate g4 = new GeoCoordinate();//31.086699,119.721966
    g4.setLongitude(119.721966);
    g4.setLatitude(31.086699);
    geoCoordinateList.add(g4);

    GeoCoordinate g5 = new GeoCoordinate();//30.608496,119.480609
    g5.setLongitude(119.480609);
    g5.setLatitude(30.608496);
    geoCoordinateList.add(g5);

    GeoCoordinate g6 = new GeoCoordinate();//30.638329,119.388623
    g6.setLongitude(119.388623);
    g6.setLatitude(30.638329);
    geoCoordinateList.add(g6);

    GeoCoordinate g7 = new GeoCoordinate();//30.542828,119.296636
    g7.setLongitude(119.296636);
    g7.setLatitude(30.542828);
    geoCoordinateList.add(g7);

    GeoCoordinate g8 = new GeoCoordinate();//30.538847,119.361027
    g8.setLongitude(119.361027);
    g8.setLatitude(30.538847);
    geoCoordinateList.add(g8);

    GeoCoordinate g9 = new GeoCoordinate();//30.359519,119.439215
    g9.setLongitude(119.439215);
    g9.setLatitude(30.359519);
    geoCoordinateList.add(g9);

    GeoCoordinate g10 = new GeoCoordinate();//30.240531,119.068970
    g10.setLongitude(119.068970);
    g10.setLatitude(30.240531);
    geoCoordinateList.add(g10);

    GeoCoordinate g11 = new GeoCoordinate();//29.784193,118.848202
    g11.setLongitude(118.848202);
    g11.setLatitude(29.784193);
    geoCoordinateList.add(g11);

    GeoCoordinate g12 = new GeoCoordinate();//29.831577,118.549246
    g12.setLongitude(118.549246);
    g12.setLatitude(29.831577);
    geoCoordinateList.add(g12);

    GeoCoordinate g13 = new GeoCoordinate();//29.917260,118.303182
    g13.setLongitude(118.303182);
    g13.setLatitude(29.917260);
    geoCoordinateList.add(g13);

    GeoCoordinate g14 = new GeoCoordinate();//30.345059,117.930637
    g14.setLongitude(117.930637);
    g14.setLatitude(30.345059);
    geoCoordinateList.add(g14);

    GeoCoordinate g15 = new GeoCoordinate();//30.671136,117.907748
    g15.setLongitude(117.907748);
    g15.setLatitude(30.671136);
    geoCoordinateList.add(g15);

    GeoCoordinate g16 = new GeoCoordinate();//31.256266,118.563044
    g16.setLongitude(118.563044);
    g16.setLatitude(31.256266);
    geoCoordinateList.add(g16);

    GeoCoordinate g17 = new GeoCoordinate();//31.360917,118.742418
    g17.setLongitude(118.742418);
    g17.setLatitude(31.360917);
    geoCoordinateList.add(g17);

    Map <String,Double>  map = 用于计算多个经纬度内的中心点.getCenterPoint(geoCoordinateList);
    System.out.println(map.get("lat") +","+ map.get("lng"));//30.589848801178064,118.65923827037791

  }
  /**
   *  根据输入的地点坐标计算中心点
   * @param geoCoordinateList
   * @return
   */
  public static Map<String,Double> getCenterPoint(List<GeoCoordinate> geoCoordinateList) {
    int total = geoCoordinateList.size();
    double X = 0, Y = 0, Z = 0;
    for (GeoCoordinate g : geoCoordinateList) {
      double lat, lon, x, y, z;
      lat = g.getLatitude() * Math.PI / 180;
      lon = g.getLongitude() * Math.PI / 180;
      x = Math.cos(lat) * Math.cos(lon);
      y = Math.cos(lat) * Math.sin(lon);
      z = Math.sin(lat);
      X += x;
      Y += y;
      Z += z;
    }

    X = X / total;
    Y = Y / total;
    Z = Z / total;
    double Lon = Math.atan2(Y, X);
    double Hyp = Math.sqrt(X * X + Y * Y);
    double Lat = Math.atan2(Z, Hyp);

    Map<String,Double> map = new HashMap<String,Double>();
    map.put("lng", Lon * 180 / Math.PI);
    map.put("lat", Lat * 180 / Math.PI);
    return map;
  }

  /**
   * 根据输入的地点坐标计算中心点（适用于400km以下的场合）
   * @param geoCoordinateList
   * @return
   */
  public static Map<String,Double> getCenterPoint400(List<GeoCoordinate> geoCoordinateList) {
    // 以下为简化方法（400km以内）
    int total = geoCoordinateList.size();
    double lat = 0, lon = 0;
    for (GeoCoordinate g : geoCoordinateList) {
      lat += g.getLatitude() * Math.PI / 180;
      lon += g.getLongitude() * Math.PI / 180;
    }
    lat /= total;
    lon /= total;

    Map<String,Double> map = new HashMap<String,Double>();
    map.put("lng", lon * 180 / Math.PI);
    map.put("lat", lat * 180 / Math.PI);
    return map;
  }



}




/**
 * 封装经纬度坐标类
 * Created by fc.w on 2017/12/8.
 */
class GeoCoordinate {

  private double latitude;
  private double longitude;

  public GeoCoordinate() {
  }

  public GeoCoordinate(double latitude, double longitude) {
    this.latitude = latitude;
    this.longitude = longitude;
  }

  public double getLatitude() {
    return latitude;
  }

  public void setLatitude(double latitude) {
    this.latitude = latitude;
  }

  public double getLongitude() {
    return longitude;
  }

  public void setLongitude(double longitude) {
    this.longitude = longitude;
  }
}

