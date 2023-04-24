package com.iglens;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.imageio.ImageIO;
import javax.swing.JLabel;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;
import org.scilab.forge.jlatexmath.TeXIcon;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/** @author 12074 原文链接：https://blog.csdn.net/qxianx/article/details/80685420 */
public class latex表达式转为base64 {
  public static void main(String[] args) throws IOException {
    String latex = "\\tan {{231}^{{}^\\circ }}\\_\\_\\_\\_\\_\\_\\_\\_\\tan {{237}^{{}^\\circ }}";
    System.out.println(latex2Png(latex));
  }
  /**
   * @Description: 将base64编码字符串转换为图片 @Author: @CreateTime:
   *
   * @param imgStr base64编码字符串
   * @param path 图片路径-具体到文件
   * @return
   */
  public static boolean generateImage(String imgStr, String path) {
    if (imgStr == null) {
      return false;
    }
    BASE64Decoder decoder = new BASE64Decoder();
    try {
      byte[] b = decoder.decodeBuffer(imgStr);
      for (int i = 0; i < b.length; i++) {
        if (b[i] < 0) {
          b[i] += 256;
        }
      }
      OutputStream out = new FileOutputStream(path);
      out.write(b);
      out.flush();
      out.close();
      return true;
    } catch (Exception e) {
      // TODO: handle exception
      return false;
    }
  }
  /**
   * @Description: 根据图片地址转换为base64编码字符串 @Author: @CreateTime:
   *
   * @return
   */
  public static String getImageStr(String imgFile) {
    InputStream inputStream = null;
    byte[] data = null;
    try {
      inputStream = new FileInputStream(imgFile);
      data = new byte[inputStream.available()];
      inputStream.read(data);
      inputStream.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    // 加密
    BASE64Encoder encoder = new BASE64Encoder();
    return encoder.encode(data);
  }

  // latex 转 imgbase64
  public static String latex2Png(String latex) {
    try {
      TeXFormula formula = new TeXFormula(latex);
      // render the formla to an icon of the same size as the formula.
      TeXIcon icon = formula.createTeXIcon(TeXConstants.STYLE_DISPLAY, 20);
      // insert a border
      icon.setInsets(new Insets(5, 5, 5, 5));
      // now create an actual image of the rendered equation
      BufferedImage image =
          new BufferedImage(
              icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_BYTE_GRAY);
      Graphics2D g2 = image.createGraphics();
      g2.setColor(Color.white);
      g2.fillRect(0, 0, icon.getIconWidth(), icon.getIconHeight());
      JLabel jl = new JLabel();
      jl.setForeground(new Color(0, 0, 0));
      icon.paintIcon(jl, g2, 0, 0);
      // at this point the image is created, you could also save it with ImageIO
      // saveImage(image, "png", "F:\\b.png");
      // ImageIO.write(image, "png", new File("F:\\c.png"));
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      try {
        ImageIO.write(image, "png", outputStream);
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        return null;
      }
      byte[] buffer = outputStream.toByteArray();
      BASE64Encoder encoder = new BASE64Encoder();
      return ("data:image/png;base64," + encoder.encode(buffer));
    } catch (Exception e) {
      // e.printStackTrace();
      // ExceptionUtil.log(log, e);
      System.err.println("公式解析有误：\n" + latex);
      // e.printStackTrace();
      return null;
    }
  }

  public static void saveImage(BufferedImage image, String format, String filePath) {
    try {
      ImageIO.write(image, format, new File(filePath));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
