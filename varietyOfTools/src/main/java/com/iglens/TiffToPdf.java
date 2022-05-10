package com.iglens;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.RandomAccessFileOrArray;
import com.itextpdf.text.pdf.codec.TiffImage;
import java.awt.DisplayMode;
import java.awt.FlowLayout;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

public class TiffToPdf extends JFrame implements ActionListener {

  public final static String INDEX_FILEEXT = ".tif.tiff";
  private JTextField inPath;
  private JTextField outPath;
  private JTextField text;
  private JButton selectInPath;
  private JButton selectOutPath;
  private JButton submit;
  private JLabel lb1;
  private JLabel lb2;
  private JLabel lb3;
  private JFileChooser chooser;
  private Box baseBox, box1, box2, box3, box4, box5;
  JProgressBar progressBar;
  int allCount = 0;// 统计成功转换成双层pdf的个数
  int successCount = 0;
  //final static JavaSDK javaSDK = new JavaSDK();

  public TiffToPdf(String title) {
    init(title);

    baseBox = Box.createVerticalBox();
    box1 = Box.createHorizontalBox();
    //box2 = Box.createHorizontalBox();
    box3 = Box.createHorizontalBox();
    box4 = Box.createHorizontalBox();
    box5 = Box.createHorizontalBox();

    box4 = Box.createHorizontalBox();

    progressBar = new JProgressBar();
    box5.add(progressBar);
    progressBar.setStringPainted(true);

    lb1 = new JLabel("输入源：");
    //lb2 = new JLabel("输出源：");
    // lb3=new JLabel("文件转换结果：");
    // lb3.setVisible(false);

    inPath = new JTextField(30);
    inPath.setEditable(true);

    //outPath = new JTextField(30);
    //outPath.setEditable(false);

    text = new JTextField(30);
    text.setEditable(false);
    text.setVisible(false);

    submit = new JButton("确定");
    selectInPath = new JButton("浏览");
    //selectOutPath = new JButton("浏览");

    box1.add(lb1);
    box1.add(inPath);
    box1.add(selectInPath);

    //box2.add(lb2);
    //box2.add(outPath);
    //box2.add(selectOutPath);

    // box3.add(lb3);
    box3.add(text);

    box4.add(submit);

    baseBox.add(Box.createVerticalStrut(25));
    baseBox.add(box1);
    //baseBox.add(Box.createVerticalStrut(10));
    //baseBox.add(box2);
    baseBox.add(Box.createVerticalStrut(10));
    baseBox.add(box3);
    baseBox.add(Box.createVerticalStrut(10));
    baseBox.add(box4);
    baseBox.add(Box.createVerticalStrut(10));
    baseBox.add(box5);

    add(baseBox);

    selectInPath.addActionListener(this);
    //selectOutPath.addActionListener(this);
    submit.addActionListener(this);

    setVisible(true);

  }

  public void init(String title) {
    setLayout(new FlowLayout());
    /* 获取屏幕尺寸，使界面居中显示 */
    GraphicsEnvironment graphicsEnvironment = GraphicsEnvironment
        .getLocalGraphicsEnvironment();
    GraphicsDevice graphicsDevice = graphicsEnvironment
        .getDefaultScreenDevice();
    DisplayMode displayMode = graphicsDevice.getDisplayMode();
    int wWidth = displayMode.getWidth();
    int wheight = displayMode.getHeight();
    int width = 500;
    int height = 300;
    setTitle(title);
    setBounds((wWidth - width) / 2, (wheight - height) / 2, 500, 300);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

  }

