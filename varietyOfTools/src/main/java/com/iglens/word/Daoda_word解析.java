
package com.iglens.word;

import com.iglens.txt.去除ASCII中方块null值;
import com.iglens.txt.获取TXT文本编码格式;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.model.PicturesTable;
import org.apache.poi.hwpf.usermodel.*;
import org.apache.poi.xwpf.usermodel.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

@Slf4j
public class Daoda_word解析 {

  /**
   * word的全角空格 以及\t 制表符
   */
  private static final String WORD_BLANK = "[\u00a0|\u3000|\u0020|\b|\t]";

  /**
   * word的它自己造换行符 要换成string的换行符
   */
  private static final String WORD_LINE_BREAK = "[\u000B|\r|\u0007]";

  /**
   * word table中的换行符和空格
   */
  private static final String WORD_TABLE_FILTER = "[\\t|\\n|\\r|\\f|\\s+| +]";

  /**
   * 抽取文字时去掉不必须字符正则
   */
  private static final String splitter = "[\\t|\\n|\\r|\\f|\\s+|\u00a0+]";

  private static final String regexClearBeginBlank = "^" + splitter + "*|" + splitter + "*$";

  public static void main(String[] args) {

    String path = "D:\\mxy\\0110\\测试文件1-26\\测试附件图片\\test.doc";

    File bwFile = new File(path);
    List<String> docText;
    if (path.endsWith("doc")) {
      docText = getDocText(bwFile, new ArrayList<>(), bwFile.getParent());
    } else if (path.endsWith("docx")) {
      docText = getDocxText(bwFile, new ArrayList<>(), bwFile.getParent());
    } else {
      docText = getTxtText(bwFile);
    }
    System.out.println(StringUtils.join(docText, "\n"));


  }

  /**
   * 获取txt中所有文本
   *
   * @param bwFile txt文件
   * @return 提取出来的每行字符集合
   */
  public static List<String> getTxtText(File bwFile) {
    List<String> txtList = new ArrayList<>();
    //获取bwFile的编码格式
    String textFileEncode = 获取TXT文本编码格式.getFilecharset(bwFile);
    try (FileInputStream in = new FileInputStream(bwFile);
        BufferedReader br = new BufferedReader(new InputStreamReader(in, textFileEncode))) {
      String line;
      while ((line = br.readLine()) != null) {
        //去除文本中的AsciiNul值
        line = 去除ASCII中方块null值.trimAsciiNull(line);
        if (StringUtils.isBlank(line) || StringUtils.equals(line, "\n")) {
          continue;
        }
        txtList.add(line);
      }
    } catch (Exception e) {
      log.error("解析 TXT 格式报文，抽取TXT内文件时： 出现问题：{}", e.getMessage(), e);
    }
    return txtList;
  }

  /**
   * 预览doc 得到结构对象  这里默认对单元格中的文字做去换行操作
   *
   * @param bwFile word文件
   * @return 提取出来的每行字符集合
   */
  public static List<String> previewDocText(File bwFile) {
    // 开始抽取doc中的文字
    List<String> stList = new ArrayList<>();
    try (FileInputStream fileInputStream = new FileInputStream(bwFile);
        HWPFDocument doc = new HWPFDocument(fileInputStream)) {
      //上一行文字
      String upLine = null;
      // 得到文档的读取范围
      Range range = doc.getRange();
      for (int i = 0; i < range.numParagraphs(); i++) {
        Paragraph paragraph = range.getParagraph(i);
        // 拿出段落中不包括表格的文字
        if (!paragraph.isInTable()) {
          String text = paragraph.text();
          text = StringUtils.trimToEmpty(text);
          if (StringUtils.isBlank(text)) {
            continue;
          }
          String textWithSameBlankAndBreak =
              text.replaceAll(WORD_BLANK, " ").replaceAll(WORD_LINE_BREAK, "\n")
                  .replaceAll("\n+", "\n");
          String clearBeginBlank = textWithSameBlankAndBreak.replaceAll(regexClearBeginBlank, "");
          if (StringUtils.isNotBlank(clearBeginBlank)) {
            //（避免出现上下两行文字一样）
            if (!StringUtils.equals(upLine, clearBeginBlank)) {
              stList.add(clearBeginBlank);
            }
            upLine = clearBeginBlank;
          }
        } else {
          //获取表格中文字
          Table table;
          try {
            table = range.getTable(paragraph);
          } catch (IllegalArgumentException e) {
            log.warn("DOC文档 {} 中第 {} 个范围不为表格，无法读取，跳过！", bwFile.getPath(), i);
            continue;
          }
          for (int j = 0; j < table.numRows(); j++) {
            StringBuilder cellBuilder = new StringBuilder();
            TableRow row = table.getRow(j);
            for (int k = 0; k < row.numCells(); k++) {
              //读取完该范围更新i的下标
              i++;
              TableCell cell = row.getCell(k);
              String cellValue = cell.text();
              cellValue = StringUtils.trimToEmpty(cellValue);
              if (StringUtils.isBlank(cellValue)) {
                continue;
              }
              // 将word中的特有字符转化为普通的换行符、空格符等
              String textWithSameBlankAndBreak =
                  cellValue
                      .replaceAll(WORD_BLANK, " ")
                      .replaceAll(WORD_LINE_BREAK, "\n")
                      .replaceAll("\n+", "\n");
              // 去除word特有的不可见字符
              String textClearBeginBlank =
                  textWithSameBlankAndBreak.replaceAll(WORD_TABLE_FILTER, "");
              // 为抽取的每一个单元格加上\t作为单元格标识
              cellBuilder.append(textClearBeginBlank).append("\t");
            }

            String rowText = cellBuilder.toString();
            //去除该行最后一个\t
            rowText = StringUtils.substringBeforeLast(rowText, "\t");
            if (StringUtils.isNotBlank(rowText)) {
              //（避免出现上下两行文字一样）
              if (!StringUtils.equals(upLine, rowText)) {
                stList.add(rowText);
              }
              upLine = rowText;
            }
          }
        }
      }
    } catch (IOException e) {
      log.error("解析Word格式报文时： 文件路径为：{} 解析失败，异常为：{}", bwFile.getPath(), e.getMessage(), e);
    }
    return stList;
  }

