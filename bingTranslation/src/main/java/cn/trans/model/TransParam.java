package cn.trans.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 翻译接口参数对象
 * @author mxy
 * @date 2020-10-21
 */
@ApiModel("翻译接口参数对象")
@Data
@ToString(callSuper = true)
@EqualsAndHashCode
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransParam implements Serializable {

    @ApiModelProperty("翻译源语言，默认为auto (中:zh,英:en,日:ja,韩:kor,俄:ru 内+[德:de,西:spa,法:fra,意:it,葡:pt,印地:hi,越:vie])")
    private String from = "auto";

    @ApiModelProperty("翻译目标语言，默认为zh (中:zh 外+[英:en,日:ja,韩:kor,俄:ru])")
    private String to = "zh";

    @ApiModelProperty("请求翻译query")
    private String q;



}
