package org.demo.监听本地文件夹变动;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;
import org.springframework.util.ResourceUtils;

/**
 * 文件定时器，检测文件是否变动
 *
 * @version V.1.0.1
 * @blob https://blog.csdn.net/appleyk
 */
@Component
@Slf4j
public class 通过MD5判断文件是否改变 {

  /** 监听文件路径 */
  private String licPath;

  /** 文件唯一身份标识 == 相当于人类的指纹一样 */
  private static String md5 = "";

  /** 5秒检测一次，不能太快也不能太慢，自己体会 */
  @Scheduled(cron = "0/5 * * * * ?")
  protected void timer() throws Exception {
    String readMd5 = getMd5(licPath);
    if (md5 == null || "".equals(md5)) {
      md5 = readMd5;
    }
    // 不相等，说明lic变化了
    if (!readMd5.equals(md5)) {
      // 文件改变后需要实现的业务逻辑
      /*
      dosomething。。。
       */
      md5 = readMd5;
    }
  }

  /** 获取文件的md5 */
  public static String getMd5(String filePath) throws Exception {
    File file;
    String md5 = "";
    try {
      file = ResourceUtils.getFile(filePath);
      if (file.exists()) {
        FileInputStream is = new FileInputStream(file);
        byte[] data = new byte[is.available()];
        is.read(data);
        md5 = DigestUtils.md5DigestAsHex(data);
        is.close();
      }
    } catch (FileNotFoundException e) {

    }
    return md5;
  }
}