  /**
   * 预览doc内文字，按显示行展示
   *
   * @param bwFile word文件
   * @return 提取出来的每行字符集合
   */
  public static List<String> previewDocxText(File bwFile) {
    List<String> stList = new ArrayList<>();
    // 获取该文档下所有元素
    try (FileInputStream fileInputStream = new FileInputStream(bwFile);
        XWPFDocument docx = new XWPFDocument(fileInputStream)) {
      Iterator<IBodyElement> iter = docx.getBodyElementsIterator();
      //上一行文字
      String upLine = null;
      while (iter.hasNext()) {
        IBodyElement element = iter.next();
        if (element instanceof XWPFParagraph) {
          // 获取段落元素
          XWPFParagraph paragraph = (XWPFParagraph) element;
          String text = paragraph.getText();
          text = StringUtils.trimToEmpty(text);
          if (StringUtils.isBlank(text)) {
            continue;
          }
          // 将word中的特有字符转化为普通的换行符、空格符等
          String textWithSameBlankAndBreak =
              text.replaceAll(WORD_BLANK, " ")
                  .replaceAll(WORD_LINE_BREAK, "\n")
                  .replaceAll("\n+", "\n");
          // 去除word特有的不可见字符
          String textClearBeginBlank = textWithSameBlankAndBreak.replaceAll(regexClearBeginBlank,
              "");
          if (StringUtils.isNotBlank(textClearBeginBlank)) {
            //（避免出现上下两行文字一样）
            if (!StringUtils.equals(upLine, textClearBeginBlank)) {
              stList.add(textClearBeginBlank);
            }
            upLine = textClearBeginBlank;
          }
        } else if (element instanceof XWPFTable) {
          XWPFTable table = (XWPFTable) element;
          gettable(table, stList, upLine);
        }
      }
    } catch (IOException e) {
      log.error("预览Word格式报文时： 文件路径为：{} 解析失败，异常为：{}", bwFile.getPath(), e.getMessage(), e);
    }
    return stList;
  }