  @Override
  public void actionPerformed(ActionEvent e) {
    // TODO Auto-generated method stub

    if (e.getSource() == submit) {//点击确定按钮
      File inFile = new File(inPath.getText());
      //File outFile = new File(outPath.getText());
      if (inPath.getText() != null && !("").equals(inPath.getText())) {
        if (inPath.getText().endsWith(":\\")) {
          JOptionPane.showMessageDialog(null, "不要把根目录作为输入源", "警告对话框",
              JOptionPane.WARNING_MESSAGE);
          return;
        }
      }
      if (!inFile.isDirectory()) {
        JOptionPane.showMessageDialog(null, "输入源路径不存在,请重新输入:\\", "警告对话框",
            JOptionPane.WARNING_MESSAGE);
        return;
      }

      submit.setEnabled(false);
      selectInPath.setEnabled(false);
      allCount = 0;
      successCount = 0;
      List<String> srcList = new ArrayList<String>();//含有tif的所有文件夹
      //获取指定的所有文件夹
      List<File> files = searchFiles(new File(inPath.getText()));
      getDirPath(files, srcList);
      allCount = srcList.size();
      final int size = srcList.size();
      final List<String> srcList_ = srcList;
      final List<File> files_ = files;
      this.text.setText(inPath.getText() + "目录下需转" + allCount + "个pdf文件");
      this.text.setVisible(true);
      this.setVisible(true);
      /*由于事件响应没有结束，java图形界面的进度条不会变化，所以调用ocr另起一个线程*/
      new Thread(new Runnable() {
        public void run() {
          if (srcList_ != null) {
            for (int i = 0; i < srcList_.size(); i++) {//循环每个tif文件夹
              List<String> tifPath = getTifPathOfDir(files_, srcList_.get(i));//文件夹下的tif路径
              if (toPdfByItext(tifPath, srcList_.get(i))) {
                successCount++;
              }
              //TODO 转换
              progressBar.setValue((int) ((i + 1) * 100 / size));//设置进度
            }
          }
          text.setText(inPath.getText() + "目录下要转成" + allCount + "个pdf,成功转换" + successCount
              + "个");
          text.setVisible(true);
          submit.setEnabled(true);
          selectInPath.setEnabled(true);
          setVisible(true);

        }
      }).start();

    } else if (e.getSource() == selectInPath) {//点击浏览
      int mode = 0;
      chooser = new JFileChooser();
      chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);//只能选择目录
      mode = chooser.showOpenDialog(this);
      if (mode == JFileChooser.APPROVE_OPTION) {
        File selectedFile = chooser.getSelectedFile();
        inPath.setText(selectedFile.getAbsolutePath());
      }
    }

  }


  public boolean toPdfByItext(List<String> tifPath, String src) {

    //要生成的pdf文件路径和文件名

    String destFile = src + ".pdf";
    //创建一份空PDF文档
    Document document = new Document(PageSize.A4);
    int comps = 0;
    try {
      PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(destFile));

      document.open();
      for (int i = 0; i < tifPath.size(); i++) {
        PdfContentByte cb = writer.getDirectContent();
        RandomAccessFileOrArray ra = null;
        //创建TIF文件,根据srcPath
        ra = new RandomAccessFileOrArray(tifPath.get(i));
        comps = TiffImage.getNumberOfPages(ra);
        //读取TIF图片，并写入PDF文档
        for (int c = 1; c <= comps; c++) {
          Image img = null;
          try {
            img = TiffImage.getTiffImage(ra, c);
          } catch (Exception e) {
            System.out.println("出错");
          }
          if (img != null) {
            resizeImage(img, document);
            if (i == 0) {
              document.open();//open放太前面，第一页无法根据指定的纸张横纵向设置
              cb = writer.getDirectContent();
            } else {
              document.newPage();
            }
            cb.addImage(img);

          }
        }

        ra.close();
      }
      document.close();

    } catch (IOException ex) {
      System.out.println("错误：" + ex.getMessage());
      ex.printStackTrace();
      return false;
      //throw new OriginalInfoException("文档转换过程IO错误！");
    } catch (DocumentException e) {
      System.out.println("错误：" + e.getMessage());
      e.printStackTrace();
      return false;
      //throw new OriginalInfoException("创建文档实例出错！");
    }
    return true;
  }


  private void resizeImage(Image img, Document document) {
    img.setAbsolutePosition(0, 0); // A4的最大宽度

    float intWidth = img.getWidth();
    float intHeight = img.getHeight();
    float intMaxWidth = PageSize.A4.getWidth();  //缩小一些尺寸，这样按比例缩小时，不至于太紧张
    float intMaxHeight = PageSize.A4.getHeight();

    if (img.getWidth() > img.getHeight()) { // 判断图片宽是否大于高
      // 图片横向
      document.setPageSize(PageSize.A4.rotate());

      if (intWidth > intMaxWidth || intHeight > intMaxHeight) {

        //判断有图的部分接近方图还是宽图
        float standardScale = intMaxWidth / intMaxHeight;
        float imgScale = intHeight / intWidth;

        if (standardScale > imgScale) {
          intHeight = (float) (intHeight * intMaxHeight
              / intWidth);    //A4已缩小一些尺寸，这样按比例缩小时，高度不至于太紧张
          intWidth = intMaxHeight;
        } else {  //接近方图，高度就要固定
          intWidth = (float) (intWidth * intMaxWidth / intHeight);    //这种也行，和上面算法差不多，宽度有时会太紧张
          intHeight = intMaxWidth;
        }
      }
    } else {// 图片纵向
      document.setPageSize(PageSize.A4);
      if (intWidth > intMaxWidth || intHeight > intMaxHeight) {

        //判断有图的部分接近方图还是宽图
        float standardScale = intMaxWidth / intMaxHeight;
        float imgScale = intWidth / intHeight;

        if (standardScale > imgScale) {
          intWidth = (float) (intWidth * intMaxHeight / intHeight);
          intHeight = intMaxHeight;
        } else {  //接近方图，宽度就要固定
          intHeight = (float) (intHeight * intMaxWidth / intWidth);
          intWidth = intMaxWidth;
        }
      }
    }

    img.scaleAbsolute(intWidth, intHeight);
    img.setAlignment(Image.MIDDLE);
  }


  /**
   * @param folder 文件夹
   * @return 所有tif/tiff文件
   */
  public static List<File> searchFiles(File folder) {
    List<File> result = new ArrayList<File>();
    if (folder.isFile()) {
      result.add(folder);
    }

    File[] subFolders = folder.listFiles(new FileFilter() {
      @Override
      public boolean accept(File file) {
        if (file.isDirectory()) {
          return true;
        }
        if (file.getName().toLowerCase().endsWith(".tif") || file.getName().toLowerCase()
            .endsWith(".tiff")) {
          return true;
        }
        return false;
      }
    });

    if (subFolders != null) {
      for (File file : subFolders) {
        if (file.isFile()) {
          // 如果是文件则将文件添加到结果列表中
          result.add(file);
        } else {
          // 如果是文件夹，则递归调用本方法，然后把所有的文件加到结果列表中
          result.addAll(searchFiles(file));
        }
      }
    }
    return result;
  }


  /**
   * @param files 所有tif文件的list
   * @param srcList 用来存放含有tif文件夹路径的list
   */
  public void getDirPath(List<File> files, List<String> srcList) {
    //ArrayList newList = new ArrayList();
    String dirString;
    for (File file : files) {

      dirString = file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf("\\"));
      if (!srcList.contains(dirString)) {
        srcList.add(dirString);
      }
      //System.out.println(file.getAbsolutePath());
    }
    //System.out.println("一共有："+srcList.size());

  }

  //某个文件夹下的tif文件路径

  /**
   * @param files 所有tif文件的list
   * @param dirPath 含有tif的文件夹路径
   */
  public List<String> getTifPathOfDir(List<File> files, String dirPath) {
    List<String> tifList = new ArrayList<String>();//一个文件夹下的tif文件路径
    String tifString;

    for (File file : files) {
      tifString = file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf("\\"));
      if (dirPath.equals(tifString)) {
        tifList.add(file.getAbsolutePath());
      }
    }

    return tifList;
  }

}
