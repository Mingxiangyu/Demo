package org.demo.邮件;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Date;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class MailVo {
  private String id;
  private String from;
  private String to;
  private String subject;
  private String text;
  private Date sentDate;
  private String cc;
  private String bcc;
  private String status;
  private String error;
  @JsonIgnore private MultipartFile[] multipartFiles;
}
