package com.iglens.llm.token.openAi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ToolCalls implements Serializable {
    /**
     * The ID of the tool call.
     */
    private String id;
    /**
     * The type of the tool. Currently, only function is supported.
     */
    private String type;

    private ToolCallFunction function;

    @Getter
    @AllArgsConstructor
    public enum Type {
        FUNCTION("function"),
        ;
        private final String name;
    }
}
