package org.demo.easyexcel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 监听，解析excel返回模型
 *
 * @author tzhang
 */
public class ModelExcelListener extends AnalysisEventListener {

  /** 整个excel表的数据，key是当前的sheet号，value是该sheet的数据 */
  private Map<Integer, List<Object>> result = new HashMap<>();
  /** 临时存储一个sheet页的数据 */
  private List<Object> datas = new ArrayList<>();
  /** 当前sheet号 */
  private int sheetNo = 1;

  /**
   * 功能描述: 每解析一行都会调用该方法
   *
   * @param object
   * @param context
   * @return void
   * @author tzhang
   * @date 2020/6/9 18:01
   */
  @Override
  public void invoke(Object object, AnalysisContext context) {
    int currentSheetNo = context.getCurrentSheet().getSheetNo();
    if (currentSheetNo != sheetNo) {
      sheetNo = currentSheetNo;
      datas = new ArrayList<>();
    }
    datas.add(object);
    result.put(sheetNo, datas);
  }

  @Override
  public void doAfterAllAnalysed(AnalysisContext context) {}

  /**
   * 返回数据
   *
   * @return 返回读取的数据集合
   */
  public Map<Integer, List<Object>> getResult() {
    return result;
  }
}
