package com.iglens;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;

public class GUI桌面截图 extends JFrame {
  static String 截图存储路径 = "C:\\Users\\zhouhuilin\\Desktop";

  GUI桌面截图() {
    this.setTitle("Java屏幕截图小工具");
    this.setSize(400, 300);
    this.setVisible(true);
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    JButton jButton = new JButton("点击截图");
    jButton.setSize(120, 60);
    setLayout(new FlowLayout());
    getContentPane().add(jButton);

    // 为按钮添加监听事件
    jButton.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            try {
              PrintScreen();
            } catch (AWTException | IOException ex) {
              throw new RuntimeException(ex);
            }
          }
        });
  }

  // 实现截图功能
  public static void PrintScreen() throws AWTException, IOException {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int width = (int) screenSize.getWidth();
    int height = (int) screenSize.getHeight();

    Robot robot = new Robot();
    BufferedImage bi = robot.createScreenCapture(new Rectangle(width, height));
    ImageIO.write(bi, "png", new File(截图存储路径, "上一张截图.png"));
  }

  public static void main(String[] args) {
    GUI桌面截图 printScreen = new GUI桌面截图();
  }
}
