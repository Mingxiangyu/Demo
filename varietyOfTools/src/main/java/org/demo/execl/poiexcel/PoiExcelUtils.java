package org.demo.execl.poiexcel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

/**
 * 工具类 时间：2012-08-29 下午16:30:40
 */
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
        if (cell != null && cell.getCellTypeEnum() != CellType.BLANK) {
          isEmpty = false;
          break;
        }
      }
      return isEmpty;
    }
  }

  /**
   * @param filePath 　文件完整路径
   * @return ：boolean
   * @描述：是否是2003的excel，返回true是2003 @时间：2012-08-29 下午16:29:11
   */
  public static boolean isExcel2003(String filePath) {

    return filePath.matches("^.+\\.(?i)(xls)$");
  }

  /**
   * @描述：是否是2007的excel，返回true是2007 @时间：2012-08-29 下午16:28:20 @参数：@param filePath　文件完整路径 @参数：@return
   * @返回值：boolean
   */
  public static boolean isExcel2007(String filePath) {

    return filePath.matches("^.+\\.(?i)(xlsx)$");
  }


  /**
   * 锁住单元格
   */

  public static void lockSheet() {
    // 创建Excel文件
    HSSFWorkbook workbook = new HSSFWorkbook();
    HSSFSheet sheet = workbook.createSheet(DateUtils.getDate("yyyyMMdd"));
    //锁定整个sheet   zgd为密码
    sheet.protectSheet("zgd");

    CellStyle unlockCell = workbook.createCellStyle();
    unlockCell.setLocked(false);
    // 设置dataRow这一行的第i个单元格不锁定
    dataRow.getCell(i).setCellStyle(unlockCell);
  }

  /**
   * 设置列宽
   */

  public static void setColumn(Sheet sheet) {
//    1.自适应列宽度。遇到行数多一点的数据的时候,就会耗费大量的时间
    sheet.autoSizeColumn(1); //方法一
    sheet.autoSizeColumn(1, true); //方法二

//    2.用数组将大概的宽度设置好,手动set宽度
//    int[] width = {xxx,xxx};
//    for循环
//    sheet.setColumnWidth(i,width[i]);
//    设置行高
    titleRow.setHeightInPoints(20);//目的是想把行高设置成20px
  }

  /**
   * 设置字体,颜色
   */

  public static void setColor(Sheet sheet) {
// 设置字体
    CellStyle redStyle = workbook.createCellStyle();
    HSSFFont redFont = workbook.createFont();
//颜色
    redFont.setColor(Font.COLOR_RED);
//设置字体大小
    redFont.setFontHeightInPoints((short) 10);
//字体
//redFont.setFontName("宋体");
    redStyle.setFont(redFont);

    HSSFCell cell13 = titleRow.createCell(13);
    cell13.setCellStyle(redStyle);
    cell13.setCellValue("注意:只允许修改销售价,供应价,市场价和库存");
  }

  public static void mergeCell() {
    //这里代表在第0行开始,到0行结束,从0列开始,到10列结束,进行合并,也就是合并第0行的0-10个单元格
    CellRangeAddress cellRange1 = new CellRangeAddress(0, 0, (short) 0, (short) 10);
    sheet.addMergedRegion(cellRange1);
  }

  public static void set() {
    // 创建Excel文件
    HSSFWorkbook workbook = new HSSFWorkbook();
    HSSFSheet sheet = workbook.createSheet("sheet");
//设置样式
    CellStyle blackStyle = workbook.createCellStyle();
//自动换行*重要*
    blackStyle.setWrapText(true);

//存储最大列宽
    Map<Integer, Integer> maxWidth = new HashMap<>();
// 标题行
    HSSFRow titleRow = sheet.createRow(0);
    titleRow.setHeightInPoints(20);//目的是想把行高设置成20px
    titleRow.createCell(0).setCellValue("sku编号");
    titleRow.createCell(1).setCellValue("商品标题");
    titleRow.createCell(2).setCellValue("商品名");
// 初始化标题的列宽,字体
    for (int i = 0; i <= 3; i++) {
      maxWidth.put(i, titleRow.getCell(i).getStringCellValue().getBytes().length * 256 + 200);
      titleRow.getCell(i).setCellStyle(blackStyle);//设置自动换行
    }

    for (Map<String, Object> map : list) {
      int currentRowNum = sheet.getLastRowNum() + 1;
      //数据行
      HSSFRow dataRow = sheet.createRow(currentRowNum);
      // 记录这一行的每列的长度
      List<Object> valueList = new ArrayList<Object>();

      String val0 = map.get("skuId") == null ? "—" : ((Double) (map.get("skuId"))).intValue() + "";
      valueList.add(val0);
      dataRow.createCell(0).setCellValue(val0);
      String val1 = map.get("title") == null ? "" : map.get("title").toString();
      valueList.add(val1);
      dataRow.createCell(1).setCellValue(val1);
      String val2 = map.get("goodsName") == null ? "" : map.get("goodsName").toString();
      valueList.add(val2);
      dataRow.createCell(2).setCellValue(val2);
      String val3 = map.get("catName") == null ? "" : map.get("catName").toString();
      valueList.add(val3);
      dataRow.createCell(3).setCellValue(val3);
      String val4 = map.get("brandName") == null ? "" : map.get("brandName").toString();

      for (int i = 0; i <= 3; i++) {
        int length = valueList.get(i).toString().getBytes().length * 256 + 200;
        //这里把宽度最大限制到15000
        if (length > 15000) {
          length = 15000;
        }
        maxWidth.put(i, Math.max(length, maxWidth.get(i)));
        dataRow.getCell(i).setCellStyle(blackStyle);//设置自动换行
      }
    }

    for (int i = 0; i <= 3; i++) {
      //设置列宽
      sheet.setColumnWidth(i, maxWidth.get(i));
    }
  }
}
