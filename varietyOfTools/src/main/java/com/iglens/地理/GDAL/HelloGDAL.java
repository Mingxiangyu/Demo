package com.iglens.地理.GDAL;

import org.gdal.gdal.Band;
import org.gdal.gdal.Dataset;
import org.gdal.gdal.Driver;
import org.gdal.gdal.gdal;
import org.gdal.gdalconst.gdalconstConstants;

/** */
public class HelloGDAL {

  private static final String FILE_PATH =
      "D:\\WeChat Files\\aion_my_god\\FileStorage\\File\\2022-05\\L18.tif";

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
    System.out.println("dataset 宽:" + xSize + ", 高 = " + ySzie + ", 波段数 = " + rasterCount);

    Band band = dataset.GetRasterBand(1);
    // the data type of the band.
    int type = band.GetRasterDataType();
    System.out.println("data type = " + type + ", " + (type == gdalconstConstants.GDT_Byte));

    // 六参数信息
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
    // 影像左上角投影坐标
    double[] ulCoord = new double[2];
    ulCoord[0] = geoTransform[0];
    ulCoord[1] = geoTransform[3];
    // 影像右下角投影坐标
    double[] brCoord = new double[2];
    brCoord[0] = geoTransform[0] + xSize * geoTransform[1] + ySzie * geoTransform[2];
    brCoord[1] = geoTransform[3] + xSize * geoTransform[4] + ySzie * geoTransform[5];

    // 影像投影信息
    String proj = dataset.GetProjection();

    // Frees the native resource associated to a Dataset object and close the file.
    dataset.delete();

    gdal.GDALDestroyDriverManager();
  }

  /*
  5种主要驱动程序的名称
   */
  public class DriverName {

    // String connShp = "D:\\DIST\\Code\\Project\\zhenjiang\\镇江shp\\五角星.shp";
    public static final String shp = "ESRI Shapefile";

    // String connGdb = "D:\\DIST\\geoData\\shpFile\\unionResult.gdb";
    public static final String gdb = "FileGDB"; // 或者OpenFileGDB

    // String connPg = "PG:dbname=dggis host=192.168.200.34 port=30013 user=sde password=sde";
    public static final String postgreSQL = "PostgreSQL";

    // String connStr = "OCI:sde/sde:SYS.Yjjbnt";//连接本地库可以忽略数据库实例
    // String connSde = "OCI:gis_qf/Passw0rd@192.168.1.163/orcl:CHDYZT.JSFAFW";
    // String connStr = "OCI:rcgtkjgh/pass@192.168.200.230:1521/orcl:RCGTKJGH.DELSH_STBHHX";
    public static final String sde = "OCI";

    // String connMdb = "D:\\DIST\\geoData\\shpFile\\test.mdb";
    public static final String mdb = "PGeo";
  }

  /*
  连接数据源的基本过程
  */
  // 1、根据驱动名称获取驱动
  //   Driver driver = ogr.GetDriverByName(driverName);
  // 2、通过驱动打开数据源
  //   DataSource dataSource = driver.Open(FilePath, 0);//文件路径或者连接字符串，0表示不更新数据集，为只读
  // 3、获取数据源里的图层
  //     dataSource.GetLayer(index:)//根据id获取
  //       dataSource.GetLayer(layerName:"")//根据名称获取
  //       4、获取图层里的要素
  //   读取GDB里面的图层时，获取Feature得用GetNextFeature()方法，不能根据GetFeature(long fid)
  // layer.GetFeature(int:);//根据id
  //
  // layer.ResetReading();//把要素读取顺序重置为从第一个开始
  // layer.GetNextFeature();

  /*
  读取数据基本信息
   */
  // 读取图层数量：
  // int layerCount = dataSource.GetLayerCount();
  // 图层名称
  // String layerName = layer.GetName();
  // 图层要素数量
  // long featureCount = layer.GetFeatureCount();
  // 图层空间参考信息
  // SpatialReference s = layer.GetSpatialRef();
  // 图层的属性表结构
  // FeatureDefn featureDefn = layer.GetLayerDefn();
  // 属性表字段数量
  // int fieldCount = featureDefn.GetFieldCount();
  // 属性表的属性字段
  // FieldDefn fieldDefn = featureDefn.GetFieldDefn(i1);//根据索引获取
  // 属性字段类型
  // int fieldType = fieldDefn.GetFieldType();
  // String fieldTypeName = fieldDefn.GetFieldTypeName(fieldType);
  // 属性字段名称
  // String fieldName = fieldDefn.GetName();
  // 获取FID
  // long fid = feature.GetFID();//这个是通过Feature来获取的
  // 获取Geometry
  // Geometry geometry = feature.GetGeometryRef();
  // String geoJson = geometry.ExportToJson();
  // { "type": "Polygon", "coordinates": [ [ [ 119.456586303, 32.063698523000028, 0.0 ], [
  // 119.468721554000012, 32.045852565000018, 0.0 ], [ 119.490850540999986, 32.040141859000016, 0.0
  // ], [ 119.472290745, 32.028006608, 0.0 ], [ 119.478001451999944, 32.003736104999973, 0.0 ], [
  // 119.457300141000019, 32.023723577999988, 0.0 ], [ 119.43659882999998, 32.002308428999982, 0.0
  // ], [ 119.443023375000053, 32.025865093, 0.0 ], [ 119.422322064000014, 32.034431152000025, 0.0
  // ], [ 119.445878728000025, 32.043711050000013, 0.0 ], [ 119.456586303, 32.063698523000028, 0.0 ]
  // ] ] }
  // 获取图层范围
  // double[] extent = layer.GetExtent();//返回4个坐标点

  /*
关于投影
   */
//   SpatialReference spatialReference = layer.GetSpatialRef();//获取图层的空间信息
//   //设定空间信息
//   //通过EPSG
//   SpatialReference spatialReference = new SpatialReference();
// spatialReference.ImportFromEPSG(4490);
//   //通过WKT字符串
//   String strwkt = "GEOGCS[\"GCS_North_American_1927\"," +
//       "DATUM[\"North_American_Datum_1927\"," +
//       "SPHEROID[\"Clarke_1866\",6378206.4,294.9786982]]," +
//       "PRIMEM[\"Greenwich\",0]," +
//       "UNIT[\"Degree\",0.0174532925199433]]";
//   SpatialReference spatialReference = new SpatialReference(strwkt);
}
