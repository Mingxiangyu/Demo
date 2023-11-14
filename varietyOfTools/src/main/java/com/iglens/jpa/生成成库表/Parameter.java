package com.iglens.jpa.生成成库表;


import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.Instant;

@Data
@ApiModel(value = "测井参数表", description = "测井参数表")
@Entity
@Table(name = "test")
public class Parameter implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
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

    @ApiModelProperty("订单ID")
    private Integer orderId;
    /**
     * no
     */
    @ApiModelProperty("no")
    private String no;
    /**
     * StartDepth
     */
    @ApiModelProperty("StartDepth")
    @JSONField(name = "StartDepth")
    private String startDepth;
    /**
     * EndDepth
     */
    @ApiModelProperty("EndDepth")
    @JSONField(name = "EndDepth")
    private String endDepth;
    /**
     * EndDepth
     */
    @ApiModelProperty("aiCorrectionDepth")
    private String aiCorrectionDepth;
    /**
     * PIPE3Type
     */
    @ApiModelProperty("PIPE_3Type")
    @JSONField(name = "PIPE_3Type")
    private String pIPE3Type;
    /**
     * PIPE2Offset
     */
    @ApiModelProperty("PIPE_2Offset")
    @JSONField(name = "PIPE_2Offset")
    private String pIPE2Offset;
     /**
     * PIPEString3ThkChannel
     */
    @ApiModelProperty("PIPE_3ThkChannel")
    @JSONField(name = "PIPE_3ThkChannel")
    private String pIPE3ThkChannel;
    /**
     * PIPE_2NomThkin
     */
    @ApiModelProperty("PIPE_2NomThkin")
    @JSONField(name = "PIPE_2NomThkin")
    private String pIPE2NomThkin;
    /**
     * PIPE_2LbFt
     */
    @JSONField(name = "PIPE_2LbFt")
    @ApiModelProperty("PIPE_2LbFt")
    private String pIPE2LbFt;
    /**
     * PIPE1NomThkin
     */
    @ApiModelProperty("PIPE_1NomThkin")
    @JSONField(name = "PIPE_1NomThkin")
    private String pIPE1NomThkin;
    /**
     * PIPE1Type
     */
    @ApiModelProperty("PIPE_1Type")
    @JSONField(name = "PIPE_1Type")
    private String pIPE1Type;
    /**
     * PIPE_2Type
     */
    @ApiModelProperty("PIPE_2Type")
    @JSONField(name = "PIPE_2Type")
    private String pIPE2Type;
    /**
     * PIPE_3OD
     */
    @ApiModelProperty("PIPE_3OD")
    @JSONField(name = "PIPE_3OD")
    private String pIPE3OD;
    /**
     * PIPE1ChannelContrast
     */
    @ApiModelProperty("PIPE1ChannelContrast")
    @JSONField(name = "PIPE_1ChannelContrast")
    private String pIPE1ChannelContrast;
    /**
     * PIPE2ThkChannel
     */
    @ApiModelProperty("PIPE_2ThkChannel")
    @JSONField(name = "PIPE_2ThkChannel")
    private String pIPE2ThkChannel;
    /**
     * PIPE3LbFt
     */
    @ApiModelProperty("PIPE_3LbFt")
    @JSONField(name = "PIPE_3LbFt")
    private String pIPE3LbFt;
    /**
     * PIPE1ThkChannel
     */
    @ApiModelProperty("PIPE_1ThkChannel")
    @JSONField(name = "PIPE_1ThkChannel")
    private String pIPE1ThkChannel;
    /**
     * PIPE_1Offset
     */
    @ApiModelProperty("PIPE_1Offset")
    @JSONField(name = "PIPE_1Offset")
    private String pIPE1Offset;
    /**
     * PIPE3ChannelContrast
     */
    @ApiModelProperty("PIPE_3ChannelContrast")
    @JSONField(name = "PIPE_3ChannelContrast")
    private String pIPE3ChannelContrast;
    /**
     * PIPE_3NomThkin
     */
    @ApiModelProperty("PIPE_3NomThkin")
    @JSONField(name = "PIPE_3NomThkin")
    private String pIPE3NomThkin;
    /**
     * PIPE2OD
     */
    @ApiModelProperty("PIPE_2OD")
    @JSONField(name = "PIPE_2OD")
    private String pIPE2OD;
    /**
     * PIPE1OD
     */
    @ApiModelProperty("PIPE_1OD")
    @JSONField(name = "PIPE_1OD")
    private String pIPE1OD;
    /**
     * PIPE3Offset
     */
    @ApiModelProperty("PIPE_3Offset")
    @JSONField(name = "PIPE_3Offset")
    private String pIPE3Offset;
    /**
     * PIPE1LbFt
     */
    @ApiModelProperty("PIPE_1LbFt")
    @JSONField(name = "PIPE_1LbFt")
    private String pIPE1LbFt;
    /**
     * PIPE2ChannelContrast
     */
    @ApiModelProperty("PIPE_2ChannelContrast")
    @JSONField(name = "PIPE_2ChannelContrast")
    private String pIPE2ChannelContrast;

    @ApiModelProperty("排序号")
    private Integer sort;
}

