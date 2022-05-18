package com.iglens.nlp;

import java.io.StringReader;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

/**
 * 测试 IK Analyzer 分词架构中的独立使用分词方法 IK Segmenter
 * 需要加载 IKAnalyzer2012_u6.jar
 * @author zsoft
 */
public class IKSegmenter分词 {
  public String parse(String content, boolean useSmart) throws Exception{
    StringReader sr = new StringReader(content);
    // 参数2为是否使用智能分词
    // true：使用智能分词
    // false：使用最细粒度分词
    IKSegmenter ikSegmenter = new IKSegmenter(sr, useSmart);
    Lexeme word = null;
    String w = null;
    StringBuffer sb = new StringBuffer();
    while((word = ikSegmenter.next()) != null){
      w = word.getLexemeText();
      if(sb.length() > 0){
        sb.append("|");
      }
      sb.append(w);
    }
    return sb.toString();
  }

  public static void main(String[] args) {
    String text = "我们在测试智能分词的运行效果实例";

    try {
      IKSegmenter分词 ikSegmenterTest = new IKSegmenter分词();
      String strs = ikSegmenterTest.parse(text,true);

      System.out.println("使用智能分词结果："+strs);

      strs = ikSegmenterTest.parse(text, false);

      System.out.println("最细粒度分词结果："+strs);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
