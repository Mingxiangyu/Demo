package com.iglens.地理;

import org.gdal.gdal.Dataset;
import org.gdal.gdal.Driver;
import org.gdal.gdal.gdal;
import org.gdal.gdalconst.gdalconstConstants;


/**
 * @author doubily
 * @createTime 2021/9/29 16:37
 * @description
 */
public class GDAL基于Tif生成GeoPDF {
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
    String strSavePathName = "beijing.pdf";
    //生成geoPdf
    Dataset translate = gdal.Translate(strSavePathName, dataset, null);
    //   关闭该文件
    dataset.delete();

    gdal.GDALDestroyDriverManager();
  }
}


