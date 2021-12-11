package com.iglens.对象;

import java.lang.reflect.Field;

public class 判断对象所有属性是否均为null {

  /**
   * 判断对象所有属性是否均为null
   *
   * @param object 对象
   * @return 所有属性是否均为null
   */
  public static boolean objCheckIsNull(Object object) {
    // 得到类对象
    Class<?> clazz = object.getClass();
    // 得到所有属性
    Field[] fields = clazz.getDeclaredFields();
    // 定义返回结果，默认为true
    boolean flag = true;
    for (Field field : fields) {
      field.setAccessible(true);
      Object fieldValue = null;
      try {
        // 得到属性值
        fieldValue = field.get(object);
      } catch (IllegalArgumentException | IllegalAccessException e) {
        e.printStackTrace();
      }
      // 只要有一个属性值不为null 就返回false 表示对象不为null
      if (fieldValue != null) {
        return false;
      }
    }
    return flag;
  }
}
