package com.iglens.文件;

import java.io.File;
import java.util.Objects;

public class 删除多层级文件夹内某个文件夹 {
  public static void main(String[] args) {

    // 构造多级文件夹的根目录
    File rootFolder = new File("/path/to/root");

    // 调用递归删除方法,传入根目录和要删除的文件夹名
    deleteFolderByName(rootFolder, "22");
  }

  public static void deleteFolderByName(File folder, String folderName) {
    if (!folder.exists()) {
      throw new RuntimeException("目录: " + folder + " 不存在！");
    }
    if (folder.getName().equals(folderName)) {
      // 递归删除文件夹里的所有文件
      deleteFilesInFolder(folder);
      // 删除空文件夹
      folder.delete();
      return;
    }
    // 如果当前文件夹为空,结束递归
    if (Objects.requireNonNull(folder.list()).length == 0) {
      return;
    }
    // 遍历文件夹下所有文件/文件夹
    File[] files = folder.listFiles();
    for (File file : files) {

      // 递归调用
      deleteFolderByName(file, folderName);
    }
  }

  /* 递归删除文件夹内所有文件 */
  public static void deleteFilesInFolder(File folder) {
    File[] files = folder.listFiles();

    for (File file : files) {
      if (file.isFile()) {
        file.delete();
      } else {
        deleteFilesInFolder(file);
      }
    }
  }
}
