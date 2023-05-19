package com.iglens;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;

public class 钱磊项目中气象转灰度图 {
  // 中国区域气象数据转灰度图
  // 读取数据库记录
  // 跨度 0.5度
  // 完整气象数据 抓取数量
  static int grabNum = 7200;

  // 灰度图宽度像素点个数
  static int grayWidth = 120;
  // 灰度图高度像素点个数
  static int grayHeight = 60;
  // 云层 最高最低值
  static int clouds_max = 100;
  static int clouds_min = 0;
  // 能见度 最高最低值
  static int visibility_max = 11;
  static int visibility_min = 0;
  // 降水概率 最高最低值
  static int precipitationProbability_max = 100;
  static int precipitationProbability_min = 0;
  // 降水量 最高最低值 mm
  static int precipitationAmount_max = 300;
  static int precipitationAmount_min = 0;
  // 风速 最高最低值
  static int windSpeed_max = 30;
  static int windSpeed_min = 0;
  // 气温 最高最低值
  static int temperature_max = 50;
  static int temperature_min = -70;
  // 露点 最高最低值
  static int dewPoint_max = 50;
  static int dewPoint_min = -70;
  // 相对湿度 最高最低值
  static int humidity_max = 100;
  static int humidity_min = 0;
  // 气压 最高最低值
  static int pressure_max = 1050;
  static int pressure_min = 930;

  public static void main(String[] args) {
    Path dayPath =
        Paths.get(
            "E:\\WorkSpace\\gitWorkSpace\\Demo\\varietyOfTools\\src\\main\\resources\\example\\windSpeed_2022071623.txt");

    List<String> typeList =
        Arrays.asList(
            "clouds",
            "visibility",
            "precipitationProbability",
            "precipitationAmount",
            "windSpeed",
            "temperature",
            "dewPoint",
            "humidity",
            "pressure");
    try {
      // for (String type : typeList) {
      String type = "windSpeed";
      Path pngFilePath = dayPath.resolve(type + "_" + "tets" + ".png");
      if (!Files.exists(pngFilePath)) {
        Path txtFilePath = dayPath;

        List<String> rows = Files.lines(txtFilePath).collect(Collectors.toList());
        rows = rows.subList(6, rows.size());

        int[] imgframeData = createImgframeData(rows, type);

        // }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static int[] createImgframeData(List<String> rows, String type) {
    int dataSize = grayWidth * grayHeight * 4;
    int[] imgframeData = new int[dataSize];

    int max = 0;
    int min = 0;

    switch (type) {
      case "clouds":
        max = clouds_max;
        min = clouds_min;
        break;
      case "visibility":
        max = visibility_max;
        min = visibility_min;
        break;
      case "precipitationProbability":
        max = precipitationProbability_max;
        min = precipitationProbability_min;
        break;
      case "precipitationAmount":
        max = precipitationAmount_max;
        min = precipitationAmount_min;
        break;
      case "windSpeed":
        max = windSpeed_max;
        min = windSpeed_min;
        break;
      case "temperature":
        max = temperature_max;
        min = temperature_min;
        break;
      case "dewPoint":
        max = dewPoint_max;
        min = dewPoint_min;
        break;
      case "humidity":
        max = humidity_max;
        min = humidity_min;
        break;
      case "pressure":
        max = pressure_max;
        min = pressure_min;
        break;
      default:
        throw new IllegalStateException("Unexpected value: " + type);
    }

    BufferedImage image = new BufferedImage(grayWidth, grayHeight, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2d = image.createGraphics();

    for (int i = 0; i < rows.size(); i++) {
      String[] cols = rows.get(i).trim().split("\\s+");
      for (int j = 0; j < cols.length; j++) {
        String col = cols[j];
        double d = Double.parseDouble(col);
        int gridValue = (int) d;
        int gray = min;
        if (gridValue != -9999) {
          gray = (int) Math.round(255 * (gridValue - min) / (max - min));
        }
        if (gray > 255) {
          gray = 255;
        }
        Color color = new Color(gray, 0, 0, 255);
        g2d.setColor(color);
        g2d.fillRect(j, i, 1, 1);
      }
    }
    g2d.dispose();

    try {
      File output = new File("output" + ".png");
      ImageIO.write(image, "png", output);
      System.out.println("PNG file saved to " + output.getAbsolutePath());
    } catch (IOException e) {
      e.printStackTrace();
    }

    return imgframeData;
  }
}
