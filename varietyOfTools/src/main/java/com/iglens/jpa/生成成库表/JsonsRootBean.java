package com.iglens.jpa.生成成库表;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

/**
 * Auto-generated: 2023-11-14 9:29:11
 *
 * @author www.jsons.cn
 * @website http://www.jsons.cn/json2java/
 */
@Data
@ApiModel(value = "测井参数表", description = "测井参数表")
@Entity
@Table(name = "zb_gadm_translationdict")
public class JsonsRootBean implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;
  /** 创建时间 */
  @CreatedDate
  // @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  // @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Instant createTime;

  /** 修改时间 */
  @LastModifiedDate
  // @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  // @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Instant modifyTime;

  @JsonProperty("GID_3")
  private String gid3;

  @JsonProperty("GID_0")
  private String gid0;

  @JsonProperty("COUNTRY")
  private String country;

  @JsonProperty("GID_1")
  private String gid1;

  @JsonProperty("NAME_1")
  private String name1;

  @JsonProperty("NL_NAME_1")
  private String nlName1;

  @JsonProperty("GID_2")
  private String gid2;

  @JsonProperty("NAME_2")
  private String name2;

  @JsonProperty("NL_NAME_2")
  private String nlName2;

  @JsonProperty("NAME_3")
  private String name3;

  @JsonProperty("VARNAME_3")
  private String varname3;

  @JsonProperty("NL_NAME_3")
  private String nlName3;

  @JsonProperty("TYPE_3")
  private String type3;

  @JsonProperty("ENGTYPE_3")
  private String engtype3;

  @JsonProperty("CC_3")
  private String cc3;

  @JsonProperty("HASC_3")
  private String hasc3;

  @JsonProperty("CrawlerTime")
  private String crawlertime;

  @JsonProperty("NL_NAME_1_transte")
  private String nlName1Transte;

  @JsonProperty("NL_NAME_2_transte")
  private String nlName2Transte;
}
