package org.demo.经纬度.经纬度转换;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

class frame1 extends JFrame {
  /** */
  private static final long serialVersionUID = 1L;
  // 输入输出框
  private JTextField textField1 = new JTextField(8);
  private JTextField textField2 = new JTextField(3);
  private JTextField textField3 = new JTextField(3);
  private JTextField textField4 = new JTextField(5);
  private JTextField textField5 = new JTextField(8);
  private JTextField textField6 = new JTextField(3);
  private JTextField textField7 = new JTextField(3);
  private JTextField textField8 = new JTextField(5);
  private JLabel label1 = new JLabel("度——————>度分秒");
  private JLabel label2 = new JLabel("度分秒——————>度");
  private JButton button1 = new JButton("转换");
  private JButton button2 = new JButton("转换");

  public frame1() {
    // 设置窗体大小、位置
    JFrame frame = new JFrame("度分秒转换工具");
    frame.setSize(600, 300);
    frame.setLocationRelativeTo(null);

    // 定义面板容器
    JPanel contentpanel = new JPanel();
    contentpanel.setBorder(new EmptyBorder(5, 5, 5, 5));
    frame.add(contentpanel);
    contentpanel.setLayout(null);

    JPanel panel = new JPanel();
    panel.setBounds(0, 0, 580, 380);
    contentpanel.add(panel);
    panel.setLayout(null);

    // 度转度分秒

    // 设置标签
    label1.setBounds(220, 10, 200, 28);
    panel.add(label1);

    // 设置输入文本框
    textField1.setBounds(50, 50, 80, 28);
    panel.add(textField1);

    // 设置输出文本框
    textField2.setBounds(270, 50, 50, 28);
    panel.add(textField2);
    textField3.setBounds(340, 50, 50, 28);
    panel.add(textField3);
    textField4.setBounds(410, 50, 100, 28);
    panel.add(textField4);

    // 设置按钮
    button1.setBounds(150, 50, 100, 28);
    button1.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            // 计算textfield2需要显示的数字
            Double iDouble = Double.parseDouble(textField1.getText());
            int i = iDouble.intValue();
            String string = String.valueOf(i);
            textField2.setText(string);
            // 计算textfield3需要显示的数字
            double j1 = iDouble - i;
            Double j2 = j1 * 60;
            int j3 = j2.intValue();
            textField3.setText(String.valueOf(j3));
            // 计算textField4
            double k1 = j2 - j3;
            Double k2 = k1 * 60;
            textField4.setText(String.valueOf(k2));
          }
        });
    panel.add(button1);

    // 度分秒转度

    label2.setBounds(220, 90, 200, 28);
    panel.add(label2);

    textField5.setBounds(50, 130, 50, 28);
    panel.add(textField5);
    textField6.setBounds(120, 130, 50, 28);
    textField7.setBounds(190, 130, 100, 28);
    textField8.setBounds(430, 130, 80, 28);
    panel.add(textField6);
    panel.add(textField7);
    panel.add(textField8);

    button2.setBounds(310, 130, 100, 28);
    panel.add(button2);
    button2.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent e) {
            Double iDouble = Double.parseDouble(textField7.getText());
            iDouble = iDouble / 60;
            iDouble = Double.parseDouble(textField6.getText()) + iDouble;
            iDouble = iDouble / 60;
            iDouble = iDouble + Double.parseDouble(textField5.getText());
            textField8.setText(iDouble.toString());
          }
        });

    frame.setVisible(true);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }
}

public class 度分秒互换 {
  public static void main(String[] args) {
    new frame1();
  }
}
