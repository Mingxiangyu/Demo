package com.iglens.解压;

import com.github.junrar.Archive;
import com.github.junrar.rarfile.FileHeader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;

@Slf4j
public class ZipUtils {

  /**
   * 压缩文件列表到某个zip包
   *
   * @param zipFileName zip包文件名
   * @param paths 文件列表路径
   * @throws IOException
   */
  public static void zip(String zipFileName, String... paths) throws IOException {
    zip(new FileOutputStream(zipFileName), paths);
  }

  /**
   * 压缩文件列表到某个zip包
   *
   * @param stream 流
   * @param paths 文件列表路径
   * @throws IOException
   */
  public static void zip(OutputStream stream, String... paths) throws IOException {
    try (ZipOutputStream zos = new ZipOutputStream(stream)) {
      zos.setEncoding(System.getProperty("sun.jnu.encoding"));
      for (String path : paths) {
        if (StringUtils.equals(path, "") || !new File(path).exists()) {
          log.error("附件 {} 不存在", path);
          continue;
        }
        File file = new File(path);
        if (file.exists()) {
          if (file.isDirectory()) {
            zipDirectory(zos, file.getPath(), file.getName() + File.separator);
          } else {
            zip(zos, file.getPath(), "");
          }
        }
      }
    }
  }

  /**
   * 解析多文件夹
   *
   * @param zos zip流
   * @param dirName 目录名称
   * @param basePath
   * @throws IOException
   */
  public static void zipDirectory(ZipOutputStream zos, String dirName, String basePath)
      throws IOException {
    File dir = new File(dirName);
    if (dir.exists()) {
      File files[] = dir.listFiles();
      if (files.length > 0) {
        for (File file : files) {
          if (file.isDirectory()) {
            zipDirectory(zos, file.getPath(), file.getName() + File.separator);
          } else {
            zip(zos, file.getName(), basePath);
          }
        }
      } else {
        ZipEntry zipEntry = new ZipEntry(basePath);
        zos.putNextEntry(zipEntry);
      }
    }
  }

  public static void zip(ZipOutputStream zos, String fileName, String basePath) throws IOException {
    File file = new File(fileName);
    if (file.exists()) {
      try (FileInputStream fis = new FileInputStream(fileName)) {
        ZipEntry ze = new ZipEntry(basePath + file.getName());
        zos.putNextEntry(ze);
        IOUtils.copy(fis, zos);
      }
    }
  }

  /**
   * 解压Zip文件
   *
   * @param zipFile 需要解压缩的文件
   * @param outDir 将文件解压到某个路径
   * @throws IOException
   */
  public static void unZip(File zipFile, String outDir) throws IOException {

    File outFileDir = new File(outDir);
    if (!outFileDir.exists()) {
      boolean isMakDir = outFileDir.mkdirs();
      if (isMakDir) {
        System.out.println("创建压缩目录成功");
      }
    }

    ZipFile zip = new ZipFile(zipFile);
    for (Enumeration<ZipEntry> enumeration = zip.getEntries(); enumeration.hasMoreElements(); ) {
      ZipEntry entry = enumeration.nextElement();
      String zipEntryName = entry.getName();
      try (InputStream in = zip.getInputStream(entry)) {

        // 处理压缩文件包含文件夹的情况
        if (entry.isDirectory()) {
          File fileDir = new File(outDir + zipEntryName);
          fileDir.mkdir();
          continue;
        }

        File file = new File(outDir, zipEntryName);
        FileUtils.copyInputStreamToFile(in, file);
        in.close();
      }
    }
    zip.close();
  }

  /**
   * 解压rar
   *
   * @param rarFile 需要解压的rar文件
   * @param outDir 需要解压到的文件目录
   * @throws Exception
   */
  public static void unRar(File rarFile, String outDir) throws Exception {
    File outFileDir = new File(outDir);
    if (!outFileDir.exists()) {
      boolean isMakDir = outFileDir.mkdirs();
      if (isMakDir) {
        System.out.println("创建压缩目录成功");
      }
    }
    try (Archive archive = new Archive(new FileInputStream(rarFile))) {
      FileHeader fileHeader = archive.nextFileHeader();
      while (fileHeader != null) {
        if (fileHeader.isDirectory()) {
          fileHeader = archive.nextFileHeader();
          continue;
        }
        File out = new File(outDir + fileHeader.getFileNameString());
        if (!out.exists()) {
          if (!out.getParentFile().exists()) {
            out.getParentFile().mkdirs();
          }
          out.createNewFile();
        }
        try (FileOutputStream os = new FileOutputStream(out)) {
          archive.extractFile(fileHeader, os);
          os.close();
        }
        fileHeader = archive.nextFileHeader();
      }
    }
  }

  /** 解压缩tar * @param tarFile 需要解压的tar文件 * @param outDir 需要解压到的文件目录 */
  @SneakyThrows
  public static void unTar(File tarFile, String outDir) {
    try (TarArchiveInputStream fin = new TarArchiveInputStream(new FileInputStream(tarFile))) {
      File extraceFolder = new File(outDir);
      TarArchiveEntry entry;
      // 将 tar 文件解压到 extractPath 目录下
      while ((entry = fin.getNextTarEntry()) != null) {
        if (entry.isDirectory()) {
          continue;
        }
        File curfile = new File(extraceFolder, entry.getName());
        File parent = curfile.getParentFile();
        if (!parent.exists()) {
          parent.mkdirs();
        }
        // 将文件写出到解压的目录
        try (FileOutputStream fout = new FileOutputStream(curfile)) {
          IOUtils.copy(fin, fout);
        }
      }
    }
  }

  /** 解压缩tar.gz * @param tarGzFile 需要解压的tar.gz文件 * @param outDir 需要解压到的文件目录 */
  @SneakyThrows
  public static void unTarGz(File tarGzFile, String outDir) {
    try (GzipCompressorInputStream gzin =
            new GzipCompressorInputStream(new FileInputStream(tarGzFile));
        TarArchiveInputStream fin = new TarArchiveInputStream(gzin)) {
      File extraceFolder = new File(outDir);
      TarArchiveEntry entry;
      // 将 tar 文件解压到 extractPath 目录下
      while ((entry = fin.getNextTarEntry()) != null) {
        if (entry.isDirectory()) {
          continue;
        }
        File curfile = new File(extraceFolder, entry.getName());
        File parent = curfile.getParentFile();
        if (!parent.exists()) {
          parent.mkdirs();
        }
        // 将文件写出到解压的目录
        try (FileOutputStream fout = new FileOutputStream(curfile)) {
          IOUtils.copy(fin, fout);
        }
      }
    }
  }
}
