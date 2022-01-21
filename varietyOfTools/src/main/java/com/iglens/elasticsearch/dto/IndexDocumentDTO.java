package com.iglens.elasticsearch.dto;

import java.util.Date;
import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public class IndexDocumentDTO {

  /** 创建时间 */
  private Date createStamp;

  /** 修改时间 */
  private Date modifyStamp;

  /** 数据基类 */
  private String businessType;

  /** 租户ID */
  private String tenantId;

  /** 部门ID */
  private String departmentId;

  /** 部门Name */
  private String departmentName;

  /** 库ID */
  private String repositoryId;

  /** 数据ID */
  private String id;

  /** 数据OID */
  private String oid;

  /** 文件夹 */
  private String location;

  /** 修订版本OID */
  private String revisionOid;

  /** 主数据OID */
  private String masterOid;

  /** 类型 */
  private List<String> categoryId;

  /** 所属分类节点 */
  private List<String> classification;

  /** 编号 */
  private String code;

  /** 名称 */
  private String name;

  /** 说明 */
  private String description;

  /** 视图 */
  private String view;

  /** 版本 */
  private String version;

  /** 状态 */
  private String state;

  /** 创建者 */
  private String creatorId;

  /** 修改者 */
  private String modifierId;

  /** 标准类别(标准件) */
  private String standardType;

  /** 研制阶段 */
  private String phase;

  /** 密级 */
  private String secretLevel;

  /** 图标 */
  private String icon;

  /** 型号代号 */
  private String modelCode;

  /** 默认单位 */
  private String defaultUnit;

  /** 物资编码_A(CODE_OF_MATERIAL_A) */
  private String codeOfMaterialA;

  /** 物资编码(CODE_OF_MATERIAL) */
  private String codeOfMaterial;

  /** 为标准件时被使用次数 */
  private String standardPartUsing;

  /** 是否最小新版 */
  private String latestIteration;

  /** 是否最大新版 */
  private String latestRevision;

  private Map<String, Object> details;
}
