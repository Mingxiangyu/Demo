package org.demo;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.geotools.data.FeatureSource;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.PropertyType;
import org.opengis.filter.Filter;

public class 读取shapefile文件内容 {

  /** create by: REID description: GeoTools工具类 create time: 8:45 2020/4/17 */

  /**
   * 读取Shapefiles文件表内容和对应表数据
   *
   * @param SHPFile Shapefiles文件
   * @return Map<（entity/datas）, List（对应map数据）>
   */
  public static Map<String, List> readSHP(File SHPFile) throws Exception {

    // 一个数据存储实现，允许从Shapefiles读取和写入
    ShapefileDataStore shpDataStore = new ShapefileDataStore(SHPFile.toURI().toURL());
    shpDataStore.setCharset(Charset.forName("UTF-8"));

    // 获取这个数据存储保存的类型名称数组
    // getTypeNames:获取所有地理图层
    String typeName = shpDataStore.getTypeNames()[0];

    // 通过此接口可以引用单个shapefile、数据库表等。与数据存储进行比较和约束
    FeatureSource<SimpleFeatureType, SimpleFeature> featureSource = null;
    featureSource =
        (FeatureSource<SimpleFeatureType, SimpleFeature>) shpDataStore.getFeatureSource(typeName);

    // 一个用于处理FeatureCollection的实用工具类。提供一个获取FeatureCollection实例的机制
    FeatureCollection<SimpleFeatureType, SimpleFeature> result = featureSource.getFeatures();

    FeatureIterator<SimpleFeature> iterator = result.features();

    // 迭代
    int stop = 0;
    Map<String, List> map = new HashMap<>();
    List<Map> entity = new ArrayList<>();
    List<Map> datas = new ArrayList<>();
    while (iterator.hasNext()) {
      SimpleFeature feature = iterator.next();
      Collection<Property> p = feature.getProperties();
      Iterator<Property> it = p.iterator();
      // 构建实体

      // 特征里面的属性再迭代,属性里面有字段
      String name;
      Map<String, Object> data = new HashMap<>();
      while (it.hasNext()) {
        Property pro = it.next();
        name = pro.getName().toString();
        if (stop == 0) {
          Map<String, Object> et = new HashMap<>();
          PropertyType propertyType = pro.getType();
          Class cls = propertyType.getBinding();
          String className = cls.getName();
          String tName = className.substring(className.lastIndexOf(".") + 1);
          Filter filter =
              propertyType.getRestrictions().isEmpty()
                  ? null
                  : propertyType.getRestrictions().get(0);
          String typeLength = filter != null ? filter.toString() : "0";
          Pattern pattern = Pattern.compile("[^0-9]");
          Matcher matcher = pattern.matcher(typeLength);
          String tLength = matcher.replaceAll("").trim();
          et.put("name", name);
          et.put("type", tName);
          et.put("length", tLength);
          entity.add(et);
        }

        data.put(name, pro.getValue().toString());
      } // end 里层while

      datas.add(data);
      stop++;
    } // end 最外层 while
    map.put("entity", entity);
    map.put("datas", datas);
    iterator.close();
    return map;
  }

  /**
   * 读取Shapefiles文件表内容不包含内容数据
   *
   * @param SHPFile Shapefiles文件
   * @return List<（Map）, List（对应map数据）>
   */
  public static List readSHPNoData(File SHPFile) throws Exception {

    // 一个数据存储实现，允许从Shapefiles读取和写入
    ShapefileDataStore shpDataStore = null;
    shpDataStore = new ShapefileDataStore(SHPFile.toURI().toURL());
    shpDataStore.setCharset(Charset.forName("UTF-8"));

    // 获取这个数据存储保存的类型名称数组
    // getTypeNames:获取所有地理图层
    String typeName = shpDataStore.getTypeNames()[0];

    // 通过此接口可以引用单个shapefile、数据库表等。与数据存储进行比较和约束
    FeatureSource<SimpleFeatureType, SimpleFeature> featureSource = null;
    featureSource =
        (FeatureSource<SimpleFeatureType, SimpleFeature>) shpDataStore.getFeatureSource(typeName);

    // 一个用于处理FeatureCollection的实用工具类。提供一个获取FeatureCollection实例的机制
    FeatureCollection<SimpleFeatureType, SimpleFeature> result = featureSource.getFeatures();

    FeatureIterator<SimpleFeature> iterator = result.features();

    // 迭代
    int stop = 0;
    List<Map> entity = new ArrayList<>();
    while (iterator.hasNext()) {
      SimpleFeature feature = iterator.next();
      Collection<Property> p = feature.getProperties();
      Iterator<Property> it = p.iterator();
      // 构建实体

      // 特征里面的属性再迭代,属性里面有字段
      String name;
      Map<String, Object> data = new HashMap<>();
      while (it.hasNext()) {
        Property pro = it.next();
        name = pro.getName().toString();
        if (stop == 0) {
          Map<String, Object> et = new HashMap<>();
          PropertyType propertyType = pro.getType();
          Class cls = propertyType.getBinding();
          String className = cls.getName();
          String tName = className.substring(className.lastIndexOf(".") + 1);
          Filter filter =
              propertyType.getRestrictions().isEmpty()
                  ? null
                  : propertyType.getRestrictions().get(0);
          String typeLength = filter != null ? filter.toString() : "0";
          Pattern pattern = Pattern.compile("[^0-9]");
          Matcher matcher = pattern.matcher(typeLength);
          String tLength = matcher.replaceAll("").trim();
          et.put("name", name);
          et.put("type", tName);
          et.put("length", tLength);
          entity.add(et);
        }

        data.put(name, pro.getValue().toString());
      } // end 里层while

      stop++;
    } // end 最外层 while
    iterator.close();
    return entity;
  }
}
