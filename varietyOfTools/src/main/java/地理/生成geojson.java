package 地理;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class 生成geojson {
  public static void main(String[] args) {
    生成线();

    //
  }

  private static void 生成点() {
    Map<String,String> map = new HashMap<>();
    map.put("-94.149", "36.33");
    JSONObject featureCollection = new JSONObject();
    try {
      featureCollection.put("type","FeatureCollection");
      JSONArray featureList = new JSONArray();
      // 遍历你的列表
      for (Entry<String, String> stringStringEntry : map.entrySet()) {
        // {"geometry":{"type":"Point","coordinates":[-94.149, 36.33]}
        JSONObject point = new JSONObject();
        point.put("type","Point");
        //从字符串构造JSONArray;也可以使用数组或列表
        JSONArray coord = new JSONArray(Collections.singletonList(
            "[" + stringStringEntry.getKey() + "," + stringStringEntry.getValue() + "]"));
        point.put("coordinates",coord);
        JSONObject feature = new JSONObject();
        feature.put("geometry",point);
        featureList.add(feature);
        featureCollection.put("features",featureList);
      }
    } catch(JSONException e){
      System.out.println("?????");
    }
    //输出结果
    System.out.println("featureCollection ="+ featureCollection.toString());
  }

  private static void 生成线() {
    Map<Double,Double> map = new HashMap<>();
    map.put(-94.149, 36.33);
    map.put(-95.149, 37.33);
    JSONObject featureCollection = new JSONObject();
    try {
      featureCollection.put("type","Feature");
      featureCollection.put("properties", new JSONObject());
      JSONObject geometry = new JSONObject();
      geometry.put("type","LineString");
      JSONArray coordinates = new JSONArray();
      // 遍历你的列表
      for (Entry<Double, Double> stringStringEntry : map.entrySet()) {
        JSONArray coordinate = new JSONArray();
        coordinate.add(stringStringEntry.getKey());
        coordinate.add(stringStringEntry.getValue());
        coordinates.add(coordinate);
      }
      geometry.put("coordinates", coordinates);
      featureCollection.put("geometry", geometry);
    } catch(JSONException e){
      System.out.println("?????");
    }
    //输出结果
    System.out.println("featureCollection ="+ featureCollection.toString());
  }
}
