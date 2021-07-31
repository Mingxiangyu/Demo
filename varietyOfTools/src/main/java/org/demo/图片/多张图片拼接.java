package org.demo.图片;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

public class 多张图片拼接 {
  public static void main(String[] args) throws IOException {
    String imagePath1 = "E:\\Deploy-Track\\以往版本\\Win电脑部署\\nginx-1.18.0\\dist\\mapData\\1\\1\\0.png";
    String imagePath2 = "E:\\Deploy-Track\\以往版本\\Win电脑部署\\nginx-1.18.0\\dist\\mapData\\1\\1\\1.png";

    BufferedImage bufferedImage1 = ImageIO.read(new FileInputStream(new File(imagePath1)));
    BufferedImage bufferedImage2 = ImageIO.read(new FileInputStream(new File(imagePath2)));
    // 拼接后图片
    BufferedImage bufferedImage = mergeImage(false, bufferedImage1, bufferedImage2);

    File output = new File("C:\\Users\\T480S\\Desktop\\拼接后图片.png");
    ImageIO.write(bufferedImage, "png", output);
  }

  /**
   * 合并任数量的图片成一张图片
   *
   * @param isHorizontal true代表水平合并，fasle代表垂直合并
   * @param imgs 待合并的图片数组
   * @return
   * @throws IOException
   */
  public static BufferedImage mergeImage(boolean isHorizontal, BufferedImage... imgs)
      throws IOException {
    // 生成新图片
    BufferedImage destImage = null;
    // 计算新图片的长和高
    int allw = 0, allh = 0, allwMax = 0, allhMax = 0;
    // 获取总长、总宽、最长、最宽
    for (int i = 0; i < imgs.length; i++) {
      BufferedImage img = imgs[i];
      allw += img.getWidth();
      allh += img.getHeight();
      if (img.getWidth() > allwMax) {
        allwMax = img.getWidth();
      }
      if (img.getHeight() > allhMax) {
        allhMax = img.getHeight();
      }
    }
    // 创建新图片
    if (isHorizontal) {
      destImage = new BufferedImage(allw, allhMax, BufferedImage.TYPE_INT_RGB);
    } else {
      destImage = new BufferedImage(allwMax, allh, BufferedImage.TYPE_INT_RGB);
    }
    // 合并所有子图片到新图片
    int wx = 0, wy = 0;
    for (int i = 0; i < imgs.length; i++) {
      BufferedImage img = imgs[i];
      int w1 = img.getWidth();
      int h1 = img.getHeight();
      // 从图片中读取RGB
      int[] ImageArrayOne = new int[w1 * h1];
      ImageArrayOne = img.getRGB(0, 0, w1, h1, ImageArrayOne, 0, w1); // 逐行扫描图像中各个像素的RGB到数组中
      if (isHorizontal) { // 水平方向合并
        destImage.setRGB(wx, 0, w1, h1, ImageArrayOne, 0, w1); // 设置上半部分或左半部分的RGB
      } else { // 垂直方向合并
        destImage.setRGB(0, wy, w1, h1, ImageArrayOne, 0, w1); // 设置上半部分或左半部分的RGB
      }
      wx += w1;
      wy += h1;
    }
    return destImage;
  }
}
