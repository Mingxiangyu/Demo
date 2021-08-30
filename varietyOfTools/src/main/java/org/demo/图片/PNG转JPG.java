package org.demo.图片;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

/** @author T480S */
@Slf4j
public class PNG转JPG {
  public static void main(String[] args) {
    String srcPath = "C:\\Users\\T480S\\Desktop\\微信截图_20210825202602.png";
    String targetPath = "C:\\Users\\T480S\\Desktop\\微信截图_20210825202602.jpg";
    png2jpg(srcPath, targetPath, 0);
  }

  /**
   * PNG转JPG
   *
   * @param filePath 图片文件字节数组，可替换为文件路径，文件流等
   * @param targetPath 转换后文件路径
   * @param quality 一个介于0和1之间的float ，表示所需的质量级别(0为压缩重要，1为图像质量重要）
   */
  public static void png2jpg(String filePath, String targetPath, float quality) {
    try {
      // 格式转换
      BufferedImage oriImg = ImageIO.read(new FileInputStream(filePath));
      BufferedImage newImg =
          new BufferedImage(oriImg.getWidth(), oriImg.getHeight(), BufferedImage.TYPE_INT_RGB);
      newImg.createGraphics().drawImage(oriImg, 0, 0, Color.WHITE, null);
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      // 压缩图片
      ImageOutputStream ios = ImageIO.createImageOutputStream(out);
      Iterator<ImageWriter> iter = ImageIO.getImageWritersByFormatName("jpeg");
      ImageWriter writer = iter.next();
      ImageWriteParam iwp = writer.getDefaultWriteParam();
      iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
      iwp.setCompressionQuality(quality);
      writer.setOutput(ios);
      writer.write(null, new IIOImage(newImg, null, null), iwp);
      writer.dispose();

      // 将转换后 图片字节数组 写入到目标文件中
      File file = new File(targetPath);
      FileUtils.writeByteArrayToFile(file, out.toByteArray());
    } catch (IOException e) {
      log.error(e.getMessage(), e);
    }
  }
}
