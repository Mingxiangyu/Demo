package com.iglens.minIO;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import io.minio.MinioClient;
import io.minio.PutObjectOptions;
import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Data
@Component
public class MinIOService {
  // @Value("${minio.address}")
  private String address;

  // @Value("${minio.accessKey}")
  private String accessKey;

  // @Value("${minio.secretKey}")
  private String secretKey;

  // @Value("${minio.bucketName}")
  private String bucketName;

  public MinioClient getMinioClient() {
    try {
      address = "49.5.9.35:16051";
      accessKey = "LXidNNWyrA0Qamoj";
      secretKey = "fyqpEV0Y5rZw0IXWdnvzXdxLU2qvsgNi";
      return new MinioClient(address, accessKey, secretKey);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * 检查存储桶是否存在
   *
   * @param bucketName 存储桶名称
   * @return
   */
  public boolean bucketExists(MinioClient minioClient, String bucketName) {
    boolean flag = false;
    try {
      flag = minioClient.bucketExists(bucketName);
      if (flag) {
        return true;
      }
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return false;
  }

  /**
   * 创建存储桶
   *
   * @param bucketName 存储桶名称
   */
  public boolean makeBucket(MinioClient minioClient, String bucketName) {
    try {
      boolean flag = bucketExists(minioClient, bucketName);
      // 存储桶不存在则创建存储桶
      if (!flag) {
        minioClient.makeBucket(bucketName);
      }
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * 上传文件
   *
   * @param file 上传文件
   * @return 成功则返回文件名，失败返回空
   */
  public String uploadFile(MinioClient minioClient, MultipartFile file) {
    // 创建存储桶
    boolean createFlag = makeBucket(minioClient, bucketName);
    // 创建存储桶失败
    if (createFlag == false) {
      return "";
    }
    try {
      PutObjectOptions putObjectOptions =
          new PutObjectOptions(file.getSize(), PutObjectOptions.MIN_MULTIPART_SIZE);
      putObjectOptions.setContentType(file.getContentType());
      String originalFilename = file.getOriginalFilename();
      int pointIndex = originalFilename.lastIndexOf(".");
      // 得到文件流
      InputStream inputStream = file.getInputStream();
      // 保证文件不重名(并且没有特殊字符)
      String fileName =
          bucketName
              + DateUtil.format(new Date(), "_yyyyMMddHHmmss")
              + (pointIndex > -1 ? originalFilename.substring(pointIndex) : "");
      minioClient.putObject(bucketName, fileName, inputStream, putObjectOptions);
      return fileName;
    } catch (Exception e) {
      e.printStackTrace();
      return "";
    }
  }

  /**
   * 下载文件
   *
   * @param originalName 文件路径
   */
  public InputStream downloadFile(
      MinioClient minioClient, String originalName, HttpServletResponse response) {
    try {
      InputStream file = minioClient.getObject(bucketName, originalName);
      String filename = new String(originalName.getBytes("ISO8859-1"), StandardCharsets.UTF_8);
      if (StrUtil.isNotBlank(originalName)) {
        filename = originalName;
      }
      response.setHeader("Content-Disposition", "attachment;filename=" + filename);
      ServletOutputStream servletOutputStream = response.getOutputStream();
      int len;
      byte[] buffer = new byte[1024];
      while ((len = file.read(buffer)) > 0) {
        servletOutputStream.write(buffer, 0, len);
      }
      servletOutputStream.flush();
      file.close();
      servletOutputStream.close();
      return file;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * 删除文件
   *
   * @param fileName 文件路径
   * @return
   */
  public boolean deleteFile(MinioClient minioClient, String fileName) {
    try {
      minioClient.removeObject(bucketName, fileName);
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  /**
   * 得到指定文件的InputStream
   *
   * @param originalName 文件路径
   * @return
   */
  public InputStream getObject(MinioClient minioClient, String originalName) {
    try {
      return minioClient.getObject(bucketName, originalName);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * 根据文件路径得到预览文件绝对地址
   *
   * @param minioClient
   * @param fileName 文件路径
   * @return
   */
  public String getPreviewFileUrl(MinioClient minioClient, String fileName) {
    try {
      return minioClient.presignedGetObject(bucketName, fileName);
    } catch (Exception e) {
      e.printStackTrace();
      return "";
    }
  }
}
