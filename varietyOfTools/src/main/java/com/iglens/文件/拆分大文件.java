package com.iglens.文件;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/** @author xming */
public class 拆分大文件 {
  /** 拆分大文件 fileCount 拆分的文件个数 */
  public static void splitFile(String filePath, int fileCount) throws IOException {
    FileInputStream fis = new FileInputStream(filePath);
    FileChannel inputChannel = fis.getChannel();
    final long fileSize = inputChannel.size();
    // 平均值
    long average = fileSize / fileCount;
    // 缓存块大小，自行调整
    long bufferSize = 200;
    // 申请一个缓存区
    ByteBuffer byteBuffer = ByteBuffer.allocate(Integer.parseInt(bufferSize + ""));
    // 子文件开始位置
    long startPosition = 0;
    // 子文件结束位置
    long endPosition = average < bufferSize ? 0 : average - bufferSize;
    for (int i = 0; i < fileCount; i++) {
      if (i + 1 != fileCount) {
        // 读取数据
        int read = inputChannel.read(byteBuffer, endPosition);
        readW:
        while (read != -1) {
          // 切换读模式
          byteBuffer.flip();
          byte[] array = byteBuffer.array();
          for (int j = 0; j < array.length; j++) {
            byte b = array[j];
            // 判断\n\r
            if (b == 10 || b == 13) {
              endPosition += j;
              break readW;
            }
          }
          endPosition += bufferSize;
          // 重置缓存块指针
          byteBuffer.clear();
          read = inputChannel.read(byteBuffer, endPosition);
        }
      } else {
        // 最后一个文件直接指向文件末尾
        endPosition = fileSize;
      }

      FileOutputStream fos = new FileOutputStream(filePath + (i + 1));
      FileChannel outputChannel = fos.getChannel();
      // 通道传输文件数据
      inputChannel.transferTo(startPosition, endPosition - startPosition, outputChannel);
      outputChannel.close();
      fos.close();
      startPosition = endPosition + 1;
      endPosition += average;
    }
    inputChannel.close();
    fis.close();
  }
}
