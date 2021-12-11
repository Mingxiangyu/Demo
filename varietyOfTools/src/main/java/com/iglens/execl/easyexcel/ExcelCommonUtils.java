package com.iglens.execl.easyexcel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.iglens.execl.easyexcel.dto.AnalysisExcelDto;
import com.iglens.execl.easyexcel.dto.ExportExcelDto;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/* * @author T480S */
public class ExcelCommonUtils {

  private static final Logger log = LoggerFactory.getLogger(ExcelCommonUtils.class);

  private static final String CONTENT_TYPE = "application/octet-stream;charset=ISO_8859_1";

  /**
   * 功能描述: 解析excel
   *
   * @param stream excel的输入流
   * @param analysisExcelDtos 解析excel的参数
   * @return java.util.Map<java.lang.Integer,java.util.List<java.util.List<java.lang.String>>>
   *     * @author T480S
   * @date 2020/6/9 18:08
   */
  public static Map<Integer, List<List<String>>> analysisExcel(
      InputStream stream, List<AnalysisExcelDto> analysisExcelDtos) {
    if (CollectionUtils.isEmpty(analysisExcelDtos)) {
      return new HashMap<>(8);
    }
    EasyExcelListener listener = new EasyExcelListener();
    ExcelReader excelReader = EasyExcelFactory.read(stream, null, listener).build();
    for (AnalysisExcelDto analysisExcelDto : analysisExcelDtos) {
      excelReader.read(
          EasyExcel.readSheet(analysisExcelDto.getSheetNo())
              .headRowNumber(analysisExcelDto.getHeadLineNo())
              .build());
    }
    return listener.getResult();
  }

  public static Map<Integer, List<Object>> analysisExcelToDto(
      InputStream stream, List<AnalysisExcelDto> analysisExcelDtos) {
    if (CollectionUtils.isEmpty(analysisExcelDtos)) {
      return null;
    }
    ModelExcelListener listener = new ModelExcelListener();
    ExcelReader excelReader = EasyExcelFactory.read(stream, listener).build();
    for (AnalysisExcelDto analysisExcelDto : analysisExcelDtos) {
      excelReader.read(
          EasyExcel.readSheet(analysisExcelDto.getSheetNo())
              .headRowNumber(analysisExcelDto.getHeadLineNo())
              .head(analysisExcelDto.getDtoClass())
              .build());
    }
    return listener.getResult();
  }

  /**
   * 功能描述: 响应浏览器，导出excel（无模型映射）
   *
   * @param response
   * @param exportExcelDto 导出excel的参数dto
   * @return void
   * @author T480S
   * @date 2020/6/9 18:31
   */
  public static void writExcel(HttpServletResponse response, ExportExcelDto exportExcelDto) {
    Sheet sheet = new Sheet(1, 0);
    sheet.setSheetName(exportExcelDto.getSheetName());
    // 标题头
    List<String> head = exportExcelDto.getHead();
    if (CollectionUtils.isNotEmpty(head)) {
      List<List<String>> list = new ArrayList<>();
      head.forEach(h -> list.add(Collections.singletonList(h)));
      sheet.setHead(list);
    }

    // 具体数据
    ServletOutputStream outputStream = null;
    try {
      outputStream = response.getOutputStream();
      response.setContentType(CONTENT_TYPE);
      response.setHeader(
          "Content-disposition", "attachment; filename=" + exportExcelDto.getFileName());

      ExcelWriter writer = EasyExcelFactory.getWriter(outputStream);
      writer.write1(exportExcelDto.getDatas(), sheet);
      writer.finish();

      outputStream.flush();
      outputStream.close();
    } catch (Exception e) {
      log.error(e.getLocalizedMessage(), e);
    } finally {
      try {
        if (outputStream != null) {
          outputStream.close();
        }
      } catch (IOException e) {
        log.error(e.getLocalizedMessage(), e);
      }
    }
  }

  /**
   * 功能描述: 响应浏览器，导出excel（模型映射）
   *
   * @param response 响应流
   * @param exportExcelDto 导出excel的参数dto
   * @author T480S
   * @date 2020/6/9 18:35
   */
  public static void exportExcelByModel(
      HttpServletResponse response, ExportExcelDto exportExcelDto) {

    try (ServletOutputStream outputStream = response.getOutputStream()) {
      response.setContentType(CONTENT_TYPE);
      response.setHeader(
          "Content-disposition", "attachment; filename=" + exportExcelDto.getFileName());
      ExcelWriter writer = EasyExcel.write(response.getOutputStream()).build();

      ExcelCommonUtils.writExcel(writer, exportExcelDto);

      writer.finish();
      outputStream.flush();
    } catch (Exception e) {
      log.error("WritExcel失败： " + e.getLocalizedMessage(), e);
    }
  }

  /**
   * 功能描述: 响应浏览器，批量导出excel（模型映射）
   *
   * @param response 响应流
   * @param exportExcelDtoList 导出excel的参数dto集合
   */
  public static void exportExcelByModel(
      HttpServletResponse response, List<ExportExcelDto> exportExcelDtoList) {
    if (CollectionUtils.isEmpty(exportExcelDtoList)) {
      log.error("导入时 exportExcelDtoList 数据为空！");
      return;
    }

    try (ServletOutputStream outputStream = response.getOutputStream(); ) {
      response.setContentType(CONTENT_TYPE);
      response.setHeader(
          "Content-disposition", "attachment; filename=" + exportExcelDtoList.get(0).getFileName());
      ExcelWriter writer = EasyExcel.write(response.getOutputStream()).build();

      for (ExportExcelDto exportExcelDto : exportExcelDtoList) {
        ExcelCommonUtils.writExcel(writer, exportExcelDto);
      }

      writer.finish();
      outputStream.flush();
    } catch (Exception e) {
      log.error("WritExcel失败： " + e.getLocalizedMessage(), e);
    }
  }

  /**
   * 写入excel
   *
   * @param writer 输出流
   * @param exportExcelDto 导出excel的参数dto
   */
  private static void writExcel(ExcelWriter writer, ExportExcelDto exportExcelDto) {
    Integer sheetNo = exportExcelDto.getSheetNo();
    if (Objects.isNull(sheetNo) || sheetNo == 0) {
      sheetNo = 1;
    }
    List<? extends BaseRowModel> datas = exportExcelDto.getModelDatas();
    WriteSheet writeSheet =
        EasyExcel.writerSheet(sheetNo, exportExcelDto.getSheetName())
            .head(datas.get(0).getClass())
            .build();
    writer.write(datas, writeSheet);
  }
}
