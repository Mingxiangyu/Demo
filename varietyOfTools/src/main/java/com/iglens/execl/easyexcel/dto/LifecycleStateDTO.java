package com.iglens.execl.easyexcel.dto;

import java.io.Serializable;
import lombok.Data;

/** @author fli */
@Data
public class LifecycleStateDTO implements Serializable {
  private String id;

  private String identifier;

  private String name;

  private String description;

  private Integer sort;
  /** 状态类型 */
  private String type;
}
