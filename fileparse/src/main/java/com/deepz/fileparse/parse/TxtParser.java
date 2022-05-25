package com.deepz.fileparse.parse;

import com.deepz.fileparse.domain.dto.FileDto;
import com.deepz.fileparse.domain.vo.StructableTxtVo;
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
@com.deepz.fileparse.annotation.Parser(fileType = "txt")
public class TxtParser implements Parser<StructableTxtVo> {

  /**
   * @author xming
   * @description
   */
  @Override
  public StructableTxtVo parse(String path) {
    StructableTxtVo txtVo = new StructableTxtVo();
    try {
      txtVo.setContent(new Tika().parseToString(new File(path)));
    } catch (IOException | TikaException e) {
      log.error(e.getMessage(), e);
    }
    return txtVo;
  }

  /**
   * @description
   * @author xming
   */
  @Override
  public StructableTxtVo parse(FileDto fileDto) {
    StructableTxtVo txtVo = new StructableTxtVo();
    try {
      txtVo.setContent(new Tika().parseToString(fileDto.getInputStream()));
    } catch (IOException | TikaException e) {
      log.error(e.getMessage(), e);
    }
    return txtVo;
  }
}
