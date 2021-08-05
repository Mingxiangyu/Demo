package org.demo.爬虫.基金;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * **银行理财数据爬取
 *
 * @author T480S
 */
public class KunlunBank {
  public static void main(String[] args)
      throws IOException, ParseException {
    List<T003ProductInfo> data = KunlunBank.getData();
    Object o = JSONArray.toJSON(data);
    JSONArray subMsgs = (JSONArray) o;
    System.out.println(subMsgs);
    // getData();

  }

  public static List<T003ProductInfo> getData()
      throws IOException, ParseException {
    RequestConfig globalConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.DEFAULT).build();
    CloseableHttpClient httpClient =
        HttpClients.custom().setDefaultRequestConfig(globalConfig).build();
    RequestConfig localConfig =
        RequestConfig.copy(globalConfig).setCookieSpec(CookieSpecs.STANDARD_STRICT).build();

    String url_1 = "https://www.eklb.cn";
    // 创建HttpGet实例
    HttpGet httpGet = new HttpGet(url_1);
    httpGet.setConfig(localConfig);
    // 创建响应处理器处理服务器响应内容
    ResponseHandler<String> veriCodeHandler = handler();
    // 二次请求获取cookie覆盖第一次请求，证明是同一个浏览器访问，真正获取请求
    String response = httpClient.execute(httpGet, veriCodeHandler); // 使用给定的上下文执行HTTP请求。
    // System.out.println(response);
    String getDataURL = "https://www.eklb.cn/directbank/financeQueryList.do";

    int offSet = 0, QueryNum = 5; // 分页参数

    // 这个代码多处用于Java像url发送Post请求。在发送post请求时用该list来存放参数。
    // 定义了一个数组集合对象，通过泛型声明了数组里的元素类型。
    List<NameValuePair> formparams = new ArrayList<NameValuePair>();

