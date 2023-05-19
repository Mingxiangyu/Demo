package com.iglens.图片;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class 生成RGBA格式的PNG {
  public static void main(String[] args) {
    try {
      int width = 512;
      int height = 512;
      BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
      Graphics2D g2d = image.createGraphics();
      // setColor（） 设置颜色
      g2d.setColor(Color.RED);
      Color color = new Color(255, 0, 0, 10); // 红色，透明度为 50%
      g2d.setColor(color);
      // fillRect（） 设置颜色 x表示矩形左上角的 x 坐标。 y表示矩形左上角的 y 坐标。 width表示矩形的宽度。 height表示矩形的高度。
      g2d.fillRect(width / 2, height / 2, 1, 1);
      g2d.dispose();
      File output = new File("output.png");
      ImageIO.write(image, "png", output);
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
    }
  }
}
