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
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

@Slf4j
public class 枚举转换为list {
	private static String ENUM_CLASSPATH = "java.lang.Enum";

  	// @ApiOperation("获取任务类型")
	// @GetMapping("/task-type")
	// public R getTaskType() {
	//   List<Map<String, Object>> taskTypeList =
	//       EnumListUtil.enumToListMap(CollectDataBigTypeEnum.class, null);
	//   return R.ok("获取任务类型成功", taskTypeList);
	// }

    public static void main(String[] args) {
    List<Map<String, Object>> x = enumToListMap(SysJobStatus.class,null);
    System.out.println(x);
  }

	public static List<Map<String, Object>> enumToListMap(Class<?> enumClass, String taskType) {
		List<Map<String, Object>> resultList = new ArrayList<>();
		if (!ENUM_CLASSPATH.equals(enumClass.getSuperclass().getCanonicalName())) {
			return resultList;
		}
		// 获取所有public方法
		Method[] methods = enumClass.getMethods();
		List<Field> fieldList = new ArrayList<>();
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
										enumClass.getDeclaredField(
												org.springframework.util.StringUtils
														.uncapitalize(methodName.substring(3)));
								if (null != field) {
									fieldList.add(field);
								}
							} catch (NoSuchFieldException | SecurityException e) {
								log.error(e.getMessage());
							}
						});

		if (CollectionUtils.isEmpty(fieldList)) {
			return resultList;
		}

		Enum<?>[] enums = (Enum[]) enumClass.getEnumConstants();
		for (Enum<?> anEnum : enums) {
			Map<String, Object> map = new HashMap<>(fieldList.size());
			for (Field field : fieldList) {
				field.setAccessible(true);
				try {
					// 如果需要对任务类型进行过滤
					if (StringUtils.isNotBlank(taskType)) {
						// 如果当前枚举的任务类型不符合，则直接跳过当前枚举 并更新map为空
						if (StringUtils.equals("taskTypeCode", field.getName())
								&& !StringUtils
								.equals(taskType, String.valueOf(field.get(anEnum)))) {
							map = null;
							break;
						}
					}
					// 向map集合添加字段名称 和 字段值
					map.put(field.getName(), field.get(anEnum));
				} catch (IllegalArgumentException | IllegalAccessException e) {
					log.error(e.getMessage());
				}
			}
			// 将Map添加到集合中
			if (map != null) {
				resultList.add(map);
			}
		}
		return resultList;
	}

}
