package com.iglens.dify;

import com.alibaba.fastjson.annotation.JSONField;

public class Segmentation {

    private String separator;
    @JSONField(name = "max_tokens")
    private int maxTokens;
    public void setSeparator(String separator) {
         this.separator = separator;
     }
     public String getSeparator() {
         return separator;
     }

    public void setMaxTokens(int maxTokens) {
         this.maxTokens = maxTokens;
     }
     public int getMaxTokens() {
         return maxTokens;
     }

}