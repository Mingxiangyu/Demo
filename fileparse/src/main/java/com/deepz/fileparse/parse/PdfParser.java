package com.deepz.fileparse.parse;

import com.deepz.fileparse.domain.dto.FileDto;
import com.deepz.fileparse.domain.vo.StructablePdfVo;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.io.RandomAccessBufferedFileInputStream;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.interactive.action.PDActionGoTo;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.destination.PDPageDestination;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDDocumentOutline;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineItem;
import org.apache.pdfbox.pdmodel.interactive.documentnavigation.outline.PDOutlineNode;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;

/**
 * @author xming
 * @description
 */
@Slf4j
@com.deepz.fileparse.annotation.Parser(fileType = "pdf")
public class PdfParser implements Parser<StructablePdfVo> {

  /**
   * @description
   * @author xming
   */
  @Override
  public StructablePdfVo parse(FileDto fileDto) {
    StructablePdfVo vo = new StructablePdfVo();
    InputStream inputStream = fileDto.getInputStream();
    try {
      String content = new Tika().parse(inputStream).toString();
      vo.setContent(content);
      vo.setHeads(getTitleAndPage(inputStream));
    } catch (IOException e) {
      log.error(e.getMessage(), e);
    }
    return vo;
  }

  /**
   * @author xming
   * @description 解析pdf文件
   */
  @Override
  public StructablePdfVo parse(String path) {
    return this.parse(new File(path));
  }

  /**
   * @author xming
   * @description 解析pdf文件
   */
  @Override
  public StructablePdfVo parse(File file) {
    StructablePdfVo vo = new StructablePdfVo();
    try {
      String content = new Tika().parseToString(file);
      vo.setContent(content);
      vo.setHeads(getTitleAndPage(new FileInputStream(file)));
    } catch (IOException | TikaException e) {
      log.error(e.getMessage(), e);
    }
    return vo;
  }

  public List<StructablePdfVo.Head> getTitleAndPage(InputStream inputStream) {
    List<StructablePdfVo.Head> heads = new ArrayList<>();
    PDDocument pdDocument = null;
    try (RandomAccessBufferedFileInputStream rabfis =
        new RandomAccessBufferedFileInputStream(inputStream); ) {
      PDFParser parser = new PDFParser(rabfis);
      parser.parse();
      pdDocument = parser.getPDDocument();
      PDDocumentOutline outline = pdDocument.getDocumentCatalog().getDocumentOutline();
      if (outline != null) {
        printBookmarks(outline, "", heads);
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        pdDocument.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return heads;
  }

  /**
   * @author xming
   * @description
   */
  private void printBookmarks(
      PDOutlineNode bookmark, String indentation, List<StructablePdfVo.Head> heads)
      throws IOException {
    PDOutlineItem current = bookmark.getFirstChild();
    while (current != null) {
      int pages = 0;
      if (current.getDestination() instanceof PDPageDestination) {
        PDPageDestination pd = (PDPageDestination) current.getDestination();
        pages = pd.retrievePageNumber() + 1;
      }
      if (current.getAction() instanceof PDActionGoTo) {
        PDActionGoTo gta = (PDActionGoTo) current.getAction();
        if (gta.getDestination() instanceof PDPageDestination) {
          PDPageDestination pd = (PDPageDestination) gta.getDestination();
          pages = pd.retrievePageNumber() + 1;
        }
      }
      StructablePdfVo.Head head = new StructablePdfVo.Head();
      head.setPage(pages == 0 ? null : String.valueOf(pages));
      head.setTitle(current.getTitle());
      heads.add(head);
      printBookmarks(current, indentation + "    ", heads);
      current = current.getNextSibling();
    }
  }
}
