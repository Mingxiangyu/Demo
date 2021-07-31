package org.demo.图片.tif;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.stream.IntStream;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import org.apache.commons.io.FilenameUtils;

public class tif转png {
  public static void main(String[] args) {
    convertTiffToPng(new File("C:\\Users\\T480S\\Desktop\\KT-DJDeploy-2021\\数据\\图片拼接\\天津爆炸后.tif"));
  }

  public static void convertTiffToPng(File file) {
    try {
      try (InputStream is = new FileInputStream(file)) {
        try (ImageInputStream imageInputStream = ImageIO.createImageInputStream(is)) {
          Iterator<ImageReader> iterator = ImageIO.getImageReaders(imageInputStream);
          if (iterator == null || !iterator.hasNext()) {
            throw new RuntimeException(
                "Image file format not supported by ImageIO: " + file.getAbsolutePath());
          }

          // We are just looking for the first reader compatible:
          ImageReader reader = iterator.next();
          reader.setInput(imageInputStream);

          int numPage = reader.getNumImages(true);

          // it uses to put new png files, close to original example n0_.tiff will be in
          // /png/n0_0.png
          String name = FilenameUtils.getBaseName(file.getAbsolutePath());
          String parentFolder = file.getParentFile().getAbsolutePath();

          IntStream.range(0, numPage)
              .forEach(
                  v -> {
                    try {
                      final BufferedImage tiff = reader.read(v);
                      File output = new File(parentFolder + File.separator + name + v + ".png");
                      ImageIO.write(tiff, "png", output);
                      System.out.println(output.getPath());
                    } catch (IOException e) {
                      e.printStackTrace();
                    }
                  });
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
