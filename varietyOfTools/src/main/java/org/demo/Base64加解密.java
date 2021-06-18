package org.demo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

/** 常规Base64加密解密实用工具类 */
@Slf4j
public class Base64加解密 {

  public static void main(String[] args) throws FileNotFoundException {
    String text = "123456";
    String encode = encode(text); // MTIzNDU2
    System.out.println(encode);
    String decode = decode(encode); // 123456
    System.out.println(decode);

    String docFilePath = "C:\\Users\\T480S\\Desktop\\新建文本文档.txt";
    File file = new File(docFilePath);
    String fileEncode = encode(new FileInputStream(file));
    System.out.println(fileEncode);
  }

  /**
   * 得到Base64加密后的字符串
   *
   * @param text
   * @return
   */
  public static String encode(String text) {
    byte[] arr = Base64.encodeBase64(text.getBytes(), true);
    return new String(arr);
  }

  /**
   * 得到Base64解密后的字符串
   *
   * @param base64
   * @return
   */
  public static String decode(String base64) {
    byte[] arr = Base64.decodeBase64(base64.getBytes());
    return new String(arr);
  }

  @SneakyThrows
  public static String encode(InputStream in) {
    try {
      byte[] bytes = new byte[in.available()];
      // 将文件中的内容读入到数组中
      in.read(bytes);
      // 将字节流数组转换为字符串
      return Base64.encodeBase64String(bytes);
    } finally {
      in.close();
    }
  }
}
