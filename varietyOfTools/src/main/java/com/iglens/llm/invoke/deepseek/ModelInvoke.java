package com.iglens.llm.invoke.deepseek;

import com.alibaba.fastjson.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ModelInvoke {

  static String token = "sk-f5682e72e9ef418ab28b26bac1e4de79";

  public static void main(String[] args) {
    String systemContent = "You are a helpful assistant";
    String systemRole = "system";
    String content = "生成一个json案例";
    String model = "deepseek-coder";
    // 让模型严格按照 JSON 格式来输出
    JSONObject response_format = new JSONObject();
    response_format.put("type", "json_object");
    completions(systemContent, systemRole, content, model, response_format);
  }

  public static String completions(String systemContent, String systemRole, String content, String model,
      JSONObject response_format) {
    OkHttpClient client = new OkHttpClient().newBuilder()// 设置连接超时时间
                  .connectTimeout(60, TimeUnit.SECONDS)
                  // 写入超时时间
                  .writeTimeout(60, TimeUnit.SECONDS)
                  // 从连接成功到响应的总时间
                  .readTimeout(60, TimeUnit.SECONDS).build();
    MediaType mediaType = MediaType.parse("application/json");
    String content1 = "{\n"
        + "  \"messages\": [\n"
        + "    {\n"
        + "      \"content\": \"" + systemContent + "\" ,\n"
        + "      \"role\": \"" + systemRole + "\" \n"
        + "    },\n"
        + "    {\n"
        + "      \"content\": \"" + content + "\" ,\n"
        + "      \"role\": \"user\"\n"
        + "    }\n"
        + "  ],\n"
        + "  \"model\": \"" + model + "\" ,\n"
        + "  \"frequency_penalty\": 0,\n"
        + "  \"max_tokens\": 2048,\n"
        + "  \"presence_penalty\": 0,\n"
        + "  \"response_format\": " + response_format + ",\n"
        + "  \"stop\": null,\n"
        + "  \"stream\": false,\n"
        + "  \"stream_options\": null,\n"
        + "  \"temperature\": 1,\n"
        + "  \"top_p\": 1,\n"
        + "  \"tools\": null,\n"
        + "  \"tool_choice\": \"none\",\n"
        + "  \"logprobs\": false,\n"
        + "  \"top_logprobs\": null\n"
        + "}";

    RequestBody body = RequestBody.create(mediaType,
        content1);
    Request request = new Request.Builder().url("https://api.deepseek.com/chat/completions").method("POST", body)
        .addHeader("Content-Type", "application/json").addHeader("Accept", "application/json")
        .addHeader("Authorization", "Bearer " + token).build();
    try {
      Response response = client.newCall(request).execute();
      ResponseBody body1 = response.body();
      String string1 = body1.string();
      System.out.println(string1);

      String s = processAndExtractContent(string1);
      System.out.println(s);
      return response.toString();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static String processAndExtractContent(String responseBody) throws IOException {

    StringBuilder content = new StringBuilder();
    try (BufferedReader reader = new BufferedReader(new StringReader(responseBody))) {
      String line;
      while ((line = reader.readLine()) != null) {
        Optional<String> extractedContent = extractContentFromLine(line);
        extractedContent.ifPresent(content::append);
      }
    }
    return content.toString();
  }

  private static Optional<String> extractContentFromLine(String line) {
    String dataPrefix = "{\"id\"";

    if (!line.startsWith(dataPrefix)) {
      return Optional.empty();
    }
    String content = extractContentFromLine0(line);
    return Optional.ofNullable(content);
  }

  protected static String extractContentFromLine0(String line) {
    ChatCompletionResponse chatCompletionResponse =
        JSONObject.parseObject(line, ChatCompletionResponse.class);
    return chatCompletionResponse.getChoices().get(0).getMessage().getContent();
  }


}
