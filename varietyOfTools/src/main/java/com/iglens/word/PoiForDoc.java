package com.iglens.word;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.model.PicturesTable;
import org.apache.poi.hwpf.usermodel.CharacterRun;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Picture;
import org.apache.poi.hwpf.usermodel.Range;

public class PoiForDoc {
  /** 使用HWPFDocument解析word文档 wps按doc处理即可 */
  public void parseDocByHWPFDocument() {
    try (FileInputStream is = new FileInputStream(new File("c:\\a.wps"));
        HWPFDocument document = new HWPFDocument(is); ) {
      ByteArrayOutputStream baos = new ByteArrayOutputStream(); // 字节流，用来存储图片
      Poi图片工具类doc pictures = new Poi图片工具类doc(document);
      PicturesTable pictureTable = document.getPicturesTable();

      Range r = document.getRange(); // 区间
      for (int i = 0; i < r.numParagraphs(); i++) {
        Paragraph p = r.getParagraph(i); // 段落
        int fontSize =
            p.getCharacterRun(0)
                .getFontSize(); // 字号，字号和是否加粗可用来当做标题或者某一关键标识的判断                   boolean isBold =
                                // p.getCharacterRun(0).isBold();//是否加粗
        String paragraphText = p.text(); // 段落文本

        // 以下代码解析图片,这样获取的图片是在文档流中的，是和文本按顺序解析的，可以很好的解决图片定位问题
        for (int j = 0; j < p.numCharacterRuns(); j++) {
          CharacterRun cr = p.getCharacterRun(j); // 字符
          if (pictureTable.hasPicture(cr)) {
            Picture picture = pictures.getFor(cr);
            // 如果是在页面显示图片，可转换为base64编码的图片
            picture.writeImageContent(baos); // 将图片写入字节流
            //                        String base64Image = "<img src='data:image/png;base64,"+new
            // BASE64Encoder().encode(baos.toByteArray())+"'/>";
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
