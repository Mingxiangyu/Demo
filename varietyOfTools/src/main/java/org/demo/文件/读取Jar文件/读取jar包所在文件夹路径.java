package org.demo.文件.读取Jar文件;

import java.io.File;
import java.io.UnsupportedEncodingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class 读取jar包所在文件夹路径 {
  private static final Logger log = LoggerFactory.getLogger(读取jar包所在文件夹路径.class);

  public static void main(String[] args) {
    //TODO 如果以jar包形式启动则路径 F:\ktWorkSpace\com-kantian-track\target\file:\F:\ktWorkSpace\com-kantian-track\target\KT-TRACK-0.0.1-SNAPSHOT.jar!\BOOT-INF\classes
    String jarWholePath =
        读取jar包所在文件夹路径.class.getProtectionDomain().getCodeSource().getLocation().getFile();
    log.info("jarWholePaht: {}", jarWholePath);
    try {
      jarWholePath = java.net.URLDecoder.decode(jarWholePath, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      log.info(e.toString());
    }

    File parentFile = new File(jarWholePath).getParentFile();
    log.info("parentFile: {}", parentFile);
    String jarPath = parentFile.getAbsolutePath();
    log.info("jarPath: {}", jarPath);

  }
}
