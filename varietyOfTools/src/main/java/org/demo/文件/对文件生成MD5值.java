package org.demo.文件;

import static org.apache.commons.codec.digest.MessageDigestAlgorithms.SHA_512;

import java.io.File;
import java.io.FileInputStream;
import org.apache.commons.codec.digest.DigestUtils;

/** @author T480S */
public class 对文件生成MD5值 {

  public static void main(String[] args) throws Exception {
    String path = "E:\\test\\6\\18\\KTtofloor8整理数剧以及图片.zip";
    File file = new File(path);
    FileInputStream fileInputStream = new FileInputStream(file);
    String 文件流方式hex = 文件流方式(fileInputStream);
    System.out.println(文件流方式hex);

    String 文件方式hex = 文件方式(path);
    System.out.println(文件方式hex);
  }

  public static String 文件流方式(FileInputStream fileInputStream) throws Exception {
    String hex = DigestUtils.sha512Hex(fileInputStream);
    return hex;
  }

  public static String 文件方式(String path) throws Exception {
    File file =
        new File(
            path); // 5401ba7a0096dcd9c36a7300577fa1f2abea45b8067079f1f2a69c52cac16023d01d04ef30f6989180fdd23e0266821c5c52301b0757111605bd6d3c50fe5448
    String hex = new DigestUtils(SHA_512).digestAsHex(file);

    return hex;
  }
}
