package com.iglens.地理;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Transaction;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.geojson.geom.GeometryJSON;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * 参考：https://blog.csdn.net/qiaobing1226/article/details/127612665
 */
public class GeoTools实现geojson与shp互转 {

  private static final String POINT = "Point";
  private static final String MULTIPOINT = "MultiPoint";
  private static final String LINESTRING = "LineString";
  private static final String MULTILINESTRING = "MultiLineString";
  private static final String POLYGON = "Polygon";
  private static final String MULTIPOLYGON = "MultiPolygon";
  private static final String THE_GEOM = "the_geom";
  private static final String PROPERTIES = "properties";
  private static final String GEOMETRY = "geometry";
  private static final String GBK = "GBK";


  /**
   * 工具类测试方法
   */
  public static void main(String[] args) throws Exception {
    //         Shp2GeojsonUtils shpToGeojson = new Shp2GeojsonUtils();
    // //        // shape2Geojson
    //         String shpPath = "/Users/ecarx/Desktop/geojson/上下高架mct/123/AD_Road.shp";
    //         String jsonPath = "/Users/ecarx/Desktop/geojson/AD_Road.geojson";
    //         Map<String, Object> map = shpToGeojson.shp2Geojson(shpPath, jsonPath);

    // geojson2Shape
    String shpPath = "C:\\Users\\zhouhuilin\\Desktop\\众包测试目标\\GADM\\常态化任务\\12345\\ARE\\#gadm41_ARE_1.shp";
    String jsonPath = "C:\\Users\\zhouhuilin\\Desktop\\众包测试目标\\GADM\\常态化任务\\12345\\ARE\\#gadm41_ARE_1.json";
    Map<String, Object> map = geoJson2Shape(jsonPath, shpPath);
    System.out.println(map.toString());
  }

  /**
   * geoJson转换为shp文件
   */
  public static Map<String, Object> geoJson2Shape(String jsonPath, String shpPath) {
    Map<String, Object> map = new HashMap<>();
    GeometryJSON geoJson = new GeometryJSON();
    try {
      JSONObject json = readGeoJsonFile(jsonPath);
      JSONArray features = (JSONArray) json.get("features");
      JSONObject feature0 = JSONObject.parseObject(features.get(0).toString());
      // 获取属性名称
      Set properties = JSONObject.parseObject(feature0.getString(PROPERTIES)).keySet();
      String strType = ((JSONObject) feature0.get(GEOMETRY)).getString("type");
      String strCrs = json.getJSONObject("crs").getJSONObject(PROPERTIES).getString("name");
      // CoordinateReferenceSystem crs = CRS.decode(strCrs); // 暂时注释，避免没有清楚的函数报错
      ShapefileDataStore shapefileDataStore = dataStore(properties, strType, shpPath, null);
      if (shapefileDataStore == null) {
        return map;
      }
      // 设置Writer
      FeatureWriter<SimpleFeatureType, SimpleFeature> writer = shapefileDataStore.getFeatureWriter(
          shapefileDataStore.getTypeNames()[0],
          Transaction.AUTO_COMMIT);
      for (int i = 0, len = features.size(); i < len; i++) {
        String strFeature = features.get(i).toString();
        Reader reader = new StringReader(strFeature);
        SimpleFeature feature = writer.next();
        switch (strType) {
          case POINT:
            feature.setAttribute(THE_GEOM, geoJson.readPoint(reader));
            break;
          case MULTIPOINT:
            feature.setAttribute(THE_GEOM, geoJson.readMultiPoint(reader));
            break;
          case LINESTRING:
            feature.setAttribute(THE_GEOM, geoJson.readLine(reader));
            break;
          case MULTILINESTRING:
            feature.setAttribute(THE_GEOM, geoJson.readMultiLine(reader));
            break;
          case POLYGON:
            feature.setAttribute(THE_GEOM, geoJson.readPolygon(reader));
            break;
          case MULTIPOLYGON:
            feature.setAttribute(THE_GEOM, geoJson.readMultiPolygon(reader));
            break;
        }
        Iterator iterator = properties.iterator();
        while (iterator.hasNext()) {
          String str = iterator.next().toString();
          JSONObject element = JSONObject.parseObject(features.get(i).toString());
          Object o = JSONObject.parseObject(element.getString(PROPERTIES)).get(str);
          feature.setAttribute(str, o);
        }
        writer.write();
      }
      writer.close();
      shapefileDataStore.dispose();
      map.put("status", 200);
      map.put("message", "shp转换success");
    } catch (Exception e) {
      map.put("status", 400);
      map.put("message", e.getMessage());
      e.printStackTrace();
    }
    return map;
  }

