package com.iglens;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class Consumer函数 {

  public static void main(String[] args) {
    Student student1 = new Student("Ashok","Kumar", 9.5);

    student1 = updateStudentFee(student1,
        //Lambda expression for Predicate interface
        student -> student.grade > 8.5, //添加过滤条件
        //Lambda expression for Consumer inerface
        student -> student.feeDiscount = 30.0);
    student1.printFee(); //The fee after discount: 1400.0

    Student student2 = new Student("Rajat","Verma", 8.0);
    student2 = updateStudentFee(student2,
        //Lambda expression for Predicate interface
        student -> student.grade >= 10,
        //Lambda expression for Consumer inerface
        student -> student.feeDiscount = 20.0);
    student2.printFee();//The fee after discount: 1600.0

  }


  public static Student updateStudentFee(Student student, Predicate<Student> predicate, Consumer<Student> consumer){
    if (predicate.test(student)){ //调用过滤方法
      consumer.accept(student); // 调用方法
    }
    return student;
  }
}

 class Student {

  String firstName;

  String lastName;

  Double grade;

  Double feeDiscount = 0.0;

  Double baseFee = 2000.0;

  public Student(String firstName, String lastName, Double grade) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.grade = grade;
  }

  public void printFee(){
    Double newFee = baseFee - ((baseFee * feeDiscount)/100);
    System.out.println("The fee after discount: " + newFee);
  }
}