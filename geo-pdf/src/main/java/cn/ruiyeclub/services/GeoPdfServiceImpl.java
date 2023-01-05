package cn.trans.services;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.gdal.gdal.Dataset;
import org.gdal.gdal.Driver;
import org.gdal.gdal.gdal;
import org.gdal.gdalconst.gdalconstConstants;
import org.springframework.stereotype.Service;

/** @author zhouhuilin */
@Slf4j
@Service
public class GeoPdfServiceImpl implements GeoPdfService {
  static {
    log.info("---LibraryUtil--gdal载入动态库");
    try {
      // 根据系统环境加载资源
      String systemType = System.getProperty("os.name");
      String file = "";
      boolean isWin = systemType.toLowerCase().indexOf("win") != -1;
      if (isWin) {
        // file="/gdal/win32/gdalalljni.dll";
        file = "/lib/gdalalljni.dll";
      } else {
        file = "/gdal/linux/libgdalalljni.so";
      }
      // 从资源文件加载动态库
      LibraryUtil.loadFromResource(file);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  static {
    // 注册所有的驱动
    gdal.AllRegister();
    // 为了支持中文路径，请添加下面这句代码
    gdal.SetConfigOption("GDAL_FILENAME_IS_UTF8", "YES");
    // 为了使属性表字段支持中文，请添加下面这句
    gdal.SetConfigOption("SHAPE_ENCODING", "");
  }

  @Override
  public void downloadPdf(String tifPath, HttpServletResponse response) {
    // 读取影像数据x
    Dataset dataset = gdal.Open(tifPath, gdalconstConstants.GA_ReadOnly);
    if (dataset == null) {
      System.out.println("read fail!");
      return;
    }
    Driver driver = dataset.GetDriver();
    System.out.println("driver name : " + driver.getLongName());
    String tmpDir = System.getProperty("java.io.tmpdir");
    String filename = FileUtil.getName(tifPath);
    if ((filename != null) && (filename.length() > 0)) {
      int dot = filename.lastIndexOf('.');
      if ((dot > -1) && (dot < (filename.length()))) {
        filename = filename.substring(0, dot);
      }
    }
    filename = filename + ".pdf";
    String tmpPath = tmpDir + filename;
    // 生成geoPdf
    gdal.Translate(tmpPath, dataset, null);
    //   关闭该文件
    dataset.delete();

    // gdal.GDALDestroyDriverManager();

    download(tmpPath, response, filename);

    try {
      boolean del = FileUtil.del(tmpPath);
      if (!del) {
        System.gc();
        FileUtil.del(tmpPath);
      }
    } catch (IORuntimeException e) {
      log.error(e.getMessage(), e);
    }
  }

  /**
   * 响应给浏览器
   *
   * @param downloadPath 下载路径(即实体文件在本机路径）
   * @param response 响应
   * @param fileName 文件名
   */
  public static void download(String downloadPath, HttpServletResponse response, String fileName) {
    response.setContentType("application/octet-stream;");
    // response.setContentType("application/force-download");// 设置强制下载不打开

    fileName = new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
    response.setHeader("Content-disposition", "attachment; filename=" + fileName);
    try (BufferedInputStream inputStream =
            new BufferedInputStream(new FileInputStream(downloadPath));
        ServletOutputStream outputStream = response.getOutputStream()) {

      byte[] b = new byte[2048];
      int len;
      while ((len = inputStream.read(b)) > 0) {
        outputStream.write(b, 0, len);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
