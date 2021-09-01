package org.demo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ListUtils {

  public static void main(String[] arg) {
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
    test(oldList, newList);
    List<Integer> different = getDifferent(oldList, newList);
    System.out.println(different);
    Collection same = getSame(oldList, newList);
    System.out.println(same);

    两个list集合的差集();
  }

  /**
   * 把一个List分割成多个小的List
   *
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

  /**
   * 找出两个集合中相同的元素
   *
   * @param collmax
   * @param collmin
   * @return
   */
  public static <T> List<T> getSame(List<T> collmax, List<T> collmin) {
    // 使用LinkedList防止差异过大时,元素拷贝
    List<T> csReturn = new LinkedList<>();
    List<T> max = collmax;
    List<T> min = collmin;
    // 先比较大小,这样会减少后续map的if判断次数
    if (collmax.size() < collmin.size()) {
      max = collmin;
      min = collmax;
    }
    // 直接指定大小,防止再散列
    Map<Object, Integer> map = new HashMap<Object, Integer>(max.size());
    for (Object object : max) {
      map.put(object, 1);
    }
    for (Object object : min) {
      if (map.get(object) != null) {
        csReturn.add((T) object);
      }
    }
    return csReturn;
  }

  /**
   * 获取两个List中不同的元素
   *
   * @param list1
   * @param list2
   * @param <T>
   * @return 不同的元素集合
   */
  public static <T> List<T> getDifferent(List<T> list1, List<T> list2) {
    Map<T, Integer> map = new HashMap<>(list1.size() + list2.size());
    List<T> diff = new ArrayList<>();
    List<T> maxList = list1;
    List<T> minList = list2;
    if (list2.size() > list1.size()) {
      maxList = list2;
      minList = list1;
    }
    for (T i : maxList) {
      map.put(i, 1);
    }
    for (T i : minList) {
      Integer difValue = map.get(i);
      if (difValue != null) {
        map.put(i, ++difValue);
        continue;
      }
      map.put(i, 1);
    }
    for (Map.Entry<T, Integer> entry : map.entrySet()) {
      if (entry.getValue() == 1) {
        diff.add(entry.getKey());
      }
    }
    return diff;
  }

  public static void test(List<Integer> oldList, List<Integer> newList) {

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

  /**
   * 两个list集合的差集
   *
   */
  public static void 两个list集合的差集() {
    List<String> list = new ArrayList<>(); // 作为总的list
    List<String> existList = new ArrayList<>(); // 存在的list

    list.add("oJkxxw8pYYKdC5HXtoiEImLNIqyk");
    list.add("oJkxxw6krKGhZIuYHV6rPp4uvLNw");
    list.add("oJkxxw9As9hHdLnfqRbfDHeF9WAU");
    list.add("oJkxxw1RNeDaodn6Qgz6FI4b5bKk");

    existList.add("oJkxxw1RNeDaodn6Qgz6FI4b5bKk");
    existList.add("oJkxxw9As9hHdLnfqRbfDHeF9WAU");

    list.removeAll(existList); // 将不存在的list，除掉存在的list，剩下的就是不存在的了

    System.out.println(existList);
    System.out.println(list);
  }
}
