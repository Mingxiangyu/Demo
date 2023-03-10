package com.iglens.word;

import io.micrometer.core.instrument.util.StringUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;

@Deprecated//具体方法查看word-extractor项目
@Slf4j
public class 按段落提取word并替换不同字符 {

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
 private static final String WORD_TABLE_FILTER = "[\\t|\\n|\\r|\\s+| +]";

 /**
  * 抽取文字时去掉不必须字符正则
  */
 private static final String splitter = "[\\t|\\n|\\r|\\f|\\s+|\u00a0+]";

 private static final String regexClearBeginBlank = "^" + splitter + "*|" + splitter + "*$";

 public static void main(String[] args) {
   按段落提取word并替换不同字符 tp = new 按段落提取word并替换不同字符();
   String content = tp.readWord("C:\\Users\\T480S\\Desktop\\5-10开发计划.docx");
   System.out.println(content);
 }


 public String readWord(String path) {
   StringBuilder wordText = new StringBuilder();
   try {
     if (path.endsWith(".doc")) {
       readDoc(path, wordText);
     } else if (path.endsWith("docx")) {
       readDocx(path, wordText);
     } else {
       log.error("此文件不是word文件！");
     }
   } catch (Exception e) {
     log.error(e.getMessage(), e);
   }
   return wordText.toString();
 }

 private void readDocx(String path, StringBuilder wordText) throws IOException {
   XWPFDocument docx = new XWPFDocument(new FileInputStream(new File(path)));
   // 读取docx文字部分
   Iterator<IBodyElement> iter = docx.getBodyElementsIterator();
   // 抽取表中的文字集合
   List<String> originTableTextList = new ArrayList<>();
   int count = 0;
   while (iter.hasNext()) {
     IBodyElement element = iter.next();
     if (element instanceof XWPFParagraph) {
       // 获取段落元素
       XWPFParagraph paragraph = (XWPFParagraph) element;
       String text = paragraph.getText();
       if (StringUtils.isBlank(text)) {
         continue;
       }
       // 将word中的特有字符转化为普通的换行符、空格符等
       String textWithSameBlankAndBreak =
           text.replaceAll(WORD_BLANK, " ")
               .replaceAll(WORD_LINE_BREAK, "\n")
               .replaceAll("\n+", "\n");
       // 去除word特有的不可见字符
       String textClearBeginBlank = textWithSameBlankAndBreak
           .replaceAll(regexClearBeginBlank, "");
       // 为抽取的每一个段落加上\n作为换行符标识
       wordText.append(textClearBeginBlank).append("\n");
     } else if (element instanceof XWPFTable) {
       try {
         // 获取表格中的原始文字 默认文字中不加入表格文字 取消注释可加入
         String text = originTableTextList.get(count);
         wordText.append(text);
         count++;
       } catch (Exception e) {
         log.error("docx抽表数据与对应的表格位置不一致");
       }
     }
   }
 }

 private void readDoc(String path, StringBuilder wordText) throws IOException {
   HWPFDocument doc = new HWPFDocument(new FileInputStream(new File(path)));
   // 得到文档的读取范围
   Range range = doc.getRange();
   //  开始抽取doc中的文字
   for (int i = 0; i < range.numParagraphs(); i++) {
     Paragraph paragraph = range.getParagraph(i);
     // 拿出段落中不包括表格的文字
     if (!paragraph.isInTable()) {
       String text = paragraph.text();
       if (StringUtils.isBlank(text)) {
         continue;
       }
       String textWithSameBlankAndBreak =
           text.replaceAll(WORD_BLANK, " ").replaceAll(WORD_LINE_BREAK, "\n");
       String clearBeginBlank = textWithSameBlankAndBreak.replaceAll(regexClearBeginBlank, "");
       wordText.append(clearBeginBlank).append("\n");
     } else {
       try {
         // 寻找表格的开始位置和结束位置
         int index = i;
         int endIndex = index;
         // 拿出表格中文字
         StringBuilder tableOriginText = new StringBuilder(paragraph.text());
         for (; index < range.numParagraphs(); index++) {
           Paragraph tableParagraph = range.getParagraph(index);
           if (!tableParagraph.isInTable() || tableParagraph.getTableLevel() < 1) {
             endIndex = index;
             break;
           } else {
             tableOriginText.append(tableParagraph.text());
           }
         }
         i = endIndex - 1;
         // 过滤掉表格中所有不可见符号
         String tableOriginTextWithoutBlank =
             tableOriginText.toString().replaceAll(WORD_TABLE_FILTER, "");
//               默认不加入表格中字体
         wordText.append("<tb>").append(tableOriginTextWithoutBlank).append("</tb>")
             .append("\n");
       } catch (Exception e) {
         log.error("doc抽表数据与对应的表格位置不一致");
       }
     }
   }
 }


}
