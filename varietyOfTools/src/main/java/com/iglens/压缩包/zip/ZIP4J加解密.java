package com.iglens.压缩包.zip;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import org.apache.commons.lang3.StringUtils;

/** @author T480S */
@Slf4j
public class ZIP4J加解密 {

  public static void main(String[] args) {
    String zipFilePath  = "C:\\Users\\T480S\\Desktop\\zip4j.zip";
    String fileFolder = "C:\\Users\\T480S\\Desktop\\zip4j";
    exportZipPackage(zipFilePath,"1234", fileFolder);
  }

  /**
   * 根据所给密码解压zip压缩包到指定目录
   *
   * <p>如果指定目录不存在,可以自动创建,不合法的路径将导致异常被抛出
   *
   * @param zipFile zip压缩包绝对路径
   * @param dest 指定解压文件夹位置
   * @param passwd 密码(可为空)
   * @return 解压后的文件数组
   * @throws ZipException
   */
  @SuppressWarnings("unchecked")
  public static File[] deCompress(File zipFile, String dest, String passwd) throws ZipException {
    // 1.判断指定目录是否存在
    File destDir = new File(dest);
    if (destDir.isDirectory() && !destDir.exists()) {
      destDir.mkdir();
    }
    // 2.初始化zip工具
    ZipFile zFile = new ZipFile(zipFile);
    zFile.setFileNameCharset("UTF-8");
    if (!zFile.isValidZipFile()) {
      throw new ZipException("压缩文件不合法,可能被损坏.");
    }
    // 3.判断是否已加密
    if (zFile.isEncrypted()) {
      zFile.setPassword(passwd.toCharArray());
    }
    // 4.解压所有文件
    zFile.extractAll(dest);
    List<FileHeader> headerList = zFile.getFileHeaders();
    List<File> extractedFileList = new ArrayList<File>();
    for (FileHeader fileHeader : headerList) {
      if (!fileHeader.isDirectory()) {
        extractedFileList.add(new File(destDir, fileHeader.getFileName()));
      }
    }
    File[] extractedFiles = new File[extractedFileList.size()];
    extractedFileList.toArray(extractedFiles);
    return extractedFiles;
  }

  public static void exportZipPackage(String zipFilePath, String passwd, String fileFolder) {
    try {
      InputStream is;
      // 内层压缩包
      ZipFile entryZip = new ZipFile(zipFilePath);
      // 设置编码
      entryZip.setFileNameCharset("GBK");
      ZipParameters entryPara = new ZipParameters();
      if (!StringUtils.isEmpty(passwd)) {
        entryPara.setEncryptFiles(true);
        // 设置AES加密方式
        entryPara.setEncryptionMethod(Zip4jConstants.ENC_METHOD_AES);
        // 必须设置长度
        entryPara.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_256);
        //        entryPara.setEncryptionMethod(Zip4jConstants.ENC_METHOD_STANDARD); // 加密方式
        entryPara.setPassword(passwd.toCharArray());
      }
      // 压缩方式
      entryPara.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
      // 设置压缩级别,默认为0（即不压缩）
      entryPara.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);

      entryZip.addFolder(fileFolder, entryPara);
      // 以流方式添加文件
      //      entryPara.setSourceExternalStream(true);
      // 往压缩文件里添加流文件
      //      for (String fileId : uploadFiles.keySet()) {
      //        is = uploadFiles.get(fileId);
      //        entryPara.setFileNameInZip(fileId);
      //        if (is == null) {
      //          log.info(">>>>>FileId={}", fileId);
      //          continue;
      //        }
      //        entryZip.addStream(is, entryPara);
      //        is.close();
      //      }

      /*
       * // 外层压缩包（因为压缩包对子级文件名不加密，所以有包了一层）
       *  String outerFilePath = tmpPath + File.separator + "Outer"
       * + zipName; ZipFile outerZip = new ZipFile(outerFilePath);
       * outerZip.setFileNameCharset("GBK"); ZipParameters outerPara = new ZipParameters(); // 设置密码：
       * outerPara.setEncryptFiles(true); // 设置AES加密方式
       * outerPara.setEncryptionMethod(Zip4jConstants.ENC_METHOD_AES); // 必须设置长度
       * outerPara.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_256);
       * outerPara.setPassword("casic12345"); // 将内层压缩包放进外层压缩包里，并加密 outerZip.addFile(new
       * File(zipFilePath), outerPara);
       */
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }
}
