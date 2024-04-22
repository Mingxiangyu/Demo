// package com.iglens.地理;
//
// import it.geosolutions.geoserver.rest.GeoServerRESTManager;
// import it.geosolutions.geoserver.rest.GeoServerRESTPublisher;
// import it.geosolutions.geoserver.rest.GeoServerRESTReader;
// import it.geosolutions.geoserver.rest.encoder.datastore.GSShapefileDatastoreEncoder;
// import java.io.BufferedReader;
// import java.io.File;
// import java.io.FileNotFoundException;
// import java.io.InputStreamReader;
// import java.net.HttpURLConnection;
// import java.net.MalformedURLException;
// import java.net.URL;
// import java.nio.charset.Charset;
// import java.util.Base64;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.stereotype.Component;
//
// /**
//  * https://www.cnblogs.com/raorao1994/p/13397476.html
//  * https://www.cnblogs.com/s313139232/p/14183748.html
//  * https://blog.csdn.net/Xcong_Zhu/article/details/130510285
//  * https://www.dianjilingqu.com/641275.html
//  * https://blog.csdn.net/qq_52495835/article/details/134555633
//  */
// @Slf4j
// @Component
// public class GeoLocalPublishUtil {
//
//     // geoserver数据连接
//     @Value("${geoserver.geoServerUrl}")
//     private String geoServerUrl;
//     @Value("${geoserver.geoServerUsername}")
//     private String geoServerUsername;
//     @Value("${geoserver.geoServerPassword}")
//     private String geoServerPassword;
//     @Value("${geoserver.geoServerWorkspace}")
//     private String geoServerSpace;
//
//     // postgis数据连接
//
//     @Value("${writePg.pg_host}")
//     private String pg_host = "36.41.73.198";
//
//     @Value("${writePg.pg_port}")
//     private Integer pg_port = 25433;
//
//     @Value("${writePg.pg_database}")
//     private String pg_database = "postgres";
//
//     @Value("${writePg.pg_user}")
//     private String pg_user = "postgres";
//
//     @Value("${writePg.pg_password}")
//     private String pg_password = "postgres";
//
//     private final GeoPublishUtil geoPublishUtil;
//
//     public GeoLocalPublishUtil(GeoPublishUtil geoPublishUtil) {
//         this.geoPublishUtil = geoPublishUtil;
//     }
//
//     public void publishShape(String dataStoreName, String layerName, String afterName, String filePath) {
//         // File file = new File("/data/filedata/test/#zg_gis_osm_pofw_a_free_1.shp/gis_osm_pofw_a_free_1.shp"); // 此处为服务器地址
//         File zipFile = new File(filePath);
//         // File shpFile = new File("/data/filedata/test/#zg_gis_osm_pofw_a_free_1.shp/gis_osm_pofw_a_free_1.shp");
//
//
//         // 连接geoServer
//         GeoServerRESTManager geoServerRESTManager = null;
//         try {
//             geoServerRESTManager = new GeoServerRESTManager(new URL(geoServerUrl), geoServerUsername, geoServerPassword);
//         } catch (Exception e) {
//             System.out.println("远程连接GeoServer失败...");
//             e.printStackTrace();
//         }
//
//         // shp读写和发布
//         assert geoServerRESTManager != null;
//         GeoServerRESTReader restReader = geoServerRESTManager.getReader();
//         GeoServerRESTPublisher restPublisher = geoServerRESTManager.getPublisher();
//
//
//         // 存在相应的工作区
//         if (!restReader.existsWorkspace(geoServerSpace)) {
//             restPublisher.createWorkspace(geoServerSpace);
//         }
//
//         if (restReader.existsDatastore(geoServerSpace, dataStoreName)) {
//             restPublisher.removeDatastore(geoServerSpace, dataStoreName, true);
//         }
//         // 数据存储
//         // 创建shape文件存储
//         try {
//             // shp文件所在的位置
//             String urlDataStorePath = zipFile.getPath();
//             // 数据存储需要的文件
//             String shpFilePath = String.format("file://%s", urlDataStorePath);
//             URL urlShapeFile = new URL(shpFilePath);
//             // 创建数据集
//             GSShapefileDatastoreEncoder datastoreEncoder = new GSShapefileDatastoreEncoder(dataStoreName, urlShapeFile);
//             datastoreEncoder.setCharset(Charset.forName("GBK"));
//             geoServerRESTManager.getStoreManager().create(geoServerSpace, datastoreEncoder);
//         } catch (MalformedURLException e) {
//             e.printStackTrace();
//         }
//
//         // // style样式
//         // GisUtils gisUtils = new GisUtils();
//         // String shpType = gisUtils.getShpType("C:\\Users\\zhouhuilin\\Desktop\\众包测试源头\\OSM\\#heishan_gis_osm_landuse_a_free_1.shp\\gis_osm_landuse_a_free_1.shp");
//         //
//         // // 添加默认样式
//         // String strokeColor = "#8A8992";
//         // String strokeWidth = "1.0";
//         // String strokeTransparency = "1.0";
//         // String fillColor = "#8A8992";
//         // String fillTransparency = "1.0";
//         // String textColor = "";
//         // String labelTransparency = "";
//         // String textStrokeColor = "";
//         // String textStrokeTransparency = "";
//         // int textSize = 14;
//         // String textDisplay = "";
//         // String sld = "";
//         // if (Constant.DATA_GEOJSON_TYPE_LineString.equals(shpType)
//         //         || Constant.DATA_GEOJSON_TYPE_MultiLineString.equals(shpType)) {
//         //     sld = GeoStyleUtil.getLineSld(
//         //             null,
//         //             strokeColor,
//         //             strokeWidth,
//         //             strokeTransparency,
//         //             textColor,
//         //             labelTransparency,
//         //             textStrokeColor,
//         //             textStrokeTransparency,
//         //             textSize,
//         //             textDisplay);
//         // } else if (Constant.DATA_GEOJSON_TYPE_Polygon.equals(shpType)
//         //         || Constant.DATA_GEOJSON_TYPE_MultiPolygon.equals(shpType)) {
//         //     sld = GeoStyleUtil.getPolygonSld(
//         //             null,
//         //             strokeColor,
//         //             strokeWidth,
//         //             strokeTransparency,
//         //             fillColor,
//         //             fillTransparency,
//         //             textColor,
//         //             labelTransparency,
//         //             textStrokeColor,
//         //             textStrokeTransparency,
//         //             textSize,
//         //             textDisplay);
//         // } else if (Constant.DATA_GEOJSON_TYPE_Point.equals(shpType)
//         //         || Constant.DATA_GEOJSON_TYPE_MultiPoint.equals(shpType)) {
//         //     sld = GeoStyleUtil.getPointSld(
//         //             null,
//         //             strokeColor,
//         //             strokeWidth,
//         //             strokeTransparency,
//         //             fillColor,
//         //             fillTransparency,
//         //             null,
//         //             textColor,
//         //             labelTransparency,
//         //             textStrokeColor,
//         //             textStrokeTransparency,
//         //             textSize,
//         //             textDisplay);
//         // }
//         // boolean b = geoPublishUtil.geoserverPublishSLDBodyStyle(
//         //         geoServerWorkspace, sld, geoServerTablename);
//         // if (!b) {
//         //     log.error("服务发布失败:{}", geoServerTablename);
//         // }
//         //
//         // GSLayerEncoder layerEncoder = new GSLayerEncoder();
//         //  layerEncoder.setDefaultStyle(geoServerWorkspace, geoServerTablename);
//         //
//         //
//         //
//         // if (!restReader.existsLayer(geoServerWorkspace, geoServerTablename)) {
//         //     try {
//         //         GSFeatureTypeEncoder gsFeatureTypeEncoder = new GSFeatureTypeEncoder();
//         //         gsFeatureTypeEncoder.setTitle(geoServerTablename);
//         //         gsFeatureTypeEncoder.setName(geoServerTablename);
//         //         gsFeatureTypeEncoder.setSRS(GeoServerRESTPublisher.DEFAULT_CRS);
//         //
//         //         boolean layer = restPublisher.publishDBLayer(geoServerWorkspace, geoServerTablename, gsFeatureTypeEncoder, layerEncoder);
//         //         System.out.println("publish layer : " + layer);
//         //     } catch (Exception e) {
//         //         e.printStackTrace();
//         //     }
//         // }
//
//         // 如果该图层存在，则删除
//         if (restReader.existsLayer(geoServerSpace, layerName)) {
//             restPublisher.unpublishFeatureType(geoServerSpace, dataStoreName, layerName);
//             restPublisher.removeLayer(geoServerSpace, layerName);
//             restPublisher.removeNamespace(layerName, true);
//         }
//         // 发布shape
//         try {
//             boolean shape = restPublisher.publishShp(geoServerSpace, dataStoreName, layerName, zipFile, GeoServerRESTPublisher.DEFAULT_CRS);
//
//             System.out.println("publish shape : " + shape);
//         } catch (FileNotFoundException e) {
//             e.printStackTrace();
//         }
//         geoModifyNameTool(geoServerUrl, geoServerSpace, dataStoreName, layerName, afterName);
//         /*// 创建发布类
//         GeoServerRESTPublisher geoServerRESTPublisher = new GeoServerRESTPublisher(url, user, password);
//         try {
//             boolean flag = geoServerRESTPublisher.publishShp(geoServerWorkspace, dataSetName,
//                     new NameValuePair[]{new NameValuePair("charset", "GBK")},
//                     dataSetName,
//                     GeoServerRESTPublisher.UploadMethod.FILE,
//                     file.toURI(),
//                     GeoServerRESTPublisher.DEFAULT_CRS,
//                     styleName);
//             System.out.println("publish shp : " + flag);
//         } catch (FileNotFoundException e) {
//             e.printStackTrace();
//         }*/
//     }
//
//
//     /**
//      * 修改图层标题工具
//      *
//      * @param geoServerUrl geoserver服务连接
//      * @param workspaces   工作空间
//      * @param dataStores   储存空间
//      * @param name         图层特征类型名称
//      * @param afterName    修改后的标题
//      */
//     public void geoModifyNameTool(String geoServerUrl, String workspaces, String dataStores, String name, String afterName) {
//         try {
//             String url = geoServerUrl + "/rest/workspaces/" + workspaces + "/datastores/" + dataStores + "/featuretypes/" + name;
//             URL obj = new URL(url);
//             HttpURLConnection con = (HttpURLConnection) obj.openConnection();
//
//             // 设置请求方法为PUT
//             con.setRequestMethod("PUT");
//
//             // 设置请求头部信息
//             con.setRequestProperty("Content-Type", "application/json");
//             con.setRequestProperty("Authorization", "Basic " + Base64.getEncoder().encodeToString((geoServerUsername + ":" + geoServerPassword).getBytes()));
//
//             // 设置请求体数据
//             String data = "{\n" +
//                     " \"featureType\": {" +
//                     " \"title\": \"" + afterName + "\"\n" +
//                     "}\n}";
//             con.setDoOutput(true);
//             con.getOutputStream().write(data.getBytes("UTF-8"));
//
//             // 发送请求并获取响应
//             int responseCode = con.getResponseCode();
//             BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
//             String inputLine;
//             StringBuilder response = new StringBuilder();
//             while ((inputLine = in.readLine()) != null) {
//                 response.append(inputLine);
//             }
//             in.close();
//
//             // 打印响应结果
//             System.out.println("Response Code: " + responseCode);
//             System.out.println("Response Body: " + response.toString());
//         } catch (Exception e) {
//             e.printStackTrace();
//         }
//     }
//
// }
