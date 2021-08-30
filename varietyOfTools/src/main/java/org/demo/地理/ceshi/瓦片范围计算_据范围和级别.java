package org.demo.地理.ceshi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.WKTReader;

/**
 * @author T480S
 */
public class 瓦片范围计算_据范围和级别 {
  Point originPoint = new Point(-180.0, 180.0);

  public static void main(String[] args) throws Exception {
    String wkt =
        "POLYGON((78.718774  39.478792,79.775255  39.478792,79.570621  38.888686,78.718774  39.478792))";
    瓦片范围计算_据范围和级别 瓦片范围计算据范围和级别 = new 瓦片范围计算_据范围和级别();
    Integer level = 12;
    ArrayList<String> arrayBuffer = 瓦片范围计算据范围和级别.getGrids(wkt, level);
    System.out.println(arrayBuffer.toString());
    for (String s : arrayBuffer) {
//      System.out.println(s);
    }
  }

  public ArrayList<String> getGrids(String queryWkt, Integer level) throws Exception {
    Map<String, String> map = new HashMap<>();
    ArrayList<String> arrayList = new ArrayList<>();
    Double resolution = TileLevel.getTileResolution(level);
    GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);
    WKTReader reader = new WKTReader(geometryFactory);

    Geometry geo = reader.read(queryWkt);
    // Polygon polygon = reader.read(queryWkt).asInstanceOf[Polygon];
    Envelope envelope = geo.getEnvelopeInternal();

    MapRowCol tileRowCol = getTileRowCol(envelope, resolution);
    long totalTileCount =
        (tileRowCol.endCol - tileRowCol.startCol + 1)
            * (tileRowCol.endRow - tileRowCol.startRow + 1); // 瓦片总个数
    for (long col = tileRowCol.startCol; col <= tileRowCol.endCol; col++) {
      for (long row = tileRowCol.startRow; row <= tileRowCol.endRow; row++) {
        arrayList.add(getEnvRange(row, col, resolution));
      }
    }
    return arrayList;
  }

  public Map<String, String> getGridsMap(String queryWkt, Integer level) throws Exception {
    Map<String, String> map = new HashMap<>();
    Double resolution = TileLevel.getTileResolution(level);
    GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);
    WKTReader reader = new WKTReader(geometryFactory);

    Geometry geo = reader.read(queryWkt);
    // Polygon polygon = reader.read(queryWkt).asInstanceOf[Polygon];
    Envelope envelope = geo.getEnvelopeInternal();

    MapRowCol tileRowCol = getTileRowCol(envelope, resolution);
    long totalTileCount =
        (tileRowCol.endCol - tileRowCol.startCol + 1)
            * (tileRowCol.endRow - tileRowCol.startRow + 1); // 瓦片总个数
    String keyName = "";
    for (long col = tileRowCol.startCol; col <= tileRowCol.endCol; col++) {
      for (long row = tileRowCol.startRow; row <= tileRowCol.endRow; row++) {
        //                arrayList.add(getEnvRange(row, col, resolution));
        keyName = level + "_" + col + "_" + row;
        map.put(keyName, getEnvRange(row, col, resolution));
      }
    }
    return map;
  }

  /*
  根据行列号和空间分辨率获取格网空间wkt串
  */
  public String getEnvRange(long row, long col, Double resolution) {
    String wkt;
    Integer tileSize = 256;
    Double minX = resolution * tileSize * col + originPoint.x;
    double maxX = resolution * tileSize * (col + 1) + originPoint.x;
    double minY = originPoint.y - resolution * tileSize * (row + 1);
    double maxY = originPoint.y - resolution * tileSize * row;

    String wktBuffer =
        minX + " " + maxY + "," + maxX + " " + maxY + "," + maxX + " " + minY + "," + minX + " "
            + minY + "," + minX + " " + maxY;
    wkt = "POLYGON((" + wktBuffer + "))";
    return wkt;
  }

  /*
  根据外接矩形和分辨率获取起止行列号
   */
  public MapRowCol getTileRowCol(Envelope env, Double resolution) {

    long startRow =
        Math.round(Math.floor(Math.abs((env.getMaxY()) - originPoint.y) / 256 / resolution));
    long endRow =
        Math.round((Math.floor(Math.abs((env.getMinY()) - originPoint.y) / 256 / resolution)));
    long startCol = Math.round((Math.floor((env.getMinX() - originPoint.x) / 256 / resolution)));
    long endCol = Math.round((Math.floor((env.getMaxX() - originPoint.x) / 256 / resolution)));
    return new MapRowCol(startRow, endRow, startCol, endCol);
  }

  public static String getWktByEnvelop(Envelope envelope) {

    String wktBuffer =
        envelope.getMinX()
            + " "
            + envelope.getMaxY()
            + ","
            + envelope.getMaxX()
            + " "
            + envelope.getMaxY()
            + ","
            + envelope.getMaxX()
            + " "
            + envelope.getMinY()
            + ","
            + envelope.getMinX()
            + " "
            + envelope.getMinY()
            + ","
            + envelope.getMinX()
            + " "
            + envelope.getMaxY();
    return "POLYGON((" + wktBuffer + "))";
  }

  public ArrayList<String> getIntersectGrids(Polygon unionPolygon, ArrayList<String> gridDatas)
      throws Exception {
    ArrayList<String> arrayList = new ArrayList<>();
    String queryWkt;
    GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory(null);
    WKTReader reader = new WKTReader(geometryFactory);
    Geometry geo;
    for (String gridData : gridDatas) {
      queryWkt = gridData;
      geo = reader.read(queryWkt);
      if (unionPolygon.intersects(geo)) {
        arrayList.add(queryWkt);
      }
    }
    return arrayList;
  }
}