  /**
   * 解析doc表格 得到结构对象  这里默认对单元格中的文字做去换行操作
   *
   * @param bwFile word文件
   * @param pictureList word内图片提取出来放到该集合中
   * @param picDir 图片保存的文件夹
   * @return 提取出来的每行字符集合
   */
  public static List<String> getDocText(File bwFile, List<File> pictureList, String picDir) {
    // 开始抽取doc中的文字
    List<String> stList = new ArrayList<>();
    try (FileInputStream fileInputStream = new FileInputStream(bwFile);
        HWPFDocument doc = new HWPFDocument(fileInputStream)) {
      //上一行文字
      String upLine = null;
      // 得到文档的读取范围
      Range range = doc.getRange();
      for (int i = 0; i < range.numParagraphs(); i++) {
        Paragraph paragraph = range.getParagraph(i);
        // 拿出段落中不包括表格的文字
        if (!paragraph.isInTable()) {
          String text = paragraph.text();
          text = StringUtils.trimToEmpty(text);
          if (StringUtils.isBlank(text)) {
            continue;
          }
          String textWithSameBlankAndBreak =
              text.replaceAll(WORD_BLANK, " ").replaceAll(WORD_LINE_BREAK, "\n")
                  .replaceAll("\n+", "\n");
          String clearBeginBlank = textWithSameBlankAndBreak.replaceAll(regexClearBeginBlank, "");
          if (StringUtils.isNotBlank(clearBeginBlank)) {
            //（避免出现上下两行文字一样）
            if (!StringUtils.equals(upLine, clearBeginBlank)) {
              stList.add(clearBeginBlank);
            }
            upLine = clearBeginBlank;
          }
        } else {
          //获取表格中文字
          Table table;
          try {
            table = range.getTable(paragraph);
          } catch (IllegalArgumentException e) {
            log.warn("DOC文档 {} 中第 {} 个范围不为表格，无法读取，跳过！", bwFile.getPath(), i);
            continue;
          }
          for (int j = 0; j < table.numRows(); j++) {
            StringBuilder cellBuilder = new StringBuilder();
            TableRow row = table.getRow(j);
            for (int k = 0; k < row.numCells(); k++) {
              //读取完该范围更新i的下标
              i++;
              TableCell cell = row.getCell(k);
              String cellValue = cell.text();
              cellValue = StringUtils.trimToEmpty(cellValue);
              if (StringUtils.isBlank(cellValue)) {
                continue;
              }
              // 将word中的特有字符转化为普通的换行符、空格符等
              String textWithSameBlankAndBreak =
                  cellValue
                      .replaceAll(WORD_BLANK, " ")
                      .replaceAll(WORD_LINE_BREAK, "\n")
                      .replaceAll("\n+", "\n");
              // 去除word特有的不可见字符
              String textClearBeginBlank =
                  textWithSameBlankAndBreak.replaceAll(WORD_TABLE_FILTER, "");
              // 为抽取的每一个单元格加上\t作为单元格标识
              cellBuilder.append(textClearBeginBlank).append("\t");
            }

            String rowText = cellBuilder.toString();
            //去除该行最后一个\t
            rowText = StringUtils.substringBeforeLast(rowText, "\t");
            if (StringUtils.isNotBlank(rowText)) {
              //（避免出现上下两行文字一样）
              if (!StringUtils.equals(upLine, rowText)) {
                stList.add(rowText);
              }
              upLine = rowText;
            }
          }
        }
      }

      //获取word中图片
      try {
        PicturesTable picturesTable = doc.getPicturesTable();
        //word内图片名称集合，避免因图片名相同而被覆盖
        List<String> pictureNameList = new ArrayList<>();
        for (Picture picture : picturesTable.getAllPictures()) {
          if (picture.getSize() > 10000) {
            String fileBaseName = FilenameUtils.getBaseName(bwFile.getName());
            //trim一下，避免bw文件首尾有空格，但创建的附件文件夹没有空格，导致附件无法存储
            fileBaseName = StringUtils.trim(fileBaseName);
            String pictureFullName = picture.suggestFullFileName();
            if (pictureNameList.contains(pictureFullName)) {
              pictureFullName = new Random().nextInt(10) + pictureFullName;
            }
            pictureNameList.add(pictureFullName);
            String pictureName = fileBaseName + "_" + pictureFullName;
            //拼接上报文文件名的文件夹用于存储该报文内图片，如果不存在则创建该文件夹
            String newPicDir = picDir + File.separator + fileBaseName;
            File picDirFile = new File(newPicDir);
            if (!picDirFile.exists()) {
              picDirFile.mkdirs();
            }
            //拼接报文内图片全路径
            String picPath = newPicDir + File.separator + pictureName;
            File picFile = new File(picPath);
            picture.writeImageContent(new FileOutputStream(picFile));
            pictureList.add(picFile);
            // String dateFolder = FTPUtils.buildDateFolder(bwFile, "/");
            // 将源文件上传到MinIO服务器
            // MinioUtils.save(null, dateFolder + fileBaseName + "/" + pictureName, picFile);
            // new FTPUtils().upload(picFile, dateFolder + fileBaseName, pictureName);
          }
        }
      } catch (Exception e) {
        log.error("解析Word格式报文时： 文件路径为：{} 内图片无法解析！", bwFile.getPath(), e);
      }
    } catch (IOException e) {
      log.error("解析Word格式报文时： 文件路径为：{} 解析失败，异常为：{}", bwFile.getPath(), e.getMessage(), e);
      throw new RuntimeException(e.getMessage());
    }
    return stList;
  }

