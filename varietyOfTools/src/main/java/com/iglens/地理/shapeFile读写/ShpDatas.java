package com.iglens.地理.shapeFile读写;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.Data;

/**
 * shp数据模型对象 -- 针对读
 *
 * @author Appleyk
 * @blob https://blog.csdn.net/appleyk
 * @date Created on 下午 2018年10月24日16:31:30
 */
@Data
public class ShpDatas {

  private String name;

  /** 属性【字段】集合 */
  private List<Map<String, Object>> props;

  /** shp文件路径地址 */
  private String shpPath;

  public ShpDatas() {
    props = new ArrayList<>();
  }

  public void addProp(Map<String, Object> prop) {
    this.props.add(prop);
  }
}
