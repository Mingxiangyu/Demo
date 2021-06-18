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

  public static void main(String[] args) {
    String tempDir = getTempDir();
    System.out.println(tempDir);

    String userTmpPath = getUserDirPath();
    System.out.println(userTmpPath);
  }

  /**
   * System.getProperty(“”),可以操作一下参数：
   *
   * <p>java.version Java运行时环境版本
   *
   * <p>java.vendor Java运行时环境供应商
   *
   * <p>java.vendor.url Java供应商的 URL
   *
   * <p>java.home Java安装目录
   *
   * <p>java.vm.specification.version Java虚拟机规范版本
   *
   * <p>java.vm.specification.vendor Java虚拟机规范供应商
   *
   * <p>java.vm.specification.name Java虚拟机规范名称
   *
   * <p>java.vm.version Java虚拟机实现版本
   *
   * <p>java.vm.vendor Java虚拟机实现供应商
   *
   * <p>java.vm.name Java虚拟机实现名称
   *
   * <p>java.specification.version Java运行时环境规范版本
   *
   * <p>java.specification.vendor Java运行时环境规范供应商
   *
   * <p>java.specification.name Java运行时环境规范名称
   *
   * <p>java.class.version Java类格式版本号
   *
   * <p>java.class.path Java类路径
   *
   * <p>java.library.path 加载库时搜索的路径列表
   *
   * <p>java.io.tmpdir 默认的临时文件路径
   *
   * <p>java.compiler 要使用的 JIT 编译器的名称
   *
   * <p>java.ext.dirs 一个或多个扩展目录的路径
   *
   * <p>os.name 操作系统的名称
   *
   * <p>os.arch 操作系统的架构
   *
   * <p>os.version 操作系统的版本
   *
   * <p>file.separator 文件分隔符（在 UNIX 系统中是“/”）
   *
   * <p>path.separator 路径分隔符（在 UNIX 系统中是“:”）
   *
   * <p>line.separator 行分隔符（在 UNIX 系统中是“/n”）
   *
   * <p>user.name 用户的账户名称
   *
   * <p>user.home 用户的主目录
   *
   * <p>user.dir 用户的当前工作目录
   */

  /** 获取操作系统缓存的临时目录 */
  public static String getTempDir() {
    /**
     * System.getproperty(“java.io.tmpdir”)是获取操作系统缓存的临时目录，不同操作系统的缓存临时目录不一样，
     *
     * <p>在Windows的缓存目录为：C:\Users\登录用户~1\AppData\Local\Temp\
     *
     * <p>Linux：/tmp
     */
    return System.getProperty("java.io.tmpdir");
  }

  /** 获取系统用户工作目录 */
  public static String getUserDirPath() {
    /* 默认定位到的当前用户目录("user.dir")（即工程根目录）
    JVM就可以据"user.dir" + "你自己设置的目录" 得到完整的路径（即绝对路径）
    这有个前提，你的工程不是web项目，不然，这个返回值就不是项目的根目录啦，是tomcat的bin目录。*/

    // 当前进程所在路径(工作目录)
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
