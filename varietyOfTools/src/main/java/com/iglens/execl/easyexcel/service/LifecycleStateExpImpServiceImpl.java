package com.iglens.execl.easyexcel.service;

import com.iglens.execl.easyexcel.ExcelCommonUtils;
import com.iglens.execl.easyexcel.dto.ExportExcelDto;
import com.iglens.execl.easyexcel.dto.LifecycleStateDTO;
import com.iglens.execl.easyexcel.importDto.LifecycleStateImportDTO;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import org.apache.catalina.LifecycleState;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** @author mxy */
@Service
@Transactional(rollbackFor = Exception.class)
public class LifecycleStateExpImpServiceImpl implements LifecycleStateExpImpService {
  private static final Logger log = LoggerFactory.getLogger(LifecycleStateExpImpServiceImpl.class);

  @Override
  public void importLifecycleState(List<LifecycleStateDTO> forms) {
    StringBuilder builder = new StringBuilder();

    for (LifecycleStateDTO form : forms) {
      String identifier = StringUtils.trimToEmpty(form.getIdentifier());
      // 查询数据
      // LifecycleState lifecycleStateByIdentifier =
      //     stateService.getLifecycleStateByIdentifier(identifier);
      try {
        // 存在则更新，不存在则放弃
        // if (lifecycleStateByIdentifier != null) {
        //   stateService.modifyLifecycleState(
        //       String.valueOf(lifecycleStateByIdentifier.getId()), form);
        // } else {
        //   stateService.createLifecycleState(tenantId, form);
        // }
      } catch (Exception e) {
        builder.append(e.getMessage()).append(" ");
      }
    }
    if (StringUtils.isNotBlank(builder.toString())) {
      throw new RuntimeException(builder.toString());
    }
  }

  @Override
  public void downloadExcel(HttpServletResponse response) {
    String fileName =
        "生命周期状态管理-" + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE) + ".xlsx";
    fileName = new String(fileName.getBytes(), StandardCharsets.ISO_8859_1);

    List<LifecycleStateImportDTO> importDtoList = new ArrayList<>();
    if (CollectionUtils.isEmpty(importDtoList)) {
      importDtoList.add(new LifecycleStateImportDTO());
    }
    ExportExcelDto templateExcelDto = new ExportExcelDto();
    templateExcelDto.setFileName(fileName);
    templateExcelDto.setSheetName("生命周期状态");
    templateExcelDto.setSheetNo(1);
    templateExcelDto.setModelDatas(importDtoList);
    ExcelCommonUtils.exportExcelByModel(response, templateExcelDto);
  }

  @Override
  public void exportLifecycleState(List<String> ids, HttpServletResponse response) {
    String fileName =
        "生命周期状态管理-" + LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE) + ".xlsx";
    fileName = new String(fileName.getBytes(), StandardCharsets.ISO_8859_1);
    List<LifecycleState> stateList = new ArrayList<>();
    // 获取生命周期状态数据
    // if (CollectionUtils.isEmpty(ids1)) {
    //   stateList = stateService.listLifecycleState(); // 如果没有id则直接导出所有
    // } else {
    //   stateList = ids.stream().map(stateService::getLifecycleState).collect(Collectors.toList()); 查询数据
    // }

    List<LifecycleStateImportDTO> importDtoList =
        stateList.stream().map(this::convertToExpImpDto).collect(Collectors.toList());
    if (CollectionUtils.isEmpty(importDtoList)) {
      importDtoList.add(new LifecycleStateImportDTO());
    }

    ExportExcelDto templateExcelDto = new ExportExcelDto();
    templateExcelDto.setFileName(fileName);
    templateExcelDto.setSheetName("生命周期状态");
    templateExcelDto.setSheetNo(1);
    templateExcelDto.setModelDatas(importDtoList);
    ExcelCommonUtils.exportExcelByModel(response, templateExcelDto);
  }

  protected LifecycleStateImportDTO convertToExpImpDto(LifecycleState source) {
    LifecycleStateImportDTO target = new LifecycleStateImportDTO();
    if (source == null) {
      return target;
    }
    BeanUtils.copyProperties(source, target);

    return target;
  }
}
