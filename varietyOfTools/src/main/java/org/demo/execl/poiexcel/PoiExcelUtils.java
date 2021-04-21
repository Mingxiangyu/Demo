package org.demo.execl.poiexcel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

/** 工具类 时间：2012-08-29 下午16:30:40 */
public class PoiExcelUtils {

  /**
   * 判断是否为空行
   *
   * @param row 如果为空行返回true
   * @return 是否为空行
   */

  public static boolean isEmptyRow(Row row) {
    if (row == null || row.toString().isEmpty()) {
      return true;
    } else {
      boolean isEmpty = true;
      // 从第一个不为空的列开始 到 最后一个 不为空的列(有格式就算一列 )
      for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
        Cell cell = row.getCell(c);
        if (cell != null && cell.getCellTypeEnum()!= CellType.BLANK){
          isEmpty = false;
          break;
        }
      }
      return isEmpty;
    }
  }

  /**
   * @描述：是否是2003的excel，返回true是2003 @时间：2012-08-29 下午16:29:11
   *
   * @param filePath　文件完整路径
   * @return ：boolean
   */
  public static boolean isExcel2003(String filePath) {

    return filePath.matches("^.+\\.(?i)(xls)$");
  }

  /**
   * @描述：是否是2007的excel，返回true是2007 @时间：2012-08-29 下午16:28:20 @参数：@param
   * filePath　文件完整路径 @参数：@return @返回值：boolean
   */
  public static boolean isExcel2007(String filePath) {

    return filePath.matches("^.+\\.(?i)(xlsx)$");
  }
}
