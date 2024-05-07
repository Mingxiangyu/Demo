package com.iglens.execl.easyexcel.importDto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadStyle;
import com.alibaba.excel.metadata.BaseRowModel;
import com.iglens.execl.easyexcel.dto.LifecycleConstants;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.poi.ss.usermodel.FillPatternType;

/** @author mxy */
@Data
@ColumnWidth(30)
@ContentRowHeight(30)
@EqualsAndHashCode(callSuper = true)
public class LifecycleStateImportDTO extends BaseRowModel implements Serializable {
  private static final long serialVersionUID = 1L;

  @HeadStyle(fillPatternType = FillPatternType.SOLID_FOREGROUND, fillForegroundColor = 2)
  @ExcelProperty(value = LifecycleConstants.STATE_NAME)
  private String name;

  @HeadStyle(fillPatternType = FillPatternType.SOLID_FOREGROUND, fillForegroundColor = 2)
  @ExcelProperty(value = LifecycleConstants.STATE_IDENTIFIER)
  private String identifier;

  @ColumnWidth(15)
  @ExcelProperty(value = LifecycleConstants.PHASE_SORT)
  private Integer sort;

  @ExcelProperty(value = LifecycleConstants.STATE_DESCRIPTION)
  private String description;

  /** 状态类型 */
  @ExcelProperty(value = LifecycleConstants.STATE_TYPE)
  private String type;
}