  /**
   * 获取doc内文字，按显示行展示
   *
   * @param bwFile word文件
   * @param pictureList word内图片提取出来放到该集合中
   * @param picDir 图片保存的文件夹
   * @return 提取出来的每行字符集合
   */
  public static List<String> getDocxText(File bwFile, List<File> pictureList, String picDir) {
    List<String> stList = new ArrayList<>();
    // 获取该文档下所有元素
    try (FileInputStream fileInputStream = new FileInputStream(bwFile);
        XWPFDocument docx = new XWPFDocument(fileInputStream)) {
      Iterator<IBodyElement> iter = docx.getBodyElementsIterator();
      //上一行文字
      String upLine = null;
      while (iter.hasNext()) {
        IBodyElement element = iter.next();
        if (element instanceof XWPFParagraph) {
          // 获取段落元素
          XWPFParagraph paragraph = (XWPFParagraph) element;
          String text = paragraph.getText();
          text = StringUtils.trimToEmpty(text);
          if (StringUtils.isBlank(text)) {
            continue;
          }
          // 将word中的特有字符转化为普通的换行符、空格符等
          String textWithSameBlankAndBreak =
              text.replaceAll(WORD_BLANK, " ")
                  .replaceAll(WORD_LINE_BREAK, "\n")
                  .replaceAll("\n+", "\n");
          // 去除word特有的不可见字符
          String textClearBeginBlank = textWithSameBlankAndBreak.replaceAll(regexClearBeginBlank,
              "");
          if (StringUtils.isNotBlank(textClearBeginBlank)) {
            //（避免出现上下两行文字一样）
            if (!StringUtils.equals(upLine, textClearBeginBlank)) {
              stList.add(textClearBeginBlank);
            }
            upLine = textClearBeginBlank;
          }
        } else if (element instanceof XWPFTable) {
          XWPFTable table = (XWPFTable) element;
          gettable(table, stList, upLine);
        } else {
          log.warn("解析Word格式报文时： 文件路径为：{} 存在非表格非段落元素，未解析", bwFile.getPath());
        }
      }

      try {
        //提取docx中图片
        List<XWPFPictureData> picList = docx.getAllPictures();
        //word内图片名称集合，避免因图片名相同而被覆盖
        List<String> pictureNameList = new ArrayList<>();
        for (XWPFPictureData picture : picList) {
          byte[] data = picture.getData();
          //大于 5000 字节的图片才进行抽取，避免莫名的小图片干扰
          if (data.length > 10000) {
            String fileBaseName = FilenameUtils.getBaseName(bwFile.getName());
            //trim一下，避免bw文件首尾有空格，但创建的附件文件夹没有空格，导致附件无法存储
            fileBaseName = StringUtils.trim(fileBaseName);
            String pictureFullName = picture.getFileName();
            if (pictureNameList.contains(pictureFullName)) {
              pictureFullName = new Random().nextInt(10) + pictureFullName;
            }
            pictureNameList.add(pictureFullName);
            String pictureName = fileBaseName + "_" + pictureFullName;
            //拼接上报文文件名的文件夹用于存储该报文内图片，如果不存在则创建该文件夹
            String newPicDir = picDir + File.separator + fileBaseName;
            File picDirFile = new File(newPicDir);
            if (!picDirFile.exists()) {
              picDirFile.mkdirs();
            }
            //拼接报文内图片全路径
            String picPath = newPicDir + File.separator + pictureName;
            File picFile = new File(picPath);
            FileOutputStream fos = new FileOutputStream(picFile);
            fos.write(data);
            pictureList.add(picFile);
            // String dateFolder = FTPUtils.buildDateFolder(bwFile, "/");
            //将源文件上传到MinIO服务器
            // MinioUtils.save(null, dateFolder + fileBaseName + "/" + pictureName, picFile);
            // new FTPUtils().upload(picFile, dateFolder + fileBaseName, pictureName);
          }
        }
      } catch (Exception e) {
        log.error("解析Word格式报文时： 文件路径为：{} 内图片无法解析！", bwFile.getPath(), e);
      }
    } catch (IOException e) {
      log.error("解析Word格式报文时： 文件路径为：{} 解析失败，异常为：{}", bwFile.getPath(), e.getMessage(), e);
      throw new RuntimeException(e.getMessage());
    }
    return stList;
  }

  /**
   * 获取docx表格中文字
   *
   * @param table 表格对象
   * @param upLine 上一行字符串，避免重复文字
   */
  private static void gettable(XWPFTable table, List<String> stringList, String upLine) {
    List<XWPFTableRow> rows = table.getRows();
    for (XWPFTableRow row : rows) {
      StringBuilder cellBuilder = new StringBuilder();
      // 读取每一列数据
      List<XWPFTableCell> cells = row.getTableCells();
      for (XWPFTableCell cell : cells) {
        // 输出当前的单元格的数据
        List<XWPFTable> tables = cell.getTables();
        //如果存在合并单元格，则递归解析，否则正常读取
        if (tables.size() > 0) {
          for (XWPFTable xwpfTable : tables) {
            gettable(xwpfTable, stringList, upLine);
          }
        } else {
          String cellValue = cell.getText();
          cellValue = StringUtils.trimToEmpty(cellValue);
          if (StringUtils.isBlank(cellValue)) {
            continue;
          }
          // 将word中的特有字符转化为普通的换行符、空格符等
          String textWithSameBlankAndBreak =
              cellValue
                  .replaceAll(WORD_BLANK, " ")
                  .replaceAll(WORD_LINE_BREAK, "\n")
                  .replaceAll("\n+", "\n");
          // 去除word特有的不可见字符
          String textClearBeginBlank =
              textWithSameBlankAndBreak.replaceAll(WORD_TABLE_FILTER, "");
          // 为抽取的每一个段落加上\n作为换行符标识
          cellBuilder.append(textClearBeginBlank).append("\t");
        }
      }
      String rowText = cellBuilder.toString();
      //去除最后一个\t
      rowText = StringUtils.substringBeforeLast(rowText, "\t");
      if (StringUtils.isNotBlank(rowText)) {
        //（避免出现上下两行文字一样）
        if (!StringUtils.equals(upLine, rowText)) {
          stringList.add(rowText);
        }
        upLine = rowText;
      }
    }
  }

