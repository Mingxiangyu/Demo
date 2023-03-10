package cn.trans.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.Data;

/**
 * 微软翻译接口参数对象
 * @author mxy
 * @date 2022-12-22
 */
@ApiModel("微软翻译接口参数对象")
@Data
public class MicrosoftTransParamItem implements Serializable {

    @ApiModelProperty("源文本")
    @JsonProperty("Text")
    private String text;

}
