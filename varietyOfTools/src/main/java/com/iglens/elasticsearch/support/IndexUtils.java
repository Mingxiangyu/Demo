package com.iglens.elasticsearch.support;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.Normalizer;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

/**
 * @author T480S
 */
public class IndexUtils {

  private static Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");

  /** 索引库的类型名称 */
  public static final String DEFAULT_TYPE_NAME = "item";

  /**
   * 根据租户ID返回Elastic Search的Index名称或者Apache Solr的core名称
   *
   * @param tenantIdentifier
   * @return
   */
  public static String formatIndexName(String tenantIdentifier) {
    if (StringUtils.isBlank(tenantIdentifier)) {
      return null;
    }
    try {
      return URLEncoder.encode(unAccent(tenantIdentifier), "UTF-8").toLowerCase();
    } catch (UnsupportedEncodingException e) {
      return null;
    }
  }

  /**
   * 根据业务数据类型返回搜索引擎的类型
   *
   * @param typeIdentifier
   * @return
   */
  public static String formatTypeName(String typeIdentifier) {
    if (StringUtils.isBlank(typeIdentifier)) {
      return null;
    }
    String typeName = StringUtils.lowerCase(typeIdentifier);
    return typeName;
  }

  private static String unAccent(String s) {
    String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
    return pattern.matcher(temp).replaceAll("").replaceAll("\\p{javaSpaceChar}", "_");
  }
}
