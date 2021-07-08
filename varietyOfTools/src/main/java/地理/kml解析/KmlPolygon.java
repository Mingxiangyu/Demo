package 地理.kml解析;

import de.micromata.opengis.kml.v_2_2_0.Coordinate;
import java.util.List;

/**
 * @program: ParseKMLForJava
 * @description:
 * @author: Mr.Yue
 * @create: 2018-12-04 21:12
 **/
public class KmlPolygon {
  private List<Coordinate> points;
  private String name;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<Coordinate> getPoints() {
    return points;
  }

  public void setPoints(List<Coordinate> points) {
    this.points = points;
  }
}
