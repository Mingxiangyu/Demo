package org.demo.execl.easyexcel.dto;

import lombok.Data;

/**
 * 解析excel的参数DTO
 *
 * @author tzhang
 */
@Data
public class AnalysisExcelDto {

  /** sheet编号（从0开始） */
  private Integer sheetNo;

  /** 数据起始行（从0开始） */
  private Integer headLineNo;

  /** 解析后转换的DTO类型 */
  private Class<?> dtoClass;
}
