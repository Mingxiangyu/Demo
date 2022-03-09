package com.iglens.图片;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.imageio.ImageIO;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

/** @author xming */
public class Thumbnailator生成缩略图 {

  /**
   * @param args
   * @throws IOException
   */
  public static void main(String[] args) throws IOException {
    //注意，该targetImagePath不会创建父级路径
    String sourceimagePath = "/Users/ming/Desktop/398091-20160605174410680-1218395382.jpg";
    String targetImagePath = "images/test.jpg";
    Thumbnailator生成缩略图 thumbnailatorTest = new Thumbnailator生成缩略图();
    thumbnailatorTest.自适应比例指定大小进行缩放(sourceimagePath, targetImagePath);
    thumbnailatorTest.按照比例进行缩放(sourceimagePath, targetImagePath);
    thumbnailatorTest.不按照比例指定大小进行缩放(sourceimagePath, targetImagePath);
    thumbnailatorTest.旋转(sourceimagePath, targetImagePath);
    //    thumbnailatorTest.水印(sourceimagePath,targetImagePath);
    thumbnailatorTest.裁剪(sourceimagePath, targetImagePath);
    thumbnailatorTest.转化图像格式(sourceimagePath, targetImagePath);
    thumbnailatorTest.输出到OutputStreamtest8(sourceimagePath, targetImagePath);
    thumbnailatorTest.输出到BufferedImage(sourceimagePath, targetImagePath);
  }

  /**
   * 指定大小进行缩放
   *
   * @throws IOException
   * @param sourceimagePath
   * @param targetImagePath
   */
  private void 自适应比例指定大小进行缩放(String sourceimagePath, String targetImagePath) throws IOException {
    /*
     * size(width,height) 若图片横比200小，高比300小，不变
     * 若图片横比200小，高比300大，高缩小到300，图片比例不变 若图片横比200大，高比300小，横缩小到200，图片比例不变
     * 若图片横比200大，高比300大，图片按比例缩小，横为200或高为300
     */
    Thumbnails.of(sourceimagePath)
        .size(200, 300)
        .toFile("/Users/ming/Desktop/image/image_200x300.jpg");
    Thumbnails.of(sourceimagePath)
        .size(2560, 2048)
        .toFile("/Users/ming/Desktop/image/image_2560x2048.jpg");
  }

  /**
   * 按照比例进行缩放
   *
   * @throws IOException
   * @param sourceimagePath
   * @param targetImagePath
   */
  private void 按照比例进行缩放(String sourceimagePath, String targetImagePath) throws IOException {
    /** scale(比例) */
    Thumbnails.of(sourceimagePath).scale(0.25f).toFile("/Users/ming/Desktop/image/image_25%.jpg");
    Thumbnails.of(sourceimagePath).scale(1.10f).toFile("/Users/ming/Desktop/image/image_110%.jpg");
  }

  /**
   * 不按照比例，指定大小进行缩放
   *
   * @throws IOException
   * @param sourceimagePath
   * @param targetImagePath
   */
  private void 不按照比例指定大小进行缩放(String sourceimagePath, String targetImagePath) throws IOException {
    /** keepAspectRatio(false) 默认是按照比例缩放的 */
    Thumbnails.of(sourceimagePath)
        .size(120, 120)
        .keepAspectRatio(false)
        .toFile("/Users/ming/Desktop/image/image_120x120.jpg");
  }

  /**
   * 旋转
   *
   * @throws IOException
   * @param sourceimagePath
   * @param targetImagePath
   */
  private void 旋转(String sourceimagePath, String targetImagePath) throws IOException {
    /** rotate(角度),正数：顺时针 负数：逆时针 */
    Thumbnails.of(sourceimagePath)
        .size(1280, 1024)
        .rotate(90)
        .toFile("/Users/ming/Desktop/image/image+90.jpg");
    Thumbnails.of(sourceimagePath)
        .size(1280, 1024)
        .rotate(-90)
        .toFile("/Users/ming/Desktop/image/iamge-90.jpg");
  }

