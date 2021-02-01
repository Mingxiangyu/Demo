package org.demo;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

public class 获取tif图片内信息 {
  public static void main(String[] args) {
    //    String filePath = "C:\\Users\\T480S\\Desktop\\未标题-1.tif";
    String filePath =
//        "E:\\Deploy-DJ\\数据\\图片\\d3dc7d205c5496fd3919be725c6877a3_8aac14da1411c2451d0cb1de31a9d432_8.tif";
        "E:\\Deploy-DJ\\数据\\天津Tif图片\\打击后\\Level15\\天津爆炸6.tif";
    Map<String, Object> imageExt = getImageExt(new File(filePath));
    System.out.println(imageExt);
  }

  /**
   * 获取tif图片坐标
   *
   * @param file tif文件路径
   * @return 最大最小坐标值
   */
  public static Map<String, Object> getImageExt(File file) {
    Map<String, Object> map = new HashMap<>();
    Map<String, Object>test = new HashMap<>();
    try {
      Metadata metadata = ImageMetadataReader.readMetadata(file);
      for (Directory directory : metadata.getDirectories()) {
        for (Tag tag : directory.getTags()) {
          String tagName = tag.getTagName();  //标签名
          System.out.println("标签名" + tagName);
          String desc = tag.getDescription(); //标签信息
          test.put(tagName, desc);
          if (tagName.equals("Image Height")) {
            System.out.println("图片高度: " + desc);
            String s = StringUtils.substringBeforeLast(desc, " ");
            map.put("height", s);
          } else if (tagName.equals("Image Width")) {
            System.out.println("图片宽度: " + desc);
            String s = StringUtils.substringBeforeLast(desc, " ");
            map.put("width", s);
          }
        }
      }
    } catch (ImageProcessingException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    System.out.println(test);
    return map;
  }
}
