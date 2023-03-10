package com.iglens.csv;

import cn.hutool.core.collection.CollectionUtil;
import com.google.common.collect.Lists;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class 生成csv {
  public static void main(String[] args) {
    List<List<String>> csvList = new ArrayList<>();
    for (int i = 0; i < 1000; i++) {
      ArrayList<String> lineList = new ArrayList<>();
      lineList.add("1");
      lineList.add("12");
      lineList.add("123");
      lineList.add("1234");
      lineList.add("12345");
      lineList.add("123456");
      csvList.add(lineList);
    }
    List<List<List<String>>> partition = Lists.partition(csvList, 333);

    writeCsv(csvList, "./telNum.csv");
  }

  /**
   * 把数据导出到csv
   *
   * @param csvList csv包含所有行的集合
   * @param finalPath
   */
  static void writeCsv(List<List<String>> csvList, String finalPath) {
    File finalCSVFile = new File(finalPath);
    if (!finalCSVFile.exists()) {
      File parentFile = finalCSVFile.getParentFile();
      if (!parentFile.exists()) {
        // 如果不存在则先创建父目录再创建文件
        boolean mkdir = parentFile.mkdir();
        try {
          boolean newFile = finalCSVFile.createNewFile();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    try (FileOutputStream out = new FileOutputStream(finalCSVFile);
        OutputStreamWriter osw = new OutputStreamWriter(out, StandardCharsets.UTF_8);
        // 手动加上BOM标识
        // osw.write(new String(new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF }));
        BufferedWriter bw = new BufferedWriter(osw)) {

      if (CollectionUtil.isEmpty(csvList)) {
        return;
      }
      // for循环遍历
      for (List<String> lineList : csvList) {
        for (Object o : lineList) {
          String item = String.valueOf(o);
          // 字符流写单行
          bw.append(item).append(",");
        }
        // 进行换行
        bw.append("\r");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    System.out.println(finalPath + "数据导出成功");
  }
}
