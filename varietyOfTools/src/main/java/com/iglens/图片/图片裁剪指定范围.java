package com.iglens.图片;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

public class 图片裁剪指定范围 {

  public static void main(String[] args) throws IOException {
    String src = "C:\\Users\\T480S\\Desktop\\微信截图_20210729114433.png";
    String dest = "C:\\Users\\T480S\\Desktop\\图标.png";
    long start = System.currentTimeMillis();
    new 图片裁剪指定范围().cutImage(src, dest, 90, 300, 900, 700);
    System.out.println(System.currentTimeMillis() - start);
  }
  /**
   * 图片裁剪通用接口
   *
   * @param src 源图片地址,图片格式PNG
   * @param dest 目的图片地址
   * @param x 图片起始点x坐标
   * @param y 图片起始点y坐标
   * @param w 图片宽度
   * @param h 图片高度
   * @throws IOException 异常处理
   */
  public void cutImage(String src, String dest, int x, int y, int w, int h) {
    try {
      Iterator iterator = ImageIO.getImageReadersByFormatName("png");
      ImageReader reader = (ImageReader) iterator.next();
      InputStream in = new FileInputStream(src);
      ImageInputStream iis = ImageIO.createImageInputStream(in);
      reader.setInput(iis, true);
      ImageReadParam param = reader.getDefaultReadParam();
      Rectangle rect = new Rectangle(x, y, w, h);
      param.setSourceRegion(rect);
      BufferedImage bi = reader.read(0, param);
      ImageIO.write(bi, "png", new File(dest));
    } catch (IOException e) {
      System.err.println("裁剪图片失败");
    }
  }
}
