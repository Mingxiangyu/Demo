package org.demo.数据库;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import net.sf.jsqlparser.expression.TimeValue;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.bootstrap.Elasticsearch;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;

/**
 * @author T480S
 * @link 原文链接：https://blog.csdn.net/qq_18769269/article/details/81259324
 */
public class ES批量导入导出 {

  public static void main(String[] args) throws Exception {
    String clustName = "tc-es";
    String indexName = "yuqing_0006_6";
    String clusttIp = "192.168.1.14";
    int clustPort = 20069;
    String filePath = "I:\\data\\data\\esdata.json";
    String typeName = "article";
    // outToFile(clustName, indexName, typeName, clusttIp, clustPort, filePath);
    fileToEs(clustName, indexName, typeName, clusttIp, clustPort, filePath);
  }

  /**
   * elasticsearch 数据到文件
   *
   * @param clustName 集群名称
   * @param indexName 索引名称
   * @param typeName type名称
   * @param sourceIp ip
   * @param sourcePort transport 服务端口
   * @param filePath 生成的文件路径
   */
  public static void outToFile(
      String clustName,
      String indexName,
      String typeName,
      String sourceIp,
      int sourcePort,
      String filePath)
      throws Exception {
    TransportClient client = createClient(clustName, sourceIp, sourcePort);
    SearchRequestBuilder builder = client.prepareSearch(indexName);
    if (typeName != null) {
      builder.setTypes(typeName);
    }
    builder
        .setQuery(QueryBuilders.matchAllQuery())
        .setQuery(QueryBuilders.matchQuery("type", "NEWS"));
    builder.setSize(10000);
    builder.setScroll(String.valueOf(new TimeValue("6000")));
    SearchResponse scrollResp = builder.execute().actionGet();
    try {
      // 把导出的结果以JSON的格式写到文件里
      BufferedWriter out = new BufferedWriter(new FileWriter(filePath, true));
      long count = 0;
      while (true) { // 循环插入，直到所有结束
        for (SearchHit hit : scrollResp.getHits().getHits()) {
          String json = hit.getSourceAsString();
          if (StringUtils.isNotEmpty(json) && !"".equals(json)) {
            out.write(json);
            out.write("\r\n");
            count++;
            System.out.println("*******************" + count);
          }
        }
        scrollResp =
            client
                .prepareSearchScroll(scrollResp.getScrollId())
                .setScroll(String.valueOf(new TimeValue("6000")))
                .execute()
                .actionGet();
        if (scrollResp.getHits().getHits().length == 0) {
          break;
        }
      }
      System.out.println("总共写入数据:" + count);
      out.close();
      client.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * 把json 格式的文件导入到elasticsearch 服务器
   *
   * @param clustName 集群名称
   * @param indexName 索引名称
   * @param typeName type 名称
   * @param sourceIp ip
   * @param sourcePort 端口
   * @param filePath json格式的文件路径
   */
  @SuppressWarnings("deprecation")
  public static void fileToEs(
      String clustName,
      String indexName,
      String typeName,
      String sourceIp,
      int sourcePort,
      String filePath)
      throws Exception {

    TransportClient client = createClient(clustName, sourceIp, sourcePort);
    try {
      // 把导出的结果以JSON的格式写到文件里
      BufferedReader br = new BufferedReader(new FileReader(filePath));
      String json = null;
      int count = 0;
      // 开启批量插入
      BulkRequestBuilder bulkRequest = client.prepareBulk();
      while ((json = br.readLine()) != null) {
        bulkRequest.add(client.prepareIndex(indexName, typeName).setSource(json));
        // 每一千条提交一次
        count++;
        if (count % 1000 == 0) {
          System.out.println("本次提交了1000条");
          BulkResponse bulkResponse = bulkRequest.execute().actionGet();
          if (bulkResponse.hasFailures()) {
            System.out.println("message:" + bulkResponse.buildFailureMessage());
          }
          // 重新创建一个bulk
          bulkRequest = client.prepareBulk();
        }
      }
      bulkRequest.execute().actionGet();
      System.out.println("总提交了：" + count);
      br.close();
      client.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * 建立连接
   *
   * @param cluster
   * @param ip
   * @param port
   * @return
   * @throws Exception
   */
  private static TransportClient createClient(String cluster, String ip, Integer port)
      throws Exception {
    TransportClient client = null;
    if (client == null) {
      synchronized (Elasticsearch.class) {
        Settings settings = Settings.settingsBuilder().put("cluster.name", cluster).build();
        client =
            TransportClient.builder()
                .settings(settings)
                .build()
                .addTransportAddress(
                    new InetSocketTransportAddress(InetAddress.getByName(ip), port));
      }
    }
    return client;
  }
}