  /**
   * 获取docx表格中文字(用于提取标题或目录使用）
   *
   * @param table 表格对象
   */
  private static void gettableText(XWPFTable table, String bt, int index, StringBuilder newBt) {
    List<XWPFTableRow> rows = table.getRows();
    for (XWPFTableRow row : rows) {
      StringBuilder cellBuilder = new StringBuilder();
      // 读取每一列数据
      List<XWPFTableCell> cells = row.getTableCells();
      for (XWPFTableCell cell : cells) {
        // 输出当前的单元格的数据
        List<XWPFTable> tables = cell.getTables();
        //如果存在合并单元格，则递归解析，否则正常读取
        if (tables.size() > 0) {
          for (XWPFTable xwpfTable : tables) {
            gettableText(xwpfTable, bt, index, newBt);
          }
        } else {
          String cellValue = cell.getText();
          cellValue = StringUtils.trimToEmpty(cellValue);
          if (StringUtils.isBlank(cellValue)) {
            continue;
          }
          // 将word中的特有字符转化为普通的换行符、空格符等
          String textWithSameBlankAndBreak =
              cellValue
                  .replaceAll(WORD_BLANK, " ")
                  .replaceAll(WORD_LINE_BREAK, "\n")
                  .replaceAll("\n+", "\n");
          // 去除word特有的不可见字符
          String textClearBeginBlank =
              textWithSameBlankAndBreak.replaceAll(WORD_TABLE_FILTER, "");
          // 为抽取的每一个段落加上\n作为换行符标识
          cellBuilder.append(textClearBeginBlank).append("\t");
        }
      }
      String rowText = cellBuilder.toString();
      //去除最后一个\t
      String clearBeginBlank = StringUtils.substringBeforeLast(rowText, "\t");

      if (clearBeginBlank.contains(bt) || index > 0) {
        //1 左对齐  2 居中  3 右对齐
        int value = 0;
        // table.getTableAlignment().getValue(); todo poi版本问题
        if (value == 2) {
          newBt.append(clearBeginBlank);
        }
        index += 1;
        if (index >= 3) {
          return;
        }
      }
    }
  }

  /**
   * 去除标题中软回车 软回车为/u000b
   *
   * @param bt 标题文字
   */
  @Deprecated
  public static String clearBeginBlank(String bt) {
    if (StringUtils.isBlank(bt)) {
      return bt;
    }
    bt = bt.replaceAll(WORD_BLANK, "")
        .replaceAll(WORD_LINE_BREAK, "")
        .replaceAll("\n+", "");
    return bt;
  }

  /**
   * 获取word标题，通过指定标题行开始往下取三行，获取符合条件的段落做标题（避免手动换行导致标题中断） （不区分doc和docx)
   *
   * @param bwFile bw文件
   * @param bt 用户指定的标题起始行标题
   * @return word标题
   */
  public static String getWordBt(File bwFile, String bt) {
    String name = bwFile.getName();
    String newBt = null;
    if (name.endsWith("doc")) {
      newBt = getDocBt(bwFile, bt);
    } else if (name.endsWith("docx")) {
      newBt = getDocxBt(bwFile, bt);
    }
    if (StringUtils.isBlank(newBt)) {
      newBt = bt;
    }
    return newBt;
  }

