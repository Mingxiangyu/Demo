package com.iglens.文件;

import java.io.File;
import java.io.IOException;

/**
 * 安全测试时提出问题
 *
 * @author T480S
 */
public class 避免目录遍历异常 {
  public static void main(String[] args) {
    failIfDirectoryTraversal("C:\\Users\\T480S\\Desktop\\测试全库导成文件\\..\\5-10开发计划.pdf");
  }

  /** @param relativePath 相对路径 */
  public static void failIfDirectoryTraversal(String relativePath) {
    File file = new File(relativePath);

    // 如果路径为绝对路径，提示异常
    if (file.isAbsolute()) {
      throw new RuntimeException("Directory traversal attempt - absolute path not allowed");
    }

    String pathUsingCanonical;
    String pathUsingAbsolute;
    try {
      pathUsingCanonical = file.getCanonicalPath();
      pathUsingAbsolute = file.getAbsolutePath();
    } catch (IOException e) {
      throw new RuntimeException("Directory traversal attempt?", e);
    }

    // Require the absolute path and canonicalized path match.
    // This is done to avoid directory traversal
    // attacks, e.g. "1/../2/"
    // 如果绝对路径和规范路径不一致，则认为尝试目录遍历
    if (!pathUsingCanonical.equals(pathUsingAbsolute)) {
      throw new RuntimeException("Directory traversal attempt?");
    }
  }
}
