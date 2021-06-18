package cn.demo.service.impl;

import cn.demo.service.SmbService;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;
import jcifs.smb.SmbFileOutputStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * (SmbLasttime)表服务实现类
 *
 * @author Ray。
 * @date 2020-07-22 14:06:53
 */
@Service
@Slf4j
public class SmbServiceImpl implements SmbService {

  @Value("${smb.sourcePath}")
  String sourcePath;

  @Value("${smb.targetPath}")
  String targetPath;

  @Override
  public void init() {
    log.info("sourcePath为: " + sourcePath);
    log.info("targetPath为: " + targetPath);

    try {
      // 创建远程文件对象
      SmbFile remoteFile = new SmbFile(sourcePath);
      remoteFile.connect();

      SmbFile copyremoteFile = new SmbFile(targetPath);
      copyremoteFile.connect();

      SmbFile[] smbFiles = remoteFile.listFiles();
      List<SmbFile> smbFileList = new ArrayList<>();
      addFile(smbFiles, smbFileList);

      for (SmbFile smbFile : smbFileList) {
        String path = smbFile.getPath();
        if (path.contains("~$")) {
          log.info("当前路径为: " + path + " 不符合,直接跳过");
          continue;
        }
        // TODO 后续rar/zip/tar不跳过
        if (path.endsWith(".rar") || path.contains(".tar")) {
          log.info("当前路径为: " + path + " 不符合,直接跳过");
          continue;
        }
        try (InputStream in = new BufferedInputStream(new SmbFileInputStream(smbFile));
            OutputStream out =
                new BufferedOutputStream(
                    new SmbFileOutputStream(copyremoteFile + smbFile.getName())); ) {
          // 读取文件内容
          byte[] buffer = new byte[4096];
          int len;
          while ((len = in.read(buffer, 0, buffer.length)) != -1) {
            out.write(buffer, 0, len);
          }
          out.flush();
        } catch (IOException e) {
          log.info(e.getMessage(), e);
        }
      }
    } catch (IOException e) {
      log.info(e.getMessage(), e);
    }
  }

  private static void addFile(SmbFile[] smbFiles, List<SmbFile> smbFileList) {
    if (smbFiles == null || smbFiles.length == 0) {
      return;
    }
    for (SmbFile smbFile : smbFiles) {
      try {
        if (smbFile.isDirectory()) {
          addFile(smbFile.listFiles(), smbFileList);
          continue;
        }
        smbFileList.add(smbFile);
      } catch (IOException e) {
        log.info(e.getMessage(), e);
      }
    }
  }
}
