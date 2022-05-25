package com.deepz.fileparse.parse;

import com.deepz.fileparse.domain.dto.FileDto;
import com.deepz.fileparse.domain.enums.TitleEnum;
import com.deepz.fileparse.domain.vo.StructableWordVo;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.model.StyleDescription;
import org.apache.poi.hwpf.model.StyleSheet;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;

/**
 * @author xming
 * @description
 */
@Slf4j
@com.deepz.fileparse.annotation.Parser(fileType = {"doc", "docx"})
public class WordParser implements Parser<StructableWordVo> {

  @Override
  public StructableWordVo parse(FileDto fileDto) {
    StructableWordVo vo = new StructableWordVo();
    try {
      String content = new Tika().parseToString(fileDto.getInputStream());
      vo.setContent(content);
    } catch (IOException | TikaException e) {
      log.error(e.getMessage(), e);
    }
    List<StructableWordVo.Head> heads = this.getHead(fileDto.getSuffx(), fileDto.getInputStream());
    vo.setHeads(heads);
    return vo;
  }

  @Override
  public StructableWordVo parse(String path) {
    return this.parse(new File(path));
  }

  /**
   * @author xming
   * @description
   */
  @Override
  public StructableWordVo parse(File file) {
    StructableWordVo vo = new StructableWordVo();
    try {
      String content = new Tika().parseToString(file);
      vo.setContent(content);
      String ext = FilenameUtils.getExtension(file.getPath());
      List<StructableWordVo.Head> heads = this.getHead(ext, new FileInputStream(file));
      vo.setHeads(heads);
    } catch (IOException | TikaException e) {
      log.error(e.getMessage(), e);
    }
    return vo;
  }

  /**
   * 获取标题
   *
   * @param suf 后缀
   * @param inputStream 文件流
   * @return
   */
  private List<StructableWordVo.Head> getHead(String suf, InputStream inputStream) {
    if ("DOC".equals(suf.toUpperCase())) {
      return getHwpfHead(inputStream);
    }
    if ("DOCX".equals(suf.toUpperCase())) {
      return getXwpfHead(inputStream);
    }

    return null;
  }

  /**
   * 获取07版本word结构
   *
   * @param inputStream
   * @return
   */
  private List<StructableWordVo.Head> getXwpfHead(InputStream inputStream) {
    List<StructableWordVo.Head> heads = new ArrayList<>();
    try (XWPFDocument document = new XWPFDocument(inputStream)) {

      List<XWPFParagraph> paragraphs = document.getParagraphs();
      for (XWPFParagraph paragraph : paragraphs) {
        String style = paragraph.getStyle();
        if (style != null) {
          // (n-1)一般代表几级标题
          if (NumberUtils.toInt(style) < 7) {
            StructableWordVo.Head head = new StructableWordVo.Head();
            head.setTitle(paragraph.getText());
            head.setStyle(TitleEnum.findTitle(NumberUtils.toInt(style)));
            heads.add(head);
          }
        }
      }
    } catch (IOException e) {
      log.error(e.getMessage(), e);
    }
    return heads;
  }

  /**
   * 获取03版本word结构
   *
   * @param inputStream
   * @return
   */
  private List<StructableWordVo.Head> getHwpfHead(InputStream inputStream) {
    List<StructableWordVo.Head> heads = new ArrayList<>();

    try (HWPFDocument doc = new HWPFDocument(inputStream); ) {
      Range r = doc.getRange();
      for (int i = 0; i < r.numParagraphs(); i++) {
        Paragraph p = r.getParagraph(i);
        int numStyles = doc.getStyleSheet().numStyles();
        int styleIndex = p.getStyleIndex();
        if (numStyles > styleIndex) {
          StyleSheet styleSheet = doc.getStyleSheet();
          StyleDescription style = styleSheet.getStyleDescription(styleIndex);
          String styleName = style.getName();
          if (styleName != null && styleName.contains("标题")) {
            StructableWordVo.Head head = new StructableWordVo.Head();
            head.setTitle(p.text());
            heads.add(head);
          }
        }
      }
    } catch (IOException e) {
      log.error(e.getMessage(), e);
    }
    return heads;
  }
}
