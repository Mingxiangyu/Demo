package org.demo.easyexcel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 监听，解析excel返回集合
 *
 * @author tzhang
 */
public class EasyExcelListener extends AnalysisEventListener {

  /** 整个excel表的数据，key是当前的sheet号，value是该sheet的数据 */
  private Map<Integer, List<List<String>>> result = new HashMap<>();
  /** 临时存储一个sheet页的数据 */
  private List<List<String>> datas = new ArrayList<>();
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
    Map<String, String> stringMap = (HashMap<String, String>) object;
    // 数据存储到list，供批量处理，或后续自己业务逻辑处理。
    datas.add(new ArrayList<>(stringMap.values()));
    result.put(sheetNo, datas);
  }

  @Override
  public void doAfterAllAnalysed(AnalysisContext context) {}

  /**
   * 返回数据
   *
   * @return 返回读取的数据集合
   */
  public Map<Integer, List<List<String>>> getResult() {
    return result;
  }
}
