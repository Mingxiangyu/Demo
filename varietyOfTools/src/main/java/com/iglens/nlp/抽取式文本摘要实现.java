package com.iglens.nlp;
// import com.hankcs.hanlp.HanLP;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;
/**
 * @Author：sks @Description：文本摘要提取文中重要的关键句子，使用top-n关键词在句子中的比例关系
 * 返回过滤句子方法为：1.均值标准差，2.top-n句子，3.最大边缘相关top-n句子 @Date：Created in 16:40 2017/12/22 @Modified by：
 */
public class 抽取式文本摘要实现 {
  // 保留关键词数量
  int N = 50;

  // 关键词间的距离阀值
  int CLUSTER_THRESHOLD = 5;

  // 前top-n句子
  int TOP_SENTENCES = 10;

  // 最大边缘相关阀值
  double λ = 0.4;

  // 句子得分使用方法
  final Set<String> styleSet = new HashSet<String>();

  // 停用词列表
  Set<String> stopWords = new HashSet<String>();

  // 句子编号及分词列表
  Map<Integer, List<String>> sentSegmentWords = null;

  public 抽取式文本摘要实现() {
    // this.loadStopWords("D:\\work\\Solr\\solr-python\\CNstopwords.txt");
    styleSet.add("meanstd");
    styleSet.add("default");
    styleSet.add("MMR");
  }

