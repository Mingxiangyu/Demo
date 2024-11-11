package com.iglens.json;

import com.alibaba.fastjson.JSONObject;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

/**
 * JSON转YAML工具类
 * 支持多级嵌套的JSON对象转换为YAML格式
 */
public class JSON转YAML工具类 {

    /**
     * 创建示例多级JSON对象
     *
     * @return 多级嵌套的JSONObject
     */
    public static JSONObject createNestedJson() {
        JSONObject root = new JSONObject(true); // 使用true保持顺序
        
        // 创建spring配置
        JSONObject spring = new JSONObject(true);
        
        // 数据源配置
        JSONObject datasource = new JSONObject(true);
        datasource.put("url", "jdbc:mysql://localhost:3306/test");
        datasource.put("username", "root");
        datasource.put("password", "123456");
        datasource.put("driver-class-name", "com.mysql.cj.jdbc.Driver");
        
        // 连接池配置
        JSONObject pool = new JSONObject(true);
        pool.put("initial-size", 5);
        pool.put("max-active", 20);
        pool.put("min-idle", 5);
        pool.put("max-wait", 60000);
        datasource.put("pool", pool);
        
        spring.put("datasource", datasource);
        
        // JPA配置
        JSONObject jpa = new JSONObject(true);
        JSONObject hibernate = new JSONObject(true);
        hibernate.put("ddl-auto", "update");
        hibernate.put("show_sql", true);
        hibernate.put("format_sql", true);
        jpa.put("hibernate", hibernate);
        spring.put("jpa", jpa);
        
        // 服务器配置
        JSONObject server = new JSONObject(true);
        server.put("port", 8080);
        server.put("servlet", new JSONObject(true){{
            put("context-path", "/api");
        }});
        
        // 组装根配置
        root.put("spring", spring);
        root.put("server", server);
        
        return root;
    }

    /**
     * 将JSONObject转换为YAML格式的字符串
     *
     * @param jsonObject JSON对象
     * @return YAML格式的字符串
     */
    public static String convertToYaml(JSONObject jsonObject) {
        // 配置YAML输出选项
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK); // 设置块样式
        options.setPrettyFlow(true);
        options.setIndent(2); // 设置缩进
        
        // 创建YAML实例
        Yaml yaml = new Yaml(options);
        
        // 将JSONObject转换为Map
        Map<String, Object> map = jsonObjectToMap(jsonObject);
        
        // 转换为YAML
        return yaml.dump(map);
    }

    /**
     * 将YAML内容写入文件
     *
     * @param yaml YAML内容
     * @param filePath 文件路径
     * @throws IOException 如果写入文件失败
     */
    public static void writeYamlToFile(String yaml, String filePath) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(yaml);
        }
    }

    /**
     * 递归将JSONObject转换为Map
     *
     * @param jsonObject JSON对象
     * @return 转换后的Map
     */
    private static Map<String, Object> jsonObjectToMap(JSONObject jsonObject) {
        Map<String, Object> map = new LinkedHashMap<>();
        
        for (String key : jsonObject.keySet()) {
            Object value = jsonObject.get(key);
            if (value instanceof JSONObject) {
                // 递归处理嵌套的JSONObject
                map.put(key, jsonObjectToMap((JSONObject) value));
            } else {
                map.put(key, value);
            }
        }
        
        return map;
    }

    /**
     * 示例使用方法
     */
    public static void main(String[] args) {
        try {
            // 创建多级JSON对象
            JSONObject jsonObject = createNestedJson();
            
            // 转换为YAML
            String yaml = convertToYaml(jsonObject);
            
            // 输出YAML到控制台
            System.out.println("Generated YAML:");
            System.out.println(yaml);
            
            // 保存到文件
            writeYamlToFile(yaml, "config.yaml");
            System.out.println("\nYAML has been written to config.yaml");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}