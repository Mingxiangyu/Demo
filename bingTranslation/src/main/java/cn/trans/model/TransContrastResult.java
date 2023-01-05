package cn.trans.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.Data;

/**
 * 翻译接口结果对象
 * @author mxy
 * @date 2020-10-21
 */
@ApiModel("翻译接口结果对象")
@Data
public class TransContrastResult implements Serializable {

    @ApiModelProperty("翻译源语言")
    private String from;

    @ApiModelProperty("翻译目标语言")
    private String to;

    @ApiModelProperty("原文")
    private String src;

    @ApiModelProperty("译文")
    private String dst;

}
