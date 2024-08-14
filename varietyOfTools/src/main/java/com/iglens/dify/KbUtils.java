package com.iglens.dify;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.iglens.http.okhttp.OkHttpUtils;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

public class KbUtils {

  static String difyUrl = "http://192.168.4.178:5001/v1/";

  public static void main(String[] args) {
    String text = "F-35 战斗机（英文：F-35，绰号：Lightning II，译文：“闪电Ⅱ”），是美国一型单座单发战斗\n"
        + "机/联合攻击机，在世界上属于第五代战斗机，是世界上最大的单发单座舰载战斗机和世界\n"
        + "上仅有一种已服役的舰载第五代战斗机 。\n"
        + "F-35 战斗机具备较高的隐身设计、先进的电子系统以及一定的超音速巡航能力。主要用于\n"
        + "前线支援、目标轰炸、防空截击等多种任务，并因此发展出 3 种主要的衍生版本，包括采用\n"
        + "传统跑道起降的 F-35A 型，短距离起降/垂直起降的 F-35B 型，与作为航母舰载机的 F-35C\n"
        + "型。\n"
        + "F-35 战斗机由美国洛克希德·马丁（Lockheed Martin）研制，将会成为美国及其同盟国未来\n"
        + "最主要的第五代战斗机之一。2015 年 2 月 9 日在英国皇家空军中编列服役；2015 年 6 月 28\n"
        + "日，日本第一架 F-35 开始生产。2018 年 6 月 11 日，美国洛克希德·马丁公司生产的第 300\n"
        + "架 F-35 型战机交付用户。\n"
        + "2018 年 9 月 28 日，一架 F-35B 型战机在美国南卡罗来纳州进行训练飞行时坠毁，飞行员\n"
        + "成功逃生。10 月 11 日，美国国防部宣布，所有的 F-35 战机暂时停飞以进行检查。F35 没\n"
        + "有 F22 的升限，也没有 F22 的飞行速度，但它能在隐身方面打败 F22。 2021 年 5 月 24 日，\n"
        + "美国“军事”网站报道，一美国空军的高级将领表示，美国空军可能在未来 10 年里让一些用\n"
        + "于训练的老款 F-35 战斗机退役，转而购买最先进型号的 F-35。\n"
        + "2021 年 5 月 31 日，英国“伊丽莎白女王”号航母发生一起 F-35B 舰载战斗机紧急迫降事故。\n"
        + "2022 年 7 月 29 日，美军暂时让全部 F-35 战机停飞。\n"
        + "基本信息 \n"
        + "中文：F-35 战斗机\n"
        + "外文名：F-35 Lightning II Joint Strike Fighter\n"
        + "前型/级：F-16 和 F-14 \n"
        + "研制单位：洛克希德·马丁公司\n"
        + "定型时间：2006 年 12 月 15 日\n"
        + "国家：美国\n"
        + "机型：多用途战斗机\n"
        + "造价：1.5-2.5 亿美元\n"
        + "中文译名 F-35“闪电”II 型联合攻击战斗机 [84]\n"
        + "设计特点 \n"
        + "F-35 与前一代战斗机相比有如下进步：廉价耐用的隐身技术；综合的航电设备与感应器融\n"
        + "合可以结合从机载与非机载的感应器得到的讯息。这样不但可以增加驾驶员的状况感知，目\n"
        + "标识别与武器投射的能力，还可快速地传输讯息到其他的指挥及控制（C2）节点；IEEE-1394b\n"
        + "与光纤的数据交换网络；较低的维护成本；头盔显示器已经整合到了像 JAS-39、“幼狮”这样\n"
        + "的第四代战斗机上，而 F-35 已经利用头盔显示器完全替代抬头显示器，例如联合头盔显示\n"
        + "系统（Joint Helmet Mounted Cueing System，JHMCS）。\n"
        + "隐身设计 \n"
        + "F-35 的隐身设计借鉴了 F-22 的很多技术与经验，其 RCS(雷达反射面积)分析和计算，采用\n"
        + "整机计算机模拟（综合了进气道、吸波材料/结构等的影响），比 F-117A 的分段模拟后合成\n"
        + "更先进、全面和精确，同时可以保证飞机表面采用连续曲面设计。该型的头向 RCS 约为 0.065\n"
        + "平方米，比苏-27、F-15（空机前向 RCS 均超过 10 平方米）低两个数量级。由于 F-35 武器\n"
        + "采用内挂方式，不会引起 RCS 增大，隐身优势将更明显。\n"
        + "在红外隐身方面，从一些资料可推断出 F-35 在推力损失仅有 2%－3%的情况下，将尾喷管 3-\n"
        + "5 微米中波波段的红外辐射强度减弱了 80%－90%，同时使红外辐射波瓣的宽度变窄，减小\n"
        + "了红外制导空空导弹的可攻击区。F-35 的隐身设计，不仅减小了被发现的距离，还使全机\n"
        + "雷达散射及红外辐射中心发生改变，导致来袭导弹的脱靶率增大。这样 F-35 的主动干扰机、\n"
        + "光纤拖曳雷达诱饵、先进的红外诱饵弹等对抗设备也更容易奏效。\n"
        + "根据有关模型进行计算，取 F-35 的前向 RCS 为 0.1 平方米，与 10 平方米的情况比较，在\n"
        + "其他条件相同的情况下，前者的超视距空战效能比后者高出 5 倍左右。\n"
        + "性能数据 \n"
        + "F-35 战斗机性能数据：\n"
        + "乘员：1 人\n"
        + "长度：15.67 米\n"
        + "翼展：10.7 米\n"
        + "高度：4.33 米\n"
        + "机翼面积：42.7 平方米\n"
        + "空重：13,154 千克\n"
        + "最大起飞重量：31,800 千克\n"
        + "动力系统：1×普惠 F135 加力涡扇发动机\n"
        + "推力：最大推力：125 千牛；加力推力：191 千牛 [31]\n"
        + "参考性能：\n"
        + "最大飞行速度：1.6 马赫（1,930 千米/小时）\n"
        + "实用升限：18,288 米\n"
        + "航程：2,220 千米\n"
        + "作战半径：1081 千米\n"
        + "翼载荷：526 千克/平方米 [33]\n"
        + "推重比：0.61\n"
        + "武器系统\n"
        + "主要武器\n"
        + "空对空导弹：AIM-120“AMRAAM”先进中程空对空导弹、AIM-9X“超级响尾蛇”\n"
        + "AIM-132“ASRAAM”先进近距离空对空导弹、欧洲“流星”导弹\n"
        + "空对地导弹：“JASSM”联合空对地远距攻击导弹、小直径炸弹\n"
        + "风偏修正弹药洒布器（英语：Wind Corrected Munitions Dispenser）、AGM-158C 远程反舰\n"
        + "导弹\n"
        + "辅助武器：1 具 GAU-22/A 25 毫米机炮";
    String datasetId = "e89c3553-7691-46fe-9f72-b23e07e2e04c";
    String indexingTechnique = "high_quality";
    String filePath = "C:\\Users\\zhouhuilin\\Desktop\\F-35战斗机.pdf";

    // String apiKey = "dataset-50fnnvtTy2L0Oakf22VsXWue";
    String apiKey = "dataset-HpDpwLuc682cZjjx7WPYV9Yo";


    // createByText(datasetId, apiKey, "测试文档", text, indexingTechnique);
    // createByFile(datasetId, apiKey, indexingTechnique, filePath);
    // list(apiKey);
    // createKb(apiKey, "tesetdel");
    // del(apiKey, "0c6052b9-7138-4b34-baa7-7a67fc08b0b3");

    // documentList(apiKey, datasetId);
    // delDocument(apiKey, datasetId, "21eae3df-847b-4066-9cfd-5da249a43773");

    // addSegments(datasetId, "c8fbf24f-86d4-474f-91a4-41d695fb74a3", apiKey, text, null, null);
  }

