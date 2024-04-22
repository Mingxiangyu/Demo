package com.iglens.图片;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class 按照要求缩放并剪裁图片 {

  public static final int CUT_MODE_MIDDLE = -20000;
  public static final int CUT_MODE_BUTTOM = -30000;
  public static final int CUT_MODE_LEFT = 0;
  public static final int CUT_MODE_CENTER = -20000;
  public static final int CUT_MODE_RIGHT = -30000;

  public static void main(String[] args) throws IOException {
    String imageFile = "E:\\WorkSpace\\gitWorkSpace\\color-card\\o.png";
    String disPath = "E:\\WorkSpace\\gitWorkSpace\\Demo\\test.png";
    resizeCropImage(imageFile, disPath, 2400, 3000, CUT_MODE_MIDDLE, CUT_MODE_MIDDLE);
  }

  /**
   * 按照要求缩放并剪裁图片
   *
   * @param srcPath 源文件路径和名称
   * @param disPath 目标文件路径和名称
   * @param disWidth 目标宽度
   * @param disHeight 目标高度
   * @param wCutMode 目标宽度剪切模式, 可以设置常量, 如果设置为正整数则视为从0开始的偏移量. 这个参数不会和hCutMode同时应用.
   * 方法会自动判断应用哪个.
   * @param hCutMode 目标高度剪切模式, 可以设置常量, 如果设置为正整数则视为从0开始的偏移量. 这个参数不会和wCutMode同时应用.
   * 方法会自动判断应用哪个.
   */
  private static void resizeCropImage(String srcPath, String disPath, int disWidth, int disHeight, int wCutMode,
      int hCutMode) throws IOException {
    BufferedImage sourceImage = ImageIO.read(new File(srcPath)); // 装载图像

    int height = sourceImage.getHeight();
    int width = sourceImage.getWidth();

    double tmpWidth = (width * disHeight * 1.0 / height);
    double tmpHeight = (height * disWidth * 1.0 / width);
    if (Math.abs(tmpWidth - disWidth) <= 1) // 提高一下容错度.
    {
      tmpWidth = disWidth;
    }
    if (Math.abs(tmpHeight - disHeight) <= 1) {
      tmpHeight = disHeight;
    }

    int x = 0, y = 0; // 图像左上角坐标,用于剪裁
    if ((height > width && tmpHeight >= disHeight) || (height <= width && tmpWidth <= disWidth)) {
      // 判断分支1: 正常情况或图比较小按比例缩放后宽小于目标宽度的情况
      height = (int) tmpHeight;
      width = disWidth;
      if (hCutMode == CUT_MODE_MIDDLE) {
        y = (height - disHeight) / 2;
      } else if (hCutMode == CUT_MODE_BUTTOM) {
        y = height - disHeight;
      } else if (hCutMode >= 0) { // 假设正整数作为偏移量处理
        y = hCutMode;
      }
    } else {
      height = disHeight;
      width = (int) tmpWidth;
      if (wCutMode == CUT_MODE_CENTER) {
        x = (width - disWidth) / 2;
      } else if (wCutMode == CUT_MODE_RIGHT) {
        x = width - disWidth;
      } else if (wCutMode >= 0) { // 假设正整数作为偏移量处理
        x = wCutMode;
      }
    }

    BufferedImage resizedImage = new BufferedImage(width, height, sourceImage.getType());
    Graphics2D g2d = resizedImage.createGraphics();
    g2d.drawImage(sourceImage, 0, 0, width, height, null);
    g2d.dispose();

    // 剪裁操作
    BufferedImage croppedImage = resizedImage.getSubimage(x, y, disWidth, disHeight);

    // 保存图像
    ImageIO.write(croppedImage, "png", new File(disPath));
  }

}
