package 地理.kml解析;

import java.io.File;

public class Main {
  public static void main(String[] args) {
    KmlProperty kmlProperty;
    ParsingKmlUtil parsingKmlUtil = new ParsingKmlUtil();
    File file = new File("C:\\Users\\T480S\\Desktop\\doc.kml");
    kmlProperty = parsingKmlUtil.parseKmlForJAK(file);
    assert kmlProperty != null;

    if (kmlProperty.getKmlPoints().size() > 0) {
      for (KmlPoint k : kmlProperty.getKmlPoints()) {
        System.out.println(k.getName());
      }
      System.out.println("点");
    }
    if (kmlProperty.getKmlLines().size() > 0) {
      for (KmlLine k : kmlProperty.getKmlLines()) {
        System.out.println(k.getName());
      }
      System.out.println("线");
    }
    if (kmlProperty.getKmlPoints().size() > 0) {
      for (KmlPoint k : kmlProperty.getKmlPoints()) {
        System.out.println(k.getPoints());
      }
      System.out.println("面");
    }
  }
}
