package com.iglens.压缩包.zip;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
public class zip压缩 {

  private static final byte[] buf = new byte[1024];

  public static void main(String[] args) throws Exception {
    // 测试方法1
    toZip("E:\\省市区.zip", "E:\\省市区", false);
    // 测试方法2
    List<File> fileList = new ArrayList<>();
    fileList.add(new File("E:\\省市区\\dic_road_sh.xlsx"));
    fileList.add(new File("E:\\省市区\\dist_streets.csv"));
    toZip("E:\\省市区_PART.zip", fileList);
  }

  /**
   * 压缩成ZIP 方法1
   *
   * @param zipFileName 压缩文件夹路径
   * @param sourceFileName 要压缩的文件路径
   * @param KeepDirStructure 是否保留原来的目录结构,true:保留目录结构; false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
   * @throws RuntimeException 压缩失败会抛出运行时异常
   */
  public static Boolean toZip(String zipFileName, String sourceFileName, boolean KeepDirStructure) {
    Boolean result = true;
    long start = System.currentTimeMillis(); // 开始
    try (FileOutputStream fileOutputStream = new FileOutputStream(zipFileName);
        ZipOutputStream zos = new ZipOutputStream(fileOutputStream)) {
      File sourceFile = new File(sourceFileName);
      compress(sourceFile, zos, sourceFile.getName(), KeepDirStructure);
      long end = System.currentTimeMillis(); // 结束
      System.out.println("压缩完成，耗时：" + (end - start) + " 毫秒");
    } catch (Exception e) {
      result = false;
      e.printStackTrace();
    }
    return result;
  }

  /**
   * 压缩成ZIP 方法2 一次性压缩多个文件
   *
   * @param srcFiles 需要压缩的文件列表
   * @param zipFileName 压缩文件输出
   */
  public static void toZip(String zipFileName, List<File> srcFiles) {
    long start = System.currentTimeMillis();
    try (FileOutputStream fileOutputStream = new FileOutputStream(zipFileName);
        ZipOutputStream zos = new ZipOutputStream(fileOutputStream)) {
      for (File srcFile : srcFiles) {
        compress(srcFile, zos, srcFile.getName(), true);
      }
      long end = System.currentTimeMillis();
      System.out.println("压缩完成，耗时：" + (end - start) + " 毫秒");
    } catch (Exception e) {
      throw new RuntimeException("zip error from ZipUtils", e);
    }
  }

  /**
   * 递归压缩方法
   *
   * @param sourceFile 源文件
   * @param zos zip输出流
   * @param name 压缩后的名称
   * @param KeepDirStructure 是否保留原来的目录结构,true:保留目录结构; false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
   * @throws Exception
   */
  public static void compress(
      File sourceFile, ZipOutputStream zos, String name, boolean KeepDirStructure)
      throws Exception {

    if (sourceFile.isFile()) {
      // 向zip输出流中添加一个zip实体，构造器中name为zip实体的文件的名字
      zos.putNextEntry(new ZipEntry(name));
      // copy文件到zip输出流中
      int len;
      FileInputStream in = new FileInputStream(sourceFile);
      while ((len = in.read(buf)) != -1) {
        zos.write(buf, 0, len);
      }
      // Complete the entry
      zos.closeEntry();
      in.close();
    } else {
      File[] listFiles = sourceFile.listFiles();
      if (listFiles == null || listFiles.length == 0) {
        // 需要保留原来的文件结构时,需要对空文件夹进行处理
        if (KeepDirStructure) {
          // 空文件夹的处理
          zos.putNextEntry(new ZipEntry(name + "/"));
          // 没有文件，不需要文件的copy
          zos.closeEntry();
        }
      } else {
        for (File file : listFiles) {
          // 判断是否需要保留原来的文件结构
          if (KeepDirStructure) {
            // 注意：file.getName()前面需要带上父文件夹的名字加一斜杠,
            // 不然最后压缩包中就不能保留原来的文件结构,即：所有文件都跑到压缩包根目录下了
            compress(file, zos, name + "/" + file.getName(), KeepDirStructure);
          } else {
            compress(file, zos, file.getName(), KeepDirStructure);
          }
        }
      }
    }
  }
}
