package com.iglens.时间;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 业务实现
 * @link {https://blog.csdn.net/xjx891111/article/details/120372157}
 * @date 2021-01-12 10:23:32
 */
@Slf4j
@Component
public class mongoTemplate实现按天统计数据 {

  @Autowired
  private MongoTemplate mongoTemplate;

  public HashMap<String, Object> selectMetadataCheckReportTable(String databaseGuid,
      Instant startDateTime, Instant endDateTime) {
    if (StringUtils.isBlank(databaseGuid)) {
      throw new RuntimeException("databaseGuid为空");
    }
    // 没传时间默认最近一周
    LocalDateTime endTime = LocalDateTime.now(ZoneOffset.UTC).with(LocalTime.MAX);
    LocalDateTime startTime = endTime.minusDays(6).with(LocalTime.MIN);
    //传时间参数的话，时分秒需要处理一下
    if (startDateTime != null && endDateTime != null) {
      startTime = LocalDateTime.ofInstant(startDateTime, ZoneOffset.UTC).with(LocalTime.MIN);
      endTime = LocalDateTime.ofInstant(endDateTime, ZoneId.systemDefault()).with(LocalTime.MAX);
    }
    //天数，分日统计需要用到
    int size = new Long(ChronoUnit.DAYS.between(startTime, endTime)).intValue() + 1;
    HashMap<String, Object> resultMap = new HashMap<>();
    //第一步，先取出每天中最大的taskId.(暂时按最大taskId为准)
    AggregationResults<HashMap> aggregate = getTaskIdListPerDayByCollectionName(databaseGuid,
        endTime, startTime,"collectionName");
    //第二步：将所有taskId放入list中
    List<HashMap> mr = aggregate.getMappedResults();
    List<String> taskIdList = mr.stream()
        .map(map -> (String) map.get("TASK_ID"))
        .collect(Collectors.toList());
    //第三步：再将list作为条件进行二次查询统计。
    AggregationResults<HashMap> aggregationResults = getStatisticsByTaskIdList4Table(taskIdList);
    List<HashMap> mappedResults = aggregationResults.getMappedResults();
    //4：将结果按天分组；
    Map<String, List<Object>> attrMap = getConvertData4Table(mappedResults, startTime, size);
    //5:统计总结果
    Map<String, String> totalMap = getStatisticNum4Table(taskIdList);
    resultMap.putAll(totalMap);
    resultMap.put("attrMap", attrMap);
    return resultMap;
  }

  private AggregationResults<HashMap> getStatisticsByTaskIdList4Table(List<String> taskIdList) {
    //封装条件
    List<AggregationOperation> operations = new ArrayList<>();
    operations.add(
        Aggregation.match(Criteria.where("TASK_ID").in(taskIdList)));
    //如果写了project，分组的数据要在project中出现，否则报错，将日期进行格式化并起别名
    operations.add(Aggregation
        .project("timeStamp", "metrics")
        .andExpression(
            "{$dateToString:{ format:'%Y-%m-%d',date: '$timestamp', timezone: 'Asia/Shanghai'}}")
        .as("$timestamp")
    );
    //分组统计，这里会按格式化后的日期进行分组；
    operations.add(Aggregation.group("timeStamp")
        .sum("metrics.tableCheck.attrCheck.noNameCheck").as("noNameCheck")
        .sum("metrics.tableCheck.attrCheck.subjectCheck").as("subjectCheck")
        .sum("metrics.tableCheck.attrCheck.dataLevelCheck").as("dataLevelCheck")
        .sum("metrics.tableCheck.attrCheck.lifeCycleCheck").as("lifeCycleCheck")
        .count().as("tableNum")
    );
    Aggregation aggregation = Aggregation.newAggregation(operations);
    AggregationResults<HashMap> aggregationResults = mongoTemplate.aggregate(aggregation,
        "collectionName2", HashMap.class);
    return aggregationResults;
  }

  private AggregationResults<HashMap> getTaskIdListPerDayByCollectionName(String databaseGuid,
      LocalDateTime endTime, LocalDateTime startTime, String collectionName) {

    List<AggregationOperation> operations = new ArrayList<>();
    operations.add(Aggregation.match(
        Criteria.where("databaseGuid").is(databaseGuid)));
    operations.add(Aggregation.match(
        Criteria.where("timeStamp").gte(startTime)));
    operations.add(
        Aggregation.match(Criteria.where("timeStamp").lte(endTime)));
    operations.add(Aggregation
        .project("TASK_ID",
            "timeStamp")
        .andExpression(
            "{$dateToString:{ format:'%Y-%m-%d',date: '$timestamp', timezone: 'Asia/Shanghai'}}")
        .as("$timestamp")
    );
    //按日期分组，获取当天最大的taskId。其实实际需求比这复杂，领导也觉得太难，就只让取了当天最大的一条任务id。
    operations.add(Aggregation.group("timeStamp")
        .max("TASK_ID").as("TASK_ID")

    );
    Aggregation aggregation = Aggregation.newAggregation(operations);
    return mongoTemplate.aggregate(aggregation, collectionName, HashMap.class);
  }


  private Map<String, String> getStatisticNum4Table(List<String> taskIdList) {
    // 封装查询条件
    List<AggregationOperation> operations = new ArrayList<>();
    operations.add(
        Aggregation.match(Criteria.where("TASK_ID").in(taskIdList)));
    operations.add(Aggregation.group("databaseGuid")
        .sum("metrics.tableCheck.attrCheck.noNameCheck").as("noNameCheck")
        .sum("metrics.tableCheck.attrCheck.subjectCheck").as("subjectCheck")
        .sum("metrics.tableCheck.attrCheck.dataLevelCheck").as("dataLevelCheck")
        .sum("metrics.tableCheck.attrCheck.lifeCycleCheck").as("lifeCycleCheck")
        .count().as("tableNum")
    );
    Aggregation aggregation = Aggregation.newAggregation(operations);
    AggregationResults<HashMap> ar = mongoTemplate.aggregate(aggregation,
        "collectionName2", HashMap.class);
    Map<String, String> result = getStatisticPercent4Table(ar.getUniqueMappedResult());
    return result;


  }

  private Map<String, String> getStatisticPercent4Table(HashMap map) {
    long tableNum = 0;
    long noNameCheck = 0;
    long subjectCheck = 0;
    long dataLevelCheck = 0;
    long lifeCycleCheck = 0;
    if (map != null && !map.isEmpty()) {
      tableNum = Long.parseLong(String.valueOf(map.get("tableNum")));
      noNameCheck = Long.parseLong(String.valueOf(map.get("noNameCheck")));
      subjectCheck = Long.parseLong(String.valueOf(map.get("subjectCheck")));
      dataLevelCheck = Long.parseLong(String.valueOf(map.get("dataLevelCheck")));
      lifeCycleCheck = Long.parseLong(String.valueOf(map.get("lifeCycleCheck")));
    }
    HashMap<String, String> result = new HashMap<>();
    result.put("noNameCheck", getAttrPercent(noNameCheck, tableNum));
    result.put("subjectCheck", getAttrPercent(subjectCheck, tableNum));
    result.put("dataLevelCheck", getAttrPercent(dataLevelCheck, tableNum));
    result.put("lifeCycleCheck", getAttrPercent(lifeCycleCheck, tableNum));

    return result;
  }

  private Map<String, List<Object>> getConvertData4Table(List<HashMap> mappedResults,
      LocalDateTime startTime, int size) {
    List<Object> days = getDays(startTime, size);
    Map<String, List<Object>> totalMap = new HashMap<>();
    //将数据转换成每日的数组形式；
    List<Object> noNameCheckTotals = getConvertDataByType(mappedResults, "noNameCheck", days);
    List<Object> subjectCheckTotals = getConvertDataByType(mappedResults, "subjectCheck", days);
    List<Object> dataLevelCheckTotals = getConvertDataByType(mappedResults, "dataLevelCheck", days);
    List<Object> lifeCycleCheckTotals = getConvertDataByType(mappedResults, "lifeCycleCheck", days);
    List<Object> tableNum = getConvertDataByType(mappedResults, "tableNum", days);

    totalMap.put("dates", days);
    totalMap.put("noNameCheckTotals", noNameCheckTotals);
    totalMap.put("subjectCheckTotals", subjectCheckTotals);
    totalMap.put("dataLevelCheckTotals", dataLevelCheckTotals);
    totalMap.put("lifeCycleCheckTotals", lifeCycleCheckTotals);
    totalMap.put("tableNum", tableNum);

    return totalMap;
  }


  //通过类型转换数据；
  public List<Object> getConvertDataByType(List<HashMap> mappedResults, String type,
      List<Object> days) {
    List<Map<String, Object>> results = new ArrayList<>();
    Map<String, Object> resultMap = new HashMap<>();
    mappedResults.forEach(m -> resultMap.put((String) m.get("_id"), m.get(type)));
    results.add(resultMap);
    List<Object> totals = new ArrayList<>();
    for (int i = 0; i < days.size(); i++) {
      String s = (String) days.get(i);
      results.forEach(d -> {
        Object total = d.get(s);
        if (total != null) {
          totals.add(total + "");
        } else {
          totals.add("0");
        }
      });
    }
    return totals;
  }

  //获取百分比，保留一位小数
  private String getAttrPercent(long v1, long v2) {
    String attrPercent = null;
    if (v2 != 0) {
      BigDecimal b1 = new BigDecimal(v1);
      BigDecimal b2 = new BigDecimal(v2);
      BigDecimal divide = b1.divide(b2, 3, RoundingMode.HALF_UP);
      NumberFormat percent = NumberFormat.getPercentInstance();
      percent.setMaximumFractionDigits(1);
      attrPercent = percent.format(divide.doubleValue());
    } else {
      attrPercent = "0";
    }
    return attrPercent;
  }

  /**
   * 获取日期
   */
  public List<Object> getDays(LocalDateTime beginTime, int size) {
    List<Object> days = new ArrayList<>();
    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    for (int i = 0; i < size; i++) {
      LocalDateTime date = beginTime.plusDays(i);
      String dateStr = date.format(fmt);
      days.add(dateStr);
    }
    return days;
  }
}





