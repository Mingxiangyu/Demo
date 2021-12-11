package com.iglens.爬虫.图片;

import com.alibaba.fastjson.JSONObject;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple PageProcessor.
 *
 * @author code4crafter@gmail.com <br>
 * @since 0.1.0
 */
public class SougouImgProcessor {

  private String url;
  private SougouImgPipeline pipeline;
  private List<JSONObject> dataList;
  private List<String> urlList;
  private String word;

  public SougouImgProcessor(String url, String word) {
    this.url = url;
    this.word = word;
    this.pipeline = new SougouImgPipeline();
    this.dataList = new ArrayList<>();
    this.urlList = new ArrayList<>();
  }


  public static void main(String[] args) {
    String url = "https://pic.sogou.com/napi/pc/searchList?mode=1&start=%s&xml_len=%s&query=%s";
    SougouImgProcessor processor = new SougouImgProcessor(url, "美女");

    int start = 0, size = 50, limit = 1000; // 定义爬取开始索引、每次爬取数量、总共爬取数量

    for (int i = start; i < start + limit; i += size) {
      processor.process(i, size);
    }

    processor.pipelineData();
  }

  public void process(int idx, int size) {
    String res = HttpClientUtils.get(String.format(this.url, idx, size, this.word));
    JSONObject object = JSONObject.parseObject(res);
    List<JSONObject> items = (List<JSONObject>) ((JSONObject) object.get("data")).get("items");
    for (JSONObject item : items) {
      this.urlList.add(item.getString("picUrl"));
    }
    this.dataList.addAll(items);
  }

  // 下载
  public void pipelineData() {
    //        pipeline.process(this.urlList, word);   // 单线程
    pipeline.processSync(this.urlList, this.word); // 多线程
  }
}
