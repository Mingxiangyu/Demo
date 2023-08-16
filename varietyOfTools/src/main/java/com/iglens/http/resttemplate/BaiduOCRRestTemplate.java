package com.iglens;

import com.alibaba.fastjson.JSONObject;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

/** 百度OCR接口请求 */
@Component
@Slf4j
public class BaiduOCRRestTemplate {

  @Autowired RestTemplate restTemplate;

  @Value("${baiduapi.ocr.url}")
  private String baiduOcrUrl;

  /** 扫描PDF */
  public static String is_scanned_true = "true";

  /** 文字PDF */
  public static String is_scanned_false = "false";

  /** 默认基础格式配置 */
  public static String base_style =
      "{\"title\":{\"cn_font\":{\"name\":\"黑体\",\"size\":\"16pt\"},\"en_font\":{\"name\":\"黑体\",\"size\":\"16pt\"}},\"content\":{\"cn_font\":{\"name\":\"仿宋_GB2312\",\"size\":\"16pt\"},\"en_font\":{\"name\":\"仿宋_GB2312\",\"size\":\"16pt\"}}}";

  /** 文件类型：普通文件 */
  public static String doc_type_general = "general";

  /** 文件类型：红头文件 */
  public static String doc_type_confidential = "confidential";

  /**
   * 创建任务 通用版
   *
   * @param file 待处理文件
   * @return
   * @throws IOException
   */
  public HttpEntity createTask(MultipartFile file) throws IOException {
    JSONObject parse = JSONObject.parseObject(base_style);
    return this.createTask(
        new ObjectId().toString(), doc_type_general, is_scanned_false, parse.toJSONString(), file);
  }

  /**
   * 创建任务 通用版
   *
   * @param file 待处理文件
   * @return
   * @throws IOException
   */
  public HttpEntity createTaskNs(File file) throws IOException {
    JSONObject parse = JSONObject.parseObject(base_style);
    return this.createTask(
        new ObjectId().toString(), doc_type_general, is_scanned_false, parse.toJSONString(), file);
  }

  /**
   * 创建任务 通用版
   *
   * @param file 待处理文件
   * @return
   * @throws IOException
   */
  public HttpEntity createTask(File file) throws IOException {
    JSONObject parse = JSONObject.parseObject(base_style);
    return this.createTask(
        new ObjectId().toString(), doc_type_general, is_scanned_true, parse.toJSONString(), file);
  }

  /**
   * 创建任务
   *
   * @param taskName 任务名称
   * @param docType 文件类型
   * @param isScanned 扫描PDF / 文字PDF
   * @param style 格式配置
   * @param file 待处理文件
   * @return
   */
  public HttpEntity createTask(
      String taskName, String docType, String isScanned, String style, File file) {
    MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
    param.add("name", taskName);
    param.add("doc_type", docType);
    param.add("is_scanned", isScanned);
    param.add("style", style);
    FileSystemResource resource = new FileSystemResource(file);
    param.add("file", resource);
    String url = baiduOcrUrl + "/task";
    HttpHeaders headers = new HttpHeaders();
    headers.add("request_id", UUID.randomUUID().toString().replaceAll("-", ""));
    headers.setContentType(MediaType.MULTIPART_FORM_DATA);
    headers.setAccept(Arrays.asList(MediaType.TEXT_HTML));
    HttpEntity<MultiValueMap<String, Object>> r = new HttpEntity<>(param, headers);
    HttpEntity response = null;

    try {
      response = restTemplate.exchange(url, HttpMethod.POST, r, HttpEntity.class).getBody();
    } catch (RestClientException e) {
      log.info("error -> url: {}", url);
      log.info("error -> param: {}", param);
      log.info("error -> exception: {}", e);
    }
    return response;
  }

  /**
   * 创建任务
   *
   * @param taskName
   * @param docType
   * @param isScanned
   * @param style
   * @param file
   * @return
   */
  public HttpEntity createTask(
      String taskName, String docType, String isScanned, String style, MultipartFile file)
      throws IOException {
    MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
    param.add("name", taskName);
    param.add("doc_type", docType);
    param.add("is_scanned", isScanned);
    param.add("style", style);
    param.add("file", file.getResource());
    String url = baiduOcrUrl + "/task";
    HttpHeaders headers = new HttpHeaders();
    headers.add("request_id", UUID.randomUUID().toString().replaceAll("-", ""));
    headers.setContentType(MediaType.MULTIPART_FORM_DATA);
    headers.setAccept(Arrays.asList(MediaType.TEXT_HTML));
    HttpEntity<MultiValueMap<String, Object>> r = new HttpEntity<>(param, headers);
    HttpEntity response = null;

    try {
      response = restTemplate.exchange(url, HttpMethod.POST, r, HttpEntity.class).getBody();
    } catch (RestClientException e) {
      log.info("error -> url: {}", url);
      log.info("error -> param: {}", param);
      log.info("error -> exception: {}", e);
    }
    return response;
  }

