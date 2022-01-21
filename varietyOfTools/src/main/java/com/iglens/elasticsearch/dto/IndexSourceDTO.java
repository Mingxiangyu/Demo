package com.iglens.elasticsearch.dto;

import java.util.Date;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class IndexSourceDTO {

  private static final long serialVersionUID = 1L;

  /** 创建时间 */
  private Date createStamp;

  /** 修改时间 */
  private Date modifyStamp;

  private String tenantIdentifier;

  private String typeIdentifier;

  private String objectId;

  private String content;
}
