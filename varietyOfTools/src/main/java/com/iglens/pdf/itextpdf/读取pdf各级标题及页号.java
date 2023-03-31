package com.iglens.pdf.itextpdf;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.SimpleBookmark;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class 读取pdf各级标题及页号 {

  public static void main(String[] args) throws Exception {
    String filename = "C:\\Users\\zhouhuilin\\Desktop\\pdf.pdf";
    FileInputStream file = new FileInputStream(filename);
    byte[] b= new byte[file.available()];
    if(file.read(b)>0){
      file.read(b,0,b.length);
    }

    PdfReader reader = new PdfReader(new PdfReader(b));
    int numberOfPages = reader.getNumberOfPages();
    HashMap<String, String> info = reader.getInfo();
    List<HashMap<String, Object>> list = SimpleBookmark.getBookmark(reader);
    for (Iterator<HashMap<String, Object>> i = list.iterator(); i.hasNext(); ) {

      showBookmark(i.next());
    }
    for (Iterator<HashMap<String, Object>> i = list.iterator(); i.hasNext(); ) {

      getPageNumbers(i.next());
    }
  }
  // 获取标题
  private static void showBookmark(HashMap<String, Object> bookmark) {
    System.out.println(bookmark.get("Title"));
    @SuppressWarnings("unchecked")
    ArrayList<HashMap<String, Object>> kids =
        (ArrayList<HashMap<String, Object>>) bookmark.get("Kids");
    if (kids == null) {
      return;
    }
    for (Iterator<HashMap<String, Object>> i = kids.iterator(); i.hasNext(); ) {

      showBookmark(i.next());
    }
  }

  // 获取页码
  public static void getPageNumbers(HashMap<String, Object> bookmark) {
    if (bookmark == null) {
      return;
    }

    if ("GoTo".equals(bookmark.get("Action"))) {

      String page = (String) bookmark.get("Page");
      if (page != null) {

        page = page.trim();

        int idx = page.indexOf(' ');

        int pageNum;

        if (idx < 0) {

          pageNum = Integer.parseInt(page);
          System.out.println("pageNum :" + pageNum);
        } else {

          pageNum = Integer.parseInt(page.substring(0, idx));
          System.out.println("pageNum:" + pageNum);
        }
      }
      @SuppressWarnings("unchecked")
      ArrayList<HashMap<String, Object>> kids =
          (ArrayList<HashMap<String, Object>>) bookmark.get("Kids");
      if (kids == null) {
        return;
      }
      for (HashMap<String, Object> kid : kids) {

        getPageNumbers(kid);
      }
    }
  }
}
