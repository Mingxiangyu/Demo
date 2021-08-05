package org.demo.地理.瓦片;

import java.io.File;
import org.gdal.gdal.Band;
import org.gdal.gdal.Dataset;
import org.gdal.gdal.Driver;
import org.gdal.gdal.gdal;
import org.gdal.gdalconst.gdalconst;
import org.gdal.gdalconst.gdalconstConstants;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * 原路径1 https://www.jianshu.com/p/98389e3168e8 <br>
 * 原路径2 https://blog.csdn.net/wangshuminjava/article/details/117671048
 */
public class gdal栅格切图 {

  public static void main(String[] args) throws FactoryException, NoSuchAuthorityCodeException {
    gdal.AllRegister();
    String fileName = "C:\\Users\\h\\Desktop\\tif\\map.tif";
    Dataset dataset = gdal.Open(fileName, gdalconstConstants.GA_ReadOnly);
    double[] ori_transform = dataset.GetGeoTransform();
    int rasterCount = dataset.getRasterCount();

    System.out.println(String.format("Origin = (%s, %s)", ori_transform[0], ori_transform[3]));
    System.out.println(String.format("Pixel Size = (%s, %s)", ori_transform[1], ori_transform[5]));
    // 4.1 首先获取原始影像的地理坐标范围
    int yCount = dataset.getRasterXSize();
    int xCount = dataset.getRasterYSize();
    System.err.println("yCount:" + yCount + "xCount:" + xCount);
    double latMin = ori_transform[3];
    double latMax = ori_transform[3] + (yCount * ori_transform[1]);
    double lonMin = ori_transform[0];
    double lonMax = ori_transform[0] + (xCount * ori_transform[1]);
    CoordinateReferenceSystem crs = CRS.decode("EPSG:4326", true);
    ReferencedEnvelope imageBound = new ReferencedEnvelope(lonMin, lonMax, latMin, latMax, crs);
    // 4.2 获取原始影像的像素分辨率
    // 原始图像东西方向像素分辨率
    double src_w_e_pixel_resolution = (lonMax - lonMin) / xCount;
    // 原始图像南北方向像素分辨率
    double src_n_s_pixel_resolution = (latMax - latMin) / yCount;
    // 4.3 根据原始影像地理范围求解切片行列号  // 经纬度转瓦片编号

    int zoom = 10;
    int tileRowMax = lat2tile(latMin, zoom); // 纬度  -90 ——90  lat
    int tileRowMin = lat2tile(latMax, zoom); // 经度 -180 -- 180 lon
    int tileColMin = lon2tile(lonMin, zoom);
    int tileColMax = lon2tile(lonMax, zoom);
    // 4.4 求原始影像地理范围与指定缩放级别指定行列号的切片交集
    int row = 44;
    int col = 34;
    double tempLatMin = tile2lat(row + 1, zoom);
    double tempLatMax = tile2lat(row, zoom);

    double tempLonMin = tile2lon(col, zoom);
    double tempLonMax = tile2lon(col + 1, zoom);
    ReferencedEnvelope tileBound =
        new ReferencedEnvelope(tempLonMin, tempLonMax, tempLatMin, tempLatMax, crs);
    ReferencedEnvelope intersect = tileBound.intersection(imageBound);
    /// 4.5 求解当前切片的像素分辨率(默认切片大小为256*256)
    // 切片东西方向像素分辨率
    double dst_w_e_pixel_resolution = (tempLonMax - tempLonMin) / 256;
    // 切片南北方向像素分辨率
    double dst_n_s_pixel_resolution = (tempLatMax - tempLatMin) / 256;

    // 4.6 计算交集的像素信息
    // 求切图范围和原始图像交集的起始点像素坐标
    int offset_x = (int) ((intersect.getMinX() - lonMin) / src_w_e_pixel_resolution);
    int offset_y = (int) Math.abs((intersect.getMaxY() - latMax) / src_n_s_pixel_resolution);

    // 求在切图地理范围内的原始图像的像素大小
    int block_xsize =
        (int) ((intersect.getMaxX() - intersect.getMinX()) / src_w_e_pixel_resolution);
    int block_ysize =
        (int) ((intersect.getMaxY() - intersect.getMinY()) / src_n_s_pixel_resolution);

    // 求原始图像在切片内的像素大小
    int image_Xbuf =
        (int) Math.ceil((intersect.getMaxX() - intersect.getMinX()) / dst_w_e_pixel_resolution);
    int image_Ybuf =
        (int)
            Math.ceil(
                Math.abs((intersect.getMaxY() - intersect.getMinY()) / dst_n_s_pixel_resolution));

    // 求原始图像在切片中的偏移坐标
    int imageOffsetX = (int) ((intersect.getMinX() - tempLonMin) / dst_w_e_pixel_resolution);
    int imageOffsetY =
        (int) Math.abs((intersect.getMaxY() - tempLatMax) / dst_n_s_pixel_resolution);
    imageOffsetX = imageOffsetX > 0 ? imageOffsetX : 0;
    imageOffsetY = imageOffsetY > 0 ? imageOffsetY : 0;

    //  4.7 使用GDAL的ReadRaster方法对影像指定范围进行读取与压缩。
    //    推荐在切片前建立原始影像的金字塔文件，ReadRaster在内部实现中可直接读取相应级别的金字塔文件，提高效率。
    Band in_band1 = dataset.GetRasterBand(1);
    Band in_band2 = dataset.GetRasterBand(2);
    Band in_band3 = dataset.GetRasterBand(3);

    int[] band1BuffData = new int[256 * 256 * gdalconst.GDT_Int32];
    int[] band2BuffData = new int[256 * 256 * gdalconst.GDT_Int32];
    int[] band3BuffData = new int[256 * 256 * gdalconst.GDT_Int32];

    in_band1.ReadRaster(
        offset_x,
        offset_y,
        block_xsize,
        block_ysize,
        image_Xbuf,
        image_Ybuf,
        gdalconst.GDT_Int32,
        band1BuffData,
        0,
        0);
    in_band2.ReadRaster(
        offset_x,
        offset_y,
        block_xsize,
        block_ysize,
        image_Xbuf,
        image_Ybuf,
        gdalconst.GDT_Int32,
        band2BuffData,
        0,
        0);
    in_band3.ReadRaster(
        offset_x,
        offset_y,
        block_xsize,
        block_ysize,
        image_Xbuf,
        image_Ybuf,
        gdalconst.GDT_Int32,
        band3BuffData,
        0,
        0);

    // 4.8 将切片数据写入文件
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
    dstBand1.WriteRaster(imageOffsetX, imageOffsetY, image_Xbuf, image_Ybuf, band1BuffData);
    dstBand2.WriteRaster(imageOffsetX, imageOffsetY, image_Xbuf, image_Ybuf, band2BuffData);
    dstBand3.WriteRaster(imageOffsetX, imageOffsetY, image_Xbuf, image_Ybuf, band3BuffData);
    alphaBand.WriteRaster(imageOffsetX, imageOffsetY, image_Xbuf, image_Ybuf, alphaData);

    String tileFolder = System.getProperty("java.io.tmpdir");
    String pngPath = tileFolder + File.separator + zoom + "c" + col + "r" + row + ".png";
    System.out.println("pngPath=" + pngPath);
    // 使用PNG驱动将内存中的图像数组写入文件
    Driver pngDriver = gdal.GetDriverByName("PNG");
    Dataset pngDs = pngDriver.CreateCopy(pngPath, msmDS);

    // 释放内存
    msmDS.FlushCache();
    pngDs.delete();

    //      4.8 读取临时文件，并转换成二进制存储到SQLite

    //        try {
    //            File tileFile = new File(pngPath);
    //            InputStream fis = new FileInputStream(tileFile);
    //            byte[] imgBytes = toByteArray(fis);
    //
    //            String uuid = UUID.randomUUID().toString();
    //            // 获取数据库链接
    //            Connection conn=null;
    //            PreparedStatement mapPs = conn.prepareStatement("insert into
    // map(zoom_level,tile_column,tile_row,tile_id) values (?,?,?,?)");
    //            PreparedStatement imagesPs = conn.prepareStatement("insert into
    // images(tile_data,tile_id) values (?,?)");
    //
    //            imagesPs.setBytes(1, imgBytes);
    //            imagesPs.setString(2, uuid);
    //
    //
    //            mapPs.setInt(1, zoom);
    //
    //            mapPs.setInt(2, col);
    //
    //            mapPs.setInt(3, row);
    //            mapPs.setString(4, uuid);
    //
    //            imagesPs.executeUpdate();
    //            mapPs.executeUpdate();
    //
    //            imagesPs.close();
    //            mapPs.close();
    //            fis.close();
    //            tileFile.delete();
    //        } catch (Exception e) {
    //            e.printStackTrace();
    //        }
  }
  // 经纬度转瓦片编号
  public static int lon2tile(double lon, int zoom) {
    return (int) (Math.floor((lon + 180) / 360 * Math.pow(2, zoom)));
  }

  public static int lat2tile(double lat, int zoom) {
    return (int)
        (Math.floor(
            (1
                    - Math.log(Math.tan(lat * Math.PI / 180) + 1 / Math.cos(lat * Math.PI / 180))
                        / Math.PI)
                / 2
                * Math.pow(2, zoom)));
  }

  // 瓦片编号转经纬度
  private static double tile2lon(int col, int zoom) {
    return col / Math.pow(2.0, zoom) * 360.0 - 180;
  }
  ;

  private static double tile2lat(int row, int zoom) {
    double n = Math.PI - (2.0 * Math.PI * row) / Math.pow(2.0, zoom);
    return Math.toDegrees(Math.atan(Math.sinh(n)));
  }
  ;
}
