package com.iglens;/**
 *代码中 有一些代码是没有用的
 *重点：a.时间，数量，暂停这三点的逻辑关系
 *     b.可以多次暂停
 *     c.取消暂停后要将焦点重新定到框架上
 *代码可以直接运行。但是背景图与bgm 需要另行设置。
 */

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.UIManager;

public class 打字练习 extends JFrame implements Runnable {
  /**
   *
   */
  private static final long serialVersionUID = 112233445566L;
  JPanel contentPane;
  JPanel jMainPanel = new JPanel();
  // 开始、结束 按钮
  JButton jBtnStart = new JButton();
  JButton jBtnStop = new JButton();
  JButton jBtnPause1 = new JButton(); // 暂停按钮
  JButton jBtnPause2 = new JButton(); // 暂停按钮

  JSlider jSlider1 = new JSlider(); // 速度条
  JSlider jSlider2 = new JSlider(); // 时间条
  JSlider jSlider3 = new JSlider(); // 字符数量

  JLabel jLblRate1 = new JLabel();
  JLabel jLblRate2 = new JLabel();
  JLabel jLblRate3 = new JLabel();
  JLabel jLblTypedResult = new JLabel();

  Thread t ;
  MyListener mylister;
  boolean pause = true;   //判断是否位暂停
  boolean fo = true;
  int count = 1; // count 当前进行的个数
  int count_geshu = 20;   //默认数量
  int time = 0;           //默认时间段
  long timeNum =0; //多次暂停
  long t1 = 0; // 开始
  long t2 = 0; // 暂停
  long t3 = 0; // 暂停结束
  long t4 = 0; // 结束
  int rapidity = 80; // rapidity 游标的位置
  int rapidity2 = 91;
  int rapidity3 = 81;
  // 打字后的成功个数和失败个数
  int nTypedCorrectNum = 0;
  int nTypedErrorNum = 0;
  int[] rush = { 10, 20, 30 }; // 游戏每关的个数 可以自由添加.列 { 10 ,20 ,30 ,40,50}
  // 记录关数
  int rushCount = 0;
  // 随机出现的数字列表，可以自由添加
  char[] chrList = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S',
      'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

  Vector number = new Vector();
  Vector x = new Vector();
  Vector y = new Vector();
  Vector value = new Vector();
  String paiduan = "true";
  AudioClip musciAnjian, musicShibai, musicChenggong;

  public 打字练习() {
    try {
      setDefaultCloseOperation(EXIT_ON_CLOSE);
      // -----------------声音文件---------------------
      musciAnjian = Applet.newAudioClip(new File("sounds//anjian.wav").toURL());
      musicShibai = Applet.newAudioClip(new File("sounds//shibai.wav").toURL());
      musicChenggong = Applet.newAudioClip(new File("sounds//chenggong.wav").toURL());
      // ---------------------------------------
      jbInit();
    } catch (Exception exception) {
      exception.printStackTrace();
    }
  }

