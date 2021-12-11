package com.iglens.字体;

import com.iglens.多个流关闭的简单方法;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/** 使用本地字体 */
public class 字体工具类 {

  public static Font getIconFont(String name, float size) {
    return getLocalFont("iconfont/" + name, Font.PLAIN, size);
  }

  /** 雅黑-Consolas */
  public static Font YaHeiConsolas(float size) {
    return getLocalFont("YaHei.Consolas.1.12.ttf", Font.PLAIN, size);
  }

  /** 思源黑体-中文 */
  public static Font SourceHanSansCNNormal(float size) {
    return getLocalFont("SourceHanSansCN-Normal.ttf", Font.PLAIN, size);
  }

  /** 获取本地字体 */
  private static Font getLocalFont(String name, int style, float size) {
    Font definedFont = null;
    InputStream is = null;
    BufferedInputStream bis = null;
    try {
      is = 字体工具类.class.getResourceAsStream("com/iglens/字体/font/" + name);
      bis = new BufferedInputStream(is);
      // createFont返回一个使用指定字体类型和输入数据的新 Font。<br>
      // 新 Font磅值为 1，样式为 PLAIN,注意 此方法不会关闭 InputStream
      definedFont = Font.createFont(Font.TRUETYPE_FONT, bis);
      // 复制此 Font对象并应用自定义样式，创建一个指定磅值的新 Font对象。
      definedFont = definedFont.deriveFont(style, size);
    } catch (FontFormatException | IOException e) {
      e.printStackTrace();
    } finally {
      多个流关闭的简单方法.close(bis, is);
    }
    return definedFont;
  }
}
