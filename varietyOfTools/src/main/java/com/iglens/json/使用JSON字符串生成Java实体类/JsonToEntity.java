package com.iglens.json.使用JSON字符串生成Java实体类;

import com.alibaba.fastjson.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * 将json格式字符串生成出实体字段
 *
 * @author xunjian.xiao
 * @date 2022/11/11 15:51
 */

public class JsonToEntity {
    /**
     * 结果字符串
     */
    private StringBuilder resultString = new StringBuilder("\n");

    /**
     * 给json的key-设置属性名
     */
    private Map<String, String> fieldMap = new HashMap<>(16);

    /**
     * 给json的key-设置属性名
     *
     * @param key
     * @param value
     */
    public void fieldMapPut(String key, String value) {
        this.fieldMap.put(key, value);
    }

    /**
     * 给json的key-设置说明
     */
    private Map<String, String> fieldDescriptionMap = new HashMap<>(16);

    /**
     * 给json的key-设置说明
     *
     * @param key
     * @param value
     */
    public void fieldDescriptionMapPut(String key, String value) {
        this.fieldDescriptionMap.put(key, value);
    }


    /**
     * 需要追加的注解,规则:{key}-为json中的key,{value}-为json中的value <br>
     * 例如:<br>
     * "@ExcelImport(value = \"{key}\",kv = \"\",required = true,maxLength = 255,unique = true)"
     */
    private List<String> appendInterfaceList = new ArrayList<>(16);

    public void appendInterfaceAdd(String interfaceStr) {
        this.appendInterfaceList.add(interfaceStr);
    }

    /**
     * 将json字符串转换成实体对象
     *
     * @param clazz            class对象
     * @param classDescription 对象说明
     * @param json             json字符串
     * @author xunjian.xiao
     * @date 2022/11/14 11:06
     */
    public void toEntityOrUpdateFile(Class clazz, String classDescription, String json) {
        String result = toEntity(clazz, classDescription, json);
    System.out.println(result);
        // CustomIOUtil.rewriteFirst(clazz, null, result);
    }

    /**
     * 默认开启Lombok,swagger
     *
     * @param clazz            class对象
     * @param classDescription 对象说明
     * @param json             json字符串
     * @return 生成结果字符串
     * @author xunjian.xiao
     * @date 2022/11/14 9:23
     */
    public String toEntity(Class clazz, String classDescription, String json) {
        return toEntity(clazz, classDescription, true, true, json);
    }

    /**
     * 将json字符串转换成实体对象
     *
     * @param clazz            class对象
     * @param classDescription 对象说明
     * @param swagger          是否开启swagger注释 默认关闭 swagger说明
     * @param lombok           是否开启lombok简化代码 默认关闭 lombok 代码简化
     * @param json             json字符串
     * @return
     * @throws
     * @author xunjian.xiao
     * @date 2022/11/11 17:09
     */
    public String toEntity(Class clazz, String classDescription, Boolean swagger, Boolean lombok, String json) {
        if (clazz == null) {
            System.out.println("请选择对象");
            return null;
        }
        if (swagger == null) {
            swagger = false;//默认关闭 swagger说明
        }
        if (lombok == null) {
            lombok = false;//默认关闭 lombok 代码简化
        }

        if (lombok) {
            resultString.append("@Data\n" +
                    "@Accessors(chain = true)");
        }
        if (swagger) {
            resultString.append("@ApiModel(value = \"" + clazz.getSimpleName() + "\", description = \"" + classDescription + "\")\n");
        }
        resultString.append("public class " + clazz.getSimpleName() + "{\n");

        Map<String, Object> stringStringMap = JSONObject.parseObject(json, Map.class);
        for (Map.Entry<String, Object> entity : stringStringMap.entrySet()) {
            resultString.append("\t");
            String key = entity.getKey();

            Object value = "";
            if (ObjectUtils.isNotEmpty(entity.getValue())) {
                value = entity.getValue();
            }

            String fieldDescription = fieldDescriptionMap.get(key);
            String field = fieldMap.get(key);
            field = Objects.isNull(field) ? key : field;
            if (Objects.nonNull(fieldDescription)) {
                //有说明
                key = fieldDescription;
            }

            //注释
            resultString.append(String.format("/**%s*/\n", key));

            //扩展功能,追加注解
            addInterface(key, value);

            if (swagger) {
                resultString.append("\t");
                resultString.append(String.format("@ApiModelProperty(\"%s\")", key));
                resultString.append("\n");
            }

            //校验格式
            String type = typeHandle(key, value);

            resultString.append("\t");
            resultString.append(String.format("private %s %s;", type, field));
            resultString.append("\n");
        }

        resultString.append("}");


        return resultString.toString();
    }


    /**
     * 字段类型格式处理
     *
     * @param key
     * @param value
     * @return {@link String}
     * @throws
     * @author xunjian.xiao
     * @date 2022/11/14 9:22
     */
    private String typeHandle(String key, Object value) {
        String type = getType(value);
        //if (key.contains("时间")) {
            //type = LocalDateTime.class.getSimpleName();
            //String a = String.format("@JsonFormat( pattern = \"yyyy-MM-dd HH:mm:ss\", timezone = \"GMT+8\")\n\t@DateTimeFormat(pattern = \"yyyy-MM-dd HH:mm:ss\")");
            //resultString.append("\t");
            //resultString.append(a);
            //resultString.append("\n");
        //}

        return type;
    }

    /**
     * 添加要增加的注解
     */
    private void addInterface(String key, Object value) {
        for (String appendInterface : appendInterfaceList) {
            resultString.append("\t");
            appendInterface = appendInterface.replace("{key}", key);
            appendInterface = appendInterface.replace("{value}", Objects.nonNull(value) ? value.toString() : StringUtils.EMPTY);
            resultString.append(appendInterface);
            resultString.append("\n");
        }

    }


    /**
     * 类型判断
     *
     * @param value 值
     * @return {@link String}
     * @throws
     * @author xunjian.xiao
     * @date 2022/11/11 15:57
     */
    private String getType(Object value) {
        //默认字符串格式
        String type = String.class.getSimpleName();

        if (value != null) {
            type = value.getClass().getSimpleName();
        }
        return type;
    }

    /**
     * 打印json格式相关功能字符串
     *
     * @param json
     * @author xunjian.xiao
     * @date 2022/11/14 11:27
     */
    public static void prlintlnJsonMap(String json) {
        Map<String, Object> stringObjectMap = JSONObject.parseObject(json, Map.class);
        int i = 97;
        for (Map.Entry<String, Object> a : stringObjectMap.entrySet()) {
            System.out.println(String.format("jsonToEntity.fieldMapPut(\"%s\",\"%c\");", a.getKey(), i++));
        }
    }
}

