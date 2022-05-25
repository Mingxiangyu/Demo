package com.deepz.fileparse.domain.dto;

import java.io.InputStream;
import lombok.Data;

/**
 * @author xming
 * @description
 */
@Data
public class FileDto {

  private InputStream inputStream;

  /** 文件后缀(不包含 .) */
  private String suffx;
}