    formparams.add(new BasicNameValuePair("chanlNo", "DB"));
    formparams.add(new BasicNameValuePair("bySort", ""));
    formparams.add(new BasicNameValuePair("descAsc", "desc"));
    formparams.add(new BasicNameValuePair("tranType", "actualYield"));
    formparams.add(new BasicNameValuePair("page", String.valueOf(offSet)));
    formparams.add(new BasicNameValuePair("pageSize", String.valueOf(QueryNum)));
    UrlEncodedFormEntity requestFormEntity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);

    HttpPost httpPost = new HttpPost(getDataURL);
    httpPost.setEntity(requestFormEntity);
    httpPost.setConfig(localConfig);
    ResponseHandler<String> getDataHandler = handler();
    response = httpClient.execute(httpPost, getDataHandler);

    List<T003ProductInfo> list = new ArrayList<>();
    if (null != response) {
      JSONObject firstPageJson = JSONObject.parseObject(response);
      // System.out.println(firstPageJson);
      String resCode = firstPageJson.getString("errorCode");
      if ("000000".equals(resCode)) {
        int pageTotalNum = NumberUtils.toInt(firstPageJson.getString("pageTotalNum"));
        int page = Integer.valueOf(firstPageJson.getString("page"));
        JSONArray records = firstPageJson.getJSONArray("financeList");
        // System.out.println("-------------------------"+records);
        records.forEach(
            oneRecord -> {
              // 每一个产品的解析
              JSONObject oneRecordJson = (JSONObject) oneRecord;
              try {
                list.add(getFormatData(oneRecordJson));
              } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
              }
            });
        // 开始分页请求
        int pages = pageTotalNum / QueryNum;
        if (pageTotalNum % QueryNum > 0) {
          pages += 1;
        }
        for (int i = 2; i <= page; i++) {
          formparams = new ArrayList<NameValuePair>();
          formparams.add(new BasicNameValuePair("chanlNo", "DB"));
          formparams.add(new BasicNameValuePair("bySort", ""));
          formparams.add(new BasicNameValuePair("descAsc", "desc"));
          formparams.add(new BasicNameValuePair("tranType", "actualYield"));
          // formparams.add(new BasicNameValuePair("page", String.valueOf(offSet)));
          formparams.add(new BasicNameValuePair("OffSet", String.valueOf((i - 1) * 4 + 1)));
          formparams.add(new BasicNameValuePair("pageSize", String.valueOf(QueryNum)));
          requestFormEntity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
          httpPost = new HttpPost(getDataURL);
          httpPost.setEntity(requestFormEntity);
          httpPost.setConfig(localConfig);
          getDataHandler = handler();
          response = httpClient.execute(httpPost, getDataHandler);
          if (null != response) {
            if ("000000".equals(resCode)) {
              records = firstPageJson.getJSONArray("financeList");
              records.forEach(
                  oneRecord -> {
                    // 每一个产品的解析
                    JSONObject oneRecordJson = (JSONObject) oneRecord;
                    try {
                      list.add(getFormatData(oneRecordJson));
                    } catch (ParseException e) {
                      // TODO Auto-generated catch block
                      e.printStackTrace();
                    }
                  });
            }
          }
        }
      }
    }
    return list;
  }

  private static T003ProductInfo getFormatData(JSONObject oneJsonRecord) throws ParseException {
    T003ProductInfo oneProductInfo = new T003ProductInfo();
    // 银行名称
    oneProductInfo.setBankName("**银行");
    // 产品ID
    oneProductInfo.setPrdId(oneJsonRecord.getString("productId"));
    // 产品名称
    oneProductInfo.setPrdName(oneJsonRecord.getString("productName"));
    // 年化收益
    oneProductInfo.setIncomeRate(oneJsonRecord.getString("actualYield"));
    // 投资时长
    oneProductInfo.setInvestDuration(oneJsonRecord.getString("productTerm"));
    // 时长单位 1:天;2:月;3:年
    oneProductInfo.setDurationUnit("天");
    // 计算最少起投金额
    oneProductInfo.setFloor(oneJsonRecord.getString("raiseMinAmt"));
    // 计算剩余额度
    oneProductInfo.setTotalAmount(oneJsonRecord.getString("maxAccRaiseAmt"));
    String maxAccRaiseAmt = oneJsonRecord.getString("maxAccRaiseAmt");
    String maxRaiseAmt = oneJsonRecord.getString("maxRaiseAmt");
    oneProductInfo.setLeftAmount(
        String.valueOf(
            NumberUtils.toDouble(maxAccRaiseAmt)
                -NumberUtils.toDouble(maxRaiseAmt)));
    // 产品类型码（理财、基金、票据、P2P、各种宝）
    oneProductInfo.setPrdType("理财");
    // 理财类型(0:封闭型、1:开放型)
    oneProductInfo.setFinancingType("0");
    // 募集开始日募集结束日,"projectBeginTime": "20171117093000","jMBeginTime":"20171119160000",       //
    // 募集开始日
    oneProductInfo.setStartDay(oneJsonRecord.getString("raiseBegDate"));
    // 募集结束日
    oneProductInfo.setEndDay(oneJsonRecord.getString("raiseEndDate"));
    // 起息日
    oneProductInfo.setActiveDay(getStartDate(oneJsonRecord.getString("raiseEndDate")));
    // 到期日
    oneProductInfo.setOverdueDay(oneJsonRecord.getString("expireDate"));

    return oneProductInfo;
  }

  /** @return */
  private static <T> ResponseHandler<T> handler() {
    // TODO Auto-generated method stub
    ResponseHandler<T> loginHandler =
        new ResponseHandler<T>() {
          @SuppressWarnings("unchecked")
          @Override
          public T handleResponse(final HttpResponse response) throws IOException {
            StatusLine statusLine = response.getStatusLine();
            HttpEntity entity = response.getEntity();
            if (statusLine.getStatusCode() >= 300) {
              throw new HttpResponseException(
                  statusLine.getStatusCode(), statusLine.getReasonPhrase());
            }
            if (entity == null) {
              throw new ClientProtocolException("Response contains no content");
            }
            entity = new BufferedHttpEntity(entity);
            String responseAsString = EntityUtils.toString(entity, "UTF-8");
            return (T) responseAsString;
          }
        };
    return loginHandler;
  }

  /*
   * raiseEndDate  20180109(募集结束日期)
   *
   * 起息日  20180110 ( raiseEndDate + 1 )
   */
  public static String getStartDate(String endDay) throws ParseException {
    SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
    Calendar ca = Calendar.getInstance();
    Date d = sf.parse(endDay);
    ca.setTime(d);
    ca.add(Calendar.DAY_OF_YEAR, 1);
    return sf.format(ca.getTime());
  }
}
