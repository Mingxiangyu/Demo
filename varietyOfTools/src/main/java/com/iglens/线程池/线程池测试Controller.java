package com.iglens.线程池;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class 线程池测试Controller {
  @Autowired private 线程池测试Service asyncService;

  @GetMapping("/async")
  public void async() {
    asyncService.executeAsync();
  }
}
