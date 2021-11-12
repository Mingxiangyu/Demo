package org.demo.图片;

import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import javax.imageio.ImageIO;

public class 绘制多边形 {
  public static void main(String[] args) {

    String inputFilePath = "C:\\Users\\T480S\\Desktop\\H9OGeRPIxp.png";
    String outFilePath = "C:\\Users\\T480S\\Desktop\\20200615000000002.jpg";

    // 裁剪多边形
    int[] x = {0, 0, 150, 300, 600, 680}; // 横轴坐标，左上角为原点
    int[] y = {0, 150, 200, 400, 590, 680, 1500}; // 竖轴坐标，左上角为原点
    cutPolygonImage(inputFilePath, outFilePath, x, y);
  }

  /**
   * 裁剪(多边形)
   *
   * @param inputFilePath 图片输入路径
   * @param outFilePath 图片输出路径
   * @param x x轴坐标点数组
   * @param y y轴坐标点数组
   * @param n 坐标点数量
   */
  public static void cutPolygonImage(String inputFilePath, String outFilePath, int[] x, int[] y) {
    try {
      BufferedImage image = ImageIO.read(new File(inputFilePath));
      GeneralPath clip = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
      clip.moveTo(x[0], y[0]);

      for (int i = 1; i < x.length; i++) {
        clip.lineTo(x[i], y[i]);
      }
      clip.closePath(); // 闭合图形

      BufferedImage img =
          new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_BGR);
      Graphics2D g2d = img.createGraphics();
      // clip.transform(AffineTransform.getTranslateInstance(0, 0));
      g2d.setClip(clip);
      // g2d.translate(0, 0);
      g2d.drawImage(image, 0, 0, null);
      g2d.dispose();

      FileOutputStream out = new FileOutputStream(outFilePath); // 输出图片的地址
      ImageIO.write(img, "png", out);
      out.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
