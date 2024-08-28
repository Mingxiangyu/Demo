package com.iglens.llm.invoke.deepseek;

import com.alibaba.fastjson.annotation.JSONField;
import java.util.List;
import lombok.Data;

@Data
public class ChatCompletionResponse {

  private String id;
  private String object;
  private long created;
  private String model;
  @JSONField(name ="system_fingerprint")
  private String systemFingerprint;
  private List<Choice> choices;
  private Usage usage;

  @Data
  public static class Choice {

    private Message message;
    private int index;
    @JSONField(name ="finish_reason")
    private String finishReason;
    private String logprobs;
  }
    @Data
    public static class Message {
        private String role;
        private String content;
    }

  @Data
  public static class Usage {

    @JSONField(name ="prompt_tokens")
    private int promptTokens;

    @JSONField(name ="completion_tokens")
    private int completionTokens;

    @JSONField(name ="total_tokens")
    private int totalTokens;

    @JSONField(name ="prompt_cache_hit_tokens")
    private int promptCacheHitTokens;

    @JSONField(name ="prompt_cache_miss_tokens")
    private int promptCacheMissTokens;
  }
}

