package org.demo.easyexcel.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * 解析excel示例DTO
 *
 * @author tzhang
 */
@Data
public class ExcelExampleDto {

  @ExcelProperty(value = "姓名", index = 0)
  private String name;

  @ExcelProperty(value = "年龄", index = 1)
  private String age;

  @ExcelProperty(value = "邮箱", index = 2)
  private String email;

  @ExcelProperty(value = "地址", index = 3)
  private String address;

  @ExcelProperty(value = "性别", index = 4)
  private String sex;

  @ExcelProperty(value = "高度", index = 5)
  private String heigh;

  @ExcelProperty(value = "备注", index = 6)
  private String last;
}
