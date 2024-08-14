package com.iglens.dify;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

@Data
public class DataSetBody {

    @JSONField(name = "indexing_technique")
    private String indexingTechnique;

    @JSONField(name = "process_rule")
    private ProcessRule processRule;
}