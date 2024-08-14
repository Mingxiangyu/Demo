package com.iglens.dify;
import com.alibaba.fastjson.annotation.JSONField;
import java.util.List;
import lombok.Data;

@Data
public class Rule {

    @JSONField(name = "pre_processing_rules")
    private List<PreProcessingRule> preProcessingRules;

    private Segmentation segmentation;

}