  /**
   * 任务多条件查询接口
   *
   * @param id
   * @param name
   * @param pageSize
   * @param pageNo
   * @param orderby
   * @param order
   * @return
   */
  public HttpEntity pageTask(
      String id, String name, Integer pageSize, Integer pageNo, String orderby, String order) {
    MultiValueMap<String, Object> param = new LinkedMultiValueMap();
    param.add("Id", id);
    //        param.add("name", name);
    param.add("page_size", pageSize);
    param.add("page_no", pageNo);
    param.add("orderby", orderby);
    param.add("order", order);
    String url = baiduOcrUrl + "/task";
    if (StringUtils.isNotBlank(name)) {
      url = baiduOcrUrl + "/task" + "?name=" + name;
    }
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set("request_id", UUID.randomUUID().toString().replaceAll("-", ""));
    HttpEntity<MultiValueMap> r = new HttpEntity<>(param, headers);
    HttpEntity response = null;

    try {
      response = restTemplate.exchange(url, HttpMethod.GET, r, HttpEntity.class).getBody();
    } catch (RestClientException e) {
      log.info("error -> url: {}", url);
      log.info("error -> param: {}", param);
      log.info("error -> exception: {}", e);
    }
    return response;
  }

  /**
   * 任务删除接口
   *
   * @param taskId
   */
  public HttpEntity deleteTask(List<String> taskId) {
    MultiValueMap<String, Object> param = new LinkedMultiValueMap();
    param.add("task_id", taskId);
    String url = baiduOcrUrl + "/task";
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set("request_id", UUID.randomUUID().toString().replaceAll("-", ""));
    HttpEntity<MultiValueMap> r = new HttpEntity<>(param, headers);
    HttpEntity response = null;
    try {
      response = restTemplate.exchange(url, HttpMethod.DELETE, r, HttpEntity.class).getBody();
    } catch (RestClientException e) {
      log.info("error -> url: {}", url);
      log.info("error -> param: {}", param);
      log.info("error -> exception: {}", e);
    }
    return response;
  }

  /**
   * 某任务文档多条件查询接口
   *
   * @param task_id
   * @param name
   * @param pageSize
   * @param pageNo
   * @param orderby 排序字段 start_time/end_time
   * @param order desc/asc
   * @return
   */
  public HttpEntity pageDocByTask(
      String task_id, String name, Integer pageSize, Integer pageNo, String orderby, String order) {
    MultiValueMap<String, Object> param = new LinkedMultiValueMap();
    param.add("name", name);
    param.add("page_size", pageSize);
    param.add("page_no", pageNo);
    param.add("orderby", orderby);
    param.add("order", order);

    String url = baiduOcrUrl + "/task/" + task_id + "/doc";
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set("request_id", UUID.randomUUID().toString().replaceAll("-", ""));
    HttpEntity<MultiValueMap> r = new HttpEntity<>(param, headers);
    HttpEntity response = null;

    try {
      response = restTemplate.exchange(url, HttpMethod.GET, r, HttpEntity.class).getBody();
    } catch (RestClientException e) {
      log.info("error -> url: {}", url);
      log.info("error -> param: {}", param);
      log.info("error -> exception: {}", e);
    }
    return response;
  }

  public HttpEntity pageDoc(
      String id, String name, Integer pageSize, Integer pageNo, String orderby, String order) {
    MultiValueMap<String, Object> param = new LinkedMultiValueMap();
    //        param.add("Id", id);
    //        JSONObject param = new JSONObject();
    param.add("Id", id);
    param.add("name", name);
    param.add("page_size", pageSize);
    param.add("page_no", pageNo);
    param.add("orderby", orderby);
    param.add("order", order);

    String url = baiduOcrUrl + "/doc";
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
    headers.set("request_id", UUID.randomUUID().toString().replaceAll("-", ""));
    HttpEntity<MultiValueMap> r = new HttpEntity<>(param, headers);
    HttpEntity response = null;

    try {
      response = restTemplate.exchange(url, HttpMethod.GET, r, HttpEntity.class).getBody();
    } catch (RestClientException e) {
      log.info("error -> url: {}", url);
      log.info("error -> param: {}", param);
      log.info("error -> exception: {}", e);
    }
    return response;
  }

