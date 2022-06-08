package com.deepz.fileparse.parse;

import com.deepz.fileparse.domain.dto.FileDto;
import com.deepz.fileparse.domain.vo.StructableTxtVo;
import java.io.IOException;
import java.net.URL;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;

/**
 * @author xming
 * @description
 */
@Slf4j
@com.deepz.fileparse.annotation.Parser(fileType = "url")
public class UrlParser implements Parser<StructableTxtVo> {

  /**
   * @description
   * @author xming
   */
  @Override
  public StructableTxtVo parse(FileDto fileDto) {
    StructableTxtVo txtVo = new StructableTxtVo();
    Tika tika = new Tika();
    try {
      txtVo.setContent(tika.parseToString(new URL(fileDto.getStrContent())));
    } catch (IOException | TikaException e) {
      log.error(e.getMessage(), e);
    }
    return txtVo;
  }
}
