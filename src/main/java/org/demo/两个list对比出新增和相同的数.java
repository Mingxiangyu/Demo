package org.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class 两个list对比出新增和相同的数 {


  public static void main(String[] arg) {
    test();
  }

  public static void test() {
    List<Integer> oldList = new ArrayList<Integer>() {{
      add(1);
      add(2);
      add(4);
      add(5);
    }};
    List<Integer> newList = new ArrayList<Integer>() {{
      add(3);
      add(4);
      add(5);
      add(6);
    }};
    Map<Integer, Integer> map = new HashMap<>();

    for (Integer i : oldList) {
      map.put(i, 0);
    }
    System.out.print(map);

    for (Integer j : newList) {

      //value为1 ，更新的数据
      if (map.containsKey(j)) {
        map.put(j, 1);
      } else {
        //value为2 ，新增的数据
        map.put(j, 2);
      }
    }
    System.out.println(map);
    for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
      if (entry.getValue().equals(0)) {
        System.out.println("旧的值：" + entry.getKey());
      }
      if (entry.getValue().equals(1)) {
        System.out.println("更新的值：" + entry.getKey());
      }
      if (entry.getValue().equals(3)) {
        System.out.println("新增的值：" + entry.getKey());
      }
    }

    System.out.println(map);
  }
}
