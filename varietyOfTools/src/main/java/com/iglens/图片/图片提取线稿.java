package com.iglens.图片;

import cn.hutool.core.io.FileUtil;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class 图片提取线稿 {
  public static final int CUT_MODE_MIDDLE = -20000;
  public static final int CUT_MODE_BUTTOM = -30000;
  public static final int CUT_MODE_LEFT = 0;
  public static final int CUT_MODE_CENTER = -20000;
  public static final int CUT_MODE_RIGHT = -30000;
  public static void main(String[] args) {
    // 读取图片到BufferedImage
    String imageFile = "C:\\Users\\12074\\Desktop\\灯光话\\原创手绘亲子头像全家福一家四口头像_2_小珠_来自小红书网页版.png";
    String parent = FileUtil.getParent(imageFile, 1);
    BufferedImage bf = readImage(imageFile);
    // 将图片转换为二维数组
    int width = bf.getWidth();
    int height = bf.getHeight();
    int[][] rawArray = convertImageToArray(bf);
    int[][] newArray = new int[height - 2][width - 2];
    // 给定卷积核
    int[][] kernel = new int[3][3];

    System.out.println("开始");
    convolve(rawArray, newArray, kernel);
    System.out.println("结束");

    // 输出图片到指定文件
    writeImageFromArray(parent+File.separator+"线稿.png", "png", newArray, 2400, 3000,CUT_MODE_MIDDLE, CUT_MODE_MIDDLE);
    // 这里写你要输出的绝对路径+文件名
    System.out.println("图片输出完毕!");
  }

  /**
   * 将输入的整数限制在0到255之间。
   *
   * @param num 输入的整数
   */
  private static int ctrlLim(int num) {
    if (num > 255) {
      return 255;
    }
    if (num < 0) {
      return 0;
    }
    return num;
  }

  /**
   * rgb 转灰色
   */
  private static int rgbToGray(int color) {
    int red = (color & 0x00FF0000) >> 16;
    int green = (color & 0x0000FF00) >> 8;
    int blue = (color & 0x000000FF);
    return (red + green + blue) / 3;
  }

  private static int grayToRgb(int color) {
    return color * (65536 + 256 + 1);
  }


  /**
   * 该函数的功能是对一张图片进行黑白化处理，并使用给定的卷积核进行滤波操作。  具体步骤如下：
   * 1. 获取原始图片的大小和卷积核的大小。
   * 2. 计算卷积核的中心位置。
   * 3. 遍历原始图片的每个像素点，将其转换为灰度值。
   * 4. 遍历原始图片的每个像素点，使用卷积核进行滤波操作。
   * 5. 在滤波过程中，找到卷积核中像素点的最大值。
   * 6. 将最大值转换为反色值。
   * 7. 根据原始像素点和最大值的灰度值，计算新的像素点的灰度值。
   * 8. 将新的像素点的灰度值转换为RGB值，并存储到新的图片中。
   * 该函数的输入参数包括原始图片、新的图片和卷积核。输出结果为新的图片。
   */
  private static void convolve(int[][] rawArray, int[][] newArray, int[][] kernel) {
    int kernelHeight = kernel.length;
    int kernelWidth = kernel[0].length;
    int pictureHeight = rawArray.length;
    int pictureWidth = rawArray[0].length;
    int r = (kernelHeight - 1) / 2;
    //整张图片黑白化
    for (int j = 0; j < pictureHeight; j++) {
      for (int i = 0; i < pictureWidth; i++) {
        rawArray[j][i] = rgbToGray(rawArray[j][i]);
      }
    }

    for (int j = 0; j < pictureHeight - kernelHeight + 1; j++) {
      for (int i = 0; i < pictureWidth - kernelWidth + 1; i++) {
        int rawColor = rawArray[j + r][i + r];
        //找到最大值
        int maxColor = 0;
        for (int m = 0; m < kernelHeight; m++) {
          for (int n = 0; n < kernelWidth; n++) {
            if (rawArray[j + m][i + n] > maxColor) {
              maxColor = rawArray[j + m][i + n];
            }
          }
        }
        //反色
        maxColor = 255 - maxColor;
        //颜色减淡
        int newColor = 0;
        if (rawColor == 255 || maxColor == 255) {
          newColor = 255;
        } else {
          newColor = (int) (rawColor / (1.0 - maxColor / 255.0));
        }
        newArray[j][i] = grayToRgb(ctrlLim(newColor));
      }
    }
  }

  public static BufferedImage readImage(String imageFile) {
    File file = new File(imageFile);
    BufferedImage bf = null;
    try {
      bf = ImageIO.read(file);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return bf;
  }

  public static int[][] convertImageToArray(BufferedImage bf) {
    // 获取图片宽度和高度
    int width = bf.getWidth();
    int height = bf.getHeight();
    // 将图片sRGB数据写入一维数组
    int[] data = new int[width * height];
    bf.getRGB(0, 0, width, height, data, 0, width);
    // 将一维数组转换为为二维数组
    int[][] rgbArray = new int[height][width];
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        rgbArray[i][j] = data[i * width + j];
      }
    }
    return rgbArray;
  }

  /**
   * 生成原始图片尺寸
   */
  public static void writeImageFromArray(String imageFile, String type, int[][] rgbArray) {
    // 获取数组宽度和高度
    int width = rgbArray[0].length;
    int height = rgbArray.length;
    // 将二维数组转换为一维数组
    int[] data = new int[width * height];
    for (int i = 0; i < height; i++) {
      System.arraycopy(rgbArray[i], 0, data, i * width, width);
    }
    // 将数据写入BufferedImage
    BufferedImage bf = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
    bf.setRGB(0, 0, width, height, data, 0, width);
    // 输出图片
    try {
      File file = new File(imageFile);
      ImageIO.write((RenderedImage) bf, type, file);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * 生成指定尺寸的图片
   */
  public static void writeImageFromArray(String imageFile, String type, int[][] rgbArray, int targetWidth,
      int targetHeight) {
    int width = rgbArray[0].length;
    int height = rgbArray.length;

    double aspectRatio = (double) width / height;
    double targetAspectRatio = (double) targetWidth / targetHeight;

    int newWidth, newHeight;

    if (aspectRatio > targetAspectRatio) {
      // 原图宽高比大于目标宽高比,需要裁剪宽度
      newWidth = targetWidth;
      newHeight = (int) (targetWidth / aspectRatio);
    } else {
      // 原图宽高比小于目标宽高比,需要裁剪高度
      newWidth = (int) (targetHeight * aspectRatio);
      newHeight = targetHeight;
    }

    // 创建一个新的 BufferedImage 对象,并指定调整后的尺寸
    BufferedImage bf = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_BGR);

    // 绘制原始图像到 BufferedImage 对象
    Graphics2D g2d = bf.createGraphics();
    g2d.drawImage(convertArrayToImage(rgbArray), 0, 0, newWidth, newHeight, null);
    g2d.dispose();

    // 创建一个新的 BufferedImage 对象,并指定目标尺寸
    BufferedImage targetImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_BGR);

    // 将调整后的图像绘制到目标 BufferedImage 对象中心
    g2d = targetImage.createGraphics();
    int x = (targetWidth - newWidth) / 2;
    int y = (targetHeight - newHeight) / 2;
    g2d.drawImage(bf, x, y, null);
    g2d.dispose();

    // 输出图片
    try {
      File file = new File(imageFile);
      ImageIO.write((RenderedImage) targetImage, type, file);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * 按照要求缩放并剪裁图片
   *
   * @param rgbArray 源图片数组
   * @param imageFile 目标文件路径和名称
   * @param targetWidth 目标宽度
   * @param targetHeight 目标高度
   * @param wCutMode 目标宽度剪切模式, 可以设置常量, 如果设置为正整数则视为从0开始的偏移量. 这个参数不会和hCutMode同时应用.
   * 方法会自动判断应用哪个.
   * @param hCutMode 目标高度剪切模式, 可以设置常量, 如果设置为正整数则视为从0开始的偏移量. 这个参数不会和wCutMode同时应用.
   * 方法会自动判断应用哪个.
   */
   private static void writeImageFromArray( String imageFile, String type, int[][] rgbArray, int targetWidth, int targetHeight,int wCutMode,
      int hCutMode){
     BufferedImage sourceImage = convertArrayToImage(rgbArray); // 装载图像

    int height = sourceImage.getHeight();
    int width = sourceImage.getWidth();

    double tmpWidth = (width * targetHeight * 1.0 / height);
    double tmpHeight = (height * targetWidth * 1.0 / width);
    if (Math.abs(tmpWidth - targetWidth) <= 1) // 提高一下容错度.
    {
      tmpWidth = targetWidth;
    }
    if (Math.abs(tmpHeight - targetHeight) <= 1) {
      tmpHeight = targetHeight;
    }

    int x = 0, y = 0; // 图像左上角坐标,用于剪裁
    if ((height > width && tmpHeight >= targetHeight) || (height <= width && tmpWidth <= targetWidth)) {
      // 判断分支1: 正常情况或图比较小按比例缩放后宽小于目标宽度的情况
      height = (int) tmpHeight;
      width = targetWidth;
      if (hCutMode == CUT_MODE_MIDDLE) {
        y = (height - targetHeight) / 2;
      } else if (hCutMode == CUT_MODE_BUTTOM) {
        y = height - targetHeight;
      } else if (hCutMode >= 0) { // 假设正整数作为偏移量处理
        y = hCutMode;
      }
    } else {
      height = targetHeight;
      width = (int) tmpWidth;
      if (wCutMode == CUT_MODE_CENTER) {
        x = (width - targetWidth) / 2;
      } else if (wCutMode == CUT_MODE_RIGHT) {
        x = width - targetWidth;
      } else if (wCutMode >= 0) { // 假设正整数作为偏移量处理
        x = wCutMode;
      }
    }

    BufferedImage resizedImage = new BufferedImage(width, height, sourceImage.getType());
    Graphics2D g2d = resizedImage.createGraphics();
    g2d.drawImage(sourceImage, 0, 0, width, height, null);
    g2d.dispose();

    // 剪裁操作
    BufferedImage croppedImage = resizedImage.getSubimage(x, y, targetWidth, targetHeight);

    // 保存图像
     try {
       ImageIO.write(croppedImage, type, new File(imageFile));
     } catch (IOException e) {
       throw new RuntimeException(e);
     }
   }


  public static BufferedImage convertArrayToImage(int[][] rgbArray) {
    int width = rgbArray[0].length;
    int height = rgbArray.length;
    BufferedImage bf = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
    bf.setRGB(0, 0, width, height, convertArrayToData(rgbArray), 0, width);
    return bf;
  }

  private static int[] convertArrayToData(int[][] rgbArray) {
    int width = rgbArray[0].length;
    int height = rgbArray.length;
    int[] data = new int[width * height];
    for (int i = 0; i < height; i++) {
      System.arraycopy(rgbArray[i], 0, data, i * width, width);
    }
    return data;
  }

}
