package com.iglens.地理;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import org.geotools.geojson.geom.GeometryJSON;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.locationtech.jts.geom.Geometry;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;

public class 解析GeoJson计算面积 {
  public static double getAreaByGeoJson(String geojson) {
    JSONObject obj = JSONObject.parseObject(geojson);
    JSONArray arr = obj.getJSONArray("features");
    JSONObject nodeObj = JSONObject.parseObject(arr.getString(0));
    double area = 0;
    try {
      GeometryJSON gjson = new GeometryJSON(15);
      Reader reader = new StringReader(nodeObj.toString());
      Geometry geom = gjson.read(reader);
      geom = 经纬度坐标转Mactor(geom);
      if(geom != null) {
        area = geom.getArea();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return area;
  }


  private static Geometry 经纬度坐标转Mactor(Geometry geom) {
    Geometry res = null;
    try {
      CoordinateReferenceSystem crsTarget = CRS.decode("EPSG:3857");
      // 投影转换
      MathTransform transform = CRS.findMathTransform(DefaultGeographicCRS.WGS84, crsTarget);
      res = JTS.transform(geom, transform);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return res;
  }

}
