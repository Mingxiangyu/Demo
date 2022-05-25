package com.deepz.fileparse.parse;

import com.deepz.fileparse.domain.dto.FileDto;
import java.io.File;

/**
 * 文件解析策略
 *
 * @param <T>
 */
public interface Parser<T> {

  /**
   * 基于fileDto解析文件
   *
   * @param fileDto
   * @return
   */
  T parse(FileDto fileDto);

  /**
   * @author xming
   * @description 解析文件
   */
  default T parse(String path) {
    System.out.println("该工能暂未开发");
    return null;
  }

  default T parse(File file) {
    System.out.println("该工能暂未开发");
    return null;
  }
}
