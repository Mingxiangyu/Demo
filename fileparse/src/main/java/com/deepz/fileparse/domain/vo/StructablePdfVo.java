package com.deepz.fileparse.domain.vo;

import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

/**
 * @author xming
 * @description
 */
@Data
@Slf4j
@EqualsAndHashCode(callSuper = true)
public class StructablePdfVo extends StructableFileVo {

  /** 标题对象 */
  private List<Head> heads;

  /** 正文内容 */
  private String content;

  @Data
  public static class Head {

    /** 页码 */
    private String page;
    /** 标题 */
    private String title;
  }
}
