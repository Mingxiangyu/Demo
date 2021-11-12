//package org.demo.加解密;
//
//import java.util.Scanner;
//import org.springframework.util.StringUtils;
//
//public class jasypt明文加密 {
//  public static void main(String[] args) {
//    System.out.println("瞰天Jasypt明文加密，加密后的值请放入\n配置文件中 ENC() 内");
//    System.out.println("请输入加密盐值，如为空直接回车");
//    Scanner saltScanner = new Scanner(System.in);
//    String salt = saltScanner.nextLine();
//    if (salt == null || "".equals(salt)) {
//      salt = "algorithm";
//    }
//    System.out.println("* 请输入需要加密的字符");
//    Scanner contentScanner = new Scanner(System.in);
//    String content = contentScanner.nextLine();
//    if (StringUtils.isEmpty(content)) {
////      System.out.println("加密字符不能为空！");
////      return;
//      throw new RuntimeException("加密字符不能为空！");
//    }
//    System.out.println("输入的值为：" + content);
//
//    BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
////     加密所需的salt(盐)
//    textEncryptor.setPassword(salt);
//    String contentEncrypt = textEncryptor.encrypt(content);
//    System.out.println("加密后的值为：\n" + contentEncrypt);
//  }
//}