  /**
   * 加载停词
   *
   * @param path
   */
  private void loadStopWords(String path) {
    BufferedReader br = null;
    try {
      InputStreamReader reader = new InputStreamReader(new FileInputStream(path), "utf-8");
      br = new BufferedReader(reader);
      String line = null;
      while ((line = br.readLine()) != null) {
        stopWords.add(line);
      }
      br.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /** @Author：sks @Description：利用正则将文本拆分成句子 @Date： */
  private List<String> SplitSentences(String text) {
    List<String> sentences = new ArrayList<String>();
    String regEx = "[!?。！？.]";
    Pattern p = Pattern.compile(regEx);
    String[] sents = p.split(text);
    Matcher m = p.matcher(text);

    int sentsLen = sents.length;
    if (sentsLen > 0) { // 把每个句子的末尾标点符号加上
      int index = 0;
      while (index < sentsLen) {
        if (m.find()) {
          sents[index] += m.group();
        }
        index++;
      }
    }
    for (String sentence : sents) {
      // 文章从网页上拷贝过来后遗留下来的没有处理掉的html的标志
      sentence =
          sentence.replaceAll(
              "(&rdquo;|&ldquo;|&mdash;|&lsquo;|&rsquo;|&middot;|&quot;|&darr;|&bull;)", "");
      sentences.add(sentence.trim());
    }
    return sentences;
  }

  /**
   * 这里使用IK进行分词
   *
   * @param text
   * @return
   */
  private List<String> IKSegment(String text) {
    List<String> wordList = new ArrayList<String>();
    Reader reader = new StringReader(text);
    IKSegmenter ik = new IKSegmenter(reader, true);
    Lexeme lex = null;
    try {
      while ((lex = ik.next()) != null) {
        String word = lex.getLexemeText();
        if (word.equals("nbsp") || this.stopWords.contains(word)) continue;
        if (word.length() > 1 && word != "\t") wordList.add(word);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return wordList;
  }

  /**
   * 每个句子得分 (keywordsLen*keywordsLen/totalWordsLen)
   *
   * @param sentences 分句
   * @param topnWords keywords top-n关键词
   * @return
   */
  private Map<Integer, Double> scoreSentences(List<String> sentences, List<String> topnWords) {
    Map<Integer, Double> scoresMap = new LinkedHashMap<Integer, Double>(); // 句子编号，得分
    sentSegmentWords = new HashMap<Integer, List<String>>();
    int sentence_idx = -1; // 句子编号
    for (String sentence : sentences) {
      sentence_idx += 1;
      List<String> words = this.IKSegment(sentence); // 对每个句子分词
      //            List<String> words= HanLP.segment(sentence);
      sentSegmentWords.put(sentence_idx, words);
      List<Integer> word_idx = new ArrayList<Integer>(); // 每个关词键在本句子中的位置
      for (String word : topnWords) {
        if (words.contains(word)) {
          word_idx.add(words.indexOf(word));
        } else continue;
      }
      if (word_idx.size() == 0) continue;
      Collections.sort(word_idx);
      // 对于两个连续的单词，利用单词位置索引，通过距离阀值计算一个族
      List<List<Integer>> clusters = new ArrayList<List<Integer>>(); // 根据本句中的关键词的距离存放多个词族
      List<Integer> cluster = new ArrayList<Integer>();
      cluster.add(word_idx.get(0));
      int i = 1;
      while (i < word_idx.size()) {
        if ((word_idx.get(i) - word_idx.get(i - 1)) < this.CLUSTER_THRESHOLD)
          cluster.add(word_idx.get(i));
        else {
          clusters.add(cluster);
          cluster = new ArrayList<Integer>();
          cluster.add(word_idx.get(i));
        }
        i += 1;
      }
      clusters.add(cluster);
      // 对每个词族打分，选择最高得分作为本句的得分
      double max_cluster_score = 0.0;
      for (List<Integer> clu : clusters) {
        int keywordsLen = clu.size(); // 关键词个数
        int totalWordsLen = clu.get(keywordsLen - 1) - clu.get(0) + 1; // 总的词数
        double score = 1.0 * keywordsLen * keywordsLen / totalWordsLen;
        if (score > max_cluster_score) max_cluster_score = score;
      }
      scoresMap.put(sentence_idx, max_cluster_score);
    }
    return scoresMap;
  }

  /** @Author：sks @Description：利用均值方差自动文摘 @Date： */
  public String SummaryMeanstdTxt(String text) {
    // 将文本拆分成句子列表
    List<String> sentencesList = this.SplitSentences(text);

    // 利用IK分词组件将文本分词，返回分词列表
    List<String> words = this.IKSegment(text);
    //        List<Term> words1= HanLP.segment(text);

    // 统计分词频率
    Map<String, Integer> wordsMap = new HashMap<String, Integer>();
    for (String word : words) {
      Integer val = wordsMap.get(word);
      wordsMap.put(word, val == null ? 1 : val + 1);
    }

    // 使用优先队列自动排序
    Queue<Map.Entry<String, Integer>> wordsQueue =
        new PriorityQueue<Map.Entry<String, Integer>>(
            wordsMap.size(),
            new Comparator<Map.Entry<String, Integer>>() {
              //            @Override
              public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
                return o2.getValue() - o1.getValue();
              }
            });
    wordsQueue.addAll(wordsMap.entrySet());

    if (N > wordsMap.size()) N = wordsQueue.size();

    // 取前N个频次最高的词存在wordsList
    List<String> wordsList = new ArrayList<String>(N); // top-n关键词
    for (int i = 0; i < N; i++) {
      Entry<String, Integer> entry = wordsQueue.poll();
      wordsList.add(entry.getKey());
    }

    // 利用频次关键字，给句子打分，并对打分后句子列表依据得分大小降序排序
    Map<Integer, Double> scoresLinkedMap =
        scoreSentences(sentencesList, wordsList); // 返回的得分,从第一句开始,句子编号的自然顺序

    // approach1,利用均值和标准差过滤非重要句子
    Map<Integer, String> keySentence = new LinkedHashMap<Integer, String>();

    // 句子得分均值
    double sentenceMean = 0.0;
    for (double value : scoresLinkedMap.values()) {
      sentenceMean += value;
    }
    sentenceMean /= scoresLinkedMap.size();

    // 句子得分标准差
    double sentenceStd = 0.0;
    for (Double score : scoresLinkedMap.values()) {
      sentenceStd += Math.pow((score - sentenceMean), 2);
    }
    sentenceStd = Math.sqrt(sentenceStd / scoresLinkedMap.size());

    for (Map.Entry<Integer, Double> entry : scoresLinkedMap.entrySet()) {
      // 过滤低分句子
      if (entry.getValue() > (sentenceMean + 0.5 * sentenceStd))
        keySentence.put(entry.getKey(), sentencesList.get(entry.getKey()));
    }

    StringBuilder sb = new StringBuilder();
    for (int index : keySentence.keySet()) sb.append(keySentence.get(index));
    return sb.toString();
  }

  /** @Author：sks @Description：默认返回排序得分top-n句子 @Date： */
  public String SummaryTopNTxt(String text) {
    // 将文本拆分成句子列表
    List<String> sentencesList = this.SplitSentences(text);

    // 利用IK分词组件将文本分词，返回分词列表
    List<String> words = this.IKSegment(text);
    //        List<Term> words1= HanLP.segment(text);

    // 统计分词频率
    Map<String, Integer> wordsMap = new HashMap<String, Integer>();
    for (String word : words) {
      Integer val = wordsMap.get(word);
      wordsMap.put(word, val == null ? 1 : val + 1);
    }

    // 使用优先队列自动排序
    Queue<Map.Entry<String, Integer>> wordsQueue =
        new PriorityQueue<Map.Entry<String, Integer>>(
            wordsMap.size(),
            new Comparator<Map.Entry<String, Integer>>() {
              //            @Override
              public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
                return o2.getValue() - o1.getValue();
              }
            });
    wordsQueue.addAll(wordsMap.entrySet());

    if (N > wordsMap.size()) N = wordsQueue.size();

    // 取前N个频次最高的词存在wordsList
    List<String> wordsList = new ArrayList<String>(N); // top-n关键词
    for (int i = 0; i < N; i++) {
      Entry<String, Integer> entry = wordsQueue.poll();
      wordsList.add(entry.getKey());
    }

    // 利用频次关键字，给句子打分，并对打分后句子列表依据得分大小降序排序
    Map<Integer, Double> scoresLinkedMap =
        scoreSentences(sentencesList, wordsList); // 返回的得分,从第一句开始,句子编号的自然顺序
    List<Map.Entry<Integer, Double>> sortedSentList =
        new ArrayList<Map.Entry<Integer, Double>>(
            scoresLinkedMap.entrySet()); // 按得分从高到底排序好的句子，句子编号与得分
    // System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
    Collections.sort(
        sortedSentList,
        new Comparator<Map.Entry<Integer, Double>>() {
          //            @Override
          public int compare(Entry<Integer, Double> o1, Entry<Integer, Double> o2) {
            return o2.getValue() == o1.getValue() ? 0 : (o2.getValue() > o1.getValue() ? 1 : -1);
          }
        });

    // approach2,默认返回排序得分top-n句子
    Map<Integer, String> keySentence = new TreeMap<Integer, String>();
    int count = 0;
    for (Map.Entry<Integer, Double> entry : sortedSentList) {
      count++;
      keySentence.put(entry.getKey(), sentencesList.get(entry.getKey()));
      if (count == this.TOP_SENTENCES) break;
    }

    StringBuilder sb = new StringBuilder();
    for (int index : keySentence.keySet()) sb.append(keySentence.get(index));
    return sb.toString();
  }

  /** @Author：sks @Description：利用最大边缘相关自动文摘 @Date： */
  public String SummaryMMRNTxt(String text) {
    // 将文本拆分成句子列表
    List<String> sentencesList = this.SplitSentences(text);

    // 利用IK分词组件将文本分词，返回分词列表
    List<String> words = this.IKSegment(text);
    //        List<Term> words1= HanLP.segment(text);

    // 统计分词频率
    Map<String, Integer> wordsMap = new HashMap<String, Integer>();
    for (String word : words) {
      Integer val = wordsMap.get(word);
      wordsMap.put(word, val == null ? 1 : val + 1);
    }

    // 使用优先队列自动排序
    Queue<Map.Entry<String, Integer>> wordsQueue =
        new PriorityQueue<Map.Entry<String, Integer>>(
            wordsMap.size(),
            new Comparator<Map.Entry<String, Integer>>() {
              //            @Override
              public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
                return o2.getValue() - o1.getValue();
              }
            });
    wordsQueue.addAll(wordsMap.entrySet());

    if (N > wordsMap.size()) N = wordsQueue.size();

    // 取前N个频次最高的词存在wordsList
    List<String> wordsList = new ArrayList<String>(N); // top-n关键词
    for (int i = 0; i < N; i++) {
      Entry<String, Integer> entry = wordsQueue.poll();
      wordsList.add(entry.getKey());
    }

    // 利用频次关键字，给句子打分，并对打分后句子列表依据得分大小降序排序
    Map<Integer, Double> scoresLinkedMap =
        scoreSentences(sentencesList, wordsList); // 返回的得分,从第一句开始,句子编号的自然顺序
    List<Map.Entry<Integer, Double>> sortedSentList =
        new ArrayList<Map.Entry<Integer, Double>>(
            scoresLinkedMap.entrySet()); // 按得分从高到底排序好的句子，句子编号与得分
    // System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
    Collections.sort(
        sortedSentList,
        new Comparator<Map.Entry<Integer, Double>>() {
          //            @Override
          public int compare(Entry<Integer, Double> o1, Entry<Integer, Double> o2) {
            return o2.getValue() == o1.getValue() ? 0 : (o2.getValue() > o1.getValue() ? 1 : -1);
          }
        });

    // approach3,利用最大边缘相关，返回前top-n句子
    if (sentencesList.size() == 2) {
      return sentencesList.get(0) + sentencesList.get(1);
    } else if (sentencesList.size() == 1) return sentencesList.get(0);
    Map<Integer, String> keySentence = new TreeMap<Integer, String>();
    int count = 0;
    Map<Integer, Double> MMR_SentScore = MMR(sortedSentList);
    for (Map.Entry<Integer, Double> entry : MMR_SentScore.entrySet()) {
      count++;
      int sentIndex = entry.getKey();
      String sentence = sentencesList.get(sentIndex);
      keySentence.put(sentIndex, sentence);
      if (count == this.TOP_SENTENCES) break;
    }

    StringBuilder sb = new StringBuilder();
    for (int index : keySentence.keySet()) sb.append(keySentence.get(index));
    return sb.toString();
  }
  /**
   * 计算文本摘要
   *
   * @param text
   * @param style(meanstd,default,MMR)
   * @return
   */
  public String summarize(String text, String style) {
    try {
      if (!styleSet.contains(style) || text.trim().equals(""))
        throw new IllegalArgumentException(
            "方法 summarize(String text,String style)中text不能为空，style必须是meanstd、default或者MMR");
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      System.exit(1);
    }

    // 将文本拆分成句子列表
    List<String> sentencesList = this.SplitSentences(text);

    // 利用IK分词组件将文本分词，返回分词列表
    List<String> words = this.IKSegment(text);
    //        List<Term> words1= HanLP.segment(text);

    // 统计分词频率
    Map<String, Integer> wordsMap = new HashMap<String, Integer>();
    for (String word : words) {
      Integer val = wordsMap.get(word);
      wordsMap.put(word, val == null ? 1 : val + 1);
    }

    // 使用优先队列自动排序
    Queue<Map.Entry<String, Integer>> wordsQueue =
        new PriorityQueue<Map.Entry<String, Integer>>(
            wordsMap.size(),
            new Comparator<Map.Entry<String, Integer>>() {
              //            @Override
              public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
                return o2.getValue() - o1.getValue();
              }
            });
    wordsQueue.addAll(wordsMap.entrySet());

    if (N > wordsMap.size()) N = wordsQueue.size();

    // 取前N个频次最高的词存在wordsList
    List<String> wordsList = new ArrayList<String>(N); // top-n关键词
    for (int i = 0; i < N; i++) {
      Entry<String, Integer> entry = wordsQueue.poll();
      wordsList.add(entry.getKey());
    }

    // 利用频次关键字，给句子打分，并对打分后句子列表依据得分大小降序排序
    Map<Integer, Double> scoresLinkedMap =
        scoreSentences(sentencesList, wordsList); // 返回的得分,从第一句开始,句子编号的自然顺序

    Map<Integer, String> keySentence = null;

    // approach1,利用均值和标准差过滤非重要句子
    if (style.equals("meanstd")) {
      keySentence = new LinkedHashMap<Integer, String>();

      // 句子得分均值
      double sentenceMean = 0.0;
      for (double value : scoresLinkedMap.values()) {
        sentenceMean += value;
      }
      sentenceMean /= scoresLinkedMap.size();

      // 句子得分标准差
      double sentenceStd = 0.0;
      for (Double score : scoresLinkedMap.values()) {
        sentenceStd += Math.pow((score - sentenceMean), 2);
      }
      sentenceStd = Math.sqrt(sentenceStd / scoresLinkedMap.size());

      for (Map.Entry<Integer, Double> entry : scoresLinkedMap.entrySet()) {
        // 过滤低分句子
        if (entry.getValue() > (sentenceMean + 0.5 * sentenceStd))
          keySentence.put(entry.getKey(), sentencesList.get(entry.getKey()));
      }
    }

    List<Map.Entry<Integer, Double>> sortedSentList =
        new ArrayList<Map.Entry<Integer, Double>>(
            scoresLinkedMap.entrySet()); // 按得分从高到底排序好的句子，句子编号与得分
    // System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
    Collections.sort(
        sortedSentList,
        new Comparator<Map.Entry<Integer, Double>>() {
          //            @Override
          public int compare(Entry<Integer, Double> o1, Entry<Integer, Double> o2) {
            return o2.getValue() == o1.getValue() ? 0 : (o2.getValue() > o1.getValue() ? 1 : -1);
          }
        });

    // approach2,默认返回排序得分top-n句子
    if (style.equals("default")) {
      keySentence = new TreeMap<Integer, String>();
      int count = 0;
      for (Map.Entry<Integer, Double> entry : sortedSentList) {
        count++;
        keySentence.put(entry.getKey(), sentencesList.get(entry.getKey()));
        if (count == this.TOP_SENTENCES) break;
      }
    }

    // approach3,利用最大边缘相关，返回前top-n句子
    if (style.equals("MMR")) {
      if (sentencesList.size() == 2) {
        return sentencesList.get(0) + sentencesList.get(1);
      } else if (sentencesList.size() == 1) return sentencesList.get(0);
      keySentence = new TreeMap<Integer, String>();
      int count = 0;
      Map<Integer, Double> MMR_SentScore = MMR(sortedSentList);
      for (Map.Entry<Integer, Double> entry : MMR_SentScore.entrySet()) {
        count++;
        int sentIndex = entry.getKey();
        String sentence = sentencesList.get(sentIndex);
        keySentence.put(sentIndex, sentence);
        if (count == this.TOP_SENTENCES) break;
      }
    }
    StringBuilder sb = new StringBuilder();
    for (int index : keySentence.keySet()) sb.append(keySentence.get(index));
    // System.out.println("summarize out...");
    return sb.toString();
  }

