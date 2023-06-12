package com.iglens.http.hutool;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import java.util.Map;

/**
 * http请求工具类
 */
public class HttpUtils {


	/**
	 * Get 请求
	 *
	 * @param url 请求全路径
	 * @return
	 */
	public static String sendGet(String url) {
		return HttpUtil.get(url);
	}

	/**
	 * Get 请求
	 *
	 * @param url      请求全路径
	 * @param mapParam 参数类型: query
	 * @return
	 */
	public static String sendGet(String url, Map<String, Object> mapParam) {
		return HttpUtil.get(url, mapParam, 3000);
	}

	/**
	 * Post 请求
	 *
	 * @param url      请求全路径
	 * @param mapParam 参数类型: query
	 * @return
	 */
	public static String sendPost(String url, Map<String, Object> mapParam) {
		return HttpUtil.post(url, mapParam, 3000);
	}

	/**
	 * Post 请求
	 *
	 * @param url  请求全路径
	 * @param body 参数类型: Json请求体
	 * @return
	 */
	public static String sendPost(String url, String body) {
		return HttpUtil.post(url, body, 3000);
	}

	/**
	 * PUT 请求
	 *
	 * @param url      请求全路径
	 * @param mapParam 参数类型：query
	 * @return
	 */
	public static String sendPut(String url, Map<String, Object> mapParam) {
		return HttpRequest.put(url).form(mapParam).timeout(3000).execute().body();
	}

	/**
	 * PUT 请求
	 *
	 * @param url  请求全路径
	 * @param body 参数类型: Json请求体
	 * @return
	 */
	public static String sendPut(String url, String body) {
		return HttpRequest.put(url).body(body).timeout(3000).execute().body();
	}

	/**
	 * Delete 请求
	 *
	 * @param url      请求全路径
	 * @param mapParam 参数类型: query
	 * @return
	 */
	public static String sendDelete(String url, Map<String, Object> mapParam) {
		return HttpRequest.delete(url).form(mapParam).timeout(3000).execute().body();
	}

	/**
	 * Delete 请求
	 *
	 * @param url  请求全路径
	 * @param body 参数类型: Json请求体
	 * @return
	 */
	public static String sendDelete(String url, String body) {
		return HttpRequest.delete(url).body(body).timeout(3000).execute().body();
	}
}
