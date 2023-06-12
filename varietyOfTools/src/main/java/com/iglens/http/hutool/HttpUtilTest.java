package com.iglens.http.hutool;

import com.alibaba.fastjson.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class HttpUtilTest {

  public static void main(String[] args) {
    Map<String, Object> map = new HashMap<>();
    map.put("type", "0");
    map.put("label", "基础地理");
    map.put("title", "采集任务异常");
    map.put("context", "任务因为⽹络中断原因，不能正常运⾏");
    JSONObject json = new JSONObject(map);
    HttpUtils.sendPost("http://36.41.73.198:18080/admin/message/send", json + "");
  }
}
