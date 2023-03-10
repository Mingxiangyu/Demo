package com.iglens.时间;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class 时间排序 {

  public static void main(String[] args) {
    List<JSONObject> list = new ArrayList<>();
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("id", 1);
    jsonObject.put("sjsj",new Date());
    list.add(jsonObject);
    JSONObject jsonObject1 = new JSONObject();
    jsonObject1.put("id", 2);
    jsonObject1.put("sjsj",new Date(111111));
    list.add(jsonObject1);

    // 时间升序
    List<JSONObject> listDemo = list.stream().sorted(Comparator.comparing(a->a.get("sjsj").toString())).collect(Collectors.toList());
    System.out.println(listDemo);
  }
}
