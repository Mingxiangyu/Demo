package com.iglens.list;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;

/** https://blog.51cto.com/u_16175485/7791075 */
public class 对象集合去重 {
  public static void main(String[] args) {
    字符串去重();
    hashMap对象去重();

    stream对象去重();
  }

  private static void stream对象去重() {
    List<Person> list = new ArrayList<>();
    Person person1 = new Person("1", "Alice");
    Person person2 = new Person("2", "Bob");
    Person person3 = new Person("1", "Alice"); // 与person1的id相同，应该被去重
    list.add(person1);
    list.add(person2);
    list.add(person3);

    List<Person> result = removeDuplicates(list, Person::getId);
    System.out.println(result); // 输出：[Person(id=1, name=Alice), Person(id=2, name=Bob)]
  }

  private static void hashMap对象去重() {
    List<Person> list = new ArrayList<>();
    Person person1 = new Person("1", "Alice");
    Person person2 = new Person("2", "Bob");
    Person person3 = new Person("1", "Alice"); // 与person1的id相同，应该被去重
    list.add(person1);
    list.add(person2);
    list.add(person3);

    Map<String, Person> map = new HashMap<>();
    List<Person> result = removeDuplicates(list, map, Person::getId);
    System.out.println(result); // 输出：[Person(id=1, name=Alice), Person(id=2, name=Bob)]
  }

  private static void 字符串去重() {
    List<String> list = new ArrayList<>();
    list.add("apple");
    list.add("orange");
    list.add("banana");
    list.add("apple");
    list.add("orange");

    List<String> result = removeDuplicates(list);
    System.out.println(result); // 输出：[apple, orange, banana]
  }

  /**
   * 使用Set集合去重
   *
   * @param list
   * @param <T>
   * @return
   */
  public static <T> List<T> removeDuplicates(List<T> list) {
    Set<T> set = new HashSet<>(list);
    list.clear();
    list.addAll(set);
    return list;
  }
  /**
   * 使用HashMap去重
   *
   * @param list
   * @param map
   * @param keyExtractor
   * @param <T>
   * @param <K>
   * @return
   */
  public static <T, K> List<T> removeDuplicates(
      List<T> list, Map<K, T> map, Function<T, K> keyExtractor) {
    for (T item : list) {
      K key = keyExtractor.apply(item);
      map.put(key, item);
    }
    return new ArrayList<>(map.values());
  }

  /**
   * 使用Java 8的Stream API去重
   *
   * @param list
   * @param keyExtractor
   * @param <T>
   * @param <K>
   * @return
   */
  public static <T, K> List<T> removeDuplicates(List<T> list, Function<T, K> keyExtractor) {
    return list.stream()
        .collect(Collectors.toMap(keyExtractor, Function.identity(), (a, b) -> a))
        .values()
        .stream()
        .collect(Collectors.toList());
  }
}

@Data
@AllArgsConstructor
class Person {
  private String id;
  private String name;

  // getter and setter
}
