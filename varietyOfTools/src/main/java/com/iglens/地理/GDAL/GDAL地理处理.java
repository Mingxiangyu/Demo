package com.iglens.地理.GDAL;

import java.util.Vector;
import org.gdal.ogr.Layer;

public class GDAL地理处理 {
  /**
   * 两个图层之间的地理处理操作
   *
   * @param inputLayer：输入图层
   * @param queryLayer：求交图层
   * @param resultLayer：返回结果图层
   * @param spatialFilter：地理处理操作
   */
  public static void spatialQuery(
      Layer inputLayer, Layer queryLayer, Layer resultLayer, int spatialFilter) {

    Vector v = new Vector(4);
    v.add("SKIP_FAILURES=YES"); // 跳过处理过程中出现的错误
    v.add("PROMOTE_TO_MULTI=NO"); // Polygon不转为MultiPolygon，如果设为YES则会
    v.add("INPUT_PREFIX=1_"); // 输入图层在属性表中的字段前缀
    v.add("METHOD_PREFIX=2_"); // 求交图层的字段前缀
    switch (spatialFilter) {
        // 相交
      case 0:
        inputLayer.Intersection(queryLayer, resultLayer, v, null);
        break;
        // 合并
      case 1:
        inputLayer.Union(queryLayer, resultLayer, v, null);
    }
  }
}
