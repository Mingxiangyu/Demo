package com.iglens.图片;

import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import lombok.extern.slf4j.Slf4j;

/** @link 原文链接：https://blog.csdn.net/weixin_32475629/article/details/114063366 */
@Slf4j
public class java中的图片按比例缩放功能 {
  public static void main(String[] args) {
    String srcPath = "D:\\图片\\微信图片_20220427124546.jpg";
    String destPath = "D:\\图片\\sch.jpg";
    try {
      zoomImage(srcPath, destPath, 150, 200);
      //      zoomImage(srcPath,destPath,58);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  /**
   * 按固定长宽进行缩放
   *
   * @param src src为源文件
   * @param dest dest为缩放后保存文件
   * @param w 缩放的目标宽度
   * @param h 缩放的目标高度
   * @throws Exception
   */
  public static void zoomImage(String src, String dest, int w, int h) throws Exception {
    double wr = 0, hr = 0;

    File srcFile = new File(src);
    File destFile = new File(dest);

    BufferedImage bufImg = ImageIO.read(srcFile); // 读取图片

    Image Itemp = bufImg.getScaledInstance(w, h, Image.SCALE_SMOOTH); // 设置缩放目标图片模板

    wr = w * 1.0 / bufImg.getWidth(); // 获取缩放比例

    hr = h * 1.0 / bufImg.getHeight();

    AffineTransformOp ato = new AffineTransformOp(AffineTransform.getScaleInstance(wr, hr), null);

    Itemp = ato.filter(bufImg, null);

    try {
      ImageIO.write(
          (BufferedImage) Itemp, dest.substring(dest.lastIndexOf(".") + 1), destFile); // 写入缩减后的图片

    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  /**
   * 按固定文件大小进行缩放
   *
   * @param src src为源文件
   * @param dest dest为缩放后保存文件
   * @param size 文件大小
   * @throws Exception
   */
  public static void zoomImage(String src, String dest, Integer size) throws Exception {
    File srcFile = new File(src);
    File destFile = new File(dest);

    long fileSize = srcFile.length();

    // 文件大于size k时，才进行缩放,注意：size以K为单位
    if (fileSize < size * 1024) {
      log.error("跳过");
      return;
    }
    // 获取长宽缩放比例
    double rate = (size * 1024 * 0.5) / fileSize;

    BufferedImage bufImg = ImageIO.read(srcFile);

    Image itemp =
        bufImg.getScaledInstance(bufImg.getWidth(), bufImg.getHeight(), Image.SCALE_SMOOTH);

    AffineTransformOp ato =
        new AffineTransformOp(AffineTransform.getScaleInstance(rate, rate), null);

    itemp = ato.filter(bufImg, null);

    try {
      ImageIO.write((BufferedImage) itemp, dest.substring(dest.lastIndexOf(".") + 1), destFile);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}
