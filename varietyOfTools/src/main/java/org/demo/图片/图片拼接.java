package org.demo.图片;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

public class 图片拼接 {
  public static void main(String[] args) throws IOException {
    String imagePath1 = "E:\\Deploy-DJ\\历史版本\\Win电脑部署\\nginx-1.18.0\\dist\\mapData\\1\\1\\0.png";
    String imagePath2 = "E:\\Deploy-DJ\\历史版本\\Win电脑部署\\nginx-1.18.0\\dist\\mapData\\1\\1\\1.png";
    String[] strings = {imagePath1, imagePath2};
    InputStream merge = merge(strings, 1);
    FileOutputStream fos = new FileOutputStream("C:\\Users\\T480S\\Desktop\\拼接后图片.png");

    byte[] b = new byte[1024];

    int length;

    while ((length = merge.read(b)) > 0) {

      fos.write(b, 0, length);
    }

    merge.close();

    fos.close();
  }

  /**
   * 图片拼接 江风成
   *
   * @param files 要拼接的文件列表
   * @param type2 纵向拼接
   * @return
   */
  public static InputStream merge(String[] files, int type) {
    ByteArrayInputStream in = null;
    try {
      /* 1 读取第一张图片 */
      File fileOne = new File(files[0]);
      BufferedImage imageFirst = ImageIO.read(fileOne);
      int width = imageFirst.getWidth(); // 图片宽度
      int height = imageFirst.getHeight(); // 图片高度
      int[] imageArrayFirst = new int[width * height]; // 从图片中读取RGB
      imageArrayFirst = imageFirst.getRGB(0, 0, width, height, imageArrayFirst, 0, width);
      /* 1 对第二张图片做相同的处理 */
      File fileTwo = new File(files[1]);
      BufferedImage imageSecond = ImageIO.read(fileTwo);
      int width1 = imageSecond.getWidth(); // 图片宽度
      int height2 = imageSecond.getHeight(); // 图片高度
      int[] imageArraySecond = new int[width1 * height2];
      imageArraySecond = imageSecond.getRGB(0, 0, width1, height2, imageArraySecond, 0, width1);
      int ww = width > width1 ? width : width1;
      // 生成新图片
      BufferedImage imageResult =
          new BufferedImage(ww, height2 + height, BufferedImage.TYPE_INT_RGB);
      int k = 0;
      for (int i = 0; i < height; i++) {
        for (int j = 0; j < ww; j++) {
          if (width > j) {
            imageResult.setRGB(j, i, imageArrayFirst[k]);
            k++;
          } else {
            imageResult.setRGB(j, i, -328966);
          }
        }
      }
      int k1 = 0;
      for (int i1 = 0; i1 < height2; i1++) {
        for (int j1 = 0; j1 < ww; j1++) {
          if (width1 > j1) {
            imageResult.setRGB(j1, i1 + height, imageArraySecond[k1]);
            k1++;
          } else {
            imageResult.setRGB(j1, i1 + height, -328966);
          }
        }
      }
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      ImageIO.write(imageResult, "jpg", out); // 写图片
      in = new ByteArrayInputStream(out.toByteArray());
      return in;
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        in.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return null;
  }
}
