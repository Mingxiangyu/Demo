package com.iglens.数学;


import java.util.Scanner;

public class 最小公倍数 {

    // 最大公约数
    public static int get_gcd(int a, int b) {
      int max, min;
      max = (a > b) ? a : b;
      min = (a < b) ? a : b;

      if (max % min != 0) {
        return get_gcd(min, max % min);
      } else {
        return min;
      }

    }

    // 最小公倍数
    public static int get_lcm(int a, int b) {
      return a * b / get_gcd(a, b);
    }

    public static void main(String[] args) {
      Scanner input = new Scanner(System.in);
      int n1 = input.nextInt();
      int n2 = input.nextInt();
      System.out.println("(" + n1 + "," + n2 + ")" + "=" + get_gcd(n1, n2));
      System.out.println("[" + n1 + "," + n2 + "]" + "=" + get_lcm(n1, n2));

    }


}
