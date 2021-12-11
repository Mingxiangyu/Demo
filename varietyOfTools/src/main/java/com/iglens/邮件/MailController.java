package com.iglens.邮件;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Api(tags = "邮箱")
@RestController
public class MailController {
  @Autowired private MailService mailService;

  @GetMapping("/")
  public ModelAndView index() {
    ModelAndView mv = new ModelAndView("mail/sendMail");
    mv.addObject("from", mailService.getMailSendFrom());
    return mv;
  }

  @PostMapping("/mail/send")
  public MailVo sendMail(MailVo mailVo, MultipartFile[] files) {
    mailVo.setMultipartFiles(files);
    return mailService.sendMail(mailVo);
  }
}