  /**
   * 水印
   *
   * @throws IOException
   * @param sourceimagePath
   * @param targetImagePath
   */
  private void 水印(String sourceimagePath, String targetImagePath) throws IOException {
    /** watermark(位置，水印图（也可以文字），透明度) */
    Thumbnails.of(sourceimagePath)
        .size(1280, 1024)
        .watermark(Positions.BOTTOM_RIGHT, ImageIO.read(new File("images/watermark.png")), 0.5f)
        .outputQuality(0.8f)
        .toFile("/Users/ming/Desktop/image/image_watermark_bottom_right.jpg");
    Thumbnails.of(sourceimagePath)
        .size(1280, 1024)
        .watermark(Positions.CENTER, ImageIO.read(new File("images/watermark.png")), 0.5f)
        .outputQuality(0.8f)
        .toFile("/Users/ming/Desktop/image/image_watermark_center.jpg");
  }

  /**
   * 裁剪
   *
   * @throws IOException
   * @param sourceimagePath
   * @param targetImagePath
   */
  private void 裁剪(String sourceimagePath, String targetImagePath) throws IOException {
    /** 图片中心400*400的区域 */
    Thumbnails.of(sourceimagePath)
        .sourceRegion(Positions.CENTER, 400, 400)
        .size(200, 200)
        .keepAspectRatio(false)
        .toFile("/Users/ming/Desktop/image/image_region_center.jpg");
    /** 图片右下400*400的区域 */
    Thumbnails.of(sourceimagePath)
        .sourceRegion(Positions.BOTTOM_RIGHT, 400, 400)
        .size(200, 200)
        .keepAspectRatio(false)
        .toFile("/Users/ming/Desktop/image/image_region_bootom_right.jpg");
    /** 指定坐标 */
    Thumbnails.of(sourceimagePath)
        .sourceRegion(600, 500, 400, 400)
        .size(200, 200)
        .keepAspectRatio(false)
        .toFile("/Users/ming/Desktop/image/image_region_coord.jpg");
  }

  /**
   * 转化图像格式
   *
   * @throws IOException
   * @param sourceimagePath
   * @param targetImagePath
   */
  private void 转化图像格式(String sourceimagePath, String targetImagePath) throws IOException {
    /** outputFormat(图像格式) */
    Thumbnails.of(sourceimagePath)
        .size(1280, 1024)
        .outputFormat("png")
        .toFile("/Users/ming/Desktop/image/image_1280x1024.png");
    Thumbnails.of(sourceimagePath)
        .size(1280, 1024)
        .outputFormat("gif")
        .toFile("/Users/ming/Desktop/image/image_1280x1024.gif");
  }

  /**
   * 输出到OutputStream
   *
   * @throws IOException
   * @param sourceimagePath
   * @param targetImagePath
   */
  private void 输出到OutputStreamtest8(String sourceimagePath, String targetImagePath)
      throws IOException {
    /** toOutputStream(流对象) */
    OutputStream os =
        new FileOutputStream("/Users/ming/Desktop/image/image_1280x1024_OutputStream.png");
    Thumbnails.of(sourceimagePath).size(1280, 1024).toOutputStream(os);
  }

  /**
   * 输出到BufferedImage
   *
   * @throws IOException
   * @param sourceimagePath
   * @param targetImagePath
   */
  private void 输出到BufferedImage(String sourceimagePath, String targetImagePath) throws IOException {
    /** asBufferedImage() 返回BufferedImage */
    BufferedImage thumbnail = Thumbnails.of(sourceimagePath).size(1280, 1024).asBufferedImage();
    ImageIO.write(
        thumbnail, "jpg", new File("/Users/ming/Desktop/image/image_1280x1024_BufferedImage.jpg"));
  }
}