  private static void createByText(String datasetId, String apiKey, String name, String text,
      String indexingTechnique) {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("mode", "automatic");
    jsonObject.put("rules", "");
    String process_rule = JSON.toJSONString(jsonObject);

    indexingTechnique = "high_quality";
    String sync = OkHttpUtils.builder()
        .url(difyUrl + "datasets/" + datasetId + "/document/create_by_text")
        .addHeader("Authorization", "Bearer " + apiKey)
        .addHeader("Content-Type", "application/json")
        .addParam("name", name)
        .addParam("text", text)
        .addParam("indexing_technique", indexingTechnique)
        .addParam("process_rule", process_rule)
        .post(true).sync();
    System.out.println(sync);
  }


  private static void createByFile(String datasetId, String apiKey, String indexingTechnique, String filePath) {
    DataSetBody dataSetBody = getDataSetBody(indexingTechnique);

    String string = JSONObject.toJSONString(dataSetBody);
    System.out.println(string);

    OkHttpClient client = new OkHttpClient().newBuilder()
        .build();
    RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
        .addFormDataPart("data", null,
            RequestBody.create(MediaType.parse("text/plain"), string))
        .addFormDataPart("file", FileUtil.getName(filePath),
            RequestBody.create(MediaType.parse("application/octet-stream"),
                new File(filePath)))
        .build();

    Request request = new Request.Builder()
        .url(difyUrl + "datasets/" + datasetId + "/document/create_by_file")
        .method("POST", body)
        .addHeader("Authorization", "Bearer " + apiKey)
        .build();
    try {
      Response response = client.newCall(request).execute();
      System.out.println(response);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @NotNull
  private static DataSetBody getDataSetBody(String indexingTechnique) {
    PreProcessingRule preProcessingRule = new PreProcessingRule();
    preProcessingRule.setId("remove_extra_spaces");
    preProcessingRule.setEnabled(true);

    PreProcessingRule preProcessingRule1 = new PreProcessingRule();
    preProcessingRule1.setId("remove_urls_emails");
    preProcessingRule1.setEnabled(true);

    ArrayList<PreProcessingRule> arrayList = new ArrayList<>();
    arrayList.add(preProcessingRule);
    arrayList.add(preProcessingRule1);

    Segmentation segmentation = new Segmentation();
    segmentation.setSeparator("###");
    segmentation.setMaxTokens(500);

    Rule rule = new Rule();
    rule.setSegmentation(segmentation);
    rule.setPreProcessingRules(arrayList);

    ProcessRule processRule = new ProcessRule();
    processRule.setRules(rule);
    processRule.setMode("custom");

    DataSetBody dataSetBody = new DataSetBody();
    dataSetBody.setProcessRule(processRule);
    dataSetBody.setIndexingTechnique(indexingTechnique);
    return dataSetBody;
  }

  private static void list(String apiKey) {
    String sync = OkHttpUtils.builder()
        .addHeader("Authorization", "Bearer " + apiKey)
        .url(difyUrl + "datasets?page=1&limit=20")
        .get()
        .sync();
    System.out.println(sync);
  }

  private static void createKb(String apiKey, String kbName) {
    String sync = OkHttpUtils.builder()
        .addHeader("Authorization", "Bearer " + apiKey)
        .addHeader("Content-Type", "application/json")
        .addParam("name", kbName)
        .url(difyUrl + "datasets")
        .post(true)
        .sync();
    System.out.println(sync);
  }

  private static void del(String apiKey, String datasetId) {
    String sync = OkHttpUtils.builder()
        .addHeader("Authorization", "Bearer " + apiKey)
        .url(difyUrl + "datasets/" + datasetId)
        .del()
        .sync();
    System.out.println(sync);
  }

  private static void indexingStatus(String apiKey, String datasetId, String batch) {
    String sync = OkHttpUtils.builder()
        .addHeader("Authorization", "Bearer " + apiKey)
        .url(difyUrl + "datasets/" + datasetId + "/documents/" + batch + "/indexing-status")
        .get()
        .sync();
    System.out.println(sync);
  }

  private static void documentList(String apiKey, String datasetId) {
    String sync = OkHttpUtils.builder()
        .addHeader("Authorization", "Bearer " + apiKey)
        .url(difyUrl + "datasets/" + datasetId + "/documents")
        .get()
        .sync();
    System.out.println(sync);
  }


  private static void delDocument(String apiKey, String datasetId, String documentId) {
    String sync = OkHttpUtils.builder()
        .addHeader("Authorization", "Bearer " + apiKey)
        .url(difyUrl + "datasets/" + datasetId + "/documents/" + documentId)
        .del()
        .sync();
    System.out.println(sync);
  }


  private static void addSegments(String datasetId, String documentId, String apiKey, String content, String answer,
      String keywords) {
    JSONArray objects = new JSONArray();
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("content", content);
    if (StringUtils.isNotBlank(answer)) {
      jsonObject.put("answer", answer);
    }
    if (StringUtils.isNotBlank(keywords)) {
      jsonObject.put("keywords", keywords);
    }
    objects.add(jsonObject);
    String segments = JSON.toJSONString(objects);

    String sync = OkHttpUtils.builder()
        .url(difyUrl + "datasets/" + datasetId + "/documents/" + documentId + "/segments")
        .addHeader("Authorization", "Bearer " + apiKey)
        .addHeader("Content-Type", "application/json")
        .addParam("segments", segments)
        .post(true)
        .sync();
    System.out.println(sync);

    // OkHttpClient client = new OkHttpClient().newBuilder().build();
    // MediaType mediaType = MediaType.parse("application/json");
    // RequestBody body = RequestBody.create(mediaType, segments);
    // Request request = new Request.Builder()
    //     .url(difyUrl + "datasets/" + datasetId + "/documents/" + documentId + "/segments")
    //     .method("POST", body)
    //     .addHeader("Authorization", "Bearer " + apiKey)
    //     .addHeader("Content-Type", "application/json")
    //     .build();
    // try {
    //   Response response = client.newCall(request).execute();
    //   System.out.println(response);
    // } catch (IOException e) {
    //   throw new RuntimeException(e);
    // }
  }


  private static void listSegments(String datasetId, String documentId, String apiKey, String keyword, String status) {

    String sync = OkHttpUtils.builder()
        .url(difyUrl + "/datasets/" + datasetId + "/document/" + documentId + "/segments")
        .addHeader("Authorization", "Bearer " + apiKey)
        .addHeader("Content-Type", "application/json")
        .addParam("status", status)
        .addParam("keyword", keyword)
        .get()
        .sync();
    System.out.println(sync);
  }


  private static void delSegments(String datasetId, String documentId, String segmentId, String apiKey, String keyword,
      String status) {
    String sync = OkHttpUtils.builder()
        .url(difyUrl + "/datasets/" + datasetId + "/document/" + documentId + "/segments/" + segmentId)
        .addHeader("Authorization", "Bearer " + apiKey)
        .addHeader("Content-Type", "application/json")
        .del()
        .sync();
    System.out.println(sync);
  }


  private static void upDateSegments(String datasetId, String documentId, String segmentId, String apiKey) {
    JSONArray objects = new JSONArray();
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("mode", "automatic");
    jsonObject.put("rules", "");
    objects.add(jsonObject);
    String segment = JSON.toJSONString(objects);

    String sync = OkHttpUtils.builder()
        .url(difyUrl + "/datasets/" + datasetId + "/document/" + documentId + "/segments/" + segmentId)
        .addHeader("Authorization", "Bearer " + apiKey)
        .addHeader("Content-Type", "application/json")
        .addParam("segment", segment)
        .post()
        .sync();
    System.out.println(sync);
  }


}
