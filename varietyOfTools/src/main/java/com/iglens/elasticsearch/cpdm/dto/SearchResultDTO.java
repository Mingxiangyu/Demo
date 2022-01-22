package com.iglens.elasticsearch.cpdm.dto;

import java.util.Date;
import java.util.Map;
import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class SearchResultDTO {

  private static final long serialVersionUID = 1L;

  /** 创建时间 */
  private Date createStamp;

  /** 修改时间 */
  private Date modifyStamp;

  /** 数据项类型 */
  //  private BusinessObjectType objectType;

  /** 数据项ID */
  private Long id;

  /** 数据oid */
  private String oid;

  /** 文件夹 */
  private String location;

  /** 修订版本id */
  private Long revisionId;

  /** WNC端大版本ID */
  private String wtVid;

  /** WNC端ID */
  private String wtId;

  /** 编号 */
  private String code;

  /** 名称 */
  private String name;

  /** 描述 */
  private String description;

  /** 版本 */
  private String version;

  /** 密级 */
  private String secretLevel;

  /** 型号代号 */
  private String modelCode;

  /** 产品代号 */
  private String productCode;


  private String stateKey;

  private String stateDisplay;

  private String departmentId;

  private String departmentName;

  private String repositoryId;

  private String repositoryName;

  private String creatorId;

  private String creatorUsername;

  private String creatorFullName;

  private String modifierId;

  private String modifierUsername;

  private String modifierFullName;

  /** 图标 */
  private String icon;

  /** 自定义属性(上述以外的IBA属性) */
  private Map<String, Object> customAttributes;

  /** 物资编码_A(CODE_OF_MATERIAL_A) */
  private String codeOfMaterialA;

  /** 物资编码(CODE_OF_MATERIAL) */
  private String codeOfMaterial;

  /** 阶段标记 */
  private String phaseMark;

  /** 业务数据的url地址 */
  private String objectURL;

  /** 生命周期模板显示名称 */
  private String lifecycleTemplateDisplay;

  /** 当前签审人(a,b,c) */
  private String signtrial;

  /** 相关数据发送单(a,b,c) */
  private String relevantDataSendOrder;
}
