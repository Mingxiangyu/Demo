package com.iglens.地理.GDAL;

import org.gdal.gdal.gdal;
import org.gdal.ogr.DataSource;
import org.gdal.ogr.ogr;

public class GDAL打开gdb {

  public void openGdb() {
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
    String gdbPath = "C:\\Users\\lulie\\Desktop\\test.gdb";
    DataSource gdbDataSource = gdbDriver.Open(gdbPath, 0);
    if (gdbDataSource == null) {
      System.out.println("GDAL打开gdb失败!");
      return;
    }
    for (int i = 0; i < gdbDataSource.GetLayerCount(); i++) {
      String layerName = gdbDataSource.GetLayer(i).GetName();
      System.out.println(layerName);
    }
    gdbDataSource.delete();
    gdal.GDALDestroyDriverManager();
  }
}
