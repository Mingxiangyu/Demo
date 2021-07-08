//package 地理;
//
//import com.vividsolutions.jts.geom.Geometry;
//import com.vividsolutions.jts.io.WKTReader;
//import java.io.IOException;
//import java.io.StringReader;
//import java.io.StringWriter;
//import org.geotools.data.DataUtilities;
//import org.geotools.feature.SchemaException;
//import org.geotools.feature.simple.SimpleFeatureBuilder;
//import org.geotools.geojson.feature.FeatureJSON;
//import org.geotools.geojson.geom.GeometryJSON;
//import org.geotools.geometry.jts.JTSFactoryFinder;
//import org.locationtech.jts.geom.GeometryFactory;
//import org.opengis.feature.simple.SimpleFeature;
//import org.opengis.feature.simple.SimpleFeatureType;
//import org.opengis.geometry.coordinate.LineString;
//
//public class GeoJson转换 {
//
//  public static void main(String[] args) throws IOException, SchemaException, ParseException {
////
////    // 由wkt字符串构造LineString对象
////    WKTReader reader = new WKTReader();
////    LineString lineString =
////        (LineString)
////            reader.read(
////                "LINESTRING (254058.76074485347 475001.2186020431, 255351.04293761664 474966.9279243938)");
////    // 设置保留6位小数，否则GeometryJSON默认保留4位小数
//    GeometryJSON geometryJson = new GeometryJSON(6);
////    StringWriter writer = new StringWriter();
////    geometryJson.write((Geometry) lineString, writer);
////    System.out.println(writer.toString());
////    writer.close();
//
////
//    LineString lineString1 = (LineString) geometryJson.read(new StringReader("{\n" +
//        "                \"type\": \"LineString\",\n" +
//        "                \"coordinates\": [\n" +
//        "                    [\n" +
//        "                        120.6584555,\n" +
//        "                        30.45144\n" +
//        "                    ],\n" +
//        "                    [\n" +
//        "                        120.1654515,\n" +
//        "                        30.54848\n" +
//        "                    ]\n" +
//        "                ]\n" +
//        "            }"));
//
//    // geometry是必须的，其他属性可根据需求自定义，但是支持的类型有限，例如这个版本中double是不支持的，只支持float
//    final SimpleFeatureType TYPE = DataUtilities.createType("Link",
//        "geometry:LineString," + // <- the geometry attribute: Point type
//            "gid:String," +   // <- a String attribute
//            "direction:Integer," +   // a number attribute
//            "orientation:Integer"
//    );
//    SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(TYPE);
//    GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
//    WKTReader reader = new WKTReader( geometryFactory );
//    FeatureJSON fjson = new FeatureJSON();
//    LineString lineString = (LineString)reader.read("LINESTRING (254058.76074485347 475001.2186020431, 255351.04293761664 474966.9279243938)");
//// 按照TYPE中声明的顺序为属性赋值就可以，其他方法我暂未尝试
//    featureBuilder.add(lineString);
//    featureBuilder.add("123456");
//    featureBuilder.add(2);
//    featureBuilder.add(0);
//    SimpleFeature feature = featureBuilder.buildFeature(null);
//    StringWriter writer = new StringWriter();
//    fjson.writeFeature(feature, writer);
//    System.out.println(writer.toString());
//  }
//
//  private static void lineStringToGeojson() throws ParseException, IOException {
//    // 由wkt字符串构造LineString对象
//    WKTReader reader = new WKTReader();
//    LineString lineString =
//        (LineString)
//            reader.read(
//                "LINESTRING (254058.76074485347 475001.2186020431, 255351.04293761664 474966.9279243938)");
//    // 设置保留6位小数，否则GeometryJSON默认保留4位小数
//    GeometryJSON geometryJson = new GeometryJSON(6);
//    StringWriter writer = new StringWriter();
////    geometryJson.write((Geometry) lineString, writer);
//    System.out.println(writer.toString());
//    writer.close();
//  }
//
//}
