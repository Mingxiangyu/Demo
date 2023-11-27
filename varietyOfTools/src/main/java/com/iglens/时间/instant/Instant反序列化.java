package com.iglens.时间.instant;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.ParserConfig;
import java.time.Instant;

public class Instant反序列化 {
  static {
    // 解决 serializer error/deserializer error 报错
    ParserConfig.getGlobalInstance().setAsmEnable(false);
  }
  public static void main(String[] args) {
    String jsonString =
        "{\n"
            + "\t\t\t\"business\": \"41\",\n"
            + "\t\t\t\"cleanStatus\": false,\n"
            + "\t\t\t\"continent\": \"\",\n"
            + "\t\t\t\"coordinateSystem\": \"WGS84\",\n"
            + "\t\t\t\"country\": \"ABW\",\n"
            + "\t\t\t\"createStamp\": {\n"
            + "\t\t\t\t\"epochSecond\": 1699351945,\n"
            + "\t\t\t\t\"nano\": 0\n"
            + "\t\t\t},\n"
            + "\t\t\t\"credible\": 3,\n"
            + "\t\t\t\"dataFormat\": \"json\",\n"
            + "\t\t\t\"dataName\": \"#gadm41_ABW_0.json\",\n"
            + "\t\t\t\"dataSize\": \"2495\",\n"
            + "\t\t\t\"dataSource\": \"https://gadm.org/\",\n"
            + "\t\t\t\"dataStoragePath\": \"GADM//普通任务/1384//ABW/#gadm41_ABW_0.json\",\n"
            + "\t\t\t\"dataType\": \"gadm\",\n"
            + "\t\t\t\"id\": 1384,\n"
            + "\t\t\t\"isPublishFlag\": false,\n"
            + "\t\t\t\"modifyStamp\": {\n"
            + "\t\t\t\t\"epochSecond\": 1699519388,\n"
            + "\t\t\t\t\"nano\": 549000000\n"
            + "\t\t\t},\n"
            + "\t\t\t\"name\": \"阿鲁巴2_全球行政区划\",\n"
            + "\t\t\t\"projection\": \"墨卡托\",\n"
            + "\t\t\t\"releaseTime\": {\n"
            + "\t\t\t\t\"epochSecond\": 1699351945,\n"
            + "\t\t\t\t\"nano\": 0\n"
            + "\t\t\t},\n"
            + "\t\t\t\"sourceId\": \"10\",\n"
            + "\t\t\t\"typeFirst\": \"地学与环境\",\n"
            + "\t\t\t\"typeSecond\": \"全球行政区划数据\",\n"
            + "\t\t\t\"uuid\": \"93381716-329d-4884-b875-9e638c7f59ec\",\n"
            + "\t\t\t\"version\": 2,\n"
            + "\t\t\t\"versionNum\": \"1.0\",\n"
            + "\t\t\t\"year\": \"\"\n"
            + "\t\t}";
    JSONObject metadataObj = JSONObject.parseObject(jsonString);
    // 提前取出无法序列化字段
    JSONObject createStampJson = metadataObj.getJSONObject("createStamp");
    JSONObject modifyStampJson = metadataObj.getJSONObject("modifyStamp");
    JSONObject releaseTimeJson = metadataObj.getJSONObject("releaseTime");

    // 将该字段置为空避免序列化报错
    metadataObj.put("createStamp", null);
    metadataObj.put("modifyStamp", null);
    metadataObj.put("releaseTime", null);
    MetaData metadataEntity = JSONObject.toJavaObject(metadataObj, MetaData.class);

    // 手动转换为正常字段并set回元数据
    metadataEntity.setCreateStamp(deserialization(createStampJson));
    metadataEntity.setModifyStamp(deserialization(modifyStampJson));
    metadataEntity.setReleaseTime(deserialization(releaseTimeJson));
    System.out.println(metadataEntity);
    //
  }

  private static Instant deserialization(JSONObject createStampJson) {
    Long epochSecond = createStampJson.getLong("epochSecond");
    Long nano = createStampJson.getLong("nano");
    return Instant.ofEpochSecond(epochSecond, nano);
  }
}





