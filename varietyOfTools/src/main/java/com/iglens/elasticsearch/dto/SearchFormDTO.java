package com.iglens.elasticsearch.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public class SearchFormDTO implements Serializable {

  private static final long serialVersionUID = 1L;

  /** 创建时间 */
  private Date createStamp;

  /** 修改时间 */
  private Date modifyStamp;

  private Boolean showLatestVersion = Boolean.TRUE;

  private String containerType;

  private String baseType;

  /** 搜索关键字 */
  private String q = "";

  /** 搜索范围 */
  private List<String> scope;

  /** 编号关键字 */
  private String codeKeyword = "";

  /** 名称关键字 */
  private String nameKeyword = "";

  /** 说明关键字 */
  private String description = "";

  /** 内容关键字 */
  private String content = "";

  /** 型号代号关键字 */
  private String modelCode = "";

  /** 标准类别 */
  private String standardType;

  /**
   * 搜索的数据分类
   *
   * @see com.casic.cpdm.core.BusinessObjectType
   */
  private List<String> type = new ArrayList<String>();

  /** 具体的对象类型 */
  private List<String> typeId = new ArrayList<String>();

  private String categoryId;

  /** 所属分类 */
  private List<String> classification = new ArrayList<String>();

  /** 搜索的生命周期状态，如果不指定则搜索所有状态 */
  private List<String> lifecycleStates = new ArrayList<String>();

  private String lifecycleState;

  /** 搜索的阶段如果不指定搜索所有阶段 */
  private List<String> phaseMarks = new ArrayList<String>();

  private String phaseMark;

  /** 创建者ID */
  private Long creatorId;

  /** 修改者ID */
  private Long modifierId;

  /** 创建起始 */
  private String[] creationDate;

  private String[] modificationDate;

  /** 容器ID */
  private String repositoryId;

  /** 部门ID */
  private String departmentId;
  /** 部门ID */
  private String[] departmentIds;

  /** 组织机构代码 */
  private String organCode;

  private String userCode;

  private Long containerId;

  /** 当前标准件时是否被使用 */
  private Boolean standardPartUsing;

  /** 物资编码A是否存在 */
  private Boolean codeOfMaterialAExist;

  /** 物资编码是否存在 */
  private Boolean codeOfMaterialExist;

  /** 物资编码_A(CODE_OF_MATERIAL_A) */
  private String codeOfMaterialA;

  /** 物资编码(CODE_OF_MATERIAL) */
  private String codeOfMaterial;

  /** 创建单位 */
  private String createDept;

  /** 是否只展示最新小版本 */
  private Boolean latestIteration;

  /** 自定义属性(上述以外的IBA属性) */
  private Map<String, String> customAttributes;
}
