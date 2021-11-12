package org.demo.共享文件夹.smb;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.Instant;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Version;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import jcifs.smb.SmbFileOutputStream;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.stereotype.Component;

/** @author T480S */
@Component
@Slf4j
//public class Jobs implements InitializingBean {
public class Jobs{//注释了 InitializingBean 避免启动Application后调用

//  @Value("${smb.jobSourcePath}")
  String jobSourcePath;

//  @Value("${smb.targetPath}")
  String targetPath;

//  private final SmbDao smbDao;

//  @Autowired
//  public Jobs(SmbDao smbDao) {
//    this.smbDao = smbDao;
//  }

//  @Scheduled(cron = "${smb.clean_schedule}")
  public void scheduledClearMethod() {
    log.info("清除方法: 调用清除持久化文件名方法");
    Calendar rightNow = Calendar.getInstance();
    int delMonth = rightNow.get(Calendar.MONTH);
    if (delMonth == 0) {
      delMonth = 12;
    }

//    List<SmbLasttime> byMonth = smbDao.findByMonth(delMonth);
//    log.info("清除方法: 清除数据量为: " + byMonth.size());
//    smbDao.deleteInBatch(byMonth);
    log.info("清除方法: 清除完成");
  }

  /** 定时同步远程文件夹 @Scheduled 表示设置了定时任务 cron属性 cron表达式。定时任务触发是时间的一个字符串表达形式 */
//  @Scheduled(cron = "${smb.sync_schedule}")
  public void scheduledMethod() {
    long startTime = System.currentTimeMillis();
    log.info("调用定时同步任务=====================" + startTime);

    String jobUrl = jobSourcePath;
    String copyremoteUrl = targetPath;
    log.info("同步任务中监听文件夹为: {},拷贝文件夹为: {}", jobUrl, copyremoteUrl);

    Calendar rightNow = Calendar.getInstance();
    int month = rightNow.get(Calendar.MONTH) + 1; // 第一个月从0开始，所以得到月份＋1
    int day = rightNow.get(Calendar.DAY_OF_MONTH);
    int count = 0;
    try {
      // 创建远程文件对象
      String url = jobUrl + "/" + month + "/" + day + "/";
      log.info("拼接成功后的当日监听文件夹为: " + url);
      SmbFile remoteFile = new SmbFile(url);
      remoteFile.connect();
      if (!remoteFile.exists()) {
        log.error("监听文件夹路径为: " + url + " 不存在,无法拷贝");
      }
      SmbFile[] smbFiles = remoteFile.listFiles();
      if (smbFiles == null || smbFiles.length == 0) {
        log.info("当日监听文件夹下数量为空,直接结束!");
        return;
      }
      log.info("当日监听文件夹连接成功,该文件夹下数量为: " + smbFiles.length);

      SmbFile copyremoteFile = new SmbFile(copyremoteUrl);
      copyremoteFile.connect();
      log.info("拷贝文件夹连接成功");

      List<SmbLasttime> smbLasttimeList = null;
//      smbDao.findByMonthAndDay(month, day);
      List<String> smbFileNameList =
          smbLasttimeList.stream().map(SmbLasttime::getFileName).collect(Collectors.toList());

      for (SmbFile smbFile : smbFiles) {
        String smbFileName = smbFile.getName();
        if (smbFileNameList.contains(smbFileName)) {
          if (log.isDebugEnabled()) {
            log.debug("文件名称为: {} ,已处理过,直接跳过", smbFileName);
          }
          continue;
        }
        // 构建持久化对象
        SmbLasttime newSmbLast = new SmbLasttime();
        newSmbLast.setMonth(month);
        newSmbLast.setDay(day);
        newSmbLast.setFileName(smbFile.getName());

        String path = smbFile.getPath();
        if (path.contains("~$")) {
          log.info("当前路径为: " + path + " 不符合,直接跳过");
          newSmbLast.setSuccess(false);
//          smbDao.save(newSmbLast);
          continue;
        }
        // TODO 后续rar/zip/tar不跳过
        if (path.endsWith(".rar") || path.contains(".tar")) {
          log.info("当前路径为: " + path + " 不符合,直接跳过");
          newSmbLast.setSuccess(false);
//          smbDao.save(newSmbLast);
          continue;
        }
        try (InputStream in = new BufferedInputStream(new SmbFileInputStream(smbFile));
            OutputStream out =
                new BufferedOutputStream(new SmbFileOutputStream(copyremoteFile + smbFileName))) {

          // 读取文件内容
          byte[] buffer = new byte[4096];
          int len;
          while ((len = in.read(buffer, 0, buffer.length)) != -1) {
            out.write(buffer, 0, len);
          }
          out.flush();
        } catch (IOException e) {
          log.error(e.getMessage(), e);
        }

        try {
          SmbFile copySmbFile = new SmbFile(copyremoteFile + smbFileName);
          // 如果拷贝的文件和源文件不一样,则将拷贝文件删除,本次跳过
          String copyHash = DigestUtils.sha512Hex(copySmbFile.getInputStream());
          String smbHash = DigestUtils.sha512Hex(smbFile.getInputStream());
          if (!StringUtils.equals(copyHash, smbHash)) {
            copySmbFile.delete();
            log.info("文件路径为: " + path + " 源文件和拷贝后文件哈希值不符,跳过,留待下次拷贝");
            continue;
          }
          // 更新拷贝后文件的修改时间为原文件的修改时间
          long lastModified = smbFile.lastModified();
          copySmbFile.setLastModified(lastModified);
        } catch (Exception e) {
          log.error(e.getMessage(), e);
        }
        newSmbLast.setSuccess(true);
//        smbDao.save(newSmbLast);
        count++;
      }
    } catch (IOException e) {
      log.error(e.getMessage(), e);
    }
    log.info(
        "本次定时任务执行完成,成功拷贝 {}个,耗时: {}秒。=====================",
        count,
        (System.currentTimeMillis() - startTime) / 1000);
  }

//  @Override
  public void afterPropertiesSet() {
    this.scheduledMethod();
  }
}

/**
 * (SmbLasttime)表实体类
 *
 * @author Ray。
 * @date 2020-07-22 14:06:49
 */
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
class SmbLasttime {

  /** 声明id */
  @Id @GeneratedValue private Integer id;

  /** 该月级文件夹名称 */
  private Integer month;
  /** 该日级文件夹名称 */
  private Integer day;
  /** 远程文件名 */
  private String fileName;
  /** 该文件是否迁移成功 */
  private boolean success;

  @Deprecated private String lastFileName;
  @Deprecated private Long lastModifyTime;
  @Deprecated private Boolean isNew;
  /** 创建时间 */
  @CreatedDate private Instant createStamp;

  /** 修改时间 */
  @LastModifiedDate private Instant modifyStamp;

  @Version private Integer version;
}
