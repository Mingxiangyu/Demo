package com.deepz.fileparse.parse;

import com.deepz.fileparse.domain.dto.FileDto;
import com.deepz.fileparse.domain.vo.StructablePptVo;
import java.io.File;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;

/**
 * @author xming
 * @description
 */
@Slf4j
@com.deepz.fileparse.annotation.Parser(fileType = {"ppt", "pptx"})
public class PptParser implements Parser<StructablePptVo> {

  @Override
  public StructablePptVo parse(String path) {
    StructablePptVo pptVo = new StructablePptVo();
    try {
      Tika tika = new Tika();
      String text = tika.parseToString(new File(path));
      pptVo.setContent(text);
    } catch (IOException | TikaException e) {
      log.error(e.getMessage(), e);
    }

    return pptVo;
  }

  /**
   * @description
   * @author xming
   */
  @Override
  public StructablePptVo parse(FileDto fileDto) {
    StructablePptVo vo = new StructablePptVo();
    try {
      vo.setContent(new Tika().parseToString(fileDto.getInputStream()));
    } catch (IOException | TikaException e) {
      log.error(e.getMessage(), e);
    }
    return vo;
  }
}
