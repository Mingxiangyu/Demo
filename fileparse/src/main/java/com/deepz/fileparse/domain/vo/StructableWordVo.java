package com.deepz.fileparse.domain.vo;

import com.deepz.fileparse.domain.enums.TitleEnum;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author xming
 * @description
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class StructableWordVo extends StructableFileVo {

  /** 标题对象 */
  private List<StructableWordVo.Head> heads;

  @Data
  public static class Head {
    /** 页码 */
    private String page;
    /** 标题 */
    private String title;

    /** 风格(标题风格，一般表示几级标题) */
    // 防止空指针 Json转换报错，所以给默认值
    private TitleEnum style = TitleEnum.None;
  }
}
