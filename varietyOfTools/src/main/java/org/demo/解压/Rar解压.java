//package org.demo.解压;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
//import java.util.Enumeration;
//import org.apache.tools.zip.ZipEntry;
//import org.apache.tools.zip.ZipFile;
//import de.innosystec.unrar.Archive;
//import de.innosystec.unrar.rarfile.FileHeader;
///**
// * zip和rar解压缩工具类
// *
// * @author T480S
// */
//public class Rar解压 {
//  /**
//   * 解压rar
//   *
//   * @param sourceRarPath 需要解压的rar文件全路径
//   * @param destDirPath 需要解压到的文件目录
//   * @throws Exception
//   */
//  public static void unrar(String sourceRarPath, String destDirPath) throws Exception {
//    File sourceRar = new File(sourceRarPath);
//    File destDir = new File(destDirPath);
//    Archive archive = null;
//    FileOutputStream fos = null;
//    System.out.println("Starting 开始解压...");
//    try {
//      archive = new Archive(new FileInputStream(sourceRar));
//      FileHeader fh = archive.nextFileHeader();
//      int count = 0;
//      File destFileName = null;
//      while (fh != null) {
//        System.out.println((++count) + ") " + fh.getFileNameString());
//        String compressFileName = fh.getFileNameString().trim();
//        destFileName = new File(destDir.getAbsolutePath() + "/" + compressFileName);
//        if (fh.isDirectory()) {
//          if (!destFileName.exists()) {
//            destFileName.mkdirs();
//          }
//          fh = archive.nextFileHeader();
//          continue;
//        }
//        if (!destFileName.getParentFile().exists()) {
//          destFileName.getParentFile().mkdirs();
//        }
//
//        fos = new FileOutputStream(destFileName);
//        archive.extractFile(fh, fos);
//        fos.close();
//        fos = null;
//        fh = archive.nextFileHeader();
//      }
//
//      archive.close();
//      archive = null;
//      System.out.println("Finished 解压完成!");
//    } catch (Exception e) {
//      throw e;
//    } finally {
//      if (fos != null) {
//        try {
//          fos.close();
//          fos = null;
//        } catch (Exception e) {
//        }
//      }
//      if (archive != null) {
//        try {
//          archive.close();
//          archive = null;
//        } catch (Exception e) {
//        }
//      }
//    }
//  }
//
//  /**
//   * 解压Zip文件
//   *
//   * @param zipFileName 需要解压缩的文件位置
//   * @param descFileName 将文件解压到某个路径
//   * @throws IOException
//   */
//  public static void unZip(String zipFileName, String descFileName) throws IOException {
//    System.out.println("文件解压开始...");
//    String descFileNames = descFileName;
//    if (!descFileNames.endsWith(File.separator)) {
//      descFileNames = descFileNames + File.separator;
//    }
//    try {
//      ZipFile zipFile = new ZipFile(zipFileName);
//      ZipEntry entry = null;
//      String entryName = null;
//      String descFileDir = null;
//      byte[] buf = new byte[4096];
//      int readByte = 0;
//      @SuppressWarnings("rawtypes")
//      Enumeration enums = zipFile.getEntries();
//      while (enums.hasMoreElements()) {
//        entry = (ZipEntry) enums.nextElement();
//        entryName = entry.getName();
//        descFileDir = descFileNames + entryName;
//        if (entry.isDirectory()) {
//          new File(descFileDir).mkdir();
//          continue;
//        } else {
//          new File(descFileDir).getParentFile().mkdir();
//        }
//        File file = new File(descFileDir);
//        OutputStream os = new FileOutputStream(file);
//        InputStream is = zipFile.getInputStream(entry);
//        while ((readByte = is.read(buf)) != -1) {
//          os.write(buf, 0, readByte);
//        }
//        os.close();
//        is.close();
//      }
//      zipFile.close();
//      System.out.println("文件解压成功!");
//    } catch (Exception e) {
//      System.out.println("文件解压失败!");
//      e.printStackTrace();
//    }
//  }
//
//  public static void main(String[] args) throws Exception {
//    // ZipAndRarTools.unrar(newFile("D:\\存放资料的压缩包\\员工材料.rar"),newFile("D:\\存放资料的非压缩包\\"));
//
//    Rar解压.unZip("D:\\rarTest\\jar包和配置文件资源.zip", "D:\\rarTest");
//    Rar解压.unrar("D:\\rarTest\\rar压缩包.rar", "D:\\rarTest");
//  }
//}
