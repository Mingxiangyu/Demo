package cn.demo.service;

import cn.demo.dao.SmbDao;
import cn.demo.entity.SmbLasttime;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import jcifs.smb.SmbFileOutputStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/** @author T480S */
@Component
@Slf4j
public class Jobs implements InitializingBean {

  @Value("${smb.jobSourcePath}")
  String jobSourcePath;

  @Value("${smb.targetPath}")
  String targetPath;

  private final SmbDao smbDao;

  @Autowired
  public Jobs(SmbDao smbDao) {
    this.smbDao = smbDao;
  }

  @Scheduled(cron = "${smb.clean_schedule}")
  public void scheduledClearMethod() {
    log.info("清除方法: 调用清除持久化文件名方法");
    Calendar rightNow = Calendar.getInstance();
    int month = rightNow.get(Calendar.MONTH) + 1; // 第一个月从0开始，所以得到月份＋1
    List<SmbLasttime> byMonth = smbDao.findByMonth(month);
    log.info("清除方法: 清除数据量为: "+byMonth.size());
    smbDao.deleteInBatch(byMonth);
    log.info("清除方法: 清除完成");
  }

  /** 定时同步远程文件夹 @Scheduled 表示设置了定时任务 cron属性 cron表达式。定时任务触发是时间的一个字符串表达形式 */
  @Scheduled(cron = "${smb.sync_schedule}")
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

      List<SmbLasttime> smbLasttimeList = smbDao.findByMonthAndDay(month, day);
      List<String> smbFileNameList =
          smbLasttimeList.stream().map(SmbLasttime::getFileName).collect(Collectors.toList());

      for (SmbFile smbFile : smbFiles) {
        String smbFileName = smbFile.getName();
        if (smbFileNameList.contains(smbFileName)) {
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
          smbDao.save(newSmbLast);
          continue;
        }
        // TODO 后续rar/zip/tar不跳过
        if (path.endsWith(".rar") || path.endsWith(".zip") || path.contains(".tar")) {
          log.info("当前路径为: " + path + " 不符合,直接跳过");
          newSmbLast.setSuccess(false);
          smbDao.save(newSmbLast);
          continue;
        }
        long lastModified = smbFile.lastModified();
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
          SmbFile copySmbFile = new SmbFile(copyremoteFile + smbFileName);
          copySmbFile.setLastModified(lastModified);
        } catch (IOException e) {
          log.error(e.getMessage(), e);
        }
        newSmbLast.setSuccess(true);
        smbDao.save(newSmbLast);
        count++;
      }

    } catch (IOException e) {
      log.error(e.getMessage(), e);
    }
    log.info(
        "本次定时任务执行完成,拷贝 {}个,耗时: {}秒。=====================",
        count,
        (System.currentTimeMillis() - startTime) / 1000);
  }

  @Override
  public void afterPropertiesSet() {
    this.scheduledMethod();
  }
}
