package org.demo.word.jar包方式解析;

import static java.util.regex.Pattern.compile;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

public class MriReportService {

  public static String[] findYearAndSource(File file) {
    String[] result = new String[2];
    // 日期
    String dateStr = file.getParentFile().getName();
    //        System.out.println(dateStr);
    if (compile("\\d").matcher(dateStr).find()) {
      dateStr = compile("-").matcher(dateStr).replaceAll("");
      result[0] = dateStr.substring(0, 8);
    } else {
      result[0] = "";
    }

    // 社区
    String fileName = file.getName();
    if (fileName.indexOf("-") < 0) {
      fileName = compile("\\.").matcher(fileName).replaceAll("-.");
    }
    fileName = compile("--+").matcher(fileName).replaceAll("-");
    result[1] = fileName.substring(fileName.indexOf("-") + 1, fileName.indexOf("."));

    return result;
  }

  public static LinkedList<File> findAllFile(String rootPath) {
    File file = new File(rootPath);
    LinkedList<File> list = new LinkedList<>();
    if (file.exists()) {
      File[] subDirs = file.listFiles();
      for (File tmpDir : subDirs) {
        //                System.out.println(tmpDir);
        for (File tmpFile : tmpDir.listFiles()) {
          if (tmpFile.isFile() && tmpFile.getName().indexOf("~$") < 0) {
            list.add(tmpFile);
          }
        }
      }
    }

    return list;
  }

  public static ArrayList<String> findSub(String docx) {
    String name = "";
    String gender = "";
    String age = "";
    String MRICheck = "";
    String MRIMem = "";

    if (!compile("性别：").matcher(docx).find() || !compile("年龄：").matcher(docx).find()) {
      try {
        name = docx.substring(docx.indexOf("姓名：") + 3, docx.indexOf("检查项目："));
        MRICheck = docx.substring(docx.indexOf("MRI检查描述：") + 8, docx.indexOf("MRI印象："));
        MRIMem = docx.substring(docx.indexOf("MRI印象：") + 6, docx.indexOf("报告医师："));
      } catch (StringIndexOutOfBoundsException e) {
        //                name = "";
      }
    } else {
      name = docx.substring(docx.indexOf("姓名：") + 3, docx.indexOf("性别："));
      gender = docx.substring(docx.indexOf("性别：") + 3, docx.indexOf("年龄："));
      age = docx.substring(docx.indexOf("年龄：") + 3, docx.indexOf("检查项目："));
      MRICheck = docx.substring(docx.indexOf("MRI检查描述：") + 8, docx.indexOf("MRI印象："));
      MRIMem = docx.substring(docx.indexOf("MRI印象：") + 6, docx.indexOf("报告医师："));
    }

    ArrayList<String> result = new ArrayList<>();
    result.add(name);
    result.add(gender);
    result.add(age);
    result.add(MRICheck);
    result.add(MRIMem);
    return result;
  }
}
