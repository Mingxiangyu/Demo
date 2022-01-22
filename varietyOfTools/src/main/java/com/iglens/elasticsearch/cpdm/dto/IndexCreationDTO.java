package com.iglens.elasticsearch.cpdm.dto;

import java.util.Date;
import lombok.Data;
import lombok.ToString;

/** @author T480S */
@Data
@ToString(callSuper = true)
public class IndexCreationDTO {
  private static final long serialVersionUID = 1L;

  /** 创建时间 */
  private Date createStamp;

  /** 修改时间 */
  private Date modifyStamp;

  public String name;

  private Integer numberOfShards;

  private Integer numberOfReplicas;

  private Integer autoExpandReplicas;
}
