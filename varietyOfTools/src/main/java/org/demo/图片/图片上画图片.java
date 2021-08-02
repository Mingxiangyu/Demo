package org.demo.图片;

import com.sun.image.codec.jpeg.ImageFormatException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class 图片上画图片 {
  public static void main(String[] args) {
    String srcPath = "C:\\Users\\T480S\\Desktop\\微信截图_20210730102723.png";
    String destPath = "C:\\Users\\T480S\\Desktop\\微信截图_20210730102723-fuben.png";
    String targetPath = "C:\\Users\\T480S\\Desktop\\微信截图拼接.png";
    exportImg(srcPath,destPath, targetPath);
  }
  /**
   * 图片上画图片
   *
   * @param src 主图片的路径
   * @param dest 小图片的路径
   * @param target 生成结果路径
   */
  public static void exportImg(String src, String dest, String target) {
    try {
      InputStream is = new FileInputStream(src);
//      // 通过JPEG图象流创建JPEG数据流解码器
//      JPEGImageDecoder jpegDecoder = JPEGCodec.createJPEGDecoder(is);
//      // 解码当前JPEG数据流，返回BufferedImage对象
//      BufferedImage buffImg = jpegDecoder.decodeAsBufferedImage();
      BufferedImage buffImg = ImageIO.read(is); // 读取图片

      // 得到画笔对象
      Graphics g = buffImg.getGraphics();

      // 创建你要附加的图象。
      ImageIcon imgIcon = new ImageIcon(dest);
      // 得到要附加的Image对象。
      Image img = imgIcon.getImage();

      // 将小图片绘到大图片上。
      // 5,300 .表示你的小图片在大图片上的位置。
      g.drawImage(img, 105, 405, null);

      // 设置颜色。
      g.setColor(Color.BLACK);

      // 最后一个参数用来设置字体的大小
      Font f = new Font("宋体", Font.PLAIN, 11);
      Color mycolor = Color.BLACK; // new Color(0, 0, 255);
      g.setColor(mycolor);
      g.setFont(f);

      // 10,20 表示这段文字在图片上的位置(x,y) .第一个是你设置的字体内容。
      g.drawString("扫描验证真伪", 100, 470);
      g.dispose();

      OutputStream os = new FileOutputStream(target);
      ImageIO.write(buffImg, "png", os);
    } catch (IOException | ImageFormatException e) {
      e.printStackTrace();
    }
  }
}
