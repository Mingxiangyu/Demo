package com.iglens.llm.token.openAi;

import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Parameters implements Serializable {
    /**
     * 参数类型
     */
    private String type;
    /**
     * 参数属性、描述
     */
    private Object properties;
    /**
     * 方法必输字段
     */
    private List<String> required;
}
