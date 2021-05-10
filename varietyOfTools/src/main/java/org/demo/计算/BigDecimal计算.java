package org.demo.计算;

import java.math.BigDecimal;

public class BigDecimal计算 {

  public static void main(String[] args) {

    BigDecimal num1 = new BigDecimal(0.005);
    BigDecimal num2 = new BigDecimal(1000000);
    BigDecimal num3 = new BigDecimal(-1000000);
    // 尽量用字符串的形式初始化
    BigDecimal num12 = new BigDecimal("0.005");
    BigDecimal num22 = new BigDecimal("1000000");
    BigDecimal num32 = new BigDecimal("-1000000");

    // 加法
    BigDecimal result1 = num1.add(num2);
    System.out.println("加法用value结果：" + result1);//加法用value结果：1000000.005000000000000000104083408558608425664715468883514404296875
    BigDecimal result12 = num12.add(num22);
    System.out.println("加法用string结果：" + result12);//加法用string结果：1000000.005
    // 减法
    BigDecimal result2 = num1.subtract(num2);
    System.out.println("减法value结果：" + result2);//减法value结果：-999999.994999999999999999895916591441391574335284531116485595703125
    BigDecimal result22 = num12.subtract(num22);
    System.out.println("减法用string结果：" + result22);//减法用string结果：-999999.995
    // 乘法
    BigDecimal result3 = num1.multiply(num2);
    System.out.println("乘法用value结果：" + result3);//乘法用value结果：5000.000000000000104083408558608425664715468883514404296875000000
    BigDecimal result32 = num12.multiply(num22);
    System.out.println("乘法用string结果：" + result32);//乘法用string结果：5000.000
    // 绝对值
    BigDecimal result4 = num3.abs();
    System.out.println("绝对值用value结果：" + result4);//绝对值用value结果：1000000
    BigDecimal result42 = num32.abs();
    System.out.println("绝对值用string结果：" + result42);//绝对值用string结果：1000000
    // 除法
    // 原来是在做除法的时候出现了无限不循环小数如：0.333333333333
    // 解决方案
    // 在做做除法的时候指定保留的小数的位数:
    BigDecimal result5 = num2.divide(num1, 20, BigDecimal.ROUND_HALF_UP);
    System.out.println("除法用value结果：" + result5);//除法用value结果：199999999.99999999583666365766
    BigDecimal result52 = num22.divide(num12, 20, BigDecimal.ROUND_HALF_UP);
    System.out.println("除法用string结果：" + result52);//除法用string结果：200000000.00000000000000000000
  }
}
