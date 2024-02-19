package com.iglens.压缩包.zip;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.z.ZCompressorInputStream;
import org.apache.commons.compress.utils.IOUtils;
/*
 *
 * <!-- https://mvnrepository.com/artifact/org.apache.commons/commons-compress -->
 * <dependency> <groupId>org.apache.commons</groupId> <artifactId>commons-compress</artifactId>
 * <version>1.16.1</version> </dependency>
 */

/**
 * https://www.cnblogs.com/zifeiy/p/9017982.html
 *
 * <p>Java解压tar.Z文件（使用Apache Commons-compress）
 *
 * @author xming
 */
public class Compress解压tarZ {
  public static void compressTest() throws IOException {
    // 将tar.Z解压为tar
    System.out.println("uncompress tar.Z to tar...");
    InputStream fin = Files.newInputStream(Paths.get("D:\\test.tar.Z"));
    BufferedInputStream in = new BufferedInputStream(fin);
    OutputStream out = Files.newOutputStream(Paths.get("D:\\test.tar"));
    ZCompressorInputStream zIn = new ZCompressorInputStream(in);
    byte[] buffer = new byte[1024];
    int n = 0;
    while (-1 != (n = zIn.read(buffer))) {
      out.write(buffer, 0, n);
    }
    out.close();
    zIn.close();
    System.out.println("uncompress tar.Z to tar finished!");
    // 将tar解压为文件夹
    System.out.println("uncompress tar to directory file...");
    List<String> fileNames = new ArrayList<String>();
    InputStream inputStream = new FileInputStream(new File("D:\\test.tar"));
    TarArchiveInputStream tarIn = new TarArchiveInputStream(inputStream, 1024);
    TarArchiveEntry entry = null;
    while ((entry = tarIn.getNextTarEntry()) != null) {
      System.out.println("... " + entry.getName() + " , " + entry.isDirectory());
      fileNames.add(entry.getName());
      if (entry.isDirectory()) { // 是目录
        File tmpDir = new File("D:\\test\\" + entry.getName());
        if (tmpDir.exists() == false) {
          tmpDir.mkdirs();
        }
      } else { // 是文件
        File tmpFile = new File("D:\\test\\" + entry.getName());
        File tmpDir = tmpFile.getParentFile();
        System.out.println("parent: " + tmpDir.getAbsolutePath());
        if (tmpDir.exists() == false) {
          tmpDir.mkdirs();
        }
        OutputStream outputStream = new FileOutputStream(tmpFile);
        int length = 0;
        byte[] b = new byte[1024];
        while ((length = tarIn.read(b)) != -1) {
          outputStream.write(b, 0, length);
        }
      }
    }
    IOUtils.closeQuietly(tarIn);
    System.out.println("uncompress tar to directory file finished!");
    System.out.println("all finished!");
  }

  public static void main(String[] args) throws IOException {
    compressTest();
  }
}
