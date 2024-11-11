package com.iglens.视频;

import lombok.extern.slf4j.Slf4j;
import org.bytedeco.javacv.FFmpegFrameGrabber;

@Slf4j
public class 检测RTMP是否联通 {


  public enum CheckResult {
    SUCCESS,          // 连接成功
    CONNECT_FAIL,     // 连接失败
    TIMEOUT,          // 超时
    INVALID_URL      // 无效URL
  }

  public static CheckResult check() {
    // 1. 验证URL格式
    if (!rtmpUrl.startsWith("rtmp://")) {
      return CheckResult.INVALID_URL;
    }

    // 2. 使用FFmpeg检测
    FFmpegFrameGrabber grabber = null;
    try {
      grabber = new FFmpegFrameGrabber(rtmpUrl);
      grabber.setOption("stimeout", timeout * 1000 + "");

      long startTime = System.currentTimeMillis();
      grabber.start();

      // 尝试获取一帧
      if (grabber.grabFrame() != null) {
        return CheckResult.SUCCESS;
      }

      // 检查是否超时
      if (System.currentTimeMillis() - startTime >= timeout) {
        return CheckResult.TIMEOUT;
      }

      return CheckResult.CONNECT_FAIL;

    } catch (Exception e) {
      log.error("RTMP连接检测失败: " + rtmpUrl, e);
      return CheckResult.CONNECT_FAIL;
    } finally {
      if (grabber != null) {
        try {
          grabber.stop();
          grabber.release();
        } catch (Exception e) {
          log.error("关闭FFmpeg grabber失败", e);
        }
      }
    }
  }

  public static String rtmpUrl = "rtmp://localhost:1935/stream/5";
  private static int timeout = 5000; // 默认超时5秒

  public static void main(String[] args) {
    检测RTMP是否联通.
        CheckResult result = 检测RTMP是否联通.check();
    if (result == CheckResult.SUCCESS) {
      System.out.println("RTMP流正常");
    } else {
      System.out.println("RTMP连接异常: " + result);
    }
  }
}