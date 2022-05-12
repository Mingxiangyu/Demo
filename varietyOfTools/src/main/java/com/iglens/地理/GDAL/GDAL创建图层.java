package com.iglens.地理.GDAL;

import java.io.File;
import lombok.extern.slf4j.Slf4j;
import org.gdal.ogr.DataSource;
import org.gdal.ogr.Driver;
import org.gdal.ogr.Layer;
import org.gdal.ogr.ogr;
import org.gdal.osr.SpatialReference;

@Slf4j
public class GDAL创建图层 {
  /**
   * 创建矢量图层
   *
   * @param driverName:驱动名称
   * @param path:图层保存路径，要和驱动匹配
   * @param layerName：图层名称
   * @param spatialReference：图层空间参考
   * @return 返回创建好的图层
   */
  public static Layer createLayer(
      String driverName, String path, String layerName, SpatialReference spatialReference) {
    Layer result = null;

    Driver driver = ogr.GetDriverByName(driverName);
    if (driver == null) {
      log.info(driverName + "不可用");
      System.out.println(driverName + "不可用");
      return null;
    }
    DataSource dataSource = null;
    // 这里需要判断一下path是否已经存在，存在的话先删除再创建(如果是已存在的gdb，则直接打开)
    File file = new File(path);
    if (file.exists()) {
      if (file.isFile()) {
        file.delete();
        dataSource = driver.CreateDataSource(path, null);
      } else if (file.isDirectory()) {
        dataSource = driver.Open(path, 1);
        // GDB中存在同名图层则删除
        for (int i = 0; i < dataSource.GetLayerCount(); i++) {
          Layer layer = dataSource.GetLayer(i);
          if (layerName.equals(layer.GetName())) {
            dataSource.DeleteLayer(i);
            dataSource.FlushCache();
          }
        }
      }
    } else {
      dataSource = driver.CreateDataSource(path, null);
    }
    if (dataSource == null) {
      log.info("数据源创建/打开失败");
      System.out.println("数据源创建/打开失败");
      return null;
    }
    result = dataSource.CreateLayer(layerName, spatialReference, ogr.wkbPolygon, null);
    if (result == null) {
      log.info(layerName + "创建失败");
      System.out.println(layerName + "创建失败");
      return null;
    }
    log.info("【" + layerName + "】" + "创建成功");
    return result;
  }
}
