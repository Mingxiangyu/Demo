package com.iglens.csv;

import cn.hutool.core.annotation.Alias;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.text.csv.CsvData;
import cn.hutool.core.text.csv.CsvReader;
import cn.hutool.core.text.csv.CsvRow;
import cn.hutool.core.text.csv.CsvUtil;
import java.util.List;
import lombok.Data;

/**
 * @author xming
 * @link 原文链接：https://blog.csdn.net/pan_jiabao/article/details/121743906
 */
public class 读取带逗号_换行的CSV {
  // 测试数据：
  // 标题1,标题2,标题3,标题4
  // 内容1,嘻嘻嘻嘻嘻嘻擦擦擦擦擦擦,tyyyyyy,tttt
  // 得得，得的,"芙蓉峰润肤,
  // 乳芙蓉峰,润肤乳",,feffrfrfr

  public static void main(String[] args) {
    // 使用hutool工具包实现
    CsvReader reader = CsvUtil.getReader();
    // 从文件中读取CSV数据
    // 读取为CsvRow
    CsvData data = reader.read(FileUtil.file("/xxx/工作簿1.csv"));

    // 读取为bean
    final List<TestBean> result =
        reader.read(ResourceUtil.getUtf8Reader("/xxx/工作簿1.csv"), TestBean.class);
    System.out.println(result);

    List<CsvRow> rows = data.getRows();
    int i = 0;
    // 遍历行
    for (CsvRow csvRow : rows) {
      i++;
      System.out.print("第" + i + "行   ");
      // getRawList返回一个List列表，列表的每一项为CSV中的一个单元格（既逗号分隔部分）
      System.out.print(csvRow.get(0) + "   ");
      System.out.print(csvRow.get(1) + "   ");
      System.out.print(csvRow.get(2) + "   ");
      System.out.println(csvRow.get(3));
    }
  }

  @Data
  static class TestBean {

    @Alias("标题1")
    private String title1;

    @Alias("标题2")
    private String title2;

    @Alias("标题3")
    private String title3;

    @Alias("标题4")
    private String title4;
  }
}
