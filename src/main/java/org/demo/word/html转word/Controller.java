package org.demo.word.html转word;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;

public class Controller {

  @Autowired
  public Service service;

  @PostMapping("/article/htmlFormat")
  public Ret html(String html) throws Exception {
    return service.formatHtmlStyle(html);
  }
}
