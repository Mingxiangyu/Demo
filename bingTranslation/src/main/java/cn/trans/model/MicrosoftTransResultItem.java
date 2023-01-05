package cn.trans.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;
import lombok.Data;

/**
 * 微软翻译接口结果对象
 * @author mxy
 * @date 2020-10-21
 */
@ApiModel("微软翻译接口结果对象")
@Data
public class MicrosoftTransResultItem implements Serializable {

    @ApiModelProperty("翻译结果")
    private List<Translations> translations;

    @ApiModel("翻译结果")
    @Data
    public static class Translations implements Serializable{

        @ApiModelProperty("原文")
        private String text;

        @ApiModelProperty("译文")
        private String to;

        @ApiModelProperty("段落句对齐信息")
        private SentLen sentLen;

    }

    @ApiModel("长度信息")
    @Data
    public static class SentLen implements Serializable{

        @ApiModelProperty("原文长度")
        private List<Integer> srcSentLen;

        @ApiModelProperty("译文长度")
        private List<Integer> transSentLen;

    }
}