  /**
   * Component initialization.
   *
   * @throws java.lang.Exception
   */
  private void jbInit() throws Exception {
    contentPane = (JPanel) getContentPane();
    contentPane.setLayout(null);
    setSize(new Dimension(800, 490));
    setTitle("打字练习");
    ImageIcon icon = new ImageIcon("resource/images/poinfoedit.gif");
    setIconImage(icon.getImage());

    jMainPanel.setBorder(BorderFactory.createEtchedBorder());
    jMainPanel.setBounds(new Rectangle(4, 4, 573, 419));
    jMainPanel.setLayout(null);

    // 开始
    jBtnStart.setBounds(new Rectangle(580, 154, 164, 31));
    jBtnStart.setText("开始/重新开始");
    jBtnStart.addActionListener(new TypeGameFrame_jBtnStart_actionAdapter(this));
    mylister=new MyListener();
    jBtnStart.addKeyListener(mylister);

    // 暂停
    /*
      TODO
     */
    jBtnPause1.setBounds(new Rectangle(580, 189, 164, 31));
    jBtnPause1.setText("暂停");
    jBtnPause1.addActionListener(new TypeGameFrame_jBtnPause1_actionAdapter(this));
    jBtnPause1.addMouseListener(new MouseLister());
    // 暂停2开始
    /*
      TODO
     */
    jBtnPause2.setBounds(new Rectangle(580, 189, 164, 31));
    jBtnPause2.setText("取消暂停");
    jBtnPause2.setVisible(false);
    jBtnPause2.addActionListener(new TypeGameFrame_jBtnPause2_actionAdapter(this));
    jBtnPause2.addMouseListener(new MouseLister());

    // 结束
    jBtnStop.setBounds(new Rectangle(580, 224, 164, 31));
    jBtnStop.setText("结束(退出)");
    jBtnStop.addActionListener(new TypeGameFrame_jBtnStop_actionAdapter(this));

    // 速度
    jSlider1.setBounds(new Rectangle(580, 22, 164, 21));
    jSlider1.setMaximum(100);
    jSlider1.setMinimum(1);
    jSlider1.setValue(50);
    jSlider1.addChangeListener(e -> {
      rapidity = jSlider1.getValue();
      byte nTmpRapidity = (byte) (101 - rapidity);
      jLblRate1.setText("设置速度(1-100):" + nTmpRapidity);
    });
    jLblRate1.setText("设置速度(1-100):50");
    jLblRate1.setBounds(new Rectangle(580, 4, 149, 18));

    // 数量
    jSlider3.setBounds(new Rectangle(580, 71, 164, 21));

    jSlider3.setMaximum(100);
    jSlider3.setMinimum(1);
    jSlider3.setValue(80);
    jSlider3.addChangeListener(e -> {
      rapidity3 = jSlider3.getValue();
      byte nTmpRapidity = (byte) (101 - rapidity3);
      count_geshu = 101 - rapidity3;
      jLblRate3.setText("设置数量(10-100):" + nTmpRapidity + " 个");
    });
    jLblRate3.setText("设置数量(10-100):20 个");
    jLblRate3.setBounds(new Rectangle(580, 53, 149, 18));


    // 时间
    jSlider2.setBounds(new Rectangle(580, 120, 164, 21));
    jSlider2.setMaximum(100);
    jSlider2.setMinimum(1);
    jSlider2.setValue(90);
    rapidity2 = jSlider2.getValue();
    time = 101 - rapidity2;
    jSlider2.addChangeListener(e -> {
      rapidity2 = jSlider2.getValue();
      byte nTmpRapidity = (byte) (101 - rapidity2);
      time = 101 - rapidity2;
      jLblTypedResult.setText("正确:0个,错误:0个,剩余时间:"+time+"s");
      jLblRate2.setText("设置时间(5-100):" + nTmpRapidity + " 秒");
    });
    jLblRate2.setText("设置时间(5-100):10 秒");

    jLblRate2.setBounds(new Rectangle(580, 102, 149, 18));
    // 打字结果
    jLblTypedResult.setText("正确:0个,错误:0个,剩余时间:10s");
    jLblTypedResult.setBounds(new Rectangle(256, 423, 211, 21));

    contentPane.add(jMainPanel);
    contentPane.add(jBtnStop);
    contentPane.add(jBtnPause1);
    contentPane.add(jBtnPause2);
    contentPane.add(jBtnStart);
    contentPane.add(jSlider1);
    contentPane.add(jSlider3);
    contentPane.add(jSlider2);
    contentPane.add(jLblRate1);
    contentPane.add(jLblRate2);
    contentPane.add(jLblRate3);
    contentPane.add(jLblTypedResult);
    this.addKeyListener(mylister);
  }

