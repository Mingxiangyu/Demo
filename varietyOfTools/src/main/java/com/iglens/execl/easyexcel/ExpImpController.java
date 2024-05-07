package com.iglens.execl.easyexcel;

import com.iglens.execl.easyexcel.dto.AnalysisExcelDto;
import com.iglens.execl.easyexcel.dto.LifecycleStateDTO;
import com.iglens.execl.easyexcel.importDto.LifecycleStateImportDTO;
import com.iglens.execl.easyexcel.service.LifecycleStateExpImpService;
import com.iglens.对象.判断对象所有属性是否均为null;
import io.swagger.v3.oas.annotations.Operation;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
public class ExpImpController {
  private final LifecycleStateExpImpService stateExpImpService;

  public ExpImpController(LifecycleStateExpImpService stateExpImpService) {
    this.stateExpImpService = stateExpImpService;
  }

  @Operation(summary = "导入生命周期状态模板")
  @PostMapping(value = "/lifecycle-states/import", name = "导入生命周期状态模板")
  ResponseEntity<Void> importState(@RequestParam(value = "file") MultipartFile file) {
    try (InputStream stream = file.getInputStream()) {
      // 解析EXCEL
      List<AnalysisExcelDto> parameterList = new ArrayList<>();
      AnalysisExcelDto analysisExcelDto = new AnalysisExcelDto();
      analysisExcelDto.setSheetNo(0);
      analysisExcelDto.setHeadLineNo(2);
      analysisExcelDto.setDtoClass(LifecycleStateImportDTO.class);
      parameterList.add(analysisExcelDto);
      Map<Integer, List<Object>> integerListMap =
          ExcelCommonUtils.analysisExcelToDto(stream, parameterList);
      // 构造DTO对象
      List<LifecycleStateDTO> forms = new ArrayList<>();
      for (Entry<Integer, List<Object>> integerListEntry : integerListMap.entrySet()) {
        List<Object> value = integerListEntry.getValue();
        if (CollectionUtils.isEmpty(value)) {
          throw new RuntimeException("表格中无数据，请添加数据！");
        }
        for (Object importDTO : value) {
          if (importDTO instanceof LifecycleStateImportDTO) {
            if (判断对象所有属性是否均为null.objCheckIsNull(importDTO)) {
              continue;
            }
            LifecycleStateDTO formDTO = new LifecycleStateDTO();
            BeanUtils.copyProperties(importDTO, formDTO);
            Assert.notNull(formDTO.getName(), "状态名称不能为空");
            Assert.notNull(formDTO.getIdentifier(), "状态标识不能为空");
            forms.add((formDTO));
          }
        }
      }
      stateExpImpService.importLifecycleState( forms);
      return ResponseEntity.status(HttpStatus.CREATED).build();
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      throw new RuntimeException("导入失败：" + e.getMessage());
    }
  }

  @Operation(summary = "下载空的生命周期状态excel模板")
  @GetMapping(value = "/lifecycle-states/download", name = "下载空的生命周期状态excel模板")
  ResponseEntity<Void> downloadExcel(HttpServletResponse response) {
    stateExpImpService.downloadExcel(response);
    return ResponseEntity.ok().build();
  }

  @Operation(summary = "导出生命周期状态")
  @GetMapping(value = "/lifecycle-states/export", name = "导出生命周期状态")
  ResponseEntity<Void> exportState(
      @RequestParam(required = false) List<String> ids, HttpServletResponse response) {
    stateExpImpService.exportLifecycleState(ids, response);
    return ResponseEntity.ok().build();
  }
}
