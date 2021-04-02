package org.demo.系统信息;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
public class 获取内存和CPU信息 {
  public static void main(String[] args) {
    printUsage();
  }

  private static void printUsage() {
    OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
    for (Method method : operatingSystemMXBean.getClass().getDeclaredMethods()) {
      method.setAccessible(true);
      if (method.getName().startsWith("get")
          && Modifier.isPublic(method.getModifiers())) {
        Object value;
        try {
          value = method.invoke(operatingSystemMXBean);
        } catch (Exception e) {
          value = e;
        } // try
        System.out.println(method.getName() + " = " + value);
      } // if
    } // for
  }
}
