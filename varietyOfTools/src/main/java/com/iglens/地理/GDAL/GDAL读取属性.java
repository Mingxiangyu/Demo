package com.iglens.地理.GDAL;

import java.util.ArrayList;
import java.util.List;
import org.gdal.gdal.gdal;
import org.gdal.ogr.DataSource;
import org.gdal.ogr.Feature;
import org.gdal.ogr.FeatureDefn;
import org.gdal.ogr.FieldDefn;
import org.gdal.ogr.Layer;
import org.gdal.ogr.ogr;

public class GDAL读取属性 {
  String gdbPath = "C:\\Users\\lulie\\Desktop\\test.gdb";

  // 读取属性
  public void readAttribute() {
    // 注册所有驱动
    ogr.RegisterAll();
    // 支持中文路径
    gdal.SetConfigOption("GDAL_FILENAME_IS_UTF8", "YES");
    // 属性表支持中文
    gdal.SetConfigOption("SHAPE_ENCODING", "CP936");

    // 获取GDB驱动
    String gdbDriverName = "FileGDB";
    org.gdal.ogr.Driver gdbDriver = ogr.GetDriverByName(gdbDriverName);
    if (gdbDriver == null) {
      System.out.println("不支持" + gdbDriverName + "驱动");
      return;
    }
    DataSource gdbDataSource = gdbDriver.Open(gdbPath, 0);
    if (gdbDataSource == null) {
      System.out.println("GDAL连接GDB失败!");
      return;
    }
    // 获取所有的layer图层
    for (int i = 0; i < gdbDataSource.GetLayerCount(); i++) {
      Layer layer = gdbDataSource.GetLayer(i);
      System.out.println(" layerName :" + layer.GetName());
      // 获取所有的字段名
      List<String> fieldNames = new ArrayList<String>();
      FeatureDefn featureDefn = layer.GetLayerDefn();
      for (int i1 = 0; i1 < featureDefn.GetFieldCount(); i1++) {
        FieldDefn fieldDefn = featureDefn.GetFieldDefn(i1);
        String fields = fieldDefn.GetName();
        fieldNames.add(fields);
      }
      System.out.println("所有字段名：" + fieldNames);
      // 遍历读取属性
      Feature feature = null;
      while ((feature = layer.GetNextFeature()) != null) {
        List<String> fieldValue = new ArrayList<String>();
        String geojson = feature.GetGeometryRef().ExportToJson();
        System.out.println("geojson ：" + geojson);
        // 遍历字段名读取所有属性
        for (String fieldName : fieldNames) {
          String field = feature.GetFieldAsString(fieldName);
          fieldValue.add(field);
        }
        System.out.println(fieldValue);
      }
    }
    gdbDataSource.delete();
    gdal.GDALDestroyDriverManager();
  }
}
