package org.demo.execl.easyexcel.dto;

import com.alibaba.excel.metadata.BaseRowModel;
import java.util.List;
import lombok.Data;

/**
 * 导出excel的参数DTO
 *
 * @author tzhang
 */
@Data
public class ExportExcelDto {

  /** 文件名称 */
  private String fileName;

  /** sheet名称 */
  private String sheetName;

  /** sheet页 */
  private Integer sheetNo;

  /** 表头（无模型映射） */
  private List<String> head;

  /** 数据（无模型映射） */
  private List<Object> datas;

  /**
   *
   *
   * <pre>
   * 数据（模型映射）
   * DTO需要继承BaseRowModel，
   * 属性添加注解 @ExcelProperty(value = "姓名", index =0)
   *             @ExcelProperty(value = "年龄",index = 1)
   *
   * </pre>
   */
  List<? extends BaseRowModel> modelDatas;
}
