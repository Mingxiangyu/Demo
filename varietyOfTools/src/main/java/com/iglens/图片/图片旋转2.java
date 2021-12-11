package com.iglens.图片;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class 图片旋转2 {
  public static void main(String[] args) {

    try {
      // 获取图片
      String srcPath = "C:\\Users\\T480S\\Desktop\\微信截图_20210824115052.png";
      File raw = new File(srcPath);
      BufferedImage bufferedImage = ImageIO.read(raw);

      String targetPath1 = "C:\\Users\\T480S\\Desktop\\微信截图_20210824115052---1.png";
      // 旋转图片为指定角度  图片宽高不变
      BufferedImage bufferedImage2 = rotateImage(bufferedImage, 70);
      File outputfile2 = new File(targetPath1);
      // 生成旋转后的图片
      ImageIO.write(bufferedImage2, "png", outputfile2);

      String targetPath2 = "C:\\Users\\T480S\\Desktop\\微信截图_20210824115052---2.png";
      // 旋转图片为指定角度 同时旋转宽高
      BufferedImage bufferedImage3 = rotateImage2(bufferedImage, 70);
      File outputfile3 = new File(targetPath2);
      // 生成旋转后的图片
      ImageIO.write(bufferedImage3, "png", outputfile3);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * * 旋转图片为指定角度 图片宽高不变*
   *
   * @param bufferedimage * 目标图像 *
   * @param degree * 旋转角度 *
   * @return
   */
  public static BufferedImage rotateImage(final BufferedImage bufferedimage, final int degree) {
    int w = bufferedimage.getWidth(); // 得到图片宽度。
    int h = bufferedimage.getHeight(); // 得到图片高度。
    int type = bufferedimage.getColorModel().getTransparency(); // 得到图片透明度。
    BufferedImage img; // 空的图片。
    Graphics2D graphics2d; // 空的画笔。
    (graphics2d = (img = new BufferedImage(w, h, 2)).createGraphics()) //imageType = 2为背景透明
        .setRenderingHint(
            RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    graphics2d.rotate(
        Math.toRadians(degree),
        w / 2,
        h / 2); // 旋转，degree是整型，度数，比如垂直90度。   •rotate(double arc,double x, double
    // y)：图形以点(x,y)为轴点，旋转arc弧度。
    graphics2d.drawImage(bufferedimage, 0, 0, null); // 从bufferedimagecopy图片至img，0,0是img的坐标。
    graphics2d.dispose();

    return img; // 返回复制好的图片，原图片依然没有变，没有旋转，下次还可以使用。
  }

  /**
   * * 旋转图片为指定角度 同时旋转宽高 *
   *
   * @param bufferedimage * 目标图像 *
   * @param degree * 旋转角度 *
   * @return
   */
  public static BufferedImage rotateImage2(final BufferedImage bufferedimage, final int degree) {
    int w = bufferedimage.getWidth(); // 得到图片宽度。
    int h = bufferedimage.getHeight(); // 得到图片高度。
    BufferedImage img; // 空的图片。

    int x = (h / 2) - (bufferedimage.getWidth() / 2); // 确定原点坐标
    int y = (w / 2) - (bufferedimage.getHeight() / 2);
    AffineTransform at = new AffineTransform();
    at.rotate(Math.toRadians(degree), h / 2, w / 2); // 旋转图象
    at.translate(x, y);
    AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_BICUBIC);
    img = new BufferedImage(h, w, bufferedimage.getType());
    img = op.filter(bufferedimage, img);

    return img; // 返回复制好的图片，原图片依然没有变，没有旋转，下次还可以使用。
  }
}
