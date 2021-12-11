package com.iglens.加解密;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import sun.misc.BASE64Decoder;

/** 编码工具类 实现aes加密、解密 */
public class 对称加密 {

  /*
    TODO 如果解密是报错  Given final block not properly padded. <br>
     Such issues can arise if a bad key is used during decryption.
  确认加密和解密模式是否一致，iv设置是否一致
       */

  /** 密钥 */
  private static final String KEY = "O17CcQAcxbPGVnyo";

  /** 算法 <br>
   * java 不支持PKCS7Padding，只支持PKCS5Padding(如果传过来的加密算法是PKCS7则无法解密）  */
  private static final String ALGORITHMSTR = "AES/ECB/PKCS5Padding";

  public static void main(String[] args) throws Exception {
    String content = "我爱你";
    System.out.println("加密前：" + content);

    System.out.println("加密密钥和解密密钥：" + KEY);

    String encrypt = aesEncrypt(content, KEY);
    System.out.println("加密后：" + encrypt);

    String decrypt = aesDecrypt(encrypt, KEY);
    System.out.println("解密后：" + decrypt);
  }

  /**
   * AES加密为base 64 code
   *
   * @param content 待加密的内容
   * @param encryptKey 加密密钥
   * @return 加密后的base 64 code
   * @throws Exception
   */
  public static String aesEncrypt(String content, String encryptKey) throws Exception {
    return base64Encode(aesEncryptToBytes(content, encryptKey));
  }

  /**
   * AES加密
   *
   * @param content 待加密的内容
   * @param encryptKey 加密密钥
   * @return 加密后的byte[]
   * @throws Exception
   */
  public static byte[] aesEncryptToBytes(String content, String encryptKey) throws Exception {
    //    KeyGenerator kgen = KeyGenerator.getInstance("AES");
    //    kgen.init(128);
    Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
    SecretKeySpec skeySpec = new SecretKeySpec(encryptKey.getBytes(), "AES");
    if (ALGORITHMSTR.contains("ECB")) {
      cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
    } else {
      IvParameterSpec iv =
          new IvParameterSpec(
              encryptKey.getBytes(StandardCharsets.UTF_8)); // 使用CBC模式，需要一个向量iv，可增加加密算法的强度
      cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
    }
    return cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
  }

  /**
   * 将base 64 code AES解密
   *
   * @param encryptStr 待解密的base 64 code
   * @param decryptKey 解密密钥
   * @return 解密后的string
   * @throws Exception
   */
  public static String aesDecrypt(String encryptStr, String decryptKey) throws Exception {
    return StringUtils.isEmpty(encryptStr)
        ? null
        : aesDecryptByBytes(base64Decode(encryptStr), decryptKey);
  }

  /**
   * AES解密
   *
   * @param encryptBytes 待解密的byte[]
   * @param decryptKey 解密密钥
   * @return 解密后的String
   * @throws Exception
   */
  public static String aesDecryptByBytes(byte[] encryptBytes, String decryptKey) throws Exception {
    //    KeyGenerator kgen = KeyGenerator.getInstance("AES");
    //    kgen.init(128);

    Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
    SecretKeySpec skeySpec = new SecretKeySpec(decryptKey.getBytes(), "AES");
    if (ALGORITHMSTR.contains("ECB")) {
      cipher.init(Cipher.DECRYPT_MODE, skeySpec);
    } else {
      IvParameterSpec iv =
          new IvParameterSpec(
              decryptKey.getBytes(StandardCharsets.UTF_8)); // 使用CBC模式，需要一个向量iv，可增加加密算法的强度
      cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
    }
    byte[] decryptBytes = cipher.doFinal(encryptBytes);

    return new String(decryptBytes);
  }

  /**
   * 将byte[]转为各种进制的字符串
   *
   * @param bytes byte[]
   * @param radix 可以转换进制的范围，从Character.MIN_RADIX到Character.MAX_RADIX，超出范围后变为10进制
   * @return 转换后的字符串
   */
  public static String binary(byte[] bytes, int radix) {
    return new BigInteger(1, bytes).toString(radix); // 这里的1代表正数
  }

  /**
   * base 64 encode
   *
   * @param bytes 待编码的byte[]
   * @return 编码后的base 64 code
   */
  public static String base64Encode(byte[] bytes) {
    return Base64.encodeBase64String(bytes);
  }

  /**
   * base 64 decode
   *
   * @param base64Code 待解码的base 64 code
   * @return 解码后的byte[]
   * @throws Exception
   */
  public static byte[] base64Decode(String base64Code) throws Exception {
    return StringUtils.isEmpty(base64Code) ? null : new BASE64Decoder().decodeBuffer(base64Code);
  }

  /**
   * aes解密
   *
   * @param encrypt 内容
   * @return
   * @throws Exception
   */
  public static String aesDecrypt(String encrypt) throws Exception {
    return aesDecrypt(encrypt, KEY);
  }

  /**
   * aes加密
   *
   * @param content
   * @return
   * @throws Exception
   */
  public static String aesEncrypt(String content) throws Exception {
    return aesEncrypt(content, KEY);
  }

  /*JS:
  function Encrypt(word){
      var key = CryptoJS.enc.Utf8.parse("PBPBPPBPBP");

      var srcs = CryptoJS.enc.Utf8.parse(word);
      var encrypted = CryptoJS.AES.encrypt(srcs, key, {mode:CryptoJS.mode.ECB,padding: CryptoJS.pad.Pkcs7});
      return encrypted.toString();
  }
  function Decrypt(word){
      var key = CryptoJS.enc.Utf8.parse("love431love43112");

      var decrypt = CryptoJS.AES.decrypt(word, key, {mode:CryptoJS.mode.ECB,padding: CryptoJS.pad.Pkcs7});
      return CryptoJS.enc.Utf8.stringify(decrypt).toString();
  }

  function unicode(str){
      var value='';
      for (var i = 0; i < str.length; i++) {
          value += '\\u' + left_zero_4(parseInt(str.charCodeAt(i)).toString(16));
      }
      return value;
  }
  function left_zero_4(str) {
      if (str != null && str != '' && str != 'undefined') {
          if (str.length == 2) {
              return '00' + str;
          }
      }
      return str;
  }
  $("#password1").val(Encrypt($("#password").val()));
  */
}
