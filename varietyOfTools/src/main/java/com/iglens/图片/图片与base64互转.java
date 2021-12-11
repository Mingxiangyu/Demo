package com.iglens.图片;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.codec.binary.Base64;
import sun.misc.BASE64Decoder;
/*
JDK1.8以上无法使用 sun.misc.BASE64Decoder;<pr>
需更换为以下导包
 */
// import java.util.Base64;
// import java.util.Base64.Encoder;
// import java.util.Base64.Decoder;

/**
 * 将图片转换为Base64流
 *
 * @author: 李德才
 * @date: 2020年4月18日 下午1:45:52
 */
public class 图片与base64互转 {


  public static void main(String[] args) {
    String url = "C:\\Users\\T480S\\Desktop\\微信截图_20210918110842.png"; // 待处理的图片
    String imgbese = getImgStr(url);
    System.out.println(imgbese);
    url = "C:\\Users\\T480S\\Desktop\\2.png"; // 新生成的图片
    generateImage(imgbese, url);
  }

  /**
   * 将图片转换成Base64编码
   *
   * @param imgFile 待处理图片
   * @return
   */
  public static String getImgStr(String imgFile) {
    // 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
    byte[] data = null;
    // 读取图片字节数组
    try (InputStream in = new FileInputStream(imgFile)) {
      data = new byte[in.available()];
      in.read(data);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return new String(Base64.encodeBase64(data));
  }

  /**
   * 对字节数组字符串进行Base64解码并生成图片
   *
   * @param imgStr 图片base64数据
   * @param imgFilePath 保存图片全路径地址
   * @return 是否成功
   */
  public static boolean generateImage(String imgStr, String imgFilePath) {
    if (imgStr == null) {
      return false;
    }
    try (OutputStream out = new FileOutputStream(imgFilePath)) {
      BASE64Decoder decoder = new BASE64Decoder();
      // Base64解码
      byte[] b = decoder.decodeBuffer(imgStr);
      /*
      jdk1.8以上需要使用以下方法
       */
      //      Decoder decoder=Base64.getMimeDecoder(); //注不要使用.getDecoder();
      //      byte[] buffer =decoder.decode(publicKeyStr);

      for (int i = 0; i < b.length; ++i) {
        if (b[i] < 0) { // 调整异常数据
          b[i] += 256;
        }
      }
      // 生成jpg图片
      out.write(b);
      out.flush();
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}
