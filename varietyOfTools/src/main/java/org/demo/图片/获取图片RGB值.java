package org.demo.图片;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

public class 获取图片RGB值 {
  public static void main(String[] args) throws IOException {
    File file =
        //        new File(
        //            "C:\\Users\\T480S\\Documents\\WeChat
        // Files\\aion_my_god\\FileStorage\\File\\2021-06\\GF1_PMS1_E114.0_N25.8_20191108_L1A0004375134-MSS1.tiff");
        new File("C:\\Users\\T480S\\Desktop\\test.png");
    BufferedImage bufferedImage = ImageIO.read(new FileInputStream(file));
    int width = bufferedImage.getWidth();
    int height = bufferedImage.getHeight();

    // 绝对值
    int whiteRGB = Color.white.getRGB();
    int blackRGB = Color.black.getRGB();
    // 生成灰度图片
    BufferedImage defaultGrayImage =
        new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    // 写入灰度图片，并获取最远默认宽坐标
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        int currentRgb = bufferedImage.getRGB(x, y);
//        if (currentRgb < whiteRGB) {
          // 写入灰度图片
          defaultGrayImage.setRGB(x, y, currentRgb);
//        }
//        System.out.println(currentRgb);
      }
    }
    File output = new File("C:\\Users\\T480S\\Desktop\\test1.png");
    ImageIO.write(defaultGrayImage, "png", output);
  }

}
