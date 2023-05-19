package com.iglens.图片;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * @author xming
 * @link: https://www.jb51.net/article/229192.htm
 */
public class png图片修改像素rgba值 {
  public static void main(String[] args) throws Exception {
    setAlpha("E:\\WorkSpace\\gitWorkSpace\\Demo\\input.png", "outPath");
  }

  public static void setAlpha(String os, String outPath) {
    try {
      ImageIcon imageIcon = new ImageIcon(os);
      BufferedImage bufferedImage =
          new BufferedImage(
              imageIcon.getIconWidth(), imageIcon.getIconHeight(), BufferedImage.TYPE_4BYTE_ABGR);
      Graphics2D g2D = (Graphics2D) bufferedImage.getGraphics();
      g2D.drawImage(imageIcon.getImage(), 0, 0, imageIcon.getImageObserver());
      for (int j1 = bufferedImage.getMinY(); j1 < bufferedImage.getHeight(); j1++) {
        for (int j2 = bufferedImage.getMinX(); j2 < bufferedImage.getWidth(); j2++) {
          int pixel = bufferedImage.getRGB(j2, j1); // j2横坐标,j1竖坐标
          int[] rgb = new int[3];
          rgb[0] = (pixel & 0x00ff0000) >> 16; // 按位与获取red然后右移
          rgb[1] = (pixel & 0x0000ff00) >> 8; // ..获取green...
          rgb[2] = (pixel & 0x000000ff);
          int a = (pixel & 0xff000000) >>> 24; // 无符号右移获取alpha值
          if (comp(rgb[0], rgb[1], rgb[2]) || a == 0) {
            pixel = pixel | 0xffffffff; // 透明或偏向白色射为白色
          } else {
            pixel = (pixel & 0xff000000) | 0xff000000; // 否则为黑色
          }
          bufferedImage.setRGB(j2, j1, pixel);
        }
        System.out.println();
      }
      g2D.drawImage(bufferedImage, 0, 0, imageIcon.getImageObserver());
      ImageIO.write(bufferedImage, "png", new File(outPath));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static boolean comp(int r, int g, int b) { // 判断二值化为黑还是白，true为白，false为黑
    int i = 0;
    if (r > 200) {
      i++;
    }
    if (g > 200) {
      i++;
    }
    if (b > 200) {
      i++;
    }
    return i >= 2;
  }
}
