package com.iglens.地理.GDAL;

import org.gdal.gdal.gdal;
import org.gdal.ogr.DataSource;
import org.gdal.ogr.Driver;
import org.gdal.ogr.Feature;
import org.gdal.ogr.Geometry;
import org.gdal.ogr.Layer;
import org.gdal.ogr.ogr;

public class GDAL打开shap {

  public String opeanShp(String strVectorFile) {
    // 准备，注册驱动
    ogr.RegisterAll();
    // 设置支持中文
    gdal.SetConfigOption("GDAL_FILENAME_IS_UTF8", "YES");
    gdal.SetConfigOption("SHAPE_ENCODING", "CP936");
    // 获取驱动
    String shpDriverName = "ESRI Shapefile";
    Driver shpDriver = ogr.GetDriverByName(shpDriverName);
    if (shpDriver == null) {
      throw new RuntimeException(shpDriverName + " 驱动不可用！\n");
    }

    // 获取数据源
    DataSource shpDataSource = ogr.Open(strVectorFile, 0);
    if (shpDataSource == null) {
      throw new RuntimeException("打开文件【" + strVectorFile + "】失败！");
    }
    // 获取图层0
    Layer shpLayer = shpDataSource.GetLayerByIndex(0);
    if (shpLayer == null) {
      throw new RuntimeException("获取shp图层失败！\n");
    }
    // 转化为json
    Feature feature = shpLayer.GetNextFeature();
    Geometry geometry = feature.GetGeometryRef();
    String json = geometry.ExportToJson();
    shpDataSource.delete();
    gdal.GDALDestroyDriverManager();
    return json;
  }

  public static void main(String[] args) {
    GDAL打开shap shp = new GDAL打开shap();
    String strVectorFile = "D:\\新建文件夹\\4496\\天府新区.shp";
    String info = shp.opeanShp(strVectorFile);
    System.out.println(info);
  }
}
