package com.iglens.地理.瓦片;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import lombok.extern.slf4j.Slf4j;

/**
 * @author T480S
 */
@Slf4j
public class 瓦片拼接 {

  public static void main(String[] args) {
    String zoomLevel = "19";
    int minCol = 433541;
    int maxCol = 433660;
    int minRow = 200279;
    int maxRow = 200354;
    String outPutFileName = "f:\\19.png";
    String tilePath = "F:\\nginx-1.18.0\\dist\\mapData\\";
    splicingTiles(zoomLevel, minCol, maxCol, minRow, maxRow, outPutFileName, tilePath);
    System.out.println("执行完成");
  }

  /**
   * @param zoomLevel 瓦片层级
   * @param minCol 最小列
   * @param maxCol 最大列
   * @param minRow 最小行
   * @param maxRow 最大行
   * @param outPutFileName 生成图片存储路径
   * @param tilePath 瓦片文件夹
   */
  private static void splicingTiles(
      String zoomLevel,
      int minCol,
      int maxCol,
      int minRow,
      int maxRow,
      String outPutFileName,
      String tilePath) {

    File file = new File(outPutFileName);
    if (file.exists()) {
      file.delete();
    }
    // 获取拼接图片宽度
    int imageWidth = 256 * (Math.abs(maxCol - minCol) + 1);
    // 获取拼接图片高度
    int imageHeight = 256 * (Math.abs(maxRow - minRow) + 1);
    BufferedImage memoryimg =
        new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
    // 循环读取瓦片列文件夹（即该瓦片所在文件夹）
    for (int col = minCol; col <= maxCol; col++) {
      // 循环读取瓦片行文件（即该瓦片名称）
      for (int row = minRow; row <= maxRow; row++) {
        try {
          // 获取该文件绝对路径
          String sourceFileName = tilePath + zoomLevel + "\\" + col + "\\" + row + ".png";
          File sourceFile = new File(sourceFileName);
          if (sourceFile.exists()) {
            saveBitmapBuffered(
                memoryimg, new FileInputStream(sourceFile), col - minCol, row - minRow);
          } else {
            log.error("不存在：" + sourceFileName);
          }
        } catch (Exception ex) {
          log.error(ex.getMessage());
        }
      }
    }
    try {
      ImageIO.write(memoryimg, "png", file);
    } catch (IOException e) {
      e.printStackTrace();
    }

    log.info("拼接完成");
  }

  /**
   * 将单个切片的像素值赋值给拼接后的图片
   *
   * @param image 拼接后的图片对象
   * @param sourceInputstream 单个瓦片文件流
   * @param col 当前瓦片列
   * @param row 当前瓦片行
   */
  private static void saveBitmapBuffered(
      BufferedImage image, FileInputStream sourceInputstream, int col, int row) {
    int colPixel = col * 256;
    int rowPixel = row * 256;
    try {
      BufferedImage bufferedImage = ImageIO.read(sourceInputstream);
      for (int i = 0; i < 256; i++) {
        for (int j = 0; j < 256; j++) {
          image.setRGB(colPixel + i, rowPixel + j, bufferedImage.getRGB(i, j));
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