  /**
   * 获取doc标题
   *
   * @param bwFile bw文件
   * @param bt 用户指定的标题起始行标题
   * @return word标题
   */
  public static String getDocBt(File bwFile, String bt) {
    StringBuilder newBt = new StringBuilder();
    try (FileInputStream fileInputStream = new FileInputStream(bwFile);
        HWPFDocument doc = new HWPFDocument(fileInputStream)) {
      Range range = doc.getRange();

      //取标题行开始，不超过三行
      int index = 0;
      for (int i = 0; i < range.numParagraphs(); i++) {
        Paragraph paragraph = range.getParagraph(i);
        // 拿出段落中不包括表格的文字
        if (!paragraph.isInTable()) {
          String text = paragraph.text();
          text = StringUtils.trimToEmpty(text);
          if (StringUtils.isBlank(text)) {
            continue;
          }
          String textWithSameBlankAndBreak =
              text.replaceAll(WORD_BLANK, " ").replaceAll(WORD_LINE_BREAK, "\n")
                  .replaceAll("\n+", "\n");
          String clearBeginBlank = textWithSameBlankAndBreak.replaceAll(regexClearBeginBlank, "");
          if (clearBeginBlank.contains(bt) || index > 0) {
            //0左对齐，1居中，2右对齐，3左和右
            int justification = paragraph.getJustification();
            if (justification == 1) {
              newBt.append(clearBeginBlank);
            }
            index += 1;
            if (index >= 3) {
              return newBt.toString();
            }
          }
        } else {
          //获取表格中文字
          Table table;
          try {
            table = range.getTable(paragraph);
          } catch (IllegalArgumentException e) {
            log.warn("DOC文档 {} 中第 {} 个范围不为表格，无法读取，跳过！", bwFile.getPath(), i);
            continue;
          }
          for (int j = 0; j < table.numRows(); j++) {
            StringBuilder cellBuilder = new StringBuilder();
            TableRow row = table.getRow(j);
            for (int k = 0; k < row.numCells(); k++) {
              //读取完该范围更新i的下标
              i++;
              TableCell cell = row.getCell(k);
              String cellValue = cell.text();
              cellValue = StringUtils.trimToEmpty(cellValue);
              if (StringUtils.isBlank(cellValue)) {
                continue;
              }
              // 将word中的特有字符转化为普通的换行符、空格符等
              String textWithSameBlankAndBreak =
                  cellValue
                      .replaceAll(WORD_BLANK, " ")
                      .replaceAll(WORD_LINE_BREAK, "\n")
                      .replaceAll("\n+", "\n");
              // 去除word特有的不可见字符
              String textClearBeginBlank =
                  textWithSameBlankAndBreak.replaceAll(WORD_TABLE_FILTER, "");
              // 为抽取的每一个单元格加上\t作为单元格标识
              cellBuilder.append(textClearBeginBlank).append("\t");
            }

            String rowText = cellBuilder.toString();
            //去除该行最后一个\t
            String clearBeginBlank = StringUtils.substringBeforeLast(rowText, "\t");
            if (clearBeginBlank.contains(bt) || index > 0) {
              //0左对齐，1居中，2右对齐，3左和右
              int justification = paragraph.getJustification();
              if (justification == 1) {
                newBt.append(clearBeginBlank);
              }
              index += 1;
              if (index >= 3) {
                return newBt.toString();
              }
            }
          }
        }
      }
    } catch (IOException e) {
      log.error(e.getMessage(), e);
    }
    return newBt.toString();
  }

  /**
   * 获取docx标题
   *
   * @param bwFile bw文件
   * @param bt 用户指定的标题起始行标题
   * @return word标题
   */
  public static String getDocxBt(File bwFile, String bt) {
    StringBuilder newBt = new StringBuilder();
    try (FileInputStream fileInputStream = new FileInputStream(bwFile);
        XWPFDocument docx = new XWPFDocument(fileInputStream)) {
      // 获取该文档下所有元素
      Iterator<IBodyElement> iter = docx.getBodyElementsIterator();
      //取标题行开始，不超过三行
      int index = 0;
      while (iter.hasNext()) {
        IBodyElement element = iter.next();
        if (element instanceof XWPFParagraph) {
          // 获取段落元素
          XWPFParagraph paragraph = (XWPFParagraph) element;
          String text = paragraph.getText();
          text = StringUtils.trimToEmpty(text);
          if (StringUtils.isBlank(text)) {
            continue;
          }
          // 将word中的特有字符转化为普通的换行符、空格符等
          String textWithSameBlankAndBreak =
              text.replaceAll(WORD_BLANK, " ")
                  .replaceAll(WORD_LINE_BREAK, "\n")
                  .replaceAll("\n+", "\n");
          // 去除word特有的不可见字符
          String clearBeginBlank = textWithSameBlankAndBreak.replaceAll(regexClearBeginBlank, "");
          if (clearBeginBlank.contains(bt) || index > 0) {
            //1 左对齐  2 居中  3 右对齐
            int value = paragraph.getAlignment().getValue();
            if (value == 2) {
              newBt.append(clearBeginBlank);
            }
            index += 1;
            if (index >= 3) {
              return newBt.toString();
            }
          }
        } else if (element instanceof XWPFTable) {
          XWPFTable table = (XWPFTable) element;
          gettableText(table, bt, index, newBt);
        }
      }
    } catch (IOException e) {
      log.error(e.getMessage(), e);
    }
    return newBt.toString();
  }

  /**
   * 获取word指定前n行
   */
  public static String getWordTopLine(File file, int lineCount) {
    List<String> list = new ArrayList<>();
    if (file.getName().toLowerCase().endsWith("doc")) {
      list = previewDocText(file);
    } else if (file.getName().toLowerCase().endsWith("docx")) {
      list = previewDocxText(file);
    }
    List<String> stringList;
    if (list.size() > lineCount) {
      stringList = list.subList(0, lineCount);
    } else {
      stringList = list;
    }
    return StringUtils.join(stringList, "\n");
  }

