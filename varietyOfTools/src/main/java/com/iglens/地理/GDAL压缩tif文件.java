package com.iglens.地理;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import org.gdal.gdal.Dataset;
import org.gdal.gdal.Driver;
import org.gdal.gdal.gdal;

public class GDAL压缩tif文件 {
  private static final String FILE_PATH = "D:\\WeChat Files\\aion_my_god\\FileStorage\\File\\2022-05\\L18.tif";

  public static void main(String[] args) {
    zipCompress(FILE_PATH);
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