  /**
   * 最大边缘相关(Maximal Marginal Relevance)，根据λ调节准确性和多样性 max[λ*score(i) -
   * (1-λ)*max[similarity(i,j)]]:score(i)句子的得分，similarity(i,j)句子i与j的相似度 User-tunable diversity
   * through λ parameter - High λ= Higher accuracy - Low λ= Higher diversity
   *
   * @param sortedSentList 排好序的句子，编号及得分
   * @return
   */
  private Map<Integer, Double> MMR(List<Map.Entry<Integer, Double>> sortedSentList) {
    // System.out.println("MMR In...");
    double[][] simSentArray = sentJSimilarity(); // 所有句子的相似度
    Map<Integer, Double> sortedLinkedSent = new LinkedHashMap<Integer, Double>();
    for (Map.Entry<Integer, Double> entry : sortedSentList) {
      sortedLinkedSent.put(entry.getKey(), entry.getValue());
    }
    Map<Integer, Double> MMR_SentScore = new LinkedHashMap<Integer, Double>(); // 最终的得分（句子编号与得分）
    Map.Entry<Integer, Double> Entry = sortedSentList.get(0); // 第一步先将最高分的句子加入
    MMR_SentScore.put(Entry.getKey(), Entry.getValue());
    boolean flag = true;
    while (flag) {
      int index = 0;
      double maxScore = Double.NEGATIVE_INFINITY; // 通过迭代计算获得最高分句子
      for (Map.Entry<Integer, Double> entry : sortedLinkedSent.entrySet()) {
        if (MMR_SentScore.containsKey(entry.getKey())) continue;
        double simSentence = 0.0;
        for (Map.Entry<Integer, Double> MMREntry :
            MMR_SentScore.entrySet()) { // 这个是获得最相似的那个句子的最大相似值
          double simSen = 0.0;
          if (entry.getKey() > MMREntry.getKey())
            simSen = simSentArray[MMREntry.getKey()][entry.getKey()];
          else simSen = simSentArray[entry.getKey()][MMREntry.getKey()];
          if (simSen > simSentence) {
            simSentence = simSen;
          }
        }
        simSentence = λ * entry.getValue() - (1 - λ) * simSentence;
        if (simSentence > maxScore) {
          maxScore = simSentence;
          index = entry.getKey(); // 句子编号
        }
      }
      MMR_SentScore.put(index, maxScore);
      if (MMR_SentScore.size() == sortedLinkedSent.size()) flag = false;
    }
    // System.out.println("MMR out...");
    return MMR_SentScore;
  }

