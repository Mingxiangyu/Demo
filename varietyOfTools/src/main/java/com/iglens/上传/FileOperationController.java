package com.iglens.上传;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

//import com.example.springBootdemo.utils.FileUtils;
/**
 * 单文件、多文件上传
 * @author luolei
 */
@RestController
public class FileOperationController {

  /**
   * 单文件上传
   * @param file
   * @return
   * @throws IllegalStateException
   * @throws IOException
   * String
   */
  @PostMapping("/upload.do")
  //@RequestMapping(value="/upload.do", method = RequestMethod.POST)
  //上传的文件会转换成MultipartFile对象，file名字对应html中上传控件的name
  public String upload(MultipartFile file) throws IllegalStateException, IOException{
    //取得当前上传文件的文件名称
    String originalFilename = file.getOriginalFilename();
    //transferTo是保存文件，参数就是要保存到的目录和名字
    String filePath = "C:\\Users\\Administrator\\Desktop\\images\\";
    file.transferTo(new File(filePath + originalFilename));
    System.out.println("文件类型："+file.getContentType());
    System.out.println("原文件名："+originalFilename);
    System.out.println("是否为空："+file.isEmpty());
    System.out.println("文件大小："+file.getSize());
    return "文件上传完毕";
  }
  /**
   * 多文件上传，大量文件时，防止文件名相同，重新修改存储文件名
   * @param files
   * @return
   * @throws IOException
   * String
   */
  @PostMapping(value = "/upload2.do", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  //@RequestMapping(value="/upload2.do", method = RequestMethod.POST)
  //上传的文件会转换成MultipartFile对象，file名字对应html中上传控件的name
  public String upload2(@RequestPart("file")MultipartFile[] files) throws IOException{
    if(files.length == 0){
      return "请选择要上传的文件";
    }
    for (MultipartFile multipartFile : files) {
      if(multipartFile.isEmpty()){
        return "文件上传失败";
      }
      byte[] fileBytes = multipartFile.getBytes();
      String filePath = "C:\\Users\\Administrator\\Desktop\\images\\";
      //取得当前上传文件的文件名称
      String originalFilename = multipartFile.getOriginalFilename();
      //生成文件名
      String fileName = UUID.randomUUID() +"&"+ originalFilename;
      FileUtils.uploadFile(fileBytes, filePath, fileName);
    }

    return "文件上传完毕";
  }

}