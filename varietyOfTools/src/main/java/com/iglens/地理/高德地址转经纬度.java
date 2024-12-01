package com.iglens.地理;
 
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
 
/**
 * 地址转经纬度（使用高德地图获取地址信息的经纬度）
 * https://lbs.amap.com/api/webservice/guide/api/georegeo/
 */
public class 高德地址转经纬度 {
    /**
     * Key
     */
    private static String KEY="1736f136af56af6a0be7239e2a936f63";
 
    public  static String GD_URL="https://restapi.amap.com/v3/geocode/geo?address=%s&key=%s";
 
    /**
     * 成功标识
     */
    private static String SUCCESS_FLAG="1";
 
    /**
     * 根据地址获取对应的经纬度信息
     * @param address
     * @return
     */
    public static String getLonAndLatByAddress(String address){
        String location="";
        GD_URL = String.format(GD_URL, address, KEY);
        //高德接口返回的是JSON格式的字符串
        String queryResult = getResponse(GD_URL);
        JSONObject obj = JSONObject.parseObject(queryResult);
        if(String.valueOf(obj.get("status")).equals(SUCCESS_FLAG)){
            JSONArray geocodes = obj.getJSONArray("geocodes");
            JSONObject jobJSON = JSONObject.parseObject(geocodes.get(0).toString());
            location = String.valueOf(jobJSON.get("location"));
        }else{
            throw new RuntimeException("地址转换经纬度失败，错误码：" + obj.get("infocode"));
        }
        return location;
    }
    /**
     * 发送请求
     *
     * @param serverUrl 请求地址
     */
    private static String getResponse(String serverUrl) {
        // 用JAVA发起http请求，并返回json格式的结果
        StringBuffer result = new StringBuffer();
        try {
            URL url = new URL(serverUrl);
            URLConnection conn = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }
 
    public static void main(String[] args) {
        String address="北京市朝阳区阜通东大街6号";
        String location=getLonAndLatByAddress(address);
        System.out.println("经纬度：" + location);
    }
}
 