package com.iglens.pdf.itextpdf;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;
import lombok.experimental.Accessors;

/** 原文链接：https://blog.csdn.net/qq_45699784/article/details/127791747 */
@Data
@Accessors(chain = true)
class DuizhangDomain {
  private String jg;
  private Integer ydz;
  private Integer wdz;
  private BigDecimal dzl;
}

@Data
@Accessors(chain = true)
class YqTable implements Serializable {
  private String jg;
  private Integer yqs;
}
