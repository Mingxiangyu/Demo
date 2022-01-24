package com.iglens.word;

import com.spire.doc.Document;
import com.spire.doc.FileFormat;
import com.spire.doc.documents.ImageType;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * 有问题，docx转出来后乱码
 *
 * @author ming
 */
public class Spire4word转图片 {
  public static void main(String[] args) {
    String path = "/Users/ming/Documents/明翔宇-Java-4年的副本.docx";
    changeDocToImg(new File(path), "测试");
  }

  /**
   * word转换图片
   *
   * @param file 上传的文件
   * @param imgName 随便穿 生成的文件名
   * @return DataResult<Object>是我自定义的返回值 用你们自己的就 ok
   */
  public static void changeDocToImg(File file, String imgName) {
    try {
      Document doc = new Document();
      // 加载文件 第二个参数 FileFormat.Auto 会自动去分别上传文件的 docx、doc类型
      doc.loadFromStream(new FileInputStream(file), FileFormat.Auto);
      // 上传文档页数，也是最后要生成的图片数
      int pageCount = doc.getPageCount();
      // 参数第一个和第三个都写死 第二个参数就是生成图片数
      BufferedImage[] image = doc.saveToImages(0, pageCount, ImageType.Bitmap);
      // 循环，输出图片保存到本地
      for (int i = 0; i < image.length; i++) {
        //        File f = new File("E:/img/" + imgName + "_" + (i + 1) + ".png");

        File f = new File("/Users/ming/Documents/" + imgName + "_" + (i + 1) + ".png");
        ImageIO.write(image[i], "PNG", f);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
