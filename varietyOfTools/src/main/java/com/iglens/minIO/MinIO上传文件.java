package com.iglens.minIO;

import io.minio.MinioClient;
import io.minio.Result;
import io.minio.errors.MinioException;
import io.minio.messages.Item;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import org.xmlpull.v1.XmlPullParserException;

public class MinIO上传文件 {

  public static void main(String[] args)
      throws NoSuchAlgorithmException, IOException, InvalidKeyException, XmlPullParserException {
    try {
      // 使用MinIO服务的URL，端口，Access key和Secret key创建一个MinioClient对象
      MinioClient minioClient =
          new MinioClient(
              "https://play.min.io",
              "Q3AM3UQ867SPQQA43P2F",
              "zuf+tfteSlswRu7BJ86wekitnifILbZam1KYY3TG");
      // 该配置可放到配置文件中
      /*
      # 设置单个文件大小
      spring.servlet.multipart.max-file-size= 50MB
      #minio文件服务器配置
      minio.address=http://localhost:9000
      minio.accessKey=admin
      minio.secretKey=12345678
      minio.bucketName=myfile
             */

      // 检查存储桶是否已经存在
      boolean isExist = minioClient.bucketExists("10-26");
      if (isExist) {
        System.out.println("Bucket already exists.");
      } else {
        // 创建一个名为asiatrip的存储桶，用于存储照片的zip文件。
        minioClient.makeBucket("10-26");
      }

      // 列出所有存储桶
      //      List<Bucket> bucketList = minioClient.listBuckets();
      //      for (Bucket bucket : bucketList) {
      //        System.out.println(bucket.creationDate() + ", " + bucket.name());
      //      }

      // 列出'my-bucketname'里的对象
      Iterable<Result<Item>> myObjects = minioClient.listObjects("10-26");
      for (Result<Item> result : myObjects) {
        Item item = result.get();
        System.out.println(item.lastModified() + ", " + item.size() + ", " + item.objectName());
      }

      // 使用putObject上传一个文件到存储桶中。
      minioClient.putObject("10-26", "/中午14/ces.docx", "C:\\Users\\T480S\\Desktop\\ces.docx", null);
      System.out.println(
          "/home/user/Photos/asiaphotos.zip is successfully uploaded as asiaphotos.zip to `asiatrip` bucket.");
    } catch (MinioException e) {
      System.out.println("Error occurred: " + e);
    }
  }
}
