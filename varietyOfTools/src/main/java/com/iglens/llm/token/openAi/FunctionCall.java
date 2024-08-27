package com.iglens.llm.token.openAi;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FunctionCall {
    /**
     * 方法名
     */
    private String name;
    /**
     * 方法参数
     */
    private String arguments;
}
