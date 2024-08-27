package com.iglens.llm.token.openAi;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Functions implements Serializable {
    /**
     * 方法名称
     */
    private String name;
    /**
     * 方法描述
     */
    private String description;
    /**
     * 方法参数
     * 扩展参数可以继承Parameters自己实现，json格式的数据
     */
    private Parameters parameters;
}
