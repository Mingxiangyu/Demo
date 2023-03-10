package com.iglens.list;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author xming
 * @link https://blog.csdn.net/m0_47959499/article/details/125864707
 */
public class 对指定字段进行排序操作 {
  public static void main(String[] args) {
    List<Student> studentList = new ArrayList<>();
    Student student = new Student(1, 11, "张三");
    Student student1 = new Student(2, 10, "李四");
    Student student2 = new Student(2, 9, "王五");
    Student student3 = new Student(2, 13, "赵六");
    studentList.add(student);
    studentList.add(student1);
    studentList.add(student2);
    studentList.add(student3);
    System.out.println("初始" + studentList.toString());

    // 升序 第一种方法
    studentList.sort(Comparator.comparing(Student::getAge));
    System.out.println("升序 第一种方法" + studentList.toString());

    // 降序 第一种方法
    studentList.sort(Comparator.comparing(Student::getAge).reversed());
    System.out.println("降序 第一种方法" + studentList.toString());

    studentList.sort(
        new Comparator<Student>() {
          @Override
          public int compare(Student o1, Student o2) {
            return o1.getAge() - o2.getAge();
          }
        });
    System.out.println("升序 第二种方法" + studentList.toString());

    studentList.sort(
        new Comparator<Student>() {
          @Override
          public int compare(Student o1, Student o2) {
            return o2.getAge() - o1.getAge();
          }
        });
    System.out.println("降序 第二种方法" + studentList.toString());
  }

  // 描述：排序如果使用Collections.sort(集合);的话则需要在对应的实体类实现Comparable类，然后重写Comparable方法。
  // 此处我第一种方法，我是使用集合的sort方法，然后使用comparing（），再填入需要排序的字段，比重写方法的实现更灵活。使用sort为升序，如果想降序，则使用reversed()方法；.
  // 第二种方法是在sort中实现Comparator的方法，然后在return处get需要进行排序的字段 ，此处需要着重说 public int compare(Student o1,
  // Student o2) {return o2.getAge() - o1.getAge();}这个方法，return处比较运算这块，会返回三种int类型的数值：负整数，零，正整数。
  //     return o2.getAge() - o1.getAge()为负整数时，o2位置排在前面，当为0时，位置不变，当为正整数时，o2位置排在后面
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class Student {

  private Integer id;
  private Integer age;
  private String name;
}
