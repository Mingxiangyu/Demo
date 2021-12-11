package com.iglens;

import com.google.common.collect.Maps;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

/**
 * map的一些简单操作
 *
 * @author T480S
 */
public class MapUtils {

  /**
   * 过滤map中空map
   *
   * @param param
   * @return
   */
  public static Map<String, String[]> filterEmptyParam(Map<String, String[]> param) {
    return Maps.filterValues(param, s -> s != null && s.length > 0 && StringUtils.isNotBlank(s[0]));
  }
}
