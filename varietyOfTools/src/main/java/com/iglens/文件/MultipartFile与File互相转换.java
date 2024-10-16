package com.iglens.文件;

import cn.hutool.core.io.file.FileNameUtil;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.http.entity.ContentType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

public class MultipartFile与File互相转换 {
  static String filePath = "C:\\Users\\T480S\\Desktop\\新建文本文档.txt";

  public static void main(String[] args) throws Exception {
    //    File转MultipartFile1(filePath);
    //    File转MultipartFile2(filePath);

    MultipartFile转File();
  }

  private static void File转MultipartFile1(String filePath) throws IOException {
    File file = new File(filePath);
    FileInputStream fileInputStream = new FileInputStream(file);
    // MockMultipartFile(String name, @Nullable String originalFilename, @Nullable String
    // contentType, InputStream contentStream)
    // 其中originalFilename  旧名字,可为空
    // String contentType ，类型  可为空
    // ContentType.APPLICATION_OCTET_STREAM.toString() 需要使用HttpClient的包
    MultipartFile multipartFile =
        new MockMultipartFile(
            "copy" + file.getName(),
            file.getName(),
            ContentType.APPLICATION_OCTET_STREAM.toString(),
            fileInputStream);
    System.out.println(multipartFile.getName()); // 输出copytest.txt
  }

  /**
   * 返回MultipartFile文件
   *
   * @return org.springframework.web.multipart.MultipartFile
   */
  private static MultipartFile File转MultipartFile2(String filePath) throws IOException {
    File file = new File(filePath);
    // 需要导入commons-fileupload的包
    FileItem fileItem =
        new DiskFileItem(
            file.getName(),
            Files.probeContentType(file.toPath()),
            false,
            file.getName(),
            (int) file.length(),
            file.getParentFile());
    byte[] buffer = new byte[4096];
    int n;
    try (InputStream inputStream = new FileInputStream(file);
        OutputStream os = fileItem.getOutputStream()) {
      // 也可以用IOUtils.copy(inputStream,os);
      while ((n = inputStream.read(buffer, 0, 4096)) != -1) {
        os.write(buffer, 0, n);
      }
      MultipartFile multipartFile = new CommonsMultipartFile(fileItem);
      System.out.println(multipartFile.getName());
      return multipartFile;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  private static void MultipartFile转File() throws IOException {
    // 得到MultipartFile文件
    MultipartFile multipartFile = File转MultipartFile2(filePath);
    // 输出文件的新name 就是指上传后的文件名称
    System.out.println("getName:" + multipartFile.getName());
    // 输出源文件名称 就是指上传前的文件名称
    System.out.println("Oriname:" + multipartFile.getOriginalFilename());
    // 创建文件
    String originalFilename = multipartFile.getOriginalFilename();
    String suffix = FileNameUtil.getSuffix(originalFilename);
    System.out.println("suffix: " + suffix);
    String prefix = FileNameUtil.getPrefix(originalFilename);
    System.out.println("prefix: " + prefix);
    File tempFile = File.createTempFile(prefix, suffix);

    // 将数据从 multipartFile 复制到 tempFile
    multipartFile.transferTo(tempFile);
    // 也可以使用 copyInputStreamToFile 方法，效果同上
    //    FileUtils.copyInputStreamToFile(multipartFile.getInputStream(),tempFile);

    // 读取文件第一行
    BufferedReader bufferedReader = new BufferedReader(new FileReader(tempFile));
    System.out.println(bufferedReader.readLine());
    // 输出路径
    bufferedReader.close();

    // 输出file的URL
    System.out.println(tempFile.toURI().toURL().toString());
    // 输出文件的绝对路径
    System.out.println(tempFile.getAbsolutePath());
    // 操作完上的文件 需要删除在根目录下生成的文件
    File file = new File(tempFile.toURI());
    if (file.delete()) {
      System.out.println("删除成功");
    } else {
      System.out.println("删除失败");
    }
  }

  public File 转换为临时文件(MultipartFile multipartFile) {
    //选择用缓冲区来实现这个转换即使用java创建的临时文件使用 免费讲解 登录
    File file = null;
    try {
      String originalFilename = multipartFile.getOriginalFilename();
      String[] filename = originalFilename.split("\\.");
      file = File.createTempFile(filename[0], filename[1] + ".");
      multipartFile.transferTo(file);

      //文件处理

      // 程序退出后删除临时文件
      file.deleteOnExit();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return file;
  }
}
