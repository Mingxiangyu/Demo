package com.iglens;

import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;


/**
 * @author 12074
 * 原文链接：https://www.jianshu.com/p/b734ff9525d4
 */
public class latex表达式转为图片 {
  public static Image image(String latex) throws Exception {
    int style = TeXConstants.STYLE_DISPLAY; // 样式 符号以最大的尺寸呈现
    float size = 24; // 生成公式图片的字体大小
    Color fg = Color.BLACK; // 字体颜色，黑色
    Color bg = null; // 图片背景色，默认为透明北京
    return TeXFormula.createBufferedImage(latex, style, size, fg, bg);
  }

  public static void main(String[] args) throws Exception {
    // String latex = "\\sqrt{{\\mathrm{a}}^{2}+{\\mathrm{b}}^{2}}";
    String latex = " $\\frac{2}{9}\\times3=\\frac{6}{9}$";
    File file = new File("d:/test.png"); // 保存到文件
    BufferedImage image = (BufferedImage) image(latex);
    ImageIO.write(image, "png", file);
  }
}
