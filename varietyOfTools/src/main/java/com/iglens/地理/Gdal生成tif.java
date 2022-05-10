package com.iglens.地理;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import org.gdal.gdal.Band;
import org.gdal.gdal.Dataset;
import org.gdal.gdal.Driver;
import org.gdal.gdal.gdal;
import org.gdal.gdalconst.gdalconst;
import org.gdal.osr.SpatialReference;

public class Gdal生成tif {

  public static void main(String[] args) {

    double[] floats = {120.23, 0.000102, 0.0, 37.45, 0.0, -0.000321};
    int[] re = {10};
    int[][] resultMap = {re};
    Map<Integer,  int[][]> objectObjectHashMap = new HashMap<>();
    objectObjectHashMap.put(1, resultMap);
    writeTiff(objectObjectHashMap,"ceshi.tif",4,floats,8706,4608);
  }

  /**
   *
   * @param resultMap 通道数据，key为对应通道,value int[][]第一维为行，第二维为每行大小
   * @param path 生成文件路径
   * @param band_type 通道数
   * @param argin 坐标信息 例如：{120.23,0.000102,0.0,37.45,0.0,-0.000321}
   *              // argin的内容0:左上角x坐标 1:东西方向空间分辨率 2:x方向旋转角0° 3:左上角y坐标 4:y方向旋转角0° 5:南北方向空间分辨率
   * @param ySize 多少行
   * @param xSize 多少列
   */
  public static void writeTiff(Map<Integer, int[][]> resultMap, String path, int band_type, double[] argin, int ySize, int xSize) {
    String targetTiffFile = path;
    gdal.AllRegister();
    System.out.println("***开始生成tiff***");
    // 为了支持中文路径，请添加下面这句代码
    gdal.SetConfigOption("GDAL_FILENAME_IS_UTF8", "YES");
    SpatialReference ref = new SpatialReference();
    //只有这个投影支持输出geotiff OGC WKT
    ref.SetWellKnownGeogCS("WGS84");
    String[] defWkt = null;
    //驱动名称
    String strDriverName = "GTiff";
    Driver oDriver = gdal.GetDriverByName(strDriverName);
    if (oDriver == null) {
      System.out.println(strDriverName + " 驱动不可用！\n");
      return;
    }
    //targetTiffFile，生成文件路径，xSize每行多大，ySize多少行，band_type通道数，gdalconst.GDT_Byte数据类型（Byte图片可见，int型必须arcgis才可见）
    Dataset dataset = oDriver.Create(targetTiffFile, xSize, ySize, band_type, gdalconst.GDT_Byte);
    // argin的内容0:左上角x坐标 1:东西方向空间分辨率 2:x方向旋转角0° 3:左上角y坐标 4:y方向旋转角0° 5:南北方向空间分辨率
    dataset.SetGeoTransform(argin);
    for (int index = 1; index <= band_type; index++) {
      int[][] data = resultMap.get(index);
      for (int row = 0; row < ySize; row++) {
        byte[] buf = new byte[xSize];
        for (int column = 0; column < xSize; column++) {
          if (data[row][column] == 0) {
            buf[column] = (byte) 255;
          } else {
            buf[column] = (byte) data[row][column];
          }
        }
        //获取指定波段的数据band
        Band band = dataset.GetRasterBand(index);// 波段  (色彩)
        //前两个参数标识距离左上角距离，后两个表示读取区域大小，第五个是数据类型，最后是数据内容
        band.WriteRaster(0, row, xSize, 1, gdalconst.GDT_Byte, buf);
        band.FlushCache();
      }
    }

    dataset.delete();
    oDriver.delete();
    //调用压缩
//    zipCompress(path);
  }

  /**
   * 压缩文件大小，像素不变，文件缩小
   * @param path 需要压缩文件的路径
   */
  public static void zipCompress(String path) {
    gdal.AllRegister();
    String sourcePath = path;
    //生成一个新的路径，文件名加了个2
    String targetPath = path.replace(".tif","2.tif");
    Dataset dataset = gdal.Open(sourcePath);
    Driver driver = gdal.GetDriverByName("GTiff");
    List<String> op = new ArrayList<>();
    op.add("TILED=YES");
    op.add("COMPRESS=LZW");
    Dataset datasetZip = driver.CreateCopy(targetPath, dataset, 1, new Vector(op));
    dataset.delete();
    datasetZip.delete();
    driver.delete();
  }
}