@Data
class Point {
  double x;
  double y;

  public Point(double x, double y) {
   this.x = x;
   this.y = y;
  }
}

@Data
class MapRowCol {
  public Long startRow;
  public Long endRow;
  public Long startCol;
  public Long endCol;

  public MapRowCol(Long startRow, Long endRow, Long startCol, Long endcol) {
    this.startRow = startRow;
    this.endRow = endRow;
    this.startCol = startCol;
    this.endCol = endcol;
  }
}

class TileLevel {
  public static Double getTileResolution(Integer level) {
    double resolution;
    switch (level) {
      case 0:
        resolution = 0.703125000000001;
        break;
      case 1:
        resolution = 0.351562500000001;
        break;
      case 2:
        resolution = 0.17578125;
        break;
      case 3:
        resolution = 8.78906250000002E-02;
        break;
      case 4:
        resolution = 4.39453125000001E-02;
        break;
      case 5:
        resolution = 0.02197265625;
        break;
      case 6:
        resolution = 0.010986328125;
        break;
      case 7:
        resolution = 5.49316406250001E-03;
        break;
      case 8:
        resolution = 0.00274658203125;
        break;
      case 9:
        resolution = 0.001373291015625;
        break;
      case 10:
        resolution = 6.86645507812501E-04;
        break;
      case 11:
        resolution = 3.43322753906251E-04;
        break;
      case 12:
        resolution = 1.71661376953125E-04;
        break;
      case 13:
        resolution = 8.58306884765626E-05;
        break;
      case 14:
        resolution = 4.29153442382813E-05;
        break;
      case 15:
        resolution = 2.14576721191407E-05;
        break;
      case 16:
        resolution = 1.07288360595703E-05;
        break;
      case 17:
        resolution = 5.36441802978517E-06;
        break;
      case 18:
        resolution = 2.68220901489258E-06;
        break;
      case 19:
        resolution = 1.34110450744629E-06;
        break;
      default:
        resolution = 0.703125000000001;
        break;
    }
    return resolution;
  }
}
