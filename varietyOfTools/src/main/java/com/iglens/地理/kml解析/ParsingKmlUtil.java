package com.iglens.地理.kml解析;

import de.micromata.opengis.kml.v_2_2_0.Boundary;
import de.micromata.opengis.kml.v_2_2_0.Coordinate;
import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.Feature;
import de.micromata.opengis.kml.v_2_2_0.Folder;
import de.micromata.opengis.kml.v_2_2_0.Geometry;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.LineString;
import de.micromata.opengis.kml.v_2_2_0.LinearRing;
import de.micromata.opengis.kml.v_2_2_0.MultiGeometry;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import de.micromata.opengis.kml.v_2_2_0.Point;
import de.micromata.opengis.kml.v_2_2_0.Polygon;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @program: my_project
 * @description: KML文件解析：先获取kml文件的根节点，依次遍历当前节点的子节点的信息，
 * 如果遇到节点属于Folder、Document则继续解析其子节点；反之则解析PlaceMark节点（主要解析LineString、Point、Polygon）。
 * @author T480S
 * @create: 2018-12-01 12:39
 **/
public class ParsingKmlUtil {
  //以下三行都是自定义的KML类，用于获取名称name、所有点points、样式颜色color
  private List<KmlPoint> kmlPointList = new ArrayList<>();
  private List<KmlLine> kmlLineList = new ArrayList<>();
  private List<KmlPolygon> kmlPolygonList = new ArrayList<>();
  private KmlProperty kmlProperty = new KmlProperty();

  /**
   * 保存kml数据到临时表
   *
   * @param file 上传的文件实体
   * @return 自定义的KML文件实体
   */
  public KmlProperty parseKmlForJAK(File file) {
    Kml kml = Kml.unmarshal(file);
    Feature feature = kml.getFeature();
    parseFeature(feature);
    kmlProperty.setKmlPoints(kmlPointList);
    kmlProperty.setKmlLines(kmlLineList);
    kmlProperty.setKmlPolygons(kmlPolygonList);
    return kmlProperty;
  }

  /**
   * 解析kml节点信息
   *
   * @param feature 需要解析到要素信息
   * @return
   */
  private void parseFeature(Feature feature) {
    if (feature != null) {
      //判断根节点是否为Document
      if (feature instanceof Document) {
        List<Feature> featureList = ((Document) feature).getFeature();
        //遍历已获取的节点信息(节点信息为List)，将list使用forEach进行遍历（同for、while）
        featureList.forEach(documentFeature -> {
              //判断遍历节点是否为PlaceMark，否则迭代解析
              if (documentFeature instanceof Placemark) {
                getPlaceMark((Placemark) documentFeature);
              } else {
                parseFeature(documentFeature);
              }
            }
        );
      } else if (feature instanceof Folder) {
        //原理同上
        List<Feature> featureList = ((Folder) feature).getFeature();
        featureList.forEach(documentFeature -> {
              if (documentFeature instanceof Placemark) {
                getPlaceMark((Placemark) documentFeature);
              }
              {
                parseFeature(documentFeature);
              }
            }
        );
      }
    }
  }

  /**
   * 解析PlaceMark节点下的信息
   *
   * @return
   */
  private void getPlaceMark(Placemark placemark) {
    Geometry geometry = placemark.getGeometry();
    String name = placemark.getName();
    parseGeometry(name, geometry);
  }

  /**
   * 解析PlaceMark节点下的信息
   *
   * @return
   */
  private void parseGeometry(String name, Geometry geometry) {
    if (geometry != null) {
      if (geometry instanceof Polygon) {
        Polygon polygon = (Polygon) geometry;
        Boundary outerBoundaryIs = polygon.getOuterBoundaryIs();
        if (outerBoundaryIs != null) {
          LinearRing linearRing = outerBoundaryIs.getLinearRing();
          if (linearRing != null) {
            List<Coordinate> coordinates = linearRing.getCoordinates();
            if (coordinates != null) {
              outerBoundaryIs = ((Polygon) geometry).getOuterBoundaryIs();
              addPolygonToList(kmlPolygonList, name, outerBoundaryIs);
            }
          }
        }
      } else if (geometry instanceof LineString) {
        LineString lineString = (LineString) geometry;
        List<Coordinate> coordinates = lineString.getCoordinates();
        if (coordinates != null) {
          coordinates = ((LineString) geometry).getCoordinates();
          addLineStringToList(kmlLineList, coordinates, name);
        }
      } else if (geometry instanceof Point) {
        Point point = (Point) geometry;
        List<Coordinate> coordinates = point.getCoordinates();
        if (coordinates != null) {
          coordinates = ((Point) geometry).getCoordinates();
          addPointToList(kmlPointList, coordinates, name);
        }
      } else if (geometry instanceof MultiGeometry) {
        List<Geometry> geometries = ((MultiGeometry) geometry).getGeometry();
        for (Geometry geometryToMult : geometries) {
          Boundary outerBoundaryIs;
          List<Coordinate> coordinates;
          if (geometryToMult instanceof Point) {
            coordinates = ((Point) geometryToMult).getCoordinates();
            addPointToList(kmlPointList, coordinates, name);
          } else if (geometryToMult instanceof LineString) {
            coordinates = ((LineString) geometryToMult).getCoordinates();
            addLineStringToList(kmlLineList, coordinates, name);
          } else if (geometryToMult instanceof Polygon) {
            outerBoundaryIs = ((Polygon) geometryToMult).getOuterBoundaryIs();
            addPolygonToList(kmlPolygonList, name, outerBoundaryIs);
          }
        }
      }
    }
  }

  /**
   * 将kml中所有面添加到一个list
   *
   * @return
   */
  private void addPolygonToList(List<KmlPolygon> kmlPolygonList, String name, Boundary outerBoundaryIs) {
    LinearRing linearRing;
    List<Coordinate> coordinates;
    linearRing = outerBoundaryIs.getLinearRing();//面
    coordinates = linearRing.getCoordinates();
    KmlPolygon kmlPolygon = new KmlPolygon();
    kmlPolygon.setPoints(coordinates);
    kmlPolygon.setName(name);
    kmlPolygonList.add(kmlPolygon);
  }

  /**
   * 将kml中所有线添加到一个list
   *
   * @return
   */
  private void addLineStringToList(List<KmlLine> kmlLineList, List<Coordinate> coordinates, String name) {
    KmlLine kmlLine = new KmlLine();
    kmlLine.setPoints(coordinates);
    kmlLine.setName(name);
    kmlLineList.add(kmlLine);
  }

  /**
   * 将kml中所有点添加到一个list
   *
   * @return
   */
  private void addPointToList(List<KmlPoint> kmlPointList, List<Coordinate> coordinates, String name) {
    KmlPoint kmlPoint = new KmlPoint();
    kmlPoint.setName(name);
    kmlPoint.setPoints(coordinates);
    kmlPointList.add(kmlPoint);
  }
}
