package com.iglens;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.iglens.定时任务.SysJobStatus;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

public class 枚举转换为list {
  private static String ENUM_CLASSPATH = "java.lang.Enum";

  public static void main(String[] args) {
    List<Map<String, Object>> x = enumToListMap(SysJobStatus.class);
    System.out.println(x);
  }

  public static List<Map<String, Object>> enumToListMap(Class<?> enumClass) {
    List<Map<String, Object>> resultList = new ArrayList<>();
    if (!ENUM_CLASSPATH.equals(enumClass.getSuperclass().getCanonicalName())) {
      return resultList;
    }
    // 获取所有public方法
    Method[] methods = enumClass.getMethods();
    List<Field> fieldList = new ArrayList<>();
    // 1.通过get方法提取字段，避免get作为自定义方法的开头，建议使用 ‘find’或其余命名
    Arrays.stream(methods)
        .map(Method::getName)
        .filter(
            methodName ->
                methodName.startsWith("get")
                    && !"getDeclaringClass".equals(methodName)
                    && !"getClass".equals(methodName))
        .forEachOrdered(
            methodName -> {
              try {
                Field field =
                    enumClass.getDeclaredField(StringUtils.uncapitalize(methodName.substring(3)));
                if (null != field) {
                  fieldList.add(field);
                }
              } catch (NoSuchFieldException | SecurityException e) {
                e.printStackTrace();
              }
            });

    // 2.将字段作为key，逐一把枚举值作为value 存入list
    if (CollectionUtils.isEmpty(fieldList)) {
      return resultList;
    }

    Enum<?>[] enums = (Enum[]) enumClass.getEnumConstants();
    for (Enum<?> anEnum : enums) {
      Map<String, Object> map = new HashMap<>(fieldList.size());
      for (Field field : fieldList) {
        field.setAccessible(true);
        try {
          // 向map集合添加字段名称 和 字段值
          map.put(field.getName(), field.get(anEnum));
        } catch (IllegalArgumentException | IllegalAccessException e) {
          e.printStackTrace();
        }
      }
      // 将Map添加到集合中
      resultList.add(map);
    }
    return resultList;
  }

  //    public static void main(String[] args) {
  //
  //
  //        // 枚举类
  //        System.out.println(enumToListMap(EducateStatusEnum.class));
  //
  //
  //    }

}
