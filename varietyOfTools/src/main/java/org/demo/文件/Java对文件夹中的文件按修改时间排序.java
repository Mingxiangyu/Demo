package org.demo.文件;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

public class Java对文件夹中的文件按修改时间排序 {

  public static void main(String[] args) {
    File file = new File("E:\\test");
    File[] fs = file.listFiles();
    Arrays.sort(fs, new CompratorByLastModified());
    for (int i = 0; i < fs.length; i++) {
      System.out.println(fs[i].getName() + " " + new Date(fs[i].lastModified()).toLocaleString());
    }
  }

  static class CompratorByLastModified implements Comparator<File> {

    @Override
    public int compare(File f1, File f2) {
      long diff = f1.lastModified() - f2.lastModified();
      if (diff > 0) {
        return 1;//倒序正序控制
      } else if (diff == 0) {
        return 0;
      } else {
        return -1;//倒序正序控制
      }
    }

    @Override
    public boolean equals(Object obj) {
      return true;
    }
  }
}