package com.iglens.word;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DirectColorModel;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * 艺术字图片生成工具
 * @author visy.wang
 * @date 2021/12/8 10:31
 */
public class ImageUtil {
  private static final String FONT_FILE_PATH = "font/FZXBS_GBK.TTF"; //字体文件路径
  private static final float FONT_SIZE = 140f; //字号
  private static final java.awt.Color COLOR = java.awt.Color.RED; //颜色
  private static Font fontPlain; //正常字体
  private static Font fontBold; //加粗字体

  public static Boolean enableTest = true; //测试启用开关

  // static {
  //   try{
  //     //字体加载以及设置
  //     Resource fontResource = new ClassPathResource(FONT_FILE_PATH);
  //     InputStream inputStream = fontResource.getInputStream();
  //     Font f = Font.createFont(Font.TRUETYPE_FONT, inputStream);
  //     fontPlain = f.deriveFont(Font.PLAIN, FONT_SIZE);
  //
  //     Map<TextAttribute, Object> attrs = new HashMap<>();
  //     attrs.put(TextAttribute.SIZE, FONT_SIZE);
  //     attrs.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_DEMIBOLD);
  //     fontBold = f.deriveFont(attrs);
  //   }catch (IOException | FontFormatException e){
  //     e.printStackTrace();
  //   }
  // }

  public static void main(String[] args) {
    Font font = new Font("Arial Unicode MS", Font.PLAIN, 12);
    textToImage("ceshi", font, 10, 3,1);
  }

  /**
   * 文字转艺术字图片
   * @param text 文本内容
   * @param width 宽度
   * @param width 高度
   * @param padding 两端间隔
   * @param isBold 是否加粗
   * @return 图片
   */
  public static BufferedImage toImage(String text, int width, int height, int padding, Boolean isBold) {
    int wordWidth = (width-padding*2) / text.length();

    Font font = isBold ? fontBold : fontPlain;

    // 生成图片
    BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2d = bufferedImage.createGraphics();
    // 白色背景
    g2d.setColor(java.awt.Color.white);
    // 填充矩形
    g2d.fillRect(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight());

    //消除锯齿
    antialiasing(g2d);

    char[] chars = text.toCharArray();
    for (int i = 0; i < chars.length; i++) {
      String ch = String.valueOf(chars[i]);
      BufferedImage bi = textToImage(ch, font, wordWidth, height , i);

      //测试
      printWord(bi, "afterResizeImage", i);

      g2d.drawImage(bi, i * bi.getWidth()+padding, -bi.getHeight() / 12, bi.getWidth(), bi.getHeight(), null);
    }
    g2d.dispose();

    return cutout(bufferedImage);
  }

  /**
   * 文字转图片
   * @param string 文字
   * @param font 字体
   * @param wordWidth 单个文字宽度
   * @param height 高度
   */
  private static BufferedImage textToImage(String string, Font font, int wordWidth, int height, int i){
    BufferedImage bufferedImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
    Graphics2D tempG2 = (Graphics2D) bufferedImage.getGraphics();
    Rectangle2D r2d = font.getStringBounds(string, tempG2.getFontRenderContext());
    // 生成文字大小的ARGB图片，纠正部分艺术字高度测量误差
    BufferedImage bi = new BufferedImage((int) r2d.getWidth(), (int) (r2d.getHeight()/5*6), BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2d = (Graphics2D) bi.getGraphics();
    bi = g2d.getDeviceConfiguration().createCompatibleImage(bi.getWidth(), bi.getHeight(), Transparency.TRANSLUCENT);
    g2d.dispose();
    g2d = (Graphics2D) bi.getGraphics();
    g2d.setFont(font);
    g2d.setColor(COLOR);
    g2d.setBackground(java.awt.Color.WHITE);

    //消除锯齿
    antialiasing(g2d);

    g2d.drawString(string, 0, font.getSize() - 1);
    g2d.dispose();

    //测试
    printWord(bi, "afterTextToImage", i);

    bi = resizeImage(bi, wordWidth, height);

    return bi;
  }

  /**
   * 重置图片大小
   * @param oldImage 原图
   * @param w 新宽度
   * @param h 新高度
   * @return 新图片
   */
  private static BufferedImage resizeImage( BufferedImage oldImage, int w, int h) {
    BufferedImage newImage = new BufferedImage(w, h, oldImage.getType());
    Graphics2D g2d = newImage.createGraphics();
    //消除锯齿
    antialiasing(g2d);
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2d.drawImage(oldImage, 0, 0, w, h, null);
    g2d.dispose();
    return newImage;
  }

  /**
   * 去掉背景色
   * @param bi 原图片
   * @return 去掉背景色的图片
   */
  private static BufferedImage cutout(BufferedImage bi) {
    BufferedImage image = new BufferedImage(bi.getWidth(), bi.getHeight(), bi.getType());
    Graphics2D g2d = image.createGraphics();
    //消除锯齿
    antialiasing(g2d);
    g2d.drawImage(bi, 0, 0, null);
    g2d.dispose();
    int width = image.getWidth();
    int height = image.getHeight();
    DirectColorModel dcm = (DirectColorModel) ColorModel.getRGBdefault();
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        int rgb = image.getRGB(x, y);
        int red = dcm.getRed(rgb);
        int green = dcm.getGreen(rgb);
        int blue = dcm.getBlue(rgb);
        int alp = dcm.getAlpha(rgb);
        if (red == 0 && blue == 0 && green == 0) {// 如果像素为黑，则让它透明
          alp = 0;
          rgb = alp << 24 | 0 << 16 | 0 << 8 | 0;// 进行标准ARGB输出以实现图象过滤
          image.setRGB(x, y, rgb);
        }
        if (red == 255 && blue == 255 && green == 255) {// 如果像素为白色，则让它透明
          alp = 0;
          rgb = alp << 24 | 0 << 16 | 0 << 8 | 0;// 进行标准ARGB输出以实现图象过滤
          image.setRGB(x, y, rgb);
        }
      }
    }
    return image;
  }

  //消除锯齿
  private static void antialiasing(Graphics2D g2d){
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
  }

  //测试用
  private static void printWord(BufferedImage bi, String name, int i){
    if(!enableTest){
      System.out.println("测试开关未开启");
      return;
    }
    try{
      File output = new File("D:\\test\\words\\" + name + i + ".png");
      output.mkdirs();
      ImageIO.write(bi, "png", output);
    }catch (IOException e){
      e.printStackTrace();
    }
  }
}