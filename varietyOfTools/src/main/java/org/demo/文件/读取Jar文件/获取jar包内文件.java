package org.demo.文件.读取Jar文件;

import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

public class 获取jar包内文件 {

  public static void main(String[] args) {
    String path = "";
    int current = 1;
    int size = 10;
    String fileName = ".xsl";
    //String fileName = "报告"
//    Page<JarEntry> page = new Page<JarEntry>(current, size);
    try {
      List<JarEntry> fileList = 获取jar包内文件.getJarFilesByFolderPath(path, fileName);
//      List<JarEntry> fileList = page.getRecords();
      for (JarEntry jarEntry : fileList) {

        //文件路径
        String entryUrl = jarEntry.getName();

        //文件名
        String entryName = entryUrl.substring(entryUrl.indexOf(path) + 5);//加5，是yjbg + /
        System.out.println("文件名 " + entryName);
        InputStream is = 获取jar包内文件.getJarEntryInputStream(path, entryUrl);
        System.out.println("这里的流，可以直接用HttpServletResponse返回给客户端下载 " + is);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * 获取jar里某个文件的文件流
   * @param path
   * @param entryName
   * @return
   * @throws IOException
   */
  public static InputStream getJarEntryInputStream(String path, String entryName) throws IOException {
    JarFile jarFile = getJar(path);
    ZipEntry ze = jarFile.getEntry(entryName);
    return jarFile.getInputStream(ze);
  }

  /**
   * 获取jar里的某个文件
   * @param path
   * @param entryUrl
   * @return
   * @throws IOException
   */
  public static ZipEntry getJarEntry(String path, String entryUrl) throws IOException {
    JarFile jarFile = getJar(path);
    return jarFile.getEntry(entryUrl);
  }

  /**
   * 读取文件夹的文件
   * @param path 路径（这里是的值是"yjbg"）
   * @param fileName 要匹配的文件，所包含的关键字（为空时，返回path下的所有文件）
   * @return
   * @throws IOException
   */
  public static List<JarEntry> getJarFilesByFolderPath(String path,
      String fileName)
      throws IOException {
    List<JarEntry> list = new ArrayList<JarEntry>();
    JarFile jarFile = getJar(path);
    Enumeration<JarEntry> jarEntrys = jarFile.entries();
    while (jarEntrys.hasMoreElements()) {
      JarEntry entry = jarEntrys.nextElement();
      if (entry.isDirectory()) {
        continue;
      }
      String name = entry.getName();
      boolean isNameNull = fileName == null || fileName.trim().length() == 0;
      if (name.contains(path) && (isNameNull || (!isNameNull && name.contains(fileName)))) {
        list.add(entry);
      }
    }
    orderJarEntry(list, true);
//    subListByPage(list, page);
    return list;
  }

  /**
   * 获取jar文件
   * @param path
   * @return
   * @throws IOException
   */
  public static JarFile getJar(String path) throws IOException {
    URL url = 获取jar包内文件.class.getClassLoader().getResource(path);
    String jarPath = url.toString().substring(0, url.toString().indexOf("!/") + 2);
    URL jarURL = new URL(jarPath);
    JarURLConnection jarCon = (JarURLConnection) jarURL.openConnection();
    JarFile jarFile = jarCon.getJarFile();
    return jarFile;
  }

  /**
   * 根据最后的修改时间排序
   * @param files
   * @param isDesc
   * @return
   */
  public static List<JarEntry> orderJarEntry(List<JarEntry> files, boolean isDesc) {
    files.sort((JarEntry h1, JarEntry h2) -> {
      Long h1M = h1.getLastModifiedTime().toMillis();
      Long h2M = h2.getLastModifiedTime().toMillis();
      if (isDesc) {
        return h2M.compareTo(h1M);
      } else {
        return h1M.compareTo(h2M);
      }
    });
    return files;
  }

  /**
   * 分页
   * @param list
   * @param page
   * @return
   */
//  public static <T> Page<T> subListByPage(List<T> list, Page<T> page) {
//    List<T> records = new ArrayList<T>();
//    int total = 0;
//    if (list.size() > 0) {
//      total = list.size();
//      int current = page.getCurrent();
//      int size = page.getSize();
//      int fromIndex = (current - 1) * size;
//      int toIndex = current * size;
//      toIndex = (toIndex <= total) ? toIndex: total;
//      records = list.subList(fromIndex, toIndex);
//    }
//    page.setTotal(total);
//    page.setRecords(records);
//    return page;
//  }

}

