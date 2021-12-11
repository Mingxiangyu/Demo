package com.iglens.图片;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class 图片上画圆 {

  /**
   * 图片上画圆
   *
   * @param image 缓存中图片
   * @param x 圆心x
   * @param y 圆心y
   * @param diameter 圆直径
   */
  public static void drawOval(BufferedImage image, int x, int y, int diameter) {
    Graphics2D graphics = image.createGraphics();
    graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    graphics.setColor(Color.RED);
    // 获取比例尺，然后3*这个比例尺获得画笔的宽度
    // 默认为2000宽度
    int defaultWidth = 2000;
    // 2000宽度下笔画的轮廓（画笔宽度/线宽为3px）
    int lineWidth = 3;
    // 获取长宽的较大值
    int maxPx = Math.max(image.getHeight(), image.getWidth());
    // 得到相对于2000的放大比例
    double plottingScale = (double) maxPx / defaultWidth;
    // 获取该长度下笔画的宽度(同比扩大)
    lineWidth = (int) (lineWidth * plottingScale);

    BasicStroke bs1 = new BasicStroke(lineWidth);
    graphics.setStroke(bs1);
    // 圆心半径
    int circleCenter = (int) (10 * plottingScale);
    // 绘制一个圆 爆心
    graphics.drawOval(x - circleCenter / 2, y - circleCenter / 2, circleCenter, circleCenter);
    // 1. 绘制一个圆 毁伤范围
    graphics.drawOval(x - diameter / 2, y - diameter / 2, diameter, diameter);
    graphics.dispose();
  }
}
