package com.iglens.文件;

import cn.hutool.core.io.FileUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class 获取多层级文件夹下所有文件 {

  public static void main(String[] args) {
    File file = new File("E:\\WorkSpace\\gitWorkSpace\\Demo");

    List<File> files = readFileNum(file, new ArrayList<>());
    System.out.println(files);
    //
  }


  /**
   * 读取该文件夹下所有文件
   * @param file
   * @param list
   * @return
   */
  public static List<File> readFileNum(File file, List<File> list){
    Arrays.stream(Objects.requireNonNull(file.listFiles())).forEach(f ->{
      if (f.isFile()){
        if ("txt".equals(FileUtil.getSuffix(f))) {
          list.add(f);
        }
      }else {
        readFileNum(f,list);
      }
    });
    return list;
  }
}