  /**
   * 读取geojosn文件
   */
  private static JSONObject readGeoJsonFile(String jsonPath) {
    // 读文件到Stringbuffer
    StringBuffer sb = new StringBuffer();
    BufferedReader br = null;
    try {
      br = new BufferedReader(new FileReader(jsonPath));
      String str;
      while ((str = br.readLine()) != null) {// 逐行读取
        sb.append(str + "\r\n");
      }
      br.close();
    } catch (Exception e) {
      if (br != null) {
        try {
          br.close();
        } catch (Exception exception) {
          exception.printStackTrace();
        }
      }
      e.printStackTrace();
    }
    return JSONObject.parseObject(sb.toString());
  }

  /**
   * 设置shp文件属性
   */
  private static ShapefileDataStore dataStore(Set properties, String strType, String shpPath,
      CoordinateReferenceSystem crs) {
    try {
      Class<?> geoType = null;
      switch (strType) {
        case POINT:
          geoType = Point.class;
          break;
        case MULTIPOINT:
          geoType = MultiPoint.class;
          break;
        case LINESTRING:
          geoType = LineString.class;
          break;
        case MULTILINESTRING:
          geoType = MultiLineString.class;
          break;
        case POLYGON:
          geoType = Polygon.class;
          break;
        case MULTIPOLYGON:
          geoType = MultiPolygon.class;
          break;
      }
      // 创建shape文件对象
      File file = new File(shpPath);
      Map<String, Serializable> params = new HashMap<String, Serializable>();
      params.put(ShapefileDataStoreFactory.URLP.key, file.toURI().toURL());
      ShapefileDataStore ds = (ShapefileDataStore) new ShapefileDataStoreFactory().createNewDataStore(params);
      // 定义图形信息和属性信息
      SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
      //默认84坐标
      tb.setCRS(crs == null ? DefaultGeographicCRS.WGS84 : crs);
      tb.setName("shapefile");
      // 类型，Point/MultiPoint/LineString/MultiLineString/Polygon/MultiPolygon
      tb.add("the_geom", geoType);
      Iterator propertiesIter = properties.iterator();
      // 设置属性
      while (propertiesIter.hasNext()) {
        String str = propertiesIter.next().toString();
        // 此处设置为string
        tb.add(str, String.class);
      }
      ds.createSchema(tb.buildFeatureType());
      // 设置编码
      Charset charset = Charset.forName(GBK);
      ds.setCharset(charset);
      return ds;
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return null;

  }

  /**
   * shp文件转换geojson数据
   */
  public static Map<String, Object> shp2Geojson(String shpPath, String jsonPath) {
    Map<String, Object> map = new HashMap();
    //新建json对象

    JSONObject geojsonObject = new JSONObject();
    geojsonObject.put("type", "FeatureCollection");
    try {
      JSONArray array = new JSONArray();
      String fileName = readShpContent(shpPath, array);
      geojsonObject.put("features", array);
      geojsonObject.put("name", fileName);
      String crs = getCoordinateSystemWKT(shpPath);
      //GEOGCS表示这个是地址坐标系,PROJCS则表示是平面投影坐标系
      JSONObject crsJson = new JSONObject();
      JSONObject proJson = new JSONObject();
      crsJson.put("type", "name");
      if (crs.startsWith("PROJCS")) {
        proJson.put("name", "urn:ogc:def:crs:EPSG::3857");
        crsJson.put("properties", proJson);
      } else {
        proJson.put("name", "urn:ogc:def:crs:OGC:1.3:CRS84");
        crsJson.put("properties", proJson);
      }
      geojsonObject.put("crs", crsJson);

      //            itertor.close();

      long startTime = System.currentTimeMillis();

      //将json字符串使用字符流写入文件
/*            File outputfile=new File(jsonPath);
            BufferedWriter bufferedWriter=new BufferedWriter(new FileWriter(outputfile));
            bufferedWriter.write(JSON.toJSONString(geojsonObject));
            bufferedWriter.flush();
            bufferedWriter.close();*/
      File outputfile = new File(jsonPath);
      FileOutputStream fileOutputStream = new FileOutputStream(outputfile);
      OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, "utf-8");
      outputStreamWriter.write(JSON.toJSONString(geojsonObject));
      outputStreamWriter.flush();
      outputStreamWriter.close();

      //将json字符串使用字节流写入文件
/*            File outputfile=new File(jsonPath);
            BufferedOutputStream bufferedOutputStream=new BufferedOutputStream(new FileOutputStream(outputfile));
            byte[] bytes= JSON.toJSONString(geojsonObject).getBytes("utf-8");
            bufferedOutputStream.write(bytes);
            //fileOutputStream.write(JSON.toJSONString(geojsonObject));
            bufferedOutputStream.flush();
            bufferedOutputStream.close();*/

      //            long endTime=System.currentTimeMillis();
      //            System.out.println("当前程序耗时："+(endTime-startTime)+"ms");
    } catch (Exception e) {
      map.put("status", "failure");
      map.put("message", e.getMessage());
      e.printStackTrace();

    }
    //
    return geojsonObject;
  }