  /**
   * 获取doc前N行
   */
  @Deprecated
  public static String getDocTopLine(File bwFile, int lineCount) {
    StringBuilder newBt = new StringBuilder();
    try (FileInputStream fileInputStream = new FileInputStream(bwFile);
        HWPFDocument doc = new HWPFDocument(fileInputStream)) {
      Range r = doc.getRange(); // 文档范围
      int index = 0;
      for (int i = 0; i < r.numParagraphs(); i++) {
        Paragraph paragraph = r.getParagraph(i); // 获取段落
        if (paragraph.isInTable()) {
          continue;
        }
        String text = StringUtils.trimToEmpty(paragraph.text());
        if (StringUtils.isBlank(text)) {
          continue;
        }
        String textWithSameBlankAndBreak =
            text.replaceAll(WORD_BLANK, " ").replaceAll(WORD_LINE_BREAK, "\n")
                .replaceAll("\n+", "\n");
        String clearBeginBlank = textWithSameBlankAndBreak.replaceAll(regexClearBeginBlank, "");
        newBt.append(clearBeginBlank).append("\n");
        index += 1;
        if (index >= lineCount) {
          return newBt.toString();
        }
      }
    } catch (IOException e) {
      log.error(e.getMessage(), e);
    }
    return newBt.toString();
  }

  @Deprecated
  public static String getDocxTopLine(File bwFile, int lineCount) {
    StringBuilder newBt = new StringBuilder();
    try (FileInputStream fileInputStream = new FileInputStream(bwFile);
        XWPFDocument docx = new XWPFDocument(fileInputStream)) {
      // 获取该文档下所有元素
      Iterator<IBodyElement> iter = docx.getBodyElementsIterator();
      //取标题下标，不超过三行
      int index = 0;
      while (iter.hasNext()) {
        IBodyElement element = iter.next();
        if (element instanceof XWPFParagraph) {
          // 获取段落元素
          XWPFParagraph paragraph = (XWPFParagraph) element;
          String text = paragraph.getText();
          text = StringUtils.trimToEmpty(text);
          if (StringUtils.isBlank(text)) {
            continue;
          }
          // 将word中的特有字符转化为普通的换行符、空格符等
          String textWithSameBlankAndBreak =
              text.replaceAll(WORD_BLANK, " ")
                  .replaceAll(WORD_LINE_BREAK, "\n")
                  .replaceAll("\n+", "\n");
          // 去除word特有的不可见字符
          String clearBeginBlank = textWithSameBlankAndBreak.replaceAll(regexClearBeginBlank, "");
          newBt.append(clearBeginBlank).append("\n");
          index += 1;
          if (index >= lineCount) {
            return newBt.toString();
          }
        }
      }
    } catch (IOException e) {
      log.error(e.getMessage(), e);
    }
    return newBt.toString();
  }


  /**
   * 解析word获取目录
   *
   * @param bwFile word文件
   * @param bt 用户指定的标题起始行标题
   */
  public static List<String> getWordMenu(File bwFile, String bt) {
    //TODO 需要添加表格内文字
    String filePath = bwFile.getPath();
    if (filePath.toLowerCase().endsWith("doc")) {
      return getDocMenu(bwFile, bt);
    } else if (filePath.toLowerCase().endsWith("docx")) {
      return getDocxMenu(bwFile, bt);
    }
    return new ArrayList<>();
  }

