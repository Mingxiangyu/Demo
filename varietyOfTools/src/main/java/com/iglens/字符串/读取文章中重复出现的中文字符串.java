package com.iglens.字符串;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

/**
 * https://www.cnblogs.com/sospopo/p/6591899.html
 */
public class 读取文章中重复出现的中文字符串 {

  //最小字长（两个字以上进行匹配）
  private static final int MINSIZE = 15;


  public static void createData(String filename, HashSet<String> set) throws IOException {
    //读取文章内容
    ArrayList<String> result = extracted(filename);

    //对文章内容进行遍历找出重读出现的句子或者是词语
    for (int i = 0; i < result.size() - 1; i++) {
      for (int j = 0; j < result.size() - i - 1; j++) {
        //将重复出现的词语保存到set集合里面
        set.addAll(getSameCharacter(result.get(i), result.get(j + i + 1)));
      }
    }

  }

  /**
   * 读取文章内容
   */
  private static ArrayList<String> extracted(String filename) throws IOException {
    //读取段落
    ArrayList<String> result = new ArrayList<>();
    String r;
    try (BufferedReader in = new BufferedReader(new FileReader(new File(filename).getAbsoluteFile()))) {
      while ((r = in.readLine()) != null) {
        //消除不必要的标点符号
        r = r.replaceAll("\\s+ |“|\\[|‘|《|　*|", "").trim();
        //留下” ， 。  。”  ”。 ”， ？ 》 -等作为划分句子的分割符标示
        Collections.addAll(result, r.split("，|(。”|”(。|，)|。)|(\\])|”|’|？|:|》|-"));
      }
    }
    return result;
  }

  private static ArrayList<String> getSameCharacter(String a1, String a2) {
    String maxS;
    String minS;
    //短句遍历开始处
    int start = 0;
    //词的长度最短为两个字长
    int range = 2;
    //设定短句和长句s,使得遍历更加快捷
    if (a1.length() <= a2.length()) {
      maxS = a2;
      minS = a1;
    } else {
      maxS = a1;
      minS = a2;
    }
    String result = "";
    ArrayList<String> list = new ArrayList<String>();
    //防止substring时超出范围
    while (start + range <= minS.length()) {
      //如果句子或词在对象里面，则找出相应的句子或词保存在list里面
      if (maxS.indexOf(minS.substring(start, start + range)) != -1) {
        //获取最长句子,删除短句子
        list.remove(result);
        list.add(minS.substring(start, start + range));
        result = minS.substring(start, start + range);
        range++;
        continue;
      }
      range = MINSIZE;
      start++;
    }
    return list;
  }

  public static void main(String[] args) throws IOException {
    String filename = "test.txt";
    //重复词储存
    HashSet<String> result = new HashSet<String>();
    createData(filename, result);
    System.out.println("这篇文章中的重复出现的词或句子有以下几个词或句子:\n");
    for (String s : result) {
      System.out.println(s);
    }
  }
}
