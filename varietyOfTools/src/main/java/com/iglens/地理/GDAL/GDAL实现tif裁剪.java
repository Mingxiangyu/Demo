package com.iglens.地理.GDAL;

import org.gdal.gdal.Band;
import org.gdal.gdal.Dataset;
import org.gdal.gdal.Driver;
import org.gdal.gdal.gdal;
import org.gdal.gdalconst.gdalconst;

public class GDAL实现tif裁剪 {

  public static void main(String[] args) {
    gdal.AllRegister();
    Dataset rds = gdal.Open("影像路径", gdalconst.GA_ReadOnly);
    //宽、高、波段数
    int b = rds.getRasterCount();
    //从波段中获取影像的数据类型，gdal中波段索引从1开始
    int dataType = rds.GetRasterBand(1).GetRasterDataType();

    //六参数信息
    double[] geoTransform = rds.GetGeoTransform();

    //设置要裁剪的起始像元位置，以及各方向的像元数
    //这里表示从像元(5000, 5000)开始，x方向和y方向各裁剪1000个像元
    //最终结果就是一幅1000*1000的影像
    int startX = 5000;
    int startY = 5000;
    int clipX = 1000;
    int clipY = 1000;

    //计算裁剪后的左上角坐标
    geoTransform[0] = geoTransform[0] + startX * geoTransform[1] + startY * geoTransform[2];
    geoTransform[3] = geoTransform[3] + startX * geoTransform[4] + startY * geoTransform[5];

    //创建结果图像
    Driver driver = gdal.GetDriverByName("GTIFF");
    Dataset outputDs = driver
        .Create("D:\\Javaworkspace\\gdal\\output\\clip1.tif", clipX, clipY, b, dataType);
    outputDs.SetGeoTransform(geoTransform);
    outputDs.SetProjection(rds.GetProjection());

    //按band读取
    for (int i = 0; i < clipY; i++) {
      //按行读取
      for (int j = 1; j <= b; j++) {
        Band orgBand = rds.GetRasterBand(j);
        int[] cache = new int[clipX];
        //从位置x开始，只读一行
        orgBand.ReadRaster(startX, startY + i, clipX, 1, cache);
        Band desBand = outputDs.GetRasterBand(j);
        //从左上角开始，只写一行
        desBand.WriteRaster(0, i, clipX, 1, cache);
        desBand.FlushCache();
      }
    }

    //释放资源
    rds.delete();
    outputDs.delete();
  }
}