  /**
   * 文档删除
   *
   * @param doc_id
   */
  public HttpEntity deleteDoc(List<String> doc_id) {
    MultiValueMap<String, Object> param = new LinkedMultiValueMap();
    param.add("doc_id", doc_id);
    String url = baiduOcrUrl + "/doc";

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.set("request_id", UUID.randomUUID().toString().replaceAll("-", ""));
    HttpEntity<MultiValueMap> r = new HttpEntity<>(param, headers);
    try {
      return restTemplate.exchange(url, HttpMethod.DELETE, r, HttpEntity.class).getBody();
    } catch (RestClientException e) {
      log.info("error -> url: {}", url);
      log.info("error -> param: {}", param);
      log.info("error -> exception: {}", e);
    }
    return null;
  }

  /**
   * 文档下载接口
   *
   * @param doc_id
   * @param task_id
   * @param type 文档类型：source、docx 原文件或pdf
   * @return
   */
  public HttpEntity downloadDoc(List<String> doc_id, List<String> task_id, String type) {
    String url = baiduOcrUrl + "/doc/download";
    JSONObject params = new JSONObject();
    params.put("doc_id", doc_id);
    params.put("task_id", task_id);
    params.put("type", type);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
    headers.set("request_id", UUID.randomUUID().toString().replaceAll("-", ""));
    HttpEntity<JSONObject> r = new HttpEntity<>(params, headers);

    HttpEntity response = null;

    try {
      ResponseEntity<HttpEntity> response1 = restTemplate.postForEntity(url, r, HttpEntity.class);
      response = response1.getBody();
    } catch (RestClientException e) {
      log.info("error -> url: {}", url);
      log.info("error -> exception: {}", e);
    }
    return response;
  }

  /**
   * 创建任务
   *
   * @param baiduOcrCreateTaskParam 参数
   * @return
   */
  // public HttpEntity createTask(BaiduOcrCreateTaskParam baiduOcrCreateTaskParam) {
  //     MultiValueMap<String, Object> param = new LinkedMultiValueMap<>();
  //     param.add("name", baiduOcrCreateTaskParam.getName());
  //     param.add("doc_type", baiduOcrCreateTaskParam.getDoc_type());
  //     param.add("is_scanned", baiduOcrCreateTaskParam.getIs_scanned());
  //     param.add("style", baiduOcrCreateTaskParam.getStyle());
  //     List<MultipartFile> files = baiduOcrCreateTaskParam.getFile();
  //     for (MultipartFile file : files) {
  //         param.add("file", file.getResource());
  //     }
  //     String url = baiduOcrUrl + "/task";
  //     HttpHeaders headers = new HttpHeaders();
  //     headers.add("request_id", UUID.randomUUID().toString().replaceAll("-", ""));
  //     headers.setContentType(MediaType.MULTIPART_FORM_DATA);
  //     headers.setAccept(Arrays.asList(MediaType.TEXT_HTML));
  //     HttpEntity<MultiValueMap<String, Object>> r = new HttpEntity<>(param, headers);
  //     HttpEntity response = null;
  //
  //     try {
  //         response = restTemplate.exchange(url, HttpMethod.POST, r, HttpEntity.class).getBody();
  //     } catch (RestClientException e) {
  //         log.info("error -> url: {}", url);
  //         log.info("error -> param: {}", param);
  //         log.info("error -> exception: {}", e);
  //     }
  //     return response;
  // }

  public static void main(String[] args) {
    RestTemplate restTemplate = new RestTemplate();
    String wfsSearchUrl = "http://36.41.73.198:16097/api/mapservice/wfs";

    String typeName = "gadm_行政区划_瓦利斯纳群岛_41_v1";
    String geo =
        "POLYGON((-176.7041 -12.7788,-176.7041 -13.7788,-175.7041 -13.7788,-175.7041 -12.7788,-176.7041 -12.7788))";

    MultiValueMap<String, Object> param = new LinkedMultiValueMap();
    param.add("service", "WFS");
    param.add("version", "1.0.0");
    param.add("request", "GetFeature");
    param.add("outputFormat", "json");
    param.add("typeName", "gserver:" + typeName);
    param.add("CQL_FILTER", "INTERSECTS(the_geom, " + geo + ")");

    HttpHeaders headers = new HttpHeaders();
    headers.set("Accept-Encoding", "identity");
    headers.set("Accept", "application/json, text/plain, */*");
    HttpEntity<MultiValueMap> r = new HttpEntity<>(param, headers);
    HttpEntity response = null;

    try {
      response = restTemplate.exchange(wfsSearchUrl, HttpMethod.GET, r, HttpEntity.class).getBody();
    } catch (RestClientException e) {
      log.info("error -> url: {}", wfsSearchUrl);
      log.info("error -> param: {}", param);
      log.info("error -> exception: {}", e);
    }
    System.out.println(response);
  }
}
