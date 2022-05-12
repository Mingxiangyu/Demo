package com.iglens.地理.GDAL;

import org.gdal.gdal.Band;
import org.gdal.gdal.Dataset;
import org.gdal.gdal.Driver;
import org.gdal.gdal.gdal;
import org.gdal.gdalconst.gdalconstConstants;

public class GDAL获取每个点的高程值 {
  /**
   * 获取每个点的高程值
   *
   * @param fileNameTif
   */
  public static void getElevationFromTif(String fileNameTif) {
    gdal.AllRegister();
    Dataset hDataset = gdal.Open(fileNameTif, gdalconstConstants.GA_ReadOnly);
    if (hDataset == null) {
      System.err.println("GDALOpen failed - " + gdal.GetLastErrorNo());
      System.err.println(gdal.GetLastErrorMsg());

      System.exit(1);
    }

    Driver hDriver = hDataset.GetDriver();
    System.out.println("Driver: " + hDriver.getShortName() + "/" + hDriver.getLongName());
    int iXSize = hDataset.getRasterXSize();
    int iYSize = hDataset.getRasterYSize();
    System.out.println("Size is " + iXSize + ", " + iYSize);

    Band band = hDataset.GetRasterBand(1);
    // 这里是DEM数据，所以声明一个int数组来存储，如果是其他数据类型，声明相应的类型即可
    int buf[] = new int[iXSize];

    for (int i = 0; i < 50 /*iYSize*/; i++) {
      band.ReadRaster(0, i, iXSize, 1, buf); // 读取一行数据

      // 下面是输出像元值，为了方便，我只输出了左上角 10×10的范围内的数据
      for (int j = 0; j < 50 /*iXSize*/; j++) {
        System.out.print(buf[j] + ", ");
      }

      System.out.println("\n");
    }
    hDataset.delete();

    // 可选
    gdal.GDALDestroyDriverManager();
  }
}
