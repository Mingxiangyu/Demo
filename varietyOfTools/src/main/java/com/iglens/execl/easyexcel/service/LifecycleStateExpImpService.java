package com.iglens.execl.easyexcel.service;

import com.iglens.execl.easyexcel.dto.LifecycleStateDTO;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

/** @author mxy */
public interface LifecycleStateExpImpService {

  /**
   * 导入生命周期状态
   *
   * @param forms 导入表单
   * @date 17 :57 2019/10/15
   */
  void importLifecycleState( List<LifecycleStateDTO> forms);

  /**
   * 下载生命周期状态空模板
   *
   * @param response 响应
   * @date 17 :46 2019/10/15
   */
  void downloadExcel(HttpServletResponse response);

  /**
   * 导出生命周期状态
   *
   * @param ids 生命周期状态集合
   * @param response 响应
   * @author mxy
   * @date 9 :40 2019/10/22
   */
  void exportLifecycleState(List<String> ids, HttpServletResponse response);
}
