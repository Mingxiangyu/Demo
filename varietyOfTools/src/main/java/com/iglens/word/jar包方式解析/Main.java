package com.iglens.word.jar包方式解析;

import static com.iglens.word.jar包方式解析.MriReportService.findAllFile;
import static com.iglens.word.jar包方式解析.MriReportService.findSub;
import static com.iglens.word.jar包方式解析.MriReportService.findYearAndSource;
import static java.util.regex.Pattern.compile;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

public class Main {

  public static void main(String[] args) throws Exception {

    if (args.length < 2) {
      System.out.println("请输入源文件和目标文件的完整路径！");
      System.out.println("举个例子：java -jar docx2csv.jar d:\\核磁报告 d:\\result.csv");
      System.exit(-1);
    }

    String srcPath = args[0];
    String outPath = args[1];
    ArrayList<ArrayList<String>> result = new ArrayList<>();
    for (File tmpFile : findAllFile(srcPath)) {

      String[] yearAndSrc = findYearAndSource(tmpFile);

      FileInputStream fis = new FileInputStream(tmpFile);
      XWPFDocument xdoc = new XWPFDocument(fis);
      XWPFWordExtractor extractor = new XWPFWordExtractor(xdoc);
      String docx = extractor.getText();

      docx = compile("\\s").matcher(docx).replaceAll("");
      ArrayList<String> tmpRe = findSub(docx);
      tmpRe.add(yearAndSrc[0]);
      tmpRe.add(yearAndSrc[1]);
      result.add(tmpRe);
      fis.close();
    }
    write(result, outPath);
  }

  public static void write(ArrayList<ArrayList<String>> result, String outPath) throws IOException {
    BufferedWriter bufferedWriter =
        new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outPath), "GBK"));
    for (ArrayList<String> tmpStrs : result) {
      //            System.out.println();
      bufferedWriter.write(
          tmpStrs.get(0)
              + ","
              + tmpStrs.get(1)
              + ","
              + tmpStrs.get(2)
              + ","
              + tmpStrs.get(3)
              + ","
              + tmpStrs.get(4)
              + ","
              + tmpStrs.get(5)
              + ","
              + tmpStrs.get(6));
      bufferedWriter.newLine();
    }
    bufferedWriter.close();
  }
}
