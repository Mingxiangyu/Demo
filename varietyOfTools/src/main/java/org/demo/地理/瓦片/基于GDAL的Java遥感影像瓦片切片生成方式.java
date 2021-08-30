package org.demo.地理.瓦片;

import java.io.File;
import java.util.UUID;
import org.gdal.gdal.Band;
import org.gdal.gdal.Dataset;
import org.gdal.gdal.Driver;
import org.gdal.gdal.gdal;
import org.gdal.gdalconst.gdalconst;
import org.gdal.gdalconst.gdalconstConstants;
import org.gdal.osr.SpatialReference;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.locationtech.jts.geom.Coordinate;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

/**
 * @link 原文连接
 *     http://www.itfsw.com/blog/post/2020/09/08/jiyu-gdal-java-yaogan-yingxiang-wapian-qiepian-shengcheng/
 */
public class 基于GDAL的Java遥感影像瓦片切片生成方式 {
  /** 瓦片大小 */
  public static final int TILE_SIZE = 256;
  /** web 墨卡托 EPSG 编码 */
  public static final int WEB_MERCATOR_EPSG_CODE = 3857;
  /** Wgs84 EPSG 编码 */
  public static final int WGS_84_EGPS_CODE = 4326;

  /**
   * Tif 切片
   *
   * @param tifFile tif文件路径
   * @param outputDir 下载后的文件输出文件夹路径
   * @param minZoom 最小层级
   * @param maxZoom 最大层级
   */
  public static void tif2Tiles(String tifFile, String outputDir, int minZoom, int maxZoom)
      throws FactoryException, TransformException, NoSuchAuthorityCodeException {
    // 获取gdal中tif信息
    Dataset srcDs = gdal.Open(tifFile, gdalconstConstants.GA_ReadOnly);
    if (srcDs == null) {
      System.err.println("GDALOpen failed - " + gdal.GetLastErrorNo());
      System.err.println(gdal.GetLastErrorMsg());
      return;
    }
    // 1. 影像重投影到web墨卡托
    String tmpFile =
        System.getProperty("java.io.tmpdir") + File.separator + UUID.randomUUID().toString();
    SpatialReference spatialReference = new SpatialReference();
    spatialReference.ImportFromEPSG(WEB_MERCATOR_EPSG_CODE);
    Dataset webMercatorDs =
        gdal.AutoCreateWarpedVRT(
            srcDs, null, spatialReference.ExportToWkt(), gdalconst.GRA_Bilinear);
    // 将原tif srcDs 转换为web墨卡托投影 dataset
    Dataset dataset = gdal.GetDriverByName("GTiff").CreateCopy(tmpFile, webMercatorDs);
    srcDs.delete();
    webMercatorDs.delete();

    try {
      // 2. 获取遥感影像经纬度范围，计算遥感影像像素分辨率
      // 获取原始影像的地理坐标范围
      double[] geoTransform = dataset.GetGeoTransform();
      // 获取原始影像的像素分辨率 xSize为水平方向每个像素代表的经度 ySize为垂直方向每个像素代表的纬度
      int xSize = dataset.getRasterXSize();
      int ySize = dataset.getRasterYSize();
      // 计算经纬度范围
      double lngMin = geoTransform[0];
      double latMax = geoTransform[3];
      double lngMax = lngMin + (xSize * geoTransform[1]) + (ySize * geoTransform[2]);
      double latMin = latMax + (xSize * geoTransform[4]) + (ySize * geoTransform[5]);

      // EPSG:3857 坐标转Wgs84经纬度
      CoordinateReferenceSystem sourceCRS = CRS.decode("EPSG:" + WEB_MERCATOR_EPSG_CODE);
      CoordinateReferenceSystem targetCRS = CRS.decode("EPSG:" + WGS_84_EGPS_CODE);
      MathTransform transform = CRS.findMathTransform(sourceCRS, targetCRS, false);
      Coordinate lngLatMax = JTS.transform(new Coordinate(lngMax, latMax), null, transform);
      Coordinate lngLatMin = JTS.transform(new Coordinate(lngMin, latMin), null, transform);
      lngMax = lngLatMax.getY();
      latMax = lngLatMax.getX();
      lngMin = lngLatMin.getY();
      latMin = lngLatMin.getX();

      // 原始图像东西方向像素分辨率
      double srcWePixelResolution = (lngMax - lngMin) / xSize;
      // 原始图像南北方向像素分辨率
      double srcNsPixelResolution = (latMax - latMin) / ySize;

      for (int zoom = minZoom; zoom <= maxZoom; zoom++) {
        // 3. 根据原始影像地理范围求解切片行列号
        int[] tilePointMax = coordinates2tile(lngMax, latMax, zoom);
        int[] tilePointMin = coordinates2tile(lngMin, latMin, zoom);
        int tileRowMax = tilePointMin[1];
        int tileColMax = tilePointMax[0];
        int tileRowMin = tilePointMax[1];
        int tileColMin = tilePointMin[0];
        for (int row = tileRowMin; row <= tileRowMax; row++) {
          for (int col = tileColMin; col <= tileColMax; col++) {
            // 4. 求原始影像地理范围与指定缩放级别指定行列号的切片交集
            double tempLatMin = tile2lat(row + 1, zoom);
            double tempLatMax = tile2lat(row, zoom);
            double tempLonMin = tile2lng(col, zoom);
            double tempLonMax = tile2lng(col + 1, zoom);

            ReferencedEnvelope tileBound =
                new ReferencedEnvelope(
                    tempLonMin, tempLonMax, tempLatMin, tempLatMax, DefaultGeographicCRS.WGS84);
            ReferencedEnvelope imageBound =
                new ReferencedEnvelope(lngMin, lngMax, latMin, latMax, DefaultGeographicCRS.WGS84);
            ReferencedEnvelope intersect = tileBound.intersection(imageBound);

            // 5. 求解当前切片的像素分辨率(默认切片大小为256*256)
            // 切片东西方向像素分辨率
            double dstWePixelResolution = (tempLonMax - tempLonMin) / 256;
            // 切片南北方向像素分辨率
            double dstNsPixelResolution = (tempLatMax - tempLatMin) / 256;
            // 6. 计算交集的像素信息
            // 求切图范围和原始图像交集的起始点像素坐标
            int offsetX = (int) ((intersect.getMinX() - lngMin) / srcWePixelResolution);
            int offsetY = (int) Math.abs((intersect.getMaxY() - latMax) / srcNsPixelResolution);

            // 求在切图地理范围内的原始图像的像素大小
            int blockXSize =
                (int) ((intersect.getMaxX() - intersect.getMinX()) / srcWePixelResolution);
            int blockYSize =
                (int) ((intersect.getMaxY() - intersect.getMinY()) / srcNsPixelResolution);

            // 求原始图像在切片内的像素大小
            int imageXBuf =
                (int) Math.ceil((intersect.getMaxX() - intersect.getMinX()) / dstWePixelResolution);
            int imageYBuf =
                (int)
                    Math.ceil(
                        Math.abs(
                            (intersect.getMaxY() - intersect.getMinY()) / dstNsPixelResolution));

            // 求原始图像在切片中的偏移坐标
            int imageOffsetX = (int) ((intersect.getMinX() - tempLonMin) / dstWePixelResolution);
            int imageOffsetY =
                (int) Math.abs((intersect.getMaxY() - tempLatMax) / dstNsPixelResolution);
            imageOffsetX = imageOffsetX > 0 ? imageOffsetX : 0;
            imageOffsetY = imageOffsetY > 0 ? imageOffsetY : 0;

            // 7. 使用GDAL的ReadRaster方法对影像指定范围进行读取与压缩。
            // 推荐在切片前建立原始影像的金字塔文件，ReadRaster在内部实现中可直接读取相应级别的金字塔文件，提高效率。
            Band inBand1 = dataset.GetRasterBand(1);
            Band inBand2 = dataset.GetRasterBand(2);
            Band inBand3 = dataset.GetRasterBand(3);

            int[] band1BuffData = new int[TILE_SIZE * TILE_SIZE * gdalconst.GDT_Int32];
            int[] band2BuffData = new int[TILE_SIZE * TILE_SIZE * gdalconst.GDT_Int32];
            int[] band3BuffData = new int[TILE_SIZE * TILE_SIZE * gdalconst.GDT_Int32];

            inBand1.ReadRaster(
                offsetX,
                offsetY,
                blockXSize,
                blockYSize,
                imageXBuf,
                imageYBuf,
                gdalconst.GDT_Int32,
                band1BuffData,
                0,
                0);
            inBand2.ReadRaster(
                offsetX,
                offsetY,
                blockXSize,
                blockYSize,
                imageXBuf,
                imageYBuf,
                gdalconst.GDT_Int32,
                band2BuffData,
                0,
                0);
            inBand3.ReadRaster(
                offsetX,
                offsetY,
                blockXSize,
                blockYSize,
                imageXBuf,
                imageYBuf,
                gdalconst.GDT_Int32,
                band3BuffData,
                0,
                0);

            //  8. 将切片数据写入文件
            // 使用gdal的MEM驱动在内存中创建一块区域存储图像数组
            Driver memDriver = gdal.GetDriverByName("MEM");
            Dataset msmDS = memDriver.Create("msmDS", 256, 256, 4);
            Band dstBand1 = msmDS.GetRasterBand(1);
            Band dstBand2 = msmDS.GetRasterBand(2);
            Band dstBand3 = msmDS.GetRasterBand(3);

            // 设置alpha波段数据,实现背景透明
            Band alphaBand = msmDS.GetRasterBand(4);
            int[] alphaData = new int[256 * 256 * gdalconst.GDT_Int32];
            for (int index = 0; index < alphaData.length; index++) {
              if (band1BuffData[index] > 0) {
                alphaData[index] = 255;
              }
            }
            // 写各个波段数据
            dstBand1.WriteRaster(imageOffsetX, imageOffsetY, imageXBuf, imageYBuf, band1BuffData);
            dstBand2.WriteRaster(imageOffsetX, imageOffsetY, imageXBuf, imageYBuf, band2BuffData);
            dstBand3.WriteRaster(imageOffsetX, imageOffsetY, imageXBuf, imageYBuf, band3BuffData);
            alphaBand.WriteRaster(imageOffsetX, imageOffsetY, imageXBuf, imageYBuf, alphaData);

            String pngPath = outputDir + File.separator + zoom + "-" + col + "-" + row + ".png";
            // 使用PNG驱动将内存中的图像数组写入文件
            Driver pngDriver = gdal.GetDriverByName("PNG");
            Dataset pngDs = pngDriver.CreateCopy(pngPath, msmDS);

            // 释放内存
            msmDS.FlushCache();
            msmDS.delete();
            pngDs.delete();
          }
        }
      }
    } finally {
      // 释放并删除临时文件
      dataset.delete();
      new File(tmpFile).delete();
    }
  }

