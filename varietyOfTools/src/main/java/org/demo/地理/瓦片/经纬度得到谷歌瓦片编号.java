package org.demo.地理.瓦片;

public class 经纬度得到谷歌瓦片编号 {
  public static void main(String[] args) {
    int zoom = 17;
    double minLatD = 39.02734816074371;
    double maxLatD = 39.05447065830231;
    double minlngD = 117.71792650222778;
    double maxlngD = 117.75427579879761;
    System.out.println("https://tile.openstreetmap.org/" + getTileNumber(minLatD, minlngD, zoom) + ".png");
    System.out.println("https://tile.openstreetmap.org/" + getTileNumber(maxLatD, maxlngD, zoom) + ".png");
  }

  /**
   * 获取瓦片标号
   *
   * @param lat 纬度
   * @param lon 经度
   * @param zoom 层级
   * @return
   */
  public static String getTileNumber(final double lat, final double lon, final int zoom) {
    //    Math.floor()  “向下取整” ，即小数部分直接舍去
    int col = (int) Math.floor((lon + 180) / 360 * (1 << zoom));
    int row =
        (int)
            Math.floor(
                (1
                        - Math.log(
                                Math.tan(Math.toRadians(lat)) + 1 / Math.cos(Math.toRadians(lat)))
                            / Math.PI)
                    / 2
                    * (1 << zoom));
    if (col < 0) {
      col = 0;
    }
    if (col >= (1 << zoom)) {
      col = ((1 << zoom) - 1);
    }
    if (row < 0) {
      row = 0;
    }
    if (row >= (1 << zoom)) {
      row = ((1 << zoom) - 1);
    }
    return ("" + zoom + "/" + col + "/" + row);
  }
}
