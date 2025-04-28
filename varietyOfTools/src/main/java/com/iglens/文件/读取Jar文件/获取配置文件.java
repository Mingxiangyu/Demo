package com.iglens.文件.读取Jar文件;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

/**
 * YML配置参数获取工具类
 */
@Component
public class 获取配置文件 {
// public class YmlConfigUtils {

  private static final Logger log = LoggerFactory.getLogger(获取配置文件.class);
  private static Map<String, Object> config;

  private final Environment environment;

  @Autowired
  public 获取配置文件(Environment environment) {
    this.environment = environment;
  }

  static {
    try {
      Yaml yaml = new Yaml();
      InputStream inputStream = 获取配置文件.class.getClassLoader().getResourceAsStream("application.yaml");
      config = yaml.load(inputStream);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 静态方法获取配置文件内容
   * @param key
   * @return
   */
   @SuppressWarnings("unchecked")
    public static Object getStaticProperty(String key) {
        String[] parts = key.split("\\.");
        Map<String, Object> current = config;

        for (int i = 0; i < parts.length - 1; i++) {
            current = (Map<String, Object>) current.get(parts[i]);
            if (current == null) return null;
        }

        return current.get(parts[parts.length - 1]);
    }


  /**
   * 获取字符串类型的配置值
   *
   * @param key 配置键
   * @return 配置值
   */
  public String getString(String key) {
    String property = environment.getProperty(key);
    log.debug("获取字符串类型配置参数：{} = {}", key, property);
    return property;
  }

  /**
   * 获取字符串类型的配置值，如果为空则返回默认值
   *
   * @param key 配置键
   * @param defaultValue 默认值
   * @return 配置值或默认值
   */
  public String getString(String key, String defaultValue) {
    String property = environment.getProperty(key, defaultValue);
    log.debug("获取字符串类型配置参数：{} = {}，默认值为：{}", key, property, defaultValue);
    return property;
  }

  /**
   * 获取整数类型的配置值
   *
   * @param key 配置键
   * @return 配置值
   */
  public Integer getInteger(String key) {
    String value = getString(key);
    Integer integer = value != null ? Integer.parseInt(value) : null;
    log.debug("获取整数类型配置参数：{} = {}", key, integer);
    return integer;
  }

  /**
   * 获取整数类型的配置值，如果为空则返回默认值
   *
   * @param key 配置键
   * @param defaultValue 默认值
   * @return 配置值或默认值
   */
  public Integer getInteger(String key, Integer defaultValue) {
    String value = getString(key);
    int integer = value != null ? Integer.parseInt(value) : defaultValue;
    log.debug("获取整数类型配置参数：{} = {}，默认值为：{}", key, integer, defaultValue);
    return integer;
  }

  /**
   * 获取布尔类型的配置值
   *
   * @param key 配置键
   * @return 配置值
   */
  public Boolean getBoolean(String key) {
    String value = getString(key);
    return value != null ? Boolean.parseBoolean(value) : null;
  }

  /**
   * 获取布尔类型的配置值，如果为空则返回默认值
   *
   * @param key 配置键
   * @param defaultValue 默认值
   * @return 配置值或默认值
   */
  public Boolean getBoolean(String key, Boolean defaultValue) {
    String value = getString(key);
    return value != null ? Boolean.parseBoolean(value) : defaultValue;
  }

  /**
   * 获取List类型的配置值（以逗号分隔的字符串）
   *
   * @param key 配置键
   * @return 配置值列表
   */
  public List<String> getList(String key) {
    String value = getString(key);
    return value != null ? Arrays.asList(value.split(",")) : null;
  }

  /**
   * 获取带默认值的List类型的配置值
   *
   * @param key 配置键
   * @param defaultValue 默认值列表
   * @return 配置值列表或默认值列表
   */
  public List<String> getList(String key, List<String> defaultValue) {
    String value = getString(key);
    return value != null ? Arrays.asList(value.split(",")) : defaultValue;
  }

  /**
   * 获取Optional包装的字符串类型配置值
   *
   * @param key 配置键
   * @return Optional包装的配置值
   */
  public Optional<String> getOptionalString(String key) {
    return Optional.ofNullable(getString(key));
  }

  /**
   * 检查配置键是否存在
   *
   * @param key 配置键
   * @return 是否存在
   */
  public boolean containsProperty(String key) {
    return environment.containsProperty(key);
  }

  /**
   * 获取指定前缀的所有配置键
   *
   * @param prefix 配置键前缀
   * @return 匹配前缀的配置键列表
   */
  public List<String> getPropertiesWithPrefix(String prefix) {
    return Arrays.stream(environment.getActiveProfiles())
        .filter(profile -> profile.startsWith(prefix)).collect(Collectors.toList());
  }
}