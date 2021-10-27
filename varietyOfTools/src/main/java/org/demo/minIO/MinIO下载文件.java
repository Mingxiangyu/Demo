package org.demo.minIO;

import io.minio.MinioClient;
import io.minio.errors.MinioException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class MinIO下载文件 {

  public static void main(String[] args) {
    try {
      getObject(null, null);
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (InvalidKeyException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static InputStream getObject(String bucketName, String objectName)
      throws NoSuchAlgorithmException, InvalidKeyException, IOException {
    try {
      // 使用MinIO服务的URL，端口，Access key和Secret key创建一个MinioClient对象
      MinioClient minioClient =
          new MinioClient(
              "https://play.min.io",
              "Q3AM3UQ867SPQQA43P2F",
              "zuf+tfteSlswRu7BJ86wekitnifILbZam1KYY3TG");

      // 调用statObject()来判断对象是否存在。
      // 如果不存在, statObject()抛出异常,
      // 否则则代表对象存在。
      minioClient.statObject("10-26", "/中午14/ces.docx");

      // 获取指定offset和length的"myobject"的输入流。
      InputStream stream = minioClient.getObject("10-26", "/中午14/ces.docx");

      File file = new File("C:\\Users\\T480S\\Desktop\\ces1.docx");
      FileOutputStream os = new FileOutputStream(file);
      int bytesRead;
      int len = 8192;
      byte[] buffer = new byte[len];
      while ((bytesRead = stream.read(buffer, 0, len)) != -1) {
        os.write(buffer, 0, bytesRead);
      }

      // 关闭流。
      stream.close();
      return stream;
    } catch (MinioException e) {
      System.out.println("Error occurred: " + e);
    }
    return null;
  }
}
