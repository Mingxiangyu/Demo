package org.demo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.Data;

/** @author T480S */
public class 数据上移下移 {
  public static void main(String[] args) {
    List<userTest> list = new ArrayList<>();
    userTest u1 = new userTest();
    u1.setId(1);
    u1.setName("a");
    u1.setSort(1);
    userTest u2 = new userTest();
    u2.setId(2);
    u2.setName("b");
    u2.setSort(2);
    userTest u3 = new userTest();
    u3.setId(3);
    u3.setName("c");
    u3.setSort(3);
    userTest u4 = new userTest();
    u4.setId(4);
    u4.setName("d");
    u4.setSort(4);
    userTest u5 = new userTest();
    u5.setId(5);
    u5.setName("e");
    u5.setSort(5);
    userTest u6 = new userTest();
    u6.setId(6);
    u6.setName("f");
    u6.setSort(6);
    userTest u7 = new userTest();
    u7.setId(7);
    u7.setName("g");
    u7.setSort(7);

    list.add(u1);
    list.add(u2);
    list.add(u3);
    list.add(u4);
    list.add(u5);
    list.add(u6);
    list.add(u7);

    for (userTest e : list) {
      System.out.println("第一次：" + e.getId() + " " + e.getName() + " " + e.getSort() + " ");
      //
    }

    // 1、交换list的0号数据和6号数据
    swap2(list, 4, 0);
    for (userTest e : list) {
      System.out.println(e.getId() + " " + e.getName() + " " + e.getSort() + " ");
    }
    System.out.println("-------------");
    // 2、依次交换sort数据 TODO 好像有问题，最后一个不对
    for (int i = 0; i < list.size() - 1; i++) {
      int sort = list.get(i + 1).getSort();
      list.get(i + 1).setSort(list.get(i).getSort());
      list.get(i).setSort(sort);
    }
    for (userTest e : list) {
      System.out.println(e.getId() + " " + e.getName() + " " + e.getSort() + " ");
    }
  }

  /**
   * 交换位置
   *
   * @param list 集合
   * @param oldPosition 旧位置
   * @param newPosition 新位置
   */
  public static <T> void swap2(List<T> list, int oldPosition, int newPosition) {
    if (null == list) {
      throw new IllegalStateException("The list can not be empty...");
    }
    if (oldPosition > newPosition) {
      for (int i = oldPosition; i > newPosition; i--) {
        Collections.swap(list, i, i - 1);
      }
    }
  }
}

@Data
class userTest {
  int id;
  String name;
  int sort;
}
