package com.iglens.地理.GDAL;

import org.gdal.gdal.Band;
import org.gdal.gdal.Dataset;
import org.gdal.gdal.Driver;
import org.gdal.gdal.gdal;
import org.gdal.gdalconst.gdalconstConstants;

public class GDAL获取高程 {

  public static void main(String[] args) {
    Integer selectAltitude = SelectAltitude(100.3445, 37.003);
    System.out.println("海拔是：" + selectAltitude + "米");
  }

  public static Integer SelectAltitude(double lon, double lat) {

    // 海拔
    Integer altitude = 0;
    // 支持所有驱动
    gdal.AllRegister();
    // 要读取的文件
    String fileName_tif = "D:/file/海北州高程数据/HAIBEIZHOU_DEM.TIF";
    // 只读方式读取数据
    Dataset hDataset = gdal.Open(fileName_tif, gdalconstConstants.GA_ReadOnly);
    // 支持中文路径
    gdal.SetConfigOption("gdal_FILENAME_IS_UTF8", "YES");
    // 判断是否非空
    if (hDataset == null) {
      System.err.println("GDALOpen failed - " + gdal.GetLastErrorNo());
      System.err.println(gdal.GetLastErrorMsg());
      System.exit(1);
    }
    // 图像的列和行
    Driver hDriver = hDataset.GetDriver();
    int iXSize = hDataset.getRasterXSize();
    int iYSize = hDataset.getRasterYSize();
    Band band = hDataset.GetRasterBand(1);

    // 图像六要素
    double[] dGeoTrans = hDataset.GetGeoTransform();
    // 经纬度转行列号
    double dTemp = dGeoTrans[1] * dGeoTrans[5] - dGeoTrans[2] * dGeoTrans[4];
    int Xline =
        (int)
            ((dGeoTrans[5] * (lon - dGeoTrans[0]) - dGeoTrans[2] * (lat - dGeoTrans[3])) / dTemp
                + 0.5);
    int Yline =
        (int)
            ((dGeoTrans[1] * (lat - dGeoTrans[3]) - dGeoTrans[4] * (lon - dGeoTrans[0])) / dTemp
                + 0.5);
    // 这里是DEM数据，所以声明一个int数组来存储，如果是其他数据类型，声明相应的类型即可
    int buf[] = new int[iXSize];
    // 循环遍历取出像元值
    for (int i = 0; i < iYSize; i++) {

      band.ReadRaster(0, i, iXSize, 1, buf); // 读取一行数据
      // 下面是输出像元值
      for (int j = 0; j < iXSize; j++) {
        if (i == Yline && j == Xline) {
          // System.out.println("海拔是："+buf[j]+"米");
          altitude = buf[j];
        }
      }
    }

    hDataset.delete();
    // 可选
    gdal.GDALDestroyDriverManager();

    return altitude;
  }
}
