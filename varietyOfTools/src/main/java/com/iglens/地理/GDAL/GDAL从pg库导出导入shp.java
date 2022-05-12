package com.iglens.地理.GDAL;

import org.gdal.gdal.gdal;
import org.gdal.ogr.DataSource;
import org.gdal.ogr.Feature;
import org.gdal.ogr.FeatureDefn;
import org.gdal.ogr.FieldDefn;
import org.gdal.ogr.Layer;
import org.gdal.ogr.ogr;

public class GDAL从pg库导出导入shp {
  // pg库导出shp
  public static void pg2shp() {
    ogr.RegisterAll();
    gdal.SetConfigOption("GDAL_FILENAME_IS_UTF8", "YES");
    gdal.SetConfigOption("SHAPE_ENCODING", "CP936");
    // 获取pg驱动
    String pgDriverName = "PostgreSQL";
    org.gdal.ogr.Driver pgDriver = ogr.GetDriverByName(pgDriverName);
    if (pgDriver == null) {
      System.out.println("不支持" + pgDriverName + "驱动");
      return;
    }
    // GDAL连接PostGIS
    String path = "PG:dbname=test host=localhost port=5432 user=postgres password=postgres";
    DataSource pgDataSource = pgDriver.Open(path, 0);
    if (pgDataSource == null) {
      System.out.println("GDAL连接PostGIS数据库失败!");
      return;
    }
    String strSQL = "SELECT * from province WHERE area='测试'";
    // 获取图层
    Layer pgLayer = pgDataSource.ExecuteSQL(strSQL);
    // Layer pgLayer = pgDataSource.GetLayerByName("123");
    if (pgLayer == null) {
      System.out.println("获取【" + "province" + "】图层失败！");
      return;
    }
    System.out.println(pgLayer.GetFIDColumn());

    // 创建矢量文件
    String strVectorFile = "D:\\pg2shp.shp";
    // 驱动
    String shpDriverName = "ESRI Shapefile";
    org.gdal.ogr.Driver shpDriver = ogr.GetDriverByName(shpDriverName);
    if (shpDriver == null) {
      System.out.println(shpDriverName + " 驱动不可用！\n");
      return;
    }
    // 数据源
    DataSource shpDataSource = shpDriver.CreateDataSource(strVectorFile, null);
    if (shpDataSource == null) {
      System.out.println("创建矢量文件【" + strVectorFile + "】失败！\n");
      return;
    }
    // 图层
    Layer shpLayer = shpDataSource.CreateLayer("", pgLayer.GetSpatialRef(), pgLayer.GetGeomType());
    if (shpLayer == null) {
      System.out.println("图层创建失败！\n");
      return;
    }
    // 字段
    FeatureDefn pgDefn = pgLayer.GetLayerDefn();
    int iFieldCount = pgDefn.GetFieldCount();
    for (int i = 0; i < iFieldCount; i++) {
      FieldDefn oField = pgDefn.GetFieldDefn(i);
      shpLayer.CreateField(oField, 1);
    }
    // 数据记录
    Feature oFeature = null;
    while ((oFeature = pgLayer.GetNextFeature()) != null) {
      System.out.println(oFeature.GetFID());
      shpLayer.CreateFeature(oFeature);
    }
    // 写入文件
    shpLayer.SyncToDisk();
    shpDataSource.SyncToDisk();
    // 删除数据源
    pgDataSource.delete();
    shpDataSource.delete();

    gdal.GDALDestroyDriverManager();
    System.out.println("shp文件创建成功！");
  }

  // pg库导入shp（FID会冲突，需要自己解决）
  public static void shp2pg() {
    ogr.RegisterAll();
    gdal.SetConfigOption("GDAL_FILENAME_IS_UTF8", "YES");
    gdal.SetConfigOption("SHAPE_ENCODING", "CP936");

    String shpDriverName = "ESRI Shapefile";
    org.gdal.ogr.Driver shpDriver = ogr.GetDriverByName(shpDriverName);
    if (shpDriver == null) {
      System.out.println(shpDriverName + " 驱动不可用！\n");
      return;
    }

    String strVectorFile = "D:\\pg2shp.shp";
    DataSource shpDataSource = ogr.Open(strVectorFile, 0);
    if (shpDataSource == null) {
      System.out.println("打开文件【" + strVectorFile + "】失败！");
      return;
    }
    Layer shpLayer = shpDataSource.GetLayerByIndex(0);
    if (shpLayer == null) {
      System.out.println("获取shp图层失败！\n");
      return;
    }
    System.out.println(shpLayer.GetFIDColumn());

    // 获取pg驱动
    String pgDriverName = "PostgreSQL";
    org.gdal.ogr.Driver pgDriver = ogr.GetDriverByName(pgDriverName);
    if (pgDriver == null) {
      System.out.println("不支持" + pgDriverName + "驱动");
      return;
    }
    // GDAL连接PostGIS
    String path = "PG:dbname=test host=localhost port=5432 user=postgres password=postgres";
    DataSource pgDataSource = pgDriver.Open(path, 1);
    if (pgDataSource == null) {
      System.out.println("GDAL连接PostGIS数据库失败!");
      return;
    }
    // 获取图层
    Layer pgLayer = pgDataSource.GetLayerByName("tablename");
    if (pgLayer == null) {
      System.out.println("获取【" + "tablename" + "】图层失败！");
      return;
    }

    Feature oFeature = null;
    while ((oFeature = shpLayer.GetNextFeature()) != null) {
      oFeature.SetFID(100 + oFeature.GetFID());
      System.out.println(oFeature.GetFID());
      pgLayer.CreateFeature(oFeature);
    }
    // 写入文件
    pgLayer.SyncToDisk();
    pgDataSource.SyncToDisk();
    // 删除数据源
    pgDataSource.delete();
    shpDataSource.delete();
    gdal.GDALDestroyDriverManager();
    System.out.println("shp文件导入成功！");
  }
}
