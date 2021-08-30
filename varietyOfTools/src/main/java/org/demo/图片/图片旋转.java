package org.demo.图片;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.imageio.ImageIO;

public class 图片旋转 {
  public static void main(String[] args) throws IOException {
    String destPath = "C:\\Users\\T480S\\Desktop\\微信截图_20210825210648.png";
    String destPathFb = "C:\\Users\\T480S\\Desktop\\微信截图_20210825210648destPathFb.png";

    rotate(destPath, destPathFb, 170d);
  }
  /**
   * 图像旋转
   *
   * @param srcPath 原图片
   * @param angel 角度
   * @return 旋转后的图片
   */
  public static void rotate(String srcPath,String target, double angel) throws IOException {
    InputStream is = new FileInputStream(srcPath);
    //      // 通过JPEG图象流创建JPEG数据流解码器
    //      JPEGImageDecoder jpegDecoder = JPEGCodec.createJPEGDecoder(is);
    //      // 解码当前JPEG数据流，返回BufferedImage对象
    //      BufferedImage buffImg = jpegDecoder.decodeAsBufferedImage();
    BufferedImage buffImg = ImageIO.read(is); // 读取图片
    OutputStream os = new FileOutputStream(target);
    int src_width = buffImg.getWidth(null);
    int src_height = buffImg.getHeight(null);
    // 计算旋转后图片的尺寸
    Rectangle rect_des =
        calcRotatedSize(new Rectangle(new Dimension(src_width, src_height)), angel);

    BufferedImage res = new BufferedImage(rect_des.width, rect_des.height, 2);
    Graphics2D g2 = res.createGraphics();
    // 转换坐标原点。这步不要也成，但是将当前位置转换为坐标原点后，可以节省好多计算步骤，非常好用。
    // 不过记得用完了以后，一定要把原点转换回来，要不然其他地方就乱了
    g2.translate((rect_des.width - src_width) / 2, (rect_des.height - src_height) / 2);
    g2.rotate(Math.toRadians(angel), src_width >> 1, src_height >> 1);

    g2.drawImage(buffImg, null, null);
    ImageIO.write(res, "png", os);
  }

  public static Rectangle calcRotatedSize(Rectangle src, double angel) {
    // 如果旋转的角度大于90度做相应的转换
    if (angel >= 90) {
      if (angel / 90 % 2 == 1) {
        int temp = src.height;
        src.height = src.width;
        src.width = temp;
      }
      angel = angel % 90;
    }

    double r = Math.sqrt(src.height * src.height + src.width * src.width) / 2;
    double len = 2 * Math.sin(Math.toRadians(angel) / 2) * r;
    double angel_alpha = (Math.PI - Math.toRadians(angel)) / 2;
    double angel_dalta_width = Math.atan((double) src.height / src.width);
    double angel_dalta_height = Math.atan((double) src.width / src.height);

    int len_dalta_width = (int) (len * Math.cos(Math.PI - angel_alpha - angel_dalta_width));
    len_dalta_width = len_dalta_width > 0 ? len_dalta_width : -len_dalta_width;

    int len_dalta_height = (int) (len * Math.cos(Math.PI - angel_alpha - angel_dalta_height));
    len_dalta_height = len_dalta_height > 0 ? len_dalta_height : -len_dalta_height;

    int des_width = src.width + len_dalta_width * 2;
    int des_height = src.height + len_dalta_height * 2;
    des_width = des_width > 0 ? des_width : -des_width;
    des_height = des_height > 0 ? des_height : -des_height;
    return new java.awt.Rectangle(new Dimension(des_width, des_height));
  }
}
