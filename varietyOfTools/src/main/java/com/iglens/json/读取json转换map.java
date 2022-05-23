package com.iglens.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class 读取json转换map {

  public static void main(String[] args) {
    String payload =
        "{\"bulletNumber\":\"DF-TJ-002\",\"imageBefore\":\"E:\\\\Deploy-DJ\\\\数据\\\\图片拼接\\\\天津爆炸6.tif\",\"imageAfter\":\"E:\\\\Deploy-DJ\\\\数据\\\\图片拼接\\\\天津爆炸5.tif\",\"missileBatch\":\"DF-TJ\",\"dataType\":\"SpaceReconnaissance\",\"reconnaissanceTimeAfter\":\"2020-08-05 10:29:41\",\"informationSources\":\"301基地\",\"detectionMethod\":\"天基卫星\",\"reconnaissanceTimeBefore\":\"2020-08-05 10:29:41\"}\n";
    Map<String, Object> map = getMap(payload);
    System.out.println(map);
  }

  /**
   * 读取payload获取map
   *
   * @param payload json
   * @return map
   */
  public static Map<String, Object> getMap(String payload) {
    Map<String, Object> map = new HashMap<>(16);
    try {
      ObjectMapper mapper = new ObjectMapper();
      map = mapper.readValue(payload, new TypeReference<Map<String, Object>>() {});
    } catch (IOException e) {
      e.printStackTrace();
    }
    return map;
  }
}
