package com.iglens.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;
import org.apache.commons.io.FileUtils;

public class FastJson {
  public static void main(String[] args) {
    // 1、生成json文件--测试
    List<Object> agencyList = new ArrayList<Object>();
    Map<String, Object> agencyMap = new HashMap<>();
    agencyMap.put("name", "张三");
    agencyMap.put("address", "北京市");
    agencyMap.put("companyName", "中国");
    agencyMap.put("logoImageId", "668");
    agencyMap.put("auctionAddress", "未知");
    agencyMap.put("logoImage", "你猜");
    agencyList.add(agencyMap);

    // 将集合数据转换为json字符串(当然map集合亦可以)：
    JSONArray jsonArray = new JSONArray(agencyList);
    // 添加 SerializerFeature.PrettyFormat后，导出的json有格式
    String jsonString1 =
        JSONObject.toJSONString(jsonArray.toString(), SerializerFeature.PrettyFormat);
    //    String jsonString1 = jsonArray.toString();
    createJsonFile(jsonString1, "C:\\Users\\T480s\\Desktop\\", "user");
    String pathname = "C:\\Users\\T480s\\Desktop\\user.json";

    // 2、读取json文件
    try {
      readJsonData(pathname);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
  // ----------------读取json文件
  /**
   * 读取文件数据加入到map缓存中
   *
   * @throws java.io.IOException
   */
  public static void readJsonData(String pathname) throws IOException {
    File file = new File(pathname);
    String jsonString = FileUtils.readFileToString(file, "UTF-8");
    List<User> userList = JSON.parseArray(jsonString, User.class);
    for (User user : userList) {
      System.out.println(user.getName());
    }
    System.out.println(JSON.toJSONString(userList, true));
  }

  /**
   * 读取Json文件
   * @param path json路径
   * @return json中数据
   */
  public static String readJsonFile(String path) {
    String jsonStr;
    try {
      File jsonFile = new File(path);
      Reader reader = new InputStreamReader(new FileInputStream(jsonFile), StandardCharsets.UTF_8);
      int ch;
      StringBuilder sb = new StringBuilder();
      while ((ch = reader.read())!=-1){
        sb.append((char)ch);
      }
      reader.close();
      jsonStr = sb.toString();
      return jsonStr;

    }catch (IOException e){
      e.printStackTrace();
      return null;
    }
  }

  /** 生成.json格式文件 */
  public static boolean createJsonFile(String jsonString, String filePath, String fileName) {
    // 标记文件生成是否成功
    boolean flag = true;

    // 拼接文件完整路径
    String fullPath = filePath + File.separator + fileName + ".json";

    // 生成json格式文件
    try {
      // 保证创建一个新文件
      File file = new File(fullPath);
      if (!file.getParentFile().exists()) { // 如果父目录不存在，创建父目录
        file.getParentFile().mkdirs();
      }
      if (file.exists()) { // 如果已存在,删除旧文件
        file.delete();
      }
      file.createNewFile();

      if (jsonString.indexOf("'") != -1) {
        // 将单引号转义一下，因为JSON串中的字符串类型可以单引号引起来的
        jsonString = jsonString.replaceAll("'", "\\'");
      }
      if (jsonString.indexOf("\"") != -1) {
        // 将双引号转义一下，因为JSON串中的字符串类型可以单引号引起来的
        jsonString = jsonString.replaceAll("\"", "\\\"");
      }

      if (jsonString.indexOf("\r\n") != -1) {
        // 将回车换行转换一下，因为JSON串中字符串不能出现显式的回车换行
        jsonString = jsonString.replaceAll("\r\n", "\\u000d\\u000a");
      }
      if (jsonString.indexOf("\n") != -1) {
        // 将换行转换一下，因为JSON串中字符串不能出现显式的换行
        jsonString = jsonString.replaceAll("\n", "\\u000a");
      }

      // 格式化json字符串
      jsonString = formatJson(jsonString);

      // 将格式化后的字符串写入文件
      Writer write = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
      write.write(jsonString);
      write.flush();
      write.close();
    } catch (Exception e) {
      flag = false;
      e.printStackTrace();
    }

    // 返回是否成功的标记
    return flag;
  }

  /** 单位缩进字符串。 */
  private static String SPACE = "   ";

  /**
   * 返回格式化JSON字符串。
   *
   * @param json 未格式化的JSON字符串。
   * @return 格式化的JSON字符串。
   */
  public static String formatJson(String json) {
    StringBuffer result = new StringBuffer();

    int length = json.length();
    int number = 0;
    char key = 0;

    // 遍历输入字符串。
    for (int i = 0; i < length; i++) {
      // 1、获取当前字符。
      key = json.charAt(i);

      // 2、如果当前字符是前方括号、前花括号做如下处理：
      if ((key == '[') || (key == '{')) {
        // （1）如果前面还有字符，并且字符为“：”，打印：换行和缩进字符字符串。
        if ((i - 1 > 0) && (json.charAt(i - 1) == ':')) {
          result.append('\n');
          result.append(indent(number));
        }

        // （2）打印：当前字符。
        result.append(key);

        // （3）前方括号、前花括号，的后面必须换行。打印：换行。
        result.append('\n');

        // （4）每出现一次前方括号、前花括号；缩进次数增加一次。打印：新行缩进。
        number++;
        result.append(indent(number));

        // （5）进行下一次循环。
        continue;
      }

      // 3、如果当前字符是后方括号、后花括号做如下处理：
      if ((key == ']') || (key == '}')) {
        // （1）后方括号、后花括号，的前面必须换行。打印：换行。
        result.append('\n');

        // （2）每出现一次后方括号、后花括号；缩进次数减少一次。打印：缩进。
        number--;
        result.append(indent(number));

        // （3）打印：当前字符。
        result.append(key);

        // （4）如果当前字符后面还有字符，并且字符不为“，”，打印：换行。
        if (((i + 1) < length) && (json.charAt(i + 1) != ',')) {
          result.append('\n');
        }

        // （5）继续下一次循环。
        continue;
      }

      // 4、如果当前字符是逗号。逗号后面换行，并缩进，不改变缩进次数。
      /*if ((key == ',')) {
          result.append(key);
          result.append('\n');
          result.append(indent(number));
          continue;
      }*/

      // 5、打印：当前字符。
      result.append(key);
    }

    return result.toString();
  }

  /**
   * 返回指定次数的缩进字符串。每一次缩进三个空格，即SPACE。
   *
   * @param number 缩进次数。
   * @return 指定缩进次数的字符串。
   */
  private static String indent(int number) {
    StringBuffer result = new StringBuffer();
    for (int i = 0; i < number; i++) {
      result.append(SPACE);
    }
    return result.toString();
  }
}

@Data
class User {
  private String name;
  private String address;
  private String companyName;
  private String logoImageId;
}
