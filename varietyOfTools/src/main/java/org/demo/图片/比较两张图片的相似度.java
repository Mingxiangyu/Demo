package org.demo.图片;

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

/**
 * 比较两张图片的相似度
 *
 */
public class 比较两张图片的相似度 {

  public static void main(String[] args) {
    compareImage(
        "C:\\Users\\T480S\\Desktop\\微信截图_20210730102716.png",
        "C:\\Users\\T480S\\Desktop\\微信截图_20210730102723.png");
  }

  // 改变成二进制码
  public static String[][] getPX(String path) {
    int[] rgb = new int[3];

    File file = new File(path);
    BufferedImage bi = null;
    try {
      bi = ImageIO.read(file);
    } catch (Exception e) {
      e.printStackTrace();
    }

    int width = bi.getWidth();
    int height = bi.getHeight();
    int minx = bi.getMinX();
    int miny = bi.getMinY();
    String[][] list = new String[width][height];
    for (int i = minx; i < width; i++) {
      for (int j = miny; j < height; j++) {
        // 获取该像素位置RGB值（该值为单数，通过位运算转换为rgb值）
        int pixel = bi.getRGB(i, j);
        rgb[0] = (pixel & 0xff0000) >> 16;
        rgb[1] = (pixel & 0xff00) >> 8;
        rgb[2] = (pixel & 0xff);
        list[i][j] = rgb[0] + "," + rgb[1] + "," + rgb[2];
      }
    }
    return list;
  }

  public static void compareImage(String imgPath1, String imgPath2) {
    String[] images = {imgPath1, imgPath2};
    if (images.length == 0) {
      System.out.println("Usage >java BMPLoader ImageFile.bmp");
      System.exit(0);
    }

    // 分析图片相似度 begin
    String[][] list1 = getPX(images[0]);
    String[][] list2 = getPX(images[1]);
    // 像素相似出现数量
    int xiangsi = 0;
    // 像素不同出现数量
    int busi = 0;
    int i = 0, j = 0;
    for (String[] strings : list1) {
      if ((i + 1) == list1.length) {
        continue;
      }
      for (int m = 0; m < strings.length; m++) {
        try {
          String[] value1 = list1[i][j].toString().split(",");
          String[] value2 = list2[i][j].toString().split(",");
          int k = 0;
          for (int n = 0; n < value2.length; n++) {
            // 两个像素点差值不小于5（5为阈值，可替换为变量）
            if (Math.abs(Integer.parseInt(value1[k]) - Integer.parseInt(value2[k])) < 5) {
              xiangsi++;
            } else {
              busi++;
            }
          }
        } catch (RuntimeException e) {
          continue;
        }
        j++;
      }
      i++;
    }

    list1 = getPX(images[1]);
    list2 = getPX(images[0]);
    i = 0;
    j = 0;
    for (String[] strings : list1) {
      if ((i + 1) == list1.length) {
        continue;
      }
      for (int m = 0; m < strings.length; m++) {
        try {
          String[] value1 = list1[i][j].toString().split(",");
          String[] value2 = list2[i][j].toString().split(",");
          int k = 0;
          for (int n = 0; n < value2.length; n++) {
            if (Math.abs(Integer.parseInt(value1[k]) - Integer.parseInt(value2[k])) < 5) {
              xiangsi++;
            } else {
              busi++;
            }
          }
        } catch (RuntimeException e) {
          continue;
        }
        j++;
      }
      i++;
    }
    String baifen = "";
    try {
      baifen =
          ((Double.parseDouble(xiangsi + "") / Double.parseDouble((busi + xiangsi) + "")) + "");
      baifen = baifen.substring(baifen.indexOf(".") + 1, baifen.indexOf(".") + 3);
    } catch (Exception e) {
      baifen = "0";
    }
    if (baifen.length() <= 0) {
      baifen = "0";
    }
    if (busi == 0) {
      baifen = "100";
    }

    System.out.println(
        "相似像素数量：" + xiangsi + " 不相似像素数量：" + busi + " 相似率：" + Integer.parseInt(baifen) + "%");
  }
}
