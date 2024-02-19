package com.iglens.时间.instant;

import io.swagger.annotations.ApiModelProperty;
import java.time.Instant;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

/**
 * 航母
 *
 * @author xming
 */
@Data
@ToString(callSuper = true)
public class MetaData {
  // @Id
  // @GeneratedValue(strategy =GenerationType.AUTO, generator = "myid")
  // @GenericGenerator(name = "myid", strategy = "cn.iglens.meta.jpa.ManulInsertGenerator")
  private Long id;

  /**
   * 创建时间
   */
  // @CreatedDate
  private Instant createStamp;

  /**
   * 修改时间
   */
  // @LastModifiedDate
  private Instant modifyStamp;

  @Version
  private Integer version;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "myid")
  @GenericGenerator(name = "myid", strategy = "cn.iglens.meta.jpa.ManulInsertGenerator")
  private String uuid;

  /**
   * 任务名称
   */
  private String name;

  /**
   * 数据名称
   */
  private String dataName;

  /**
   * 一级类型
   */
  private String typeFirst;

  /**
   * 二级类型
   */
  private String typeSecond;

  /**
   * 数据大小
   */
  private String dataSize;

  /**
   * 数据来源
   */
  private String dataSource;

  /**
   * 可信度
   */
  private Integer credible;

  /**
   * 坐标系统
   */
  private String coordinateSystem;

  /**
   * 投影
   */
  private String projection;

  /**
   * 数据保存路径
   */
  private String dataStoragePath;

  /**
   * 清洗状态
   */
  private Boolean cleanStatus;

  /**
   * 数据格式
   */
  private String dataFormat;

// =====================自身业务信息=================================

  /**
   * 业务
   */
  private String business;

  /**
   * 信源id
   */
  private String sourceId;
  /**
   * 发布时间
   */
  private Instant releaseTime;
  /**
   * 数据类型
   */
  private String dataType;

  /**
   * 区域（方向）
   */
  private String region;

  /**
   * 三级类型
   */
  private String typeThree;

  /**
   * （国家）
   */
  private String country;

  /**
   * 年份
   */
  private String year;

  /**
   * 经度
   */
  @ApiModelProperty("经度")
  private String longitude;

  /**
   * 纬度
   */
  @ApiModelProperty("纬度")
  private String latitude;

  /**
   * 图层url
   */
  @ApiModelProperty("图层url")
  private String layerUrl;

  /**
   * 版本号
   */
  private String versionNum;

  /**
   * 大洲
   */
  private String continent;

  /**
   * 百科类型
   */
  private String baikeType;

  /**
   * 国家名称
   */
  private String countryNameCn;

  /**
   * 省份名称
   */
  private String provinceNameCn;

  /**
   * 城市名称
   */
  private String cityNameCn;

  /**
   * 是否发布成功
   */
  private Boolean isPublishFlag;

  /**
   * 是否发布
   */
  private Boolean isPublished;
  // 其他字段定义
}
