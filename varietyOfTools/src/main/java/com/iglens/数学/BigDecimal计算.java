package com.iglens.数学;

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
    System.out.println(
        "加法用value结果："
            + result1); // 加法用value结果：1000000.005000000000000000104083408558608425664715468883514404296875
    BigDecimal result12 = num12.add(num22);
    System.out.println("加法用string结果：" + result12); // 加法用string结果：1000000.005
    // 减法
    BigDecimal result2 = num1.subtract(num2);
    System.out.println(
        "减法value结果："
            + result2); // 减法value结果：-999999.994999999999999999895916591441391574335284531116485595703125
    BigDecimal result22 = num12.subtract(num22);
    System.out.println("减法用string结果：" + result22); // 减法用string结果：-999999.995
    // 乘法
    BigDecimal result3 = num1.multiply(num2);
    System.out.println(
        "乘法用value结果："
            + result3); // 乘法用value结果：5000.000000000000104083408558608425664715468883514404296875000000
    BigDecimal result32 = num12.multiply(num22);
    System.out.println("乘法用string结果：" + result32); // 乘法用string结果：5000.000
    // 绝对值
    BigDecimal result4 = num3.abs();
    System.out.println("绝对值用value结果：" + result4); // 绝对值用value结果：1000000
    BigDecimal result42 = num32.abs();
    System.out.println("绝对值用string结果：" + result42); // 绝对值用string结果：1000000
    // 除法
    // 原来是在做除法的时候出现了无限不循环小数如：0.333333333333
    // 解决方案
    // 在做做除法的时候指定保留的小数的位数:
    BigDecimal result5 = num2.divide(num1, 20, BigDecimal.ROUND_HALF_UP);
    System.out.println("除法用value结果：" + result5); // 除法用value结果：199999999.99999999583666365766
    BigDecimal result52 = num22.divide(num12, 20, BigDecimal.ROUND_HALF_UP);
    System.out.println("除法用string结果：" + result52); // 除法用string结果：200000000.00000000000000000000

    System.out.println("\n============================================================\n");

    //     ROUND_DOWN
    BigDecimal b = new BigDecimal("2.225667").setScale(2, BigDecimal.ROUND_DOWN);
    System.out.println("直接去掉多余的位数: " + b); // 2.22 直接去掉多余的位数

    //    ROUND_UP
    BigDecimal c = new BigDecimal("2.224667").setScale(2, BigDecimal.ROUND_UP);
    System.out.println("跟上面相反，进位处理: " + c); // 2.23 跟上面相反，进位处理

    //    3. ROUND_CEILING
    //    天花板（向上），正数进位向上，负数舍位向上
    BigDecimal f = new BigDecimal("2.224667").setScale(2, BigDecimal.ROUND_CEILING);
    System.out.println("如果是正数，相当于BigDecimal.ROUND_UP: " +f); // 2.23 如果是正数，相当于BigDecimal.ROUND_UP

    BigDecimal g = new BigDecimal("-2.225667").setScale(2, BigDecimal.ROUND_CEILING);
    System.out.println("如果是负数，相当于BigDecimal.ROUND_DOWN: " +g); // -2.22 如果是负数，相当于BigDecimal.ROUND_DOWN

    //    4. ROUND_FLOOR
    //    地板（向下），正数舍位向下，负数进位向下
    BigDecimal h = new BigDecimal("2.225667").setScale(2, BigDecimal.ROUND_FLOOR);
    System.out.println(h); // 2.22 如果是正数，相当于BigDecimal.ROUND_DOWN

    BigDecimal i = new BigDecimal("-2.224667").setScale(2, BigDecimal.ROUND_FLOOR);
    System.out.println(i); // -2.23 如果是负数，相当于BigDecimal.ROUND_HALF_UP

    //    5. ROUND_HALF_UP
    BigDecimal d = new BigDecimal("2.225").setScale(2, BigDecimal.ROUND_HALF_UP);
    System.out.println("ROUND_HALF_UP" + d); // 2.23 四舍五入（若舍弃部分>=.5，就进位）

    //    6. ROUND_HALF_DOWN
    BigDecimal e = new BigDecimal("2.225").setScale(2, BigDecimal.ROUND_HALF_DOWN);
    System.out.println("ROUND_HALF_DOWN" + e); // 2.22 四舍五入（若舍弃部分>.5,就进位）

    //    7. ROUND_HALF_EVEN
    BigDecimal j = new BigDecimal("2.225").setScale(2, BigDecimal.ROUND_HALF_EVEN);
    System.out.println(j); // 2.22 如果舍弃部分左边的数字为偶数，则作 ROUND_HALF_DOWN

    BigDecimal k = new BigDecimal("2.215").setScale(2, BigDecimal.ROUND_HALF_EVEN);
    System.out.println(k); // 2.22 如果舍弃部分左边的数字为奇数，则作 ROUND_HALF_UP
    System.out.println("************************************");
    System.out.println(
        "4.05: "
            + new BigDecimal("4.05").setScale(1, BigDecimal.ROUND_HALF_EVEN)); // 4.05: 4.0 down
    System.out.println(
        "4.15: " + new BigDecimal("4.15").setScale(1, BigDecimal.ROUND_HALF_EVEN)); // 4.15: 4.2 up
    System.out.println(
        "4.25: "
            + new BigDecimal("4.25").setScale(1, BigDecimal.ROUND_HALF_EVEN)); // 4.25: 4.2 down
    System.out.println(
        "4.35: " + new BigDecimal("4.35").setScale(1, BigDecimal.ROUND_HALF_EVEN)); // 4.35: 4.4 up
    System.out.println(
        "4.45: "
            + new BigDecimal("4.45").setScale(1, BigDecimal.ROUND_HALF_EVEN)); // 4.45: 4.4 down
    System.out.println(
        "4.55: " + new BigDecimal("4.55").setScale(1, BigDecimal.ROUND_HALF_EVEN)); // 4.55: 4.6 up
    System.out.println(
        "4.65: "
            + new BigDecimal("4.65").setScale(1, BigDecimal.ROUND_HALF_EVEN)); // 4.65: 4.6 down

    System.out.println(
        "3.05: "
            + new BigDecimal("3.05").setScale(1, BigDecimal.ROUND_HALF_EVEN)); // 3.05: 3.0 down
    System.out.println(
        "3.15: " + new BigDecimal("3.15").setScale(1, BigDecimal.ROUND_HALF_EVEN)); // 3.15: 3.2 up
    System.out.println(
        "3.25: "
            + new BigDecimal("3.25").setScale(1, BigDecimal.ROUND_HALF_EVEN)); // 3.25: 3.2 down
    System.out.println(
        "3.35: " + new BigDecimal("3.35").setScale(1, BigDecimal.ROUND_HALF_EVEN)); // 3.35: 3.4 up
    System.out.println(
        "3.45: "
            + new BigDecimal("3.45").setScale(1, BigDecimal.ROUND_HALF_EVEN)); // 3.45: 3.4 down
    System.out.println(
        "3.55: " + new BigDecimal("3.55").setScale(1, BigDecimal.ROUND_HALF_EVEN)); // 3.55: 3.6 up
    System.out.println(
        "3.65: "
            + new BigDecimal("3.65").setScale(1, BigDecimal.ROUND_HALF_EVEN)); // 3.65: 3.6 down

    //    8.ROUND_UNNECESSARY
    BigDecimal l = new BigDecimal("2.215").setScale(3, BigDecimal.ROUND_UNNECESSARY);
    System.out.println(l);
    // 断言请求的操作具有精确的结果，因此不需要舍入。
    // 如果对获得精确结果的操作指定此舍入模式，则抛出ArithmeticException。
  }
}
