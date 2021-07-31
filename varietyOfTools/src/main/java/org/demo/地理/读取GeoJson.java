package org.demo.地理;

import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.geotools.data.DataUtilities;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.geojson.geom.GeometryJSON;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class 读取GeoJson {

  private static Logger logger = LoggerFactory.getLogger(读取GeoJson.class);

  /**
   * 将存储实体类的集合，转化成geojson字符串
   *
   * @param obj 需要转化成实体类的集合
   * @param entity 实体类的class文件
   * @param fieleName 属性名字
   * @param fieldType 属性类型
   * @param geometry geometry类型
   * @return
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  public static String objectToGeojsonString(
      List obj, Class entity, String fieleName, String fieldType, String geometry) {
    List<String> fieldname = splitString(fieleName);
    List<String> fieldtype = splitString(fieldType);
    if (obj == null) {
      return "[]";
    } else if (obj.size() == 0) {
      return "[]";
    } else {
      String result = null;
      // 处理拼接在geojson中的属性名字以及类型
      if (fieldname.size() == fieldtype.size()) {
      } else {
         logger.info("属性名字以及类型格式不对！");
        return "";
      }
      try {
        Class clazz = entity;
        String geojsonContent = "";
        List<Method> fieldGetMethods = new ArrayList<Method>();
        StringBuilder content = new StringBuilder();
        for (int i = 0; i < fieldname.size(); i++) {
          if (i == 0) {
            content.append("geometry");
            content.append(":");
            content.append(geometry);
            content.append(",");
          } else {
            content.append(fieldname.get(i));
            content.append(":");
            content.append(fieldtype.get(i));
            content.append(",");
          }
          String name = fieldname.get(i);
          String methodname = "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
          Method method = clazz.getMethod(methodname);
          fieldGetMethods.add(method);
        }
        geojsonContent = content.toString();
        SimpleFeatureType TYPE = DataUtilities.createType("Link", geojsonContent);
        FeatureJSON fjson = new FeatureJSON(new GeometryJSON(13));
        DefaultFeatureCollection featureCollection = new DefaultFeatureCollection();
        for (int i = 0; i < obj.size(); i++) {
          SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(TYPE);
          for (Method method : fieldGetMethods) {
            featureBuilder.add(method.invoke(obj.get(i)));
          }
          SimpleFeature feature = featureBuilder.buildFeature(null);
          featureCollection.add(feature);
        }
        StringWriter writer = new StringWriter();
        fjson.writeFeatureCollection(featureCollection, writer);
        result = writer.toString();
      } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      return result;
    }
  }

  /**
   * 将所传的一些id拆分转化
   *
   * @param id 信息的id，如1，2，3
   * @return String类型的list
   */
  public static List<String> splitString(String id) {
    return Arrays.asList(id.split(","));
  }
}
