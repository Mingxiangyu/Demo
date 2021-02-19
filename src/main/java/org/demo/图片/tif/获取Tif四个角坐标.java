package org.demo.图片.tif;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.gce.geotiff.GeoTiffReader;
import org.geotools.geometry.Envelope2D;

public class 获取Tif四个角坐标 {
  public static void main(String[] args) {
    Map<String, Object> imageExt = getImageExt("D:tif文件");
    System.out.println(imageExt);
  }

  /**
   * 获取tif图片坐标
   *
   * @param tifUrlPath tif文件路径
   * @return 最大最小坐标值
   */
  public static Map<String, Object> getImageExt(String tifUrlPath) {
    Map<String, Object> map = new HashMap<>(16);
    try {
      File input = new File(tifUrlPath);
      GeoTiffReader reader = new GeoTiffReader(input);
      GridCoverage2D coverage = reader.read(null);
      Envelope2D coverageEnvelope = coverage.getEnvelope2D();

      double maxX = coverageEnvelope.getMaxX();
      double minX = coverageEnvelope.getMinX();
      double maxY = coverageEnvelope.getMaxY();
      double minY = coverageEnvelope.getMinY();
      map.put("coverageMaxX", maxX);
      map.put("coverageMinX", minX);
      map.put("coverageMaxY", maxY);
      map.put("coverageMinY", minY);
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException("获取该图片经纬度信息出现异常：" + e.getLocalizedMessage() + " 上传失败！");
    }
    return map;
  }
}
