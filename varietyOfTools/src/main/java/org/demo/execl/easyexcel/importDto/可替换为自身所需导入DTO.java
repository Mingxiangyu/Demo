package org.demo.execl.easyexcel.importDto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadStyle;
import java.io.Serializable;
import lombok.Data;
import org.apache.poi.ss.usermodel.FillPatternType;

/** @author mxy */
@Data
@ColumnWidth(30) // Excel表格列宽
@ContentRowHeight(30) // Excel表格行高
public class 可替换为自身所需导入DTO implements Serializable {
  private static final long serialVersionUID = 1L;

  @HeadStyle(fillPatternType = FillPatternType.SOLID_FOREGROUND, fillForegroundColor = 2) // 表头样式
  @ExcelProperty(value = "LifecycleConstants.TEMPLATE_NAME") // Value值为excel中表头名称
  private String name;

  @HeadStyle(fillPatternType = FillPatternType.SOLID_FOREGROUND, fillForegroundColor = 2)
  @ExcelProperty(value = "LifecycleConstants.TEMPLATE_DESCRIPTION")
  private String description;

  @ColumnWidth(15) // 调节单列表格列宽
  @HeadStyle(fillPatternType = FillPatternType.SOLID_FOREGROUND, fillForegroundColor = 2)
  @ExcelProperty(value = "LifecycleConstants.TEMPLATE_DISABLED")
  private Boolean disabled;

  /** 模板类型 */
  @ExcelProperty(value = "LifecycleConstants.TEMPLATE_IDENTIFIER")
  private String type;
}
