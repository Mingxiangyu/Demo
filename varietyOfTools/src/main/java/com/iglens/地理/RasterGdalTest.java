package com.iglens.地理;

import org.gdal.gdal.Band;
import org.gdal.gdal.Dataset;
import org.gdal.gdal.Driver;
import org.gdal.gdal.gdal;
import org.gdal.gdalconst.gdalconstConstants;


/**
 * @author doubily
 * @createTime 2021/9/29 16:37
 * @description
 */
public class RasterGdalTest {

  private static final String FILE_PATH = "D:\\WeChat Files\\aion_my_god\\FileStorage\\File\\2022-05\\L18.tif";

  static {
    // 注册所有的驱动
    gdal.AllRegister();
    // 为了支持中文路径，请添加下面这句代码
    gdal.SetConfigOption("GDAL_FILENAME_IS_UTF8", "YES");
    // 为了使属性表字段支持中文，请添加下面这句
    gdal.SetConfigOption("SHAPE_ENCODING", "");
  }

  public static void main(String[] args) {
    // 读取影像数据x
    Dataset dataset = gdal.Open(FILE_PATH, gdalconstConstants.GA_ReadOnly);
    if (dataset == null) {
      System.out.println("read fail!");
      return;
    }

    //  providing various methods for a format specific driver.
    Driver driver = dataset.GetDriver();
    System.out.println("driver name : " + driver.getLongName());

    // 读取影像信息
    //    宽
    int xSize = dataset.getRasterXSize();
    //    高
    int ySzie = dataset.getRasterYSize();
    // 波段数
    int rasterCount = dataset.getRasterCount();
    System.out.println(
        "dataset 宽:" + xSize + ", 高 = " + ySzie + ", 波段数 = " + rasterCount);

    Band band = dataset.GetRasterBand(1);
    //the data type of the band.
    int type = band.GetRasterDataType();
    System.out.println("data type = " + type + ", " + (type == gdalconstConstants.GDT_Byte));

    //六参数信息
    double[] geoTransform = dataset.GetGeoTransform();
    /*
    这里有个数组geoTransform，容量为6，代表的是仿射变换六参数，其含义如下：
    geoTransform[0]：左上角x坐标
    geoTransform[1]：东西方向空间分辨率
    geoTransform[2]：x方向旋转角
    geoTransform[3]：左上角y坐标
    geoTransform[4]：y方向旋转角x
    geoTransform[5]：南北方向空间分辨率
    链接：https://www.jianshu.com/p/c25f9360459f
     */
    //影像左上角投影坐标
    double[] ulCoord = new double[2];
    ulCoord[0] = geoTransform[0];
    ulCoord[1] = geoTransform[3];
    //影像右下角投影坐标
    double[] brCoord = new double[2];
    brCoord[0] = geoTransform[0] + xSize * geoTransform[1] + ySzie * geoTransform[2];
    brCoord[1] = geoTransform[3] + xSize * geoTransform[4] + ySzie * geoTransform[5];

    //影像投影信息
    String proj = dataset.GetProjection();

    //Frees the native resource associated to a Dataset object and close the file.
    dataset.delete();

    gdal.GDALDestroyDriverManager();
  }
}


