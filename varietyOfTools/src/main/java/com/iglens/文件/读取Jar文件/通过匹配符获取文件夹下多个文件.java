package com.iglens.文件.读取Jar文件;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.ResourceUtils;

public class 通过匹配符获取文件夹下多个文件 {

  public static void main(String[] args) throws IOException {
    /* TODO   PathMatchingResourcePatternResolver中传入的 locationPattern 中间的 斜线/ 不能使用 File.separator <br>
    ,必须使用 / ，否则会匹配不到 */

    // "META-INF/folder/**/*.txt" 这个路径换成自己的路径即可
    String s = "image/**/*";
    //  加载当前项目classpath下META-INF/folder及其子文件夹中的所有文件
    Resource[] resources =
        new PathMatchingResourcePatternResolver()
            .getResources(ResourceUtils.CLASSPATH_URL_PREFIX + s);

    //  加载当前项目classpath下META-INF/folder及其子文件夹中的所有以.txt结尾的文件
    Resource[] resources2 =
        new PathMatchingResourcePatternResolver()
            .getResources(ResourceUtils.CLASSPATH_URL_PREFIX + s);

    //  加载当前项目及所有jar中classpath下的所有META-INF/spring.factories文件（springboot自动装配的主要功能）
    Resource[] resources3 =
        new PathMatchingResourcePatternResolver()
            .getResources(
                ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + "META-INF/spring.factories");

    // 遍历文件内容
    for (Resource resource : resources) {
      // 获取该文件的文件名，jar包情况下也可以拿到文件名称
      String filename = resource.getFilename();
      System.out.println("文件名称: " + filename);
      StringBuffer script = new StringBuffer();
      try (InputStreamReader isr =
              new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);
          BufferedReader bufferReader = new BufferedReader(isr)) {
        String tempString;
        while ((tempString = bufferReader.readLine()) != null) {
          script.append(tempString).append("\n");
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
      System.out.println("script:" + script.toString());
    }
  }
}