  /**
   * 解析doc获取目录
   *
   * @param bwFile word文件
   * @param bt 用户指定的标题起始行标题
   * @return 提取出来的目录集合
   */
  public static List<String> getDocMenu(File bwFile, String bt) {
    // 开始抽取doc中的文字
    List<String> menuList = new ArrayList<>();
    try (FileInputStream fileInputStream = new FileInputStream(bwFile);
        HWPFDocument doc = new HWPFDocument(fileInputStream)) {
      // 得到文档的读取范围
      Range range = doc.getRange();
      //取标题行开始，不超过10行
      int index = 0;
      //是否匹配用户指定正文标题
      boolean btContains = false;
      for (int i = 0; i < range.numParagraphs(); i++) {
        Paragraph paragraph = range.getParagraph(i);
        // 拿出段落中不包括表格的文字
        if (paragraph.isInTable()) {
          continue;
        }
        String text = paragraph.text();
        text = StringUtils.trimToEmpty(text);
        if (StringUtils.isBlank(text)) {
          continue;
        }
        String textWithSameBlankAndBreak =
            text.replaceAll(WORD_BLANK, " ").replaceAll(WORD_LINE_BREAK, "\n")
                .replaceAll("\n+", "\n");
        String clearBeginBlank = textWithSameBlankAndBreak.replaceAll(regexClearBeginBlank, "");
        if (StringUtils.isBlank(clearBeginBlank)) {
          continue;
        }

        if (clearBeginBlank.contains(bt)) {
          btContains = true;
          //匹配到标题后直接跳过，避免标题参与目录提取
          continue;
        }
        // 匹配到用户指定的标题才开始提取程序 TODO 如果一直没有匹配到，如何处理
        if (btContains) {
          //进入一行则下标加一
          index += 1;
          if (index >= 10) {
            return menuList;
          }
          //判断当前 paragraph 样式是否为目录样式
          //获取第一部分字符，如果不为 目录样式 则直接跳过
          String fontName0 = paragraph.getCharacterRun(0).getFontName();
          //如果字体中出现 目录字体 则跳过
          if (!fontName0.contains("指定字体")) {
            continue;
          }
          //如果有第二部分，并且不为 目录样式 则跳过
          if (paragraph.numCharacterRuns() > 1) {
            String fontName1 = paragraph.getCharacterRun(1).getFontName();
            //如果字体中出现 目录字体 则跳过
            if (!fontName1.contains("指定字体")) {
              continue;
            }
          }
          //0左对齐，1居中，2右对齐，3左和右
          int justification = paragraph.getJustification();
          //获取对齐方式，是否符合目录样式（对齐方式为不居中）
          if (justification != 1) {
            if (!menuList.contains(clearBeginBlank)) {
              menuList.add(clearBeginBlank);
            }
          }
        }
      }
    } catch (IOException e) {
      log.error("提取Doc格式报文目录时： 文件路径为：{} 解析失败，异常为：{}", bwFile.getPath(), e.getMessage(), e);
    }
    return menuList;
  }


  /**
   * 解析docx获取目录
   *
   * @param bwFile word文件
   * @param bt 用户指定的标题起始行标题
   * @return 提取出来的目录集合
   */
  public static List<String> getDocxMenu(File bwFile, String bt) {
    List<String> menuList = new ArrayList<>();
    // 获取该文档下所有元素
    try (FileInputStream fileInputStream = new FileInputStream(bwFile);
        XWPFDocument docx = new XWPFDocument(fileInputStream)) {
      Iterator<IBodyElement> iter = docx.getBodyElementsIterator();
      //取标题行开始，不超过10行
      int index = 0;
      //是否匹配用户指定正文标题
      boolean btContains = false;
      while (iter.hasNext()) {
        IBodyElement element = iter.next();
        if (element instanceof XWPFParagraph) {
          // 获取段落元素
          XWPFParagraph paragraph = (XWPFParagraph) element;
          String text = paragraph.getText();
          text = StringUtils.trimToEmpty(text);
          if (StringUtils.isBlank(text)) {
            continue;
          }
          // 将word中的特有字符转化为普通的换行符、空格符等
          String textWithSameBlankAndBreak =
              text.replaceAll(WORD_BLANK, " ")
                  .replaceAll(WORD_LINE_BREAK, "\n")
                  .replaceAll("\n+", "\n");
          // 去除word特有的不可见字符
          String textClearBeginBlank = textWithSameBlankAndBreak.replaceAll(regexClearBeginBlank,
              "");
          if (StringUtils.isBlank(textClearBeginBlank)) {
            continue;
          }

          if (textClearBeginBlank.contains(bt)) {
            btContains = true;
            //匹配到标题后直接跳过，避免标题参与目录提取
            continue;
          }
          // 匹配到用户指定的标题才开始提取程序 TODO 如果一直没有匹配到，如何处理
          if (btContains) {
            //进入一行则下标加一
            index += 1;
            if (index >= 10) {
              return menuList;
            }
            //判断当前 paragraph 样式是否为目录样式
            List<XWPFRun> runs = paragraph.getRuns();
            //获取第一部分字符，如果不为 目录样式 则直接跳过
            String fontFamily0 = runs.get(0).getFontFamily();
            //如果字体中出现 目录字体 则跳过
            if (!fontFamily0.contains("指定字体")) {
              continue;
            }
            //如果有第二部分，并且不为 目录样式 则跳过
            if (runs.size() > 1) {
              String fontFamily1 = runs.get(1).getFontFamily();
              //如果字体中出现 目录字体 则跳过
              if (!fontFamily1.contains("指定字体")) {
                continue;
              }
            }
            //1 左对齐  2 居中  3 右对齐
            int value = paragraph.getAlignment().getValue();
            //获取对齐方式，是否符合目录样式（对齐方式为不居中）
            if (value != 2) {
              if (!menuList.contains(textClearBeginBlank)) {
                menuList.add(textClearBeginBlank);
              }
            }
          }
        }
      }
    } catch (IOException e) {
      log.error("解析Docx格式报文目录时： 文件路径为：{} 解析失败，异常为：{}", bwFile.getPath(), e.getMessage(), e);
    }
    return menuList;
  }
}

