package org.demo.word.html转word;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Service {
  // 实现图片和样式处理的方法
  public Ret formatHtmlStyle(String html) {
    JSONArray picsArr = new JSONArray();

    // 缩小图片
    Document doc = Jsoup.parse(html);

    Elements elementsP = doc.getElementsByTag("p");
    for (int i = 0; i < elementsP.size(); i++) {
      Element element = elementsP.get(i);
      boolean hasImg = false;
      Elements elementsChildren = element.children();
      for (int j = 0; j < elementsChildren.size(); j++) {
        Element elementChild = elementsChildren.get(j);
        if (elementChild.nodeName().equals("img")) {
          hasImg = true;
          break;
        }
      }
      if (hasImg) {
        element.attr("style", "text-align: center ;");
      } else {
        element.attr("style",
            "font-family: FangSong_GB2312 ;font-size:18px;text-indent: 2em ;line-height:34px;text-align:justify;");
      }

    }

    Elements elements = doc.getElementsByTag("img");
    for (int i = 0; i < elements.size(); i++) {
      Element element = elements.get(i);
      String src = element.attr("src");
      JSONObject picjo = new JSONObject();
      picjo.put("index", i);
      // 将网络图片转为 base64 格式
      picjo.put("src", CommonUtil.urlToBase64(src));
      picsArr.add(picjo);
      element.attr("src", "" + i);
    }
//    Ret ret = Ret.create().setCodeAndMsg(200).set("html", doc.body().html()).set("pics", picsArr);
    return null;
  }

}
