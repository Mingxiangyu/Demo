package org.demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListUtils {

  public static void main(String[] arg) {
    test();
  }

  /**
   * 把一个List分割成多个小的List
   * @param list 被分割的List
   * @param subLength 分割后每个小List的长度
   * @param <T> List集合类型
   * @return List<List<T>>
   */
  public static <T> List<List<T>> groupList(List<T> list, int subLength) {
    List<List<T>> listGroup = new ArrayList<>();
    int size = list.size();
    for (int i = 0; i < size; i += subLength) {
      if (i + subLength > size) {
        subLength = size - i;
      }
      List<T> newList = list.subList(i, i + subLength);
      listGroup.add(newList);
    }
    return listGroup;
  }


  public static void test() {
    List<Integer> oldList =
        new ArrayList<Integer>() {
          {
            add(1);
            add(2);
            add(4);
            add(5);
          }
        };
    List<Integer> newList =
        new ArrayList<Integer>() {
          {
            add(3);
            add(4);
            add(5);
            add(6);
          }
        };
    Map<Integer, Integer> map = new HashMap<>();

    for (Integer i : oldList) {
      map.put(i, 0);
    }
    System.out.print(map);

    for (Integer j : newList) {

      // value为1 ，更新的数据
      if (map.containsKey(j)) {
        map.put(j, 1);
      } else {
        // value为2 ，新增的数据
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