  /**
   * 每个句子的相似度，这里使用简单的jaccard方法，计算所有句子的两两相似度
   *
   * @return
   */
  private double[][] sentJSimilarity() {
    // System.out.println("sentJSimilarity in...");
    int size = sentSegmentWords.size();
    double[][] simSent = new double[size][size];
    for (Map.Entry<Integer, List<String>> entry : sentSegmentWords.entrySet()) {
      for (Map.Entry<Integer, List<String>> entry1 : sentSegmentWords.entrySet()) {
        if (entry.getKey() >= entry1.getKey()) continue;
        int commonWords = 0;
        double sim = 0.0;
        for (String entryStr : entry.getValue()) {
          if (entry1.getValue().contains(entryStr)) commonWords++;
        }
        sim =
            1.0 * commonWords / (entry.getValue().size() + entry1.getValue().size() - commonWords);
        simSent[entry.getKey()][entry1.getKey()] = sim;
      }
    }
    // System.out.println("sentJSimilarity out...");
    return simSent;
  }

  public static void main(String[] args) {

    抽取式文本摘要实现 summary = new 抽取式文本摘要实现();

    String text =
        "我国古代历史演义小说的代表作。明代小说家罗贯中依据有关三国的历史、杂记，在广泛吸取民间传说和民间艺人创作成果的基础上，加工、再创作了这部长篇章回小说。"
            + "作品写的是汉末到晋初这一历史时期魏、蜀、吴三个封建统治集团间政治、军事、外交等各方面的复杂斗争。通过这些描写，揭露了社会的黑暗与腐朽，谴责了统治阶级的残暴与奸诈，"
            + "反映了人民在动乱时代的苦难和明君仁政的愿望。小说也反映了作者对农民起义的偏见，以及因果报应和宿命论等思想。战争描写是《三国演义》突出的艺术成就。"
            + "这部小说通过惊心动魄的军事、政治斗争，运用夸张、对比、烘托、渲染等艺术手法，成功地塑造了诸葛亮、曹操、关羽、张飞等一批鲜明、生动的人物形象。"
            + "《三国演义》结构宏伟而又严密精巧，语言简洁、明快、生动。有的评论认为这部作品在艺术上的不足之处是人物性格缺乏发展变化，有的人物渲染夸张过分导致失真。"
            + "《三国演义》标志着历史演义小说的辉煌成就。在传播政治、军事斗争经验、推动历史演义创作的繁荣等方面都起过积极作用。"
            + "《三国演义》的版本主要有明嘉靖刻本《三国志通俗演义》和清毛宗岗增删评点的《三国志演义》";

    String keySentences = summary.SummaryMeanstdTxt(text);
    System.out.println("summary: " + keySentences);

    String topSentences = summary.SummaryTopNTxt(text);
    System.out.println("summary: " + topSentences);

    String mmrSentences = summary.SummaryMMRNTxt(text);
    System.out.println("summary: " + mmrSentences);
  }
}
