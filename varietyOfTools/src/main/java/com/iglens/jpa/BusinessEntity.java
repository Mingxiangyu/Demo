package com.iglens.jpa;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.Instant;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

/**
 * @author ming
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("业务数据中转")
@Entity
@Table(name = "ZB_BUSINESS")
public class BusinessEntity {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  // @GenericGenerator(
  //     name = "snowflakeId",
  //     strategy = "cn.iglens.meta.jpa.SnowflakeIdentifierGenerator")
  private Long id;

  /**
   * 创建时间
   */
  @CreatedDate
  // @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  // @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Instant createStamp;

  /**
   * 修改时间
   */
  @LastModifiedDate
  // @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  // @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Instant modifyStamp;

  /**
   * 数据类型
   */
  @ApiModelProperty(value = "数据类型（数据目录中的identifier）")
  private String dataType;

  /**
   * 类别
   */
  @ApiModelProperty(value = "类别")
  private String classification;

  /**
   * 国家/地区
   */
  @ApiModelProperty(value = "国家/地区")
  private String country;

  /**
   * 业务
   */
  @ApiModelProperty(value = "业务")
  private String business;

  /**
   * 信源id
   */
  @ApiModelProperty("信源id")
  private String sourceId;

  /**
   * 采集时间
   */
  @ApiModelProperty("采集时间")
  // @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  // @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Instant collectTime;

  /**
   * 发布时间
   */
  @ApiModelProperty("发布时间")
  // @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  // @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Instant releaseTime;

  /**
   * 文件本身名字
   */
  @ApiModelProperty("文件本身名字")
  private String fileName;

  /**
   * 文件路径
   */
  @ApiModelProperty("文件路径")
  private String filePath;

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
   * 年份
   */
  @ApiModelProperty("年份")
  private String year;

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
}
