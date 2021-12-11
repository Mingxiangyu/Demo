package com.iglens.execl.poiexcel;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

/** POI工具类 */
public class PoiExcelUtils {

  // 扩展名
  public static final String XLS = "xls";
  public static final String XLSX = "xlsx";

  /**
   * * 读取excel文件
   *
   * @param excelFile excel文件
   * @param startRow 读取数据的起始行, 行号从0开始
   * @return
   * @throws IOException
   */
  public static List<String[]> readExcelFile(MultipartFile excelFile, int startRow)
      throws IOException {
    // 检查文件
    checkFile(excelFile);
    // 获得工作簿对象
    Workbook workbook = getWorkBook(excelFile);
    // 创建返回对象，把每行中的值作为一个数组，所有的行作为一个集合返回
    List<String[]> list = new ArrayList<>();
    if (workbook != null) {
      for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {
        // 获取当前sheet工作表
        Sheet sheet = workbook.getSheetAt(sheetNum);
        if (sheet == null) {
          continue;
        }
        // 获得当前sheet的结束行
        int lastRowNum = sheet.getLastRowNum();
        if (startRow < 0 || startRow > lastRowNum) {
          throw new RuntimeException("wrong startRow");
        }
        // 循环除了第一行之外的所有行
        for (int rowNum = startRow; rowNum <= lastRowNum; rowNum++) {
          // 获得当前行
          Row row = sheet.getRow(rowNum);
          if (row == null) {
            continue;
          }
          // 获得当前行的开始列
          int firstCellNum = row.getFirstCellNum();
          // 获得当前行的列数
          int lastCellNum = row.getPhysicalNumberOfCells();
          String[] cells = new String[row.getPhysicalNumberOfCells()];
          // 循环当前行
          for (int cellNum = firstCellNum; cellNum < lastCellNum; cellNum++) {
            Cell cell = row.getCell(cellNum);
            cells[cellNum] = getCellValue(cell);
          }
          list.add(cells);
        }
      }
    }
    return list;
  }

  /**
   * 生成excel文件
   *
   * @param data 数据
   * @param extension 文件扩展
   * @return Workbook
   */
  public static Workbook createExcelFile(
      List<String> attributes, List<List<String>> data, String extension) {
    Workbook workbook = createWorkbook(extension);
    if (workbook == null) {
      return null;
    }

    if (workbook != null) {
      // 2. 创建sheet
      Sheet sheet = workbook.createSheet("sheet1");
      // 3. 创建row: 添加属性行
      Row row0 = sheet.createRow(0);
      for (int i = 0; i < attributes.size(); i++) {
        Cell cell = row0.createCell(i);
        cell.setCellValue(attributes.get(i).trim());
      }
      // 4. 插入数据
      if (CollectionUtils.isNotEmpty(data)) {
        for (int i = 0; i < data.size(); i++) {
          List<String> rowInfo = data.get(i);
          Row row = sheet.createRow(i + 1);
          // 添加数据
          for (int j = 0; j < rowInfo.size(); j++) {
            row.createCell(j).setCellValue(rowInfo.get(j));
          }
        }
      }
    }
    return workbook;
  }

  /**
   * 创建workbook
   *
   * @param extension 文件扩展
   * @return workbook对象
   */
  private static Workbook createWorkbook(String extension) {
    // 1. 创建workbook
    Workbook workbook = null;
    if (StringUtils.isBlank(extension)) {
      return null;
    }

    if (extension.equalsIgnoreCase(XLS)) {
      // 2003版本
      workbook = new HSSFWorkbook();
    } else if (extension.equalsIgnoreCase(XLSX)) {
      // 2007版本
      workbook = new XSSFWorkbook();
    }
    return workbook;
  }