  private static String readShpContent(String shpPath, JSONArray array) {
    String fileName = "";
    try {
      FeatureJSON fjson = new FeatureJSON();
      //获取featurecollection
      File file = new File(shpPath);
      ShapefileDataStore shpDataStore = null;
      shpDataStore = new ShapefileDataStore(file.toURL());
      //设置编码
/*            Charset charset = Charset.forName("GBK");
            shpDataStore.setCharset(charset);*/
      fileName = shpDataStore.getTypeNames()[0];
      SimpleFeatureSource featureSource = null;
      featureSource = shpDataStore.getFeatureSource(fileName);
      SimpleFeatureCollection result = featureSource.getFeatures();
      SimpleFeatureIterator itertor = result.features();

      //遍历feature转为json对象
      while (itertor.hasNext()) {
        SimpleFeature feature = itertor.next();
        StringWriter writer = new StringWriter();
        fjson.writeFeature(feature, writer);
        String temp = writer.toString();
        Object geometry = JSONObject.parseObject(temp).getString(GEOMETRY);
        byte[] b = temp.getBytes("iso8859-1");
        temp = new String(b, GBK);
        JSONObject json = JSON.parseObject(temp);
        array.add(json);
      }
      itertor.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return fileName;
  }

  /**
   * 获取Shape文件的坐标系信息,GEOGCS表示这个是地址坐标系,PROJCS则表示是平面投影坐标系
   *
   * @shpPath
   */
  public static String getCoordinateSystemWKT(String shpPath) {
    ShapefileDataStoreFactory factory = new ShapefileDataStoreFactory();
    ShapefileDataStore dataStore = null;
    try {
      dataStore = (ShapefileDataStore) factory.createDataStore(new File(shpPath).toURI().toURL());
      return dataStore.getSchema().getCoordinateReferenceSystem().toWKT();
    } catch (UnsupportedOperationException | IOException e) {
      e.printStackTrace();
    } finally {
      dataStore.dispose();
    }
    return "";
  }
}