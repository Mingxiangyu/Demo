package com.iglens.word;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.List;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.hslf.extractor.PowerPointExtractor;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.openxmlformats.schemas.drawingml.x2006.main.CTRegularTextRun;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextBody;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraph;
import org.openxmlformats.schemas.presentationml.x2006.main.CTGroupShape;
import org.openxmlformats.schemas.presentationml.x2006.main.CTShape;
import org.openxmlformats.schemas.presentationml.x2006.main.CTSlide;

  /**
   *
   * @author T480S
   * @version 0.0.1
   */
  public class POI读取各种文档方法 {

    /**
     * 用来读取doc文件的方法
     * @param filePath
     * @return
     * @throws Exception
     */
    public static String getTextFromDoc(String filePath) throws Exception{
      StringBuilder sb = new StringBuilder();
      FileInputStream fis = new FileInputStream(new File(filePath));
      HWPFDocument doc = new HWPFDocument(fis);
      Range rang = doc.getRange();
      sb.append(rang.text());
      fis.close();
      return sb.toString();

    }
    /**
     * 用来读取docx文件
     * @param filePath
     * @return
     * @throws IOException
     * @throws Exception
     */
    @SuppressWarnings("resource")
    public static String getTextFromDocx(String filePath) throws IOException {
      FileInputStream in = new FileInputStream(filePath);
      XWPFDocument doc = new XWPFDocument(in);
      XWPFWordExtractor extractor = new XWPFWordExtractor(doc);
      String text = extractor.getText();
      in.close();
      return text;
    }
    /**
     * 用来读取pdf文件
     * @param filePath
     * @return
     * @throws IOException
     */
    public static String getTextFromPDF(String filePath) throws IOException{
      File input = new File(filePath);
      PDDocument pd = PDDocument.load(input);
      PDFTextStripper stripper = new PDFTextStripper();
      return stripper.getText(pd);
    }
    /**
     * 用来读取ppt文件
     * @param filePath
     * @return
     * @throws IOException
     */
    public static String getTextFromPPT( String filePath) throws IOException{
      FileInputStream in = new FileInputStream(filePath);
      PowerPointExtractor extractor = new PowerPointExtractor(in);
      String content = extractor.getText();
      extractor.close();
      return content;
    }
    /**
     * 用来读取pptx文件
     * @param filePath
     * @return
     * @throws IOException
     */
    public static String getTextFromPPTX( String filePath) throws IOException{
      String resultString = null;
      StringBuilder sb = new StringBuilder();
      FileInputStream in = new FileInputStream(filePath);
      try {
        XMLSlideShow xmlSlideShow = new XMLSlideShow(in);
        List<XSLFSlide> slides = xmlSlideShow.getSlides();
        for(XSLFSlide slide:slides){
          CTSlide rawSlide = slide.getXmlObject();
          CTGroupShape gs = rawSlide.getCSld().getSpTree();
          CTShape[] shapes = gs.getSpArray();
          for(CTShape shape:shapes){
            CTTextBody tb = shape.getTxBody();
            if(null==tb){
              continue;
            }
            CTTextParagraph[] paras = tb.getPArray();
            for(CTTextParagraph textParagraph:paras){
              CTRegularTextRun[] textRuns = textParagraph.getRArray();
              for(CTRegularTextRun textRun:textRuns){
                sb.append(textRun.getT());
              }
            }
          }
        }
        resultString = sb.toString();
        xmlSlideShow.close();
      } catch (Exception e) {
        e.printStackTrace();
      }
      return resultString;
    }
    /**
     * 用来读取xls
     * @param filePath
     * @return
     * @throws IOException
     */
    public static String getTextFromxls(String filePath) throws IOException{
      FileInputStream in = new FileInputStream(filePath);
      StringBuilder content = new StringBuilder();
      HSSFWorkbook workbook = new HSSFWorkbook(in);
      for(int sheetIndex=0;sheetIndex<workbook.getNumberOfSheets();sheetIndex++){
        HSSFSheet sheet = workbook.getSheetAt(sheetIndex);
        for(int rowIndex=0;rowIndex<=sheet.getLastRowNum();rowIndex++){
          HSSFRow row = sheet.getRow(rowIndex);
          if(row==null){
            continue;
          }
          for(int cellnum=0;cellnum<row.getLastCellNum();cellnum++){
            HSSFCell cell = row.getCell(cellnum);
            if(cell!=null){
              content.append(cell.getRichStringCellValue().getString()+" ");
            }

          }
        }

      }
      workbook.close();
      return content.toString();

    }
    /**
     * 用来读取xlsx文件
     * @param filePath
     * @return
     * @throws IOException
     */
    public static String getTextFromxlsx(String filePath) throws IOException{
      StringBuilder content = new StringBuilder();
      XSSFWorkbook workbook = new XSSFWorkbook(filePath);
      for(int sheet=0;sheet<workbook.getNumberOfSheets();sheet++){
        if(null!=workbook.getSheetAt(sheet)){
          XSSFSheet aSheet =workbook.getSheetAt(sheet);
          for(int row=0;row<=aSheet.getLastRowNum();row++){
            if(null!=aSheet.getRow(row)){
              XSSFRow aRow = aSheet.getRow(row);
              for(int cell=0;cell<aRow.getLastCellNum();cell++){
                if(null!=aRow.getCell(cell)){
                  XSSFCell aCell = aRow.getCell(cell);
                  if(convertCell(aCell).length()>0){
                    content.append(convertCell(aCell));
                  }
                }
                content.append(" ");
              }
            }
          }
        }
      }
      workbook.close();
      return content.toString();

    }

    private static String convertCell(Cell cell){
      NumberFormat formater = NumberFormat.getInstance();
      formater.setGroupingUsed(false);
      String cellValue="";
      if(cell==null){
        return cellValue;
      }

      switch(cell.getCellType()){
        case HSSFCell.CELL_TYPE_NUMERIC:
          cellValue = formater.format(cell.getNumericCellValue());
          break;
        case HSSFCell.CELL_TYPE_STRING:
          cellValue = cell.getStringCellValue();
          break;
        case HSSFCell.CELL_TYPE_BLANK:
          cellValue = cell.getStringCellValue();
          break;
        case HSSFCell.CELL_TYPE_BOOLEAN:
          cellValue = Boolean.valueOf(cell.getBooleanCellValue()).toString();
          break;
        case HSSFCell.CELL_TYPE_ERROR:
          cellValue = String.valueOf(cell.getErrorCellValue());
          break;
        default:cellValue="";
      }
      return cellValue.trim();
    }
  }

