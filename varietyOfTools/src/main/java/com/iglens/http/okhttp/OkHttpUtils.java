package com.daosmos.jqtt.common.utils;

import com.alibaba.fastjson.JSON;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.ConnectionPool;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author xming
 */
public class OkHttpUtils {

  private static volatile OkHttpClient okHttpClient = null;
  private static volatile Semaphore semaphore = null;
  private Map<String, String> headerMap;
  private Map<String, Object> paramMap;
  private String url;
  private Request.Builder request;

  /** 初始化okHttpClient，并且允许https访问 */
  private OkHttpUtils() {
    if (okHttpClient == null) {
      synchronized (OkHttpUtils.class) {
        if (okHttpClient == null) {

          // 设置代理方式
          //                    Proxy proxy = new Proxy(Proxy.Type.HTTP, new
          // InetSocketAddress("127.0.0.1", 8080));

          TrustManager[] trustManagers = buildTrustManagers();
          okHttpClient =
              new OkHttpClient.Builder()
                  // 设置连接超时时间
                  .connectTimeout(60, TimeUnit.SECONDS)
                  // 写入超时时间
                  .writeTimeout(60, TimeUnit.SECONDS)
                  // 从连接成功到响应的总时间
                  .readTimeout(60, TimeUnit.SECONDS)
                  // 跳过ssl认证(https)
                  .sslSocketFactory(
                      createSSLSocketFactory(trustManagers), (X509TrustManager) trustManagers[0])
                  .hostnameVerifier((hostName, session) -> true)
                  .retryOnConnectionFailure(true)
                  //                            .proxy(proxy)//代理ip
                  // 设置连接池  最大连接数量  , 持续存活的连接
                  .connectionPool(new ConnectionPool(50, 10, TimeUnit.MINUTES))
                  .build();
          addHeader(
              "User-Agent",
              "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
        }
      }
    }
  }

  /** 用于异步请求时，控制访问线程数，返回结果 */
  private static Semaphore getSemaphoreInstance() {
    // 只能1个线程同时访问
    synchronized (OkHttpUtils.class) {
      if (semaphore == null) {
        semaphore = new Semaphore(0);
      }
    }
    return semaphore;
  }

  /** 创建OkHttpUtils */
  public static OkHttpUtils builder() {
    return new OkHttpUtils();
  }

  /** 添加url */
  public OkHttpUtils url(String url) {
    this.url = url;
    return this;
  }

  /**
   * 添加参数
   *
   * @param key 参数名
   * @param value 参数值
   */
  public OkHttpUtils addParam(String key, Object value) {
    if (paramMap == null) {
      paramMap = new LinkedHashMap<>(16);
    }
    if (value != null) {
      paramMap.put(key, value);
    }
    return this;
  }
  /**
   * 添加参数
   *
   * @param values 参数值
   */
  public OkHttpUtils addObject(Object values) {
    if (paramMap == null) {
      paramMap = new LinkedHashMap<>(16);
    }
    Map<String, Object> map = new LinkedHashMap<>();
    Class<?> clazz = values.getClass();
    for (Field field : clazz.getDeclaredFields()) {
      field.setAccessible(true);
      try {
        map.put(field.getName(), field.get(values));
      } catch (IllegalAccessException e) {
        throw new RuntimeException("请填写正确的参数：" + field.getName());
      }
    }
    paramMap.putAll(map);
    return this;
  }
  /**
   * 添加请求头
   *
   * @param key 参数名
   * @param value 参数值
   */
  public OkHttpUtils addHeader(String key, String value) {
    if (headerMap == null) {
      headerMap = new LinkedHashMap<>(16);
    }
    headerMap.put(key, value);
    return this;
  }

  /** 初始化get方法 */
  public OkHttpUtils get() {
    request = new Request.Builder().get();
    StringBuilder urlBuilder = new StringBuilder(url);
    if (paramMap != null) {
      urlBuilder.append("?");
      try {
        for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
          urlBuilder
              .append(URLEncoder.encode(entry.getKey(), "utf-8"))
              .append("=")
              .append(URLEncoder.encode(String.valueOf(entry.getValue()), "utf-8"))
              .append("&");
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
      urlBuilder.deleteCharAt(urlBuilder.length() - 1);
    }
    request.url(urlBuilder.toString());
    return this;
  }

  /**
   * 初始化post方法
   *
   * <p>url路径后拼接
   */
  public OkHttpUtils post() {
    RequestBody requestBody;
    FormBody.Builder formBody = new FormBody.Builder();
    requestBody = formBody.build();
    StringBuilder urlBuilder = new StringBuilder(url);
    if (paramMap != null) {
      urlBuilder.append("?");
      try {
        for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
          urlBuilder
              .append(URLEncoder.encode(entry.getKey(), "utf-8"))
              .append("=")
              .append(URLEncoder.encode(String.valueOf(entry.getValue()), "utf-8"))
              .append("&");
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
      urlBuilder.deleteCharAt(urlBuilder.length() - 1);
    }
    request = new Request.Builder().post(requestBody).url(urlBuilder.toString());
    return this;
  }

  /**
   * 初始化post方法
   *
   * @param isJsonPost true等于json的方式提交数据，类似postman里post方法的raw false等于普通的表单提交
   */
  public OkHttpUtils post(boolean isJsonPost) {
    RequestBody requestBody;
    if (isJsonPost) {
      String json = "";
      if (paramMap != null) {
        json = JSON.toJSONString(paramMap);
        // System.out.println(json);
      }
      requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
    } else {
      FormBody.Builder formBody = new FormBody.Builder();
      if (paramMap != null) {
        for (String key : paramMap.keySet()) {
          formBody.add(key, String.valueOf(paramMap.get(key)));
        }
      }
      requestBody = formBody.build();
    }
    request = new Request.Builder().post(requestBody).url(url);
    return this;
  }

  public OkHttpUtils postJson(String jsonString) {
    RequestBody requestBody = RequestBody.create(
        MediaType.parse("application/json; charset=utf-8"),
        jsonString
    );
    request = new Request.Builder().post(requestBody).url(url);
    return this;
}

  /** 初始化post方法 */
  public OkHttpUtils postForXml(String xmlStr) {
    RequestBody requestBody = RequestBody.create(MediaType.parse("application/xml"), xmlStr);
    request = new Request.Builder().post(requestBody).url(url);
    return this;
  }

  /**
   * post上传文件
   *
   * <p>原文链接：https://blog.csdn.net/qq_36699930/article/details/84335581
   *
   * @param fileList
   */
  public OkHttpUtils postFile(List<File> fileList) {
    MultipartBody.Builder builder = new MultipartBody.Builder();
    builder.setType(MultipartBody.FORM);

    // 参数
    if (paramMap != null) {
      for (String s : paramMap.keySet()) {
        builder.addFormDataPart(s, String.valueOf(paramMap.get(s)));
      }
    }
    if (fileList != null) {
      for (File file : fileList) {
        builder.addFormDataPart(
            "file",
            file.getName(),
            RequestBody.create(MediaType.parse("application/octet-stream"), file));
      }
    }

    MultipartBody multipartBody = builder.build();
    request = new Request.Builder().url(url).post(multipartBody);
    return this;
  }

  public OkHttpUtils put(boolean isJsonPost) {
    RequestBody requestBody;
    if (isJsonPost) {
      String json = "";
      if (paramMap != null) {
        json = JSON.toJSONString(paramMap);
        System.out.println(json);
      }
      requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
    } else {
      FormBody.Builder formBody = new FormBody.Builder();
      if (paramMap != null) {
        for (String key : paramMap.keySet()) {
          formBody.add(key, String.valueOf(paramMap.get(key)));
        }
      }
      requestBody = formBody.build();
    }
    request = new Request.Builder().put(requestBody).url(url);
    return this;
  }

  /**
   * put 纯文本上传
   *
   * @param text
   * @return
   */
  public OkHttpUtils putText(String text) {
    RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), text);
    request = new Request.Builder().put(requestBody).url(url);
    return this;
  }

  public OkHttpUtils del() {
    String json = "";
    if (paramMap != null) {
      json = JSON.toJSONString(paramMap);
    }
    RequestBody requestBody =
        RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
    request = new Request.Builder().delete(requestBody).url(url);
    return this;
  }

  /** 同步请求 */
  public String sync() {
    setHeader(request);
    try {
      Response response = okHttpClient.newCall(request.build()).execute();
      assert response.body() != null;
      return response.body().string();
    } catch (IOException e) {
      e.printStackTrace();
      return "请求失败：" + e.getMessage();
    }
  }
  public Response sync1() throws IOException {
    setHeader(request);
    return okHttpClient.newCall(request.build()).execute();
  }
  /** 异步请求，有返回值 */
  public String async() {
    StringBuilder buffer = new StringBuilder("");
    setHeader(request);
    okHttpClient
        .newCall(request.build())
        .enqueue(
            new Callback() {
              @Override
              public void onFailure(Call call, IOException e) {
                buffer.append("请求出错：").append(e.getMessage());
              }

              @Override
              public void onResponse(Call call, Response response) throws IOException {
                assert response.body() != null;
                buffer.append(response.body().string());
                getSemaphoreInstance().release();
              }
            });
    try {
      getSemaphoreInstance().acquire();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return buffer.toString();
  }

  /** 异步请求，带有接口回调 */
  public void async(ICallBack callBack) {
    setHeader(request);
    okHttpClient
        .newCall(request.build())
        .enqueue(
            new Callback() {
              @Override
              public void onFailure(Call call, IOException e) {
                callBack.onFailure(call, e.getMessage());
              }

              @Override
              public void onResponse(Call call, Response response) throws IOException {
                assert response.body() != null;
                callBack.onSuccessful(call, response.body().string());
              }
            });
  }

  /** 为request添加请求头 */
  private void setHeader(Request.Builder request) {
    if (headerMap != null) {
      try {
        for (Map.Entry<String, String> entry : headerMap.entrySet()) {
          request.addHeader(entry.getKey(), entry.getValue());
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  /** 生成安全套接字工厂，用于https请求的证书跳过 */
  private static SSLSocketFactory createSSLSocketFactory(TrustManager[] trustAllCerts) {
    SSLSocketFactory ssfFactory = null;
    try {
      SSLContext sc = SSLContext.getInstance("SSL");
      sc.init(null, trustAllCerts, new SecureRandom());
      ssfFactory = sc.getSocketFactory();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return ssfFactory;
  }

  private static TrustManager[] buildTrustManagers() {
    return new TrustManager[] {
      new X509TrustManager() {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) {}

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) {}

        @Override
        public X509Certificate[] getAcceptedIssuers() {
          return new X509Certificate[] {};
        }
      }
    };
  }

  /** 自定义一个接口回调 */
  public interface ICallBack {

    void onSuccessful(Call call, String data);

    void onFailure(Call call, String errorMsg);
  }
}