  public void run() {
    number.clear();
    jLblTypedResult.setText("正确:0个,错误:0个,剩余时间:10s");
    nTypedCorrectNum = 0;
    nTypedErrorNum = 0;
    paiduan = "true";
    int tempTime=0;
    t4 = System.currentTimeMillis();
    while ( t4- t1 - timeNum <= time * 1000 ) {
      //	if (pause) {
      if (count < count_geshu) {
        try {
          t4 = System.currentTimeMillis();
          if(t4- t1 - timeNum > time * 1000 ){
            break;
          }else{
            ;
          }
          if(pause){
            Thread t = new Thread(new Tthread());
            t.start();
            count += 1;
            tempTime= time-1-(int) (t4-t1-timeNum)/1000;
            jLblTypedResult.setText("正确:" + nTypedCorrectNum + "个,错误:" + nTypedErrorNum + "个"+",剩余时间:"+tempTime+"s");
            Thread.sleep(500);
          }else{
            jLblTypedResult.setText("正确:" + nTypedCorrectNum + "个,错误:" + nTypedErrorNum + "个"+",剩余时间:"+tempTime+"s");
            Thread.sleep(100);
          }
          // Thread.sleep(500 + (int) (Math.random() * 1000)); //
          // 生产下组停顿时间
          // 最快0.5快.最慢1秒
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }else{
        try {
          t4 = System.currentTimeMillis();
          if(t4- t1 - timeNum > time * 1000 ){
            break;
          }else{
            tempTime= time-1-(int) (t4-t1-timeNum)/1000;
            jLblTypedResult.setText("正确:" + nTypedCorrectNum + "个,错误:" + nTypedErrorNum + "个"+",剩余时间:"+tempTime+"s");

          }
          Thread.sleep(100);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }

      }
//			}else{
//				continue;
//			}
    }

    fo = false;
    int falseNum=count_geshu -nTypedCorrectNum;
    JOptionPane.showMessageDialog(null, "正确:" + nTypedCorrectNum + "个,错误:" + falseNum + "个");
  }

  public void jBtnStartActionPerformed(ActionEvent e) {
    // jMainPanel.removeAll();
    for (Object o : number) {
      Bean bean = ((Bean) o);
      bean.getShow().setVisible(false);
    }
    t1 = System.currentTimeMillis();
    jLblTypedResult.setText("正确:0个,错误:0个,剩余时间:10s");
    number.clear();
    nTypedCorrectNum = 0;
    nTypedErrorNum = 0;
    count = 0;
    fo = true;
    t2 = 0;
    t3 = 0;
    pause = true;
    timeNum = 0;
    Thread t = new Thread(this);
    t.start();
  }

  // 暂停
  public void jBtnPause1_actionPerformed(ActionEvent e1) {
    t2 = System.currentTimeMillis();
    t3 = t2;
    // fo =false;
    pause = false;
    jBtnPause1.setVisible(false);
    jBtnPause2.setVisible(true);
    JOptionPane.showMessageDialog(null, "暂停中,无法打字");
    //	jBtnStart.removeKeyListener(mylister);
  }

  // 暂停2开始
  public void jBtnPause2_actionPerformed(ActionEvent e2) {
    t3 = System.currentTimeMillis();
    timeNum +=t3 -t2;
    // fo =true;
    pause = true;
    jBtnPause1.setVisible(true);
    jBtnPause2.setVisible(false);
    //	jBtnStart.addKeyListener(mylister);
    //	contentPane.requestFocusInWindow();
    jBtnStart.requestFocusInWindow();

  }

  public void jBtnStop_actionPerformed(ActionEvent e3) {
    // count = rush[rush_count] + 1;
    // paiduan = "flase";
    System.exit(0);
    contentPane.setVisible(false);
    ;
  }

  /**
   *
   * */
  class Tthread implements Runnable {
    public void run() {
      // boolean fo = true;
      //	int tempTime= time-2-(int) (t4-t1-timeNum)/1000;
      //	jLblTypedResult.setText("正确:" + nTypedCorrectNum + "个,错误:" + nTypedErrorNum + "个"+",剩余时间:"+tempTime);
      int Y = 0, X = 0;
      // int temp_y=0;
      JLabel show = new JLabel();
      show.setFont(new java.awt.Font("宋体", Font.PLAIN, 43));
      jMainPanel.add(show);
      X = 10 + (int) (Math.random() * 400);
      String parameter = chrList[(int) (Math.random() * chrList.length)] + "";
      Bean bean = new Bean();
      bean.setParameter(parameter);
      bean.setShow(show);
      number.add(bean);
      show.setText(parameter);
      while (fo) {
        // ---------------------数字下移--------------------
        if (pause) {
          show.setBounds(new Rectangle(X, Y += 2, 33, 33));
        } else {
          show.setBounds(new Rectangle(X, Y += 0, 0, 0));
        }
        try {
          Thread.sleep(rapidity);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        if (Y >= 419) {
          for (int i = number.size() - 1; i >= 0; i--) {
            Bean bn = ((Bean) number.get(i));
            if (parameter.equalsIgnoreCase(bn.getParameter())) {
              nTypedErrorNum += 1;
              //	int	tempTime= time-1-(int) (t4-t1-timeNum)/1000;
              //		jLblTypedResult.setText("正确:" + nTypedCorrectNum + "个,错误:" + nTypedErrorNum + "个"+",剩余时间:"+tempTime);
              number.removeElementAt(i);
              musicShibai.play();
              break;
            }
          }
        }
      }

    }
  }

  /**
   * 监听键盘，统计正确的个数
   *
   */
  class MyListener extends KeyAdapter {
    public void keyPressed(KeyEvent e) {
      String uu = e.getKeyChar() + "";
      for (int i = 0; i < number.size(); i++) {
        Bean bean = ((Bean) number.get(i));
        if (uu.equalsIgnoreCase(bean.getParameter())) {
          nTypedCorrectNum += 1;
          number.removeElementAt(i);
          bean.getShow().setVisible(false);
          musicChenggong.play();
          break;
        }
      }
      musciAnjian.play();
    }
  }
  /**
   *
   * @author fan
   *
   */
  class MouseLister extends MouseAdapter{
    public void mouseClieced(MouseEvent e){

    }
  }


  public static void main(String[] args) {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (Exception exception) {
      exception.printStackTrace();
    }

    // 创建窗口
    打字练习 gameFrame = new 打字练习();
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension frameSize = gameFrame.getSize();
    if (frameSize.height > screenSize.height) {
      frameSize.height = screenSize.height;
    }
    if (frameSize.width > screenSize.width) {
      frameSize.width = screenSize.width;
    }
    gameFrame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    gameFrame.setVisible(true);
  }

}

/**
 * 停止按钮适配器
 */
class TypeGameFrame_jBtnStop_actionAdapter implements ActionListener {
  private 打字练习 adaptee;

  TypeGameFrame_jBtnStop_actionAdapter(打字练习 adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.jBtnStop_actionPerformed(e);
  }
}

/**
 * 开始按钮适配器
 */
class TypeGameFrame_jBtnStart_actionAdapter implements ActionListener {
  private 打字练习 adaptee;

  TypeGameFrame_jBtnStart_actionAdapter(打字练习 adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.jBtnStartActionPerformed(e);
  }
}

/**
 * 暂停按钮适配器
 */
class TypeGameFrame_jBtnPause1_actionAdapter implements ActionListener {
  private 打字练习 adaptee;

  TypeGameFrame_jBtnPause1_actionAdapter(打字练习 adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.jBtnPause1_actionPerformed(e);
  }
}

/**
 * 暂停2开始按钮适配器
 */
class TypeGameFrame_jBtnPause2_actionAdapter implements ActionListener {
  private 打字练习 adaptee;

  TypeGameFrame_jBtnPause2_actionAdapter(打字练习 adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.jBtnPause2_actionPerformed(e);
  }
}

class Bean {
  String parameter = null;
  JLabel show = null;

  public JLabel getShow() {
    return show;
  }

  public void setShow(JLabel show) {
    this.show = show;
  }

  public String getParameter() {
    return parameter;
  }

  public void setParameter(String parameter) {
    this.parameter = parameter;
  }
}
