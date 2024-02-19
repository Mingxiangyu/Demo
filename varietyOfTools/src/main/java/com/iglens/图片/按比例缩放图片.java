package com.iglens.图片;


import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class 按比例缩放图片 {

  public static void main(String[] args) {
    BufferedImage originalImage = readImage("E:\\WorkSpace\\gitWorkSpace\\color-card\\o.png");
    int targetWidth = 2400;
    int targetHeight = 3000;

    BufferedImage resizedImage = resizeImage(originalImage, targetWidth, targetHeight);

    writeImageFromArray("原图.png", "png", convertImageToArray(resizedImage));
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

  // public static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
  //   // Calculate the image's aspect ratio
  //   double aspectRatio = (double) originalImage.getWidth() / originalImage.getHeight();
  //
  //   // Calculate the new dimensions while preserving the aspect ratio
  //   int newWidth = targetWidth;
  //   int newHeight = (int) (targetWidth / aspectRatio);
  //
  //   if (newHeight > targetHeight) {
  //     newHeight = targetHeight;
  //     newWidth = (int) (targetHeight * aspectRatio);
  //   }
  //
  //   // Create a new resized image
  //   Image resizedImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
  //   BufferedImage outputImage = new BufferedImage(newWidth, newHeight, originalImage.getType());
  //
  //   // Copy the resized image to the output image
  //   Graphics2D g2d = outputImage.createGraphics();
  //   g2d.drawImage(resizedImage, 0, 0, null);
  //   g2d.dispose();
  //
  //   return outputImage;
  // }

//   public static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
//     double aspectRatio = (double) originalImage.getWidth() / originalImage.getHeight();
//     double targetAspectRatio = (double) targetWidth / targetHeight;
//
//     int newWidth, newHeight;
//
//     if (aspectRatio > targetAspectRatio) {
//         // 原图宽高比大于目标宽高比,需要裁剪宽度
//         newWidth = targetWidth;
//         newHeight = (int) (targetWidth / aspectRatio);
//     } else {
//         // 原图宽高比小于目标宽高比,需要裁剪高度
//         newWidth = (int) (targetHeight * aspectRatio);
//         newHeight = targetHeight;
//     }
//
//     // 创建一个临时 BufferedImage 对象,并指定调整后的尺寸
//     BufferedImage tempImage = new BufferedImage(newWidth, newHeight, originalImage.getType());
//
//     // 绘制原始图像到临时 BufferedImage 对象
//     Graphics2D g2d = tempImage.createGraphics();
//     g2d.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
//     g2d.dispose();
//
//     // 创建一个新的 BufferedImage 对象,并指定目标尺寸
//     BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, originalImage.getType());
//
//     // 将调整后的图像绘制到目标 BufferedImage 对象中心
//     g2d = outputImage.createGraphics();
//     int x = (targetWidth - newWidth) / 2;
//     int y = (targetHeight - newHeight) / 2;
//     g2d.drawImage(tempImage, x, y, null);
//     g2d.dispose();
//
//     return outputImage;
// }

  public static BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) {
    double aspectRatio = (double) originalImage.getWidth() / originalImage.getHeight();
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

    // 创建一个临时 BufferedImage 对象,并指定调整后的尺寸
    BufferedImage tempImage = new BufferedImage(newWidth, newHeight, originalImage.getType());

    // 绘制原始图像到临时 BufferedImage 对象
    Graphics2D g2d = tempImage.createGraphics();
    g2d.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
    g2d.dispose();

    // 创建一个新的 BufferedImage 对象,并指定目标尺寸
    BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);

    // 将调整后的图像绘制到目标 BufferedImage 对象,并设置合成规则为 AlphaComposite.Src
    g2d = outputImage.createGraphics();
    g2d.setComposite(AlphaComposite.Src);
    g2d.drawImage(tempImage, 0, 0, null);
    g2d.dispose();

    return outputImage;
}
}
