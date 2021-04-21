package org.demo.文件;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class 获取系统用户临时目录 {
  private static final Logger log = LoggerFactory.getLogger(获取系统用户临时目录.class);

  private static final String JAR = ".jar";
  private static final String TARGET_CLASSES = "target/classes/";

  /** 获取系统用户临时目录 */
  public static String getUserTmpPath() {
    // 当前进程所在路径
    String proClassPath = System.getProperty("user.dir");
    if (StringUtils.isBlank(proClassPath)) {
      return "";
    }
    log.debug("当前路径为：" + proClassPath);
    try {
      proClassPath = URLDecoder.decode(proClassPath, "utf-8");
      log.debug("当前路径转译后为：" + proClassPath);
    } catch (UnsupportedEncodingException e) {
      log.error(e.getMessage(), e);
    }
    if (proClassPath.endsWith(JAR)) {
      proClassPath = proClassPath.substring(0, proClassPath.lastIndexOf("/") + 1);
      log.debug("当前目录路径以 jar 结尾,路径更新为：" + proClassPath);
    }
    if (proClassPath.endsWith(TARGET_CLASSES)) {
      proClassPath = proClassPath.replace("target/classes/", "");
      log.debug("当前目录路径以 classes 结尾,路径 replace 后为：" + proClassPath);
      proClassPath = proClassPath.substring(0, proClassPath.lastIndexOf("/"));
      log.debug("当前目录路径以 classes 结尾,路径更新为：" + proClassPath);
    }
    proClassPath = proClassPath + File.separator + "temp" + File.separator;
    log.debug("实体文件临时目录为：" + proClassPath);
    // 新建路径
    File path = new File(proClassPath);
    // 判断路径是否存在
    if (path.exists()) {
      if (path.isDirectory()) {
        log.info("dir exists");
      }
    } else {
      // 如果不存在创建文件路径
      boolean mkdirs = path.mkdirs();
      log.info("创建用户临时文件路径：" + mkdirs + " 路径为：" + proClassPath);
    }
    return proClassPath;
  }
}