  /**
   * 经纬度坐标转瓦片坐标
   *
   * @param lng 经度
   * @param lat 纬度
   * @param zoom 层级
   * @return
   */
  public static int[] coordinates2tile(double lng, double lat, int zoom) {
    int x = (int) Math.floor((lng + 180) / 360 * (1 << zoom));
    int y =
        (int)
            Math.floor(
                (1
                        - Math.log(
                                Math.tan(Math.toRadians(lat)) + 1 / Math.cos(Math.toRadians(lat)))
                            / Math.PI)
                    / 2
                    * (1 << zoom));
    if (x < 0) {
      x = 0;
    }
    if (x >= (1 << zoom)) {
      x = ((1 << zoom) - 1);
    }
    if (y < 0) {
      y = 0;
    }
    if (y >= (1 << zoom)) {
      y = ((1 << zoom) - 1);
    }
    return new int[] {x, y};
  }

  /**
   * 瓦片坐标转经度
   *
   * @param x
   * @param z
   * @return
   */
  public static double tile2lng(double x, int z) {
    return x / Math.pow(2.0, z) * 360.0 - 180;
  }

  /**
   * 瓦片坐标转纬度
   *
   * @param y
   * @param z
   * @return
   */
  public static double tile2lat(double y, int z) {
    double n = Math.PI - (2.0 * Math.PI * y) / Math.pow(2.0, z);
    return Math.toDegrees(Math.atan(Math.sinh(n)));
  }

  /**
   * @param args
   * @throws FactoryException
   * @throws TransformException
   */
  public static void main(String[] args)
      throws FactoryException, TransformException, NoSuchAuthorityCodeException {
    gdal.AllRegister();
    tif2Tiles("D:/test.tif", "D:/tiles", 3, 15);
  }
}