  /**
   * 获取当前列数据
   *
   * @param cell 列
   * @return 列值
   */
  private static String getCellValue(Cell cell) {
    String cellValue = "";

    if (cell == null) {
      return cellValue;
    }
    // 把数字当成String来读，避免出现1读成1.0的情况
    if (cell.getCellTypeEnum() == CellType.NUMERIC) {
      cell.setCellType(CellType.STRING);
    }
    // 判断数据的类型
    switch (cell.getCellTypeEnum()) {
      case NUMERIC:
        cellValue = String.valueOf(cell.getNumericCellValue());
        break;
      case STRING:
        cellValue = String.valueOf(cell.getStringCellValue());
        break;
      case BOOLEAN:
        cellValue = String.valueOf(cell.getBooleanCellValue());
        break;
      case FORMULA:
        cellValue = String.valueOf(cell.getCellFormula());
        break;
      case BLANK:
        cellValue = "";
        break;
      case ERROR:
        cellValue = "非法字符";
        break;
      default:
        cellValue = "未知类型";
        break;
    }
    return cellValue;
  }

  /**
   * 获得工作簿对象
   *
   * @param excelFile excel文件
   * @return 工作簿对象
   */
  public static Workbook getWorkBook(MultipartFile excelFile) {
    // 获得文件名
    String fileName = excelFile.getOriginalFilename();
    // 创建Workbook工作簿对象，表示整个excel
    Workbook workbook = null;
    try {
      // 获得excel文件的io流
      InputStream is = excelFile.getInputStream();
      // 根据文件后缀名不同(xls和xlsx)获得不同的workbook实现类对象
      assert fileName != null;
      if (fileName.endsWith(XLS)) {
        // 2003版本
        workbook = new HSSFWorkbook(is);
      } else if (fileName.endsWith(XLSX)) {
        // 2007版本
        workbook = new XSSFWorkbook(is);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return workbook;
  }

  /**
   * 检查文件
   *
   * @param excelFile excel文件
   * @throws IOException
   */
  public static void checkFile(MultipartFile excelFile) throws IOException {
    // 判断文件是否存在
    if (null == excelFile) {
      throw new FileNotFoundException("文件不存在");
    }
    // 获得文件名
    String fileName = excelFile.getOriginalFilename();
    // 判断文件是否是excel文件
    if (!fileName.endsWith(XLS) && !fileName.endsWith(XLSX)) {
      throw new IOException(fileName + "不是excel文件");
    }
  }

  public static void function() throws IOException {
    //    得到Excel常用对象
    POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream("d:/test.xls"));
    // 得到Excel工作簿对象
    HSSFWorkbook wb = new HSSFWorkbook(fs);
    // 得到Excel工作表对象
    HSSFSheet sheet = wb.getSheetAt(0);
    // 得到Excel工作表的行
    HSSFRow row = sheet.getRow(1);
    // 得到Excel工作表指定行的单元格
    HSSFCell cell = row.getCell((short) 1);
    HSSFCellStyle cellStyle = cell.getCellStyle(); // 得到单元格样式

    //    2、建立Excel常用对象
    //    HSSFWorkbook wb = new HSSFWorkbook();//创建Excel工作簿对象
    //    HSSFSheet sheet = wb.createSheet("new sheet");//创建Excel工作表对象
    //    HSSFRow row = sheet.createRow((short)0); //创建Excel工作表的行
    cellStyle = wb.createCellStyle(); // 创建单元格样式
    row.createCell((short) 0).setCellStyle(cellStyle); // 创建Excel工作表指定行的单元格
    row.createCell((short) 0).setCellValue(1); // 设置Excel工作表的值

    //    3、设置sheet名称和单元格内容
    wb.setSheetName(1, "第一张工作表");
    cell.setCellValue("单元格内容");

    //    4、取得sheet的数目
    int numberOfSheets = wb.getNumberOfSheets();

    //    5、 根据index取得sheet对象
    //    HSSFSheet sheet = wb.getSheetAt(0);

    //    6、取得有效的行数
    int rowcount = sheet.getLastRowNum();

    //    7、取得一行的有效单元格个数
    short lastCellNum = row.getLastCellNum();

    //    8、单元格值类型读写
    cell.setCellType(HSSFCell.CELL_TYPE_STRING); // 设置单元格为STRING类型
    cell.getNumericCellValue(); // 读取为数值类型的单元格内容

    //    9、设置列宽、行高
    sheet.setColumnWidth((short) 1, (short) 1);
    row.setHeight((short) 2);

    //    10、添加区域，合并单元格
    CellRangeAddress region =
        new CellRangeAddress((short) 1, (short) 1, (short) 1, (short) 1); // 合并从第rowFrom行columnFrom列
    sheet.addMergedRegion(region); // 到rowTo行columnTo的区域
    // 得到所有区域
    int numMergedRegions = sheet.getNumMergedRegions();

    //    11、保存Excel文件
    FileOutputStream fileOut = new FileOutputStream("path");
    wb.write(fileOut);

    //    13、常用单元格边框格式
    HSSFCellStyle style = wb.createCellStyle();
    style.setBorderBottom(BorderStyle.DOTTED); // 下边框
    style.setBorderLeft(BorderStyle.DOTTED); // 左边框
    style.setBorderRight(BorderStyle.THIN); // 右边框
    style.setBorderTop(BorderStyle.THIN); // 上边框

    //    14、设置字体和内容位置
    HSSFFont f = wb.createFont();
    f.setFontHeightInPoints((short) 11); // 字号
    f.setBold(false); // 加粗
    style.setFont(f);
    style.setAlignment(HorizontalAlignment.CENTER); // 左右居中
    style.setVerticalAlignment(VerticalAlignment.CENTER); // 上下居中
    style.setRotation((short) 70); // 单元格内容的旋转的角度
    HSSFDataFormat df = wb.createDataFormat();
    style.setDataFormat(df.getFormat("0.00%")); // 设置单元格数据格式
    cell.setCellFormula("公式"); // 给单元格设公式
    style.setRotation((short) 70); // 单元格内容的旋转的角度

    //    15、插入图片
    // 先把读进来的图片放到一个ByteArrayOutputStream中，以便产生ByteArray
    ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
    BufferedImage bufferImg = ImageIO.read(new File("ok.jpg"));
    ImageIO.write(bufferImg, "jpg", byteArrayOut);
    // 读进一个excel模版
    FileInputStream fos = new FileInputStream("filePathName" + "/stencil.xlt");
    fs = new POIFSFileSystem(fos);
    // 创建一个工作薄
    //    HSSFWorkbook wb = new HSSFWorkbook(fs);
    //     HSSFSheet sheet = wb.getSheetAt(0);
    HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
    HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, 1023, 255, (short) 0, 0, (short) 10, 10);
    patriarch.createPicture(
        anchor, wb.addPicture(byteArrayOut.toByteArray(), HSSFWorkbook.PICTURE_TYPE_JPEG));

    //    16、调整工作表位置
    //    HSSFWorkbook wb = new HSSFWorkbook();
    //    HSSFSheet sheet = wb.createSheet("format sheet");
    HSSFPrintSetup ps = sheet.getPrintSetup();
    sheet.setAutobreaks(true);
    ps.setFitHeight((short) 1);
    ps.setFitWidth((short) 1);
  }
  //    12、根据单元格不同属性返回字符串数值
  public String getCellStringValue(HSSFCell cell) {
    String cellValue = "";
    switch (cell.getCellType()) {
      case HSSFCell.CELL_TYPE_STRING: // 字符串类型
        cellValue = cell.getStringCellValue();
        if ("".equals(cellValue.trim()) || cellValue.trim().length() <= 0) {
          cellValue = " ";
        }
        break;
      case HSSFCell.CELL_TYPE_NUMERIC: // 数值类型
        cellValue = String.valueOf(cell.getNumericCellValue());
        break;
      case HSSFCell.CELL_TYPE_FORMULA: // 公式
        cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
        cellValue = String.valueOf(cell.getNumericCellValue());
        break;
      case HSSFCell.CELL_TYPE_BLANK:
        cellValue = " ";
        break;
      case HSSFCell.CELL_TYPE_BOOLEAN:
        break;
      case HSSFCell.CELL_TYPE_ERROR:
        break;
      default:
        break;
    }
    return cellValue;
  }
}
