package com.iglens.elasticsearch;

import com.alibaba.fastjson.JSONObject;
import java.io.IOException;
import java.util.Map;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;

/**
 * es客户端的增删改查 7.6.1版本
 *
 * @author T480S
 */
public class EsUtil {
  // 生成批量处理对象
  private static BulkRequest bulkRequest = new BulkRequest();

  /**
   * 添加数据到es
   *
   * @param indexName
   * @param typeName
   * @param indexId
   * @param json
   */
  public static void add(
      RestHighLevelClient restHighLevelClient,
      String indexName,
      String typeName,
      String indexId,
      Map<String, Object> json)
      throws IOException {
    IndexRequest indexRequest = new IndexRequest(indexName, typeName, indexId);
    indexRequest.source(new JSONObject(json).toString(), XContentType.JSON);
    try {
      restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  /**
   * 判断索引名是否存在
   *
   * @param indexName
   * @return
   */
  public static boolean existsIndex(RestHighLevelClient restHighLevelClient, String indexName) {
    try {
      GetIndexRequest request = new GetIndexRequest(indexName);
      return restHighLevelClient.indices().exists(request, RequestOptions.DEFAULT);
    } catch (Exception e) {
      System.out.println("Exception");
    }
    return false;
  }
  /**
   * @param : client
   * @description : 判断文档是否存在
   */
  public static boolean isExist(
      RestHighLevelClient restHighLevelClient, String indexName, String typeName, String indexId)
      throws IOException {

    GetRequest request = new GetRequest(indexName, typeName, indexId);
    // 1.同步判断
    boolean exists = restHighLevelClient.exists(request, RequestOptions.DEFAULT);

    // 2.异步判断
    ActionListener<Boolean> listener =
        new ActionListener<Boolean>() {
          @Override
          public void onResponse(Boolean exists) {
            if (exists) {
              System.out.println("文档存在");
            } else {
              System.out.println("文档不存在");
            }
          }

          @Override
          public void onFailure(Exception e) {}
        };
    // client.existsAsync(request, RequestOptions.DEFAULT, listener);

    return exists;
  }

  /**
   * @param : client
   * @description : 删除文档
   */
  public static void deleteDocument(
      RestHighLevelClient restHighLevelClient, String indexName, String typeName, String indexId)
      throws IOException {

    DeleteRequest request = new DeleteRequest(indexName, typeName, indexId);

    // 设置请求超时时间：2分钟
    request.timeout(TimeValue.timeValueMinutes(2));
    // request.timeout("2m");

    // 同步删除
    DeleteResponse deleteResponse = restHighLevelClient.delete(request, RequestOptions.DEFAULT);

    // 异步删除
    ActionListener<DeleteResponse> listener =
        new ActionListener<DeleteResponse>() {
          @Override
          public void onResponse(DeleteResponse deleteResponse) {
            System.out.println("删除后操作");
          }

          @Override
          public void onFailure(Exception e) {
            System.out.println("删除失败");
          }
        };
  }

  /**
   * 批量增加数据的方法
   *
   * @param restHighLevelClient
   * @param indexname
   * @param typename
   * @param row_key
   * @param map
   * @throws Exception
   */
  public void bulkadd(
      RestHighLevelClient restHighLevelClient,
      String indexname,
      String typename,
      String row_key,
      Map<String, Object> map)
      throws Exception {

    try {
      // 生成批量处理对象
      // BulkRequest bulkRequest = new BulkRequest();

      // 得到某一行的数据,并封装成索引对象
      IndexRequest indexRequest = new IndexRequest(indexname, typename, row_key);
      indexRequest.source(new JSONObject(map).toString(), XContentType.JSON);

      // 判断是否执行加载
      if (bulkRequest.numberOfActions() != 0 && (bulkRequest.numberOfActions() > 100)) {
        try {
          bulkRequest(restHighLevelClient);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
      // 装填数据
      bulkRequest.add(indexRequest);

    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      bulkRequest(restHighLevelClient);
    }
  }

  /**
   * 批量具体执行方法 execute bulk process
   *
   * @throws Exception
   */
  private void bulkRequest(RestHighLevelClient restHighLevelClient) throws Exception {
    // 加载数据
    BulkResponse bulkResponse = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);

    // 判断加载情况
    if (bulkResponse.hasFailures()) {
      System.out.println("失败");
    } else {
      System.out.println("成功");
      // 重新定义
      bulkRequest = new BulkRequest();
    }
  }
}
