package com.iglens.统计代码;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * 统计项目下所有有效代码的行数
 *
 * @author wrcb_shaojl
 */
public class 统计Java项目代码行数 {

  private static int fileCount = 0;
  private static int fileLineCount = 0;
  private static int allLineCount = 0;

  public static void countFileRows(File path) throws IOException {
    // 如果是目录，递归获取
    if (path.isDirectory()) {
      File[] files = path.listFiles();
      for (File file : files) {
        countFileRows(file);
      }
    }
    // 如果是文件
    if (path.isFile()) {
      // 判断文件类型
      String filePathName = path.getName();
      if (filePathName.endsWith(".java")) {
        fileLineCount = 0;
        fileCount++;
        String line;
        BufferedReader bufferedReader = new BufferedReader(new FileReader(path));
        while ((line = bufferedReader.readLine()) != null) {
          // 排除空白行和单行注释，多行注释未找到合适的正则表达式
          if (line.matches("^[\\\\s&&[^\\\\n]]*\\\\n$") || line.trim().matches("\\/\\/(.*)")) {
            // if (line.matches("^\s*$") || line.trim().matches("\\/\\/(.*)")) {
            continue;
          }
          fileLineCount++;
          allLineCount++;
        }
        String[] filePaths = filePathName.split("\\\\");
        String fileName = filePaths[filePaths.length - 1];
        System.out.println("[FileName]: " + fileName + ", [LineCounts]:" + fileLineCount);
        bufferedReader.close();
      }
    }
  }

  public static void main(String[] args) throws IOException {
    // 选择一个文件路径
    File file = new File("F:\\mxy\\workspase\\Demo");
    countFileRows(file);
    System.out.println("[FileCount]:" + fileCount + ", [allLineCount]:" + allLineCount);
  }

}
