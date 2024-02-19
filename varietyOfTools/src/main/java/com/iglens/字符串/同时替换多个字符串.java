package com.iglens.字符串;

/**
 * @author xming
 * https://blog.csdn.net/qq_39390545/article/details/106020221
 */
public class 同时替换多个字符串 {
  public static void main(String[] args) {
    // 同时替换多个文字
    String str1 = "广东省，福建省，北京市，海淀区，河北省，上海市";
    str1 = str1.replaceAll("(?:省|市|区)", "");
    System.out.println("替换多个中文：" + str1);

    // 同时替换多个字符
    //   注意了，符号替换与文字不同，需要用 “\\” 双斜杠转义。
    String str2 = "0*00*00//33?23?23/";
    str2 = str2.replaceAll("\\*|\\/|\\?", "");
    System.out.println("替换多个字符：" + str2);
  }
}
