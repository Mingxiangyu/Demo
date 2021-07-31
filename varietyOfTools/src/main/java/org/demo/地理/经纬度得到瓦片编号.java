package org.demo.地理;

public class 经纬度得到瓦片编号 {
  public static void main(String[] args) {
    int zoom = 10;
    double lat = 47.968056d;
    double lon = 7.909167d;
    System.out.println("https://tile.openstreetmap.org/" + getTileNumber(lat, lon, zoom) + ".png");
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
    int xtile = (int) Math.floor((lon + 180) / 360 * (1 << zoom));
    int ytile =
        (int)
            Math.floor(
                (1
                        - Math.log(
                                Math.tan(Math.toRadians(lat)) + 1 / Math.cos(Math.toRadians(lat)))
                            / Math.PI)
                    / 2
                    * (1 << zoom));
    if (xtile < 0) xtile = 0;
    if (xtile >= (1 << zoom)) xtile = ((1 << zoom) - 1);
    if (ytile < 0) ytile = 0;
    if (ytile >= (1 << zoom)) ytile = ((1 << zoom) - 1);
    return ("" + zoom + "/" + xtile + "/" + ytile);
  }
}
