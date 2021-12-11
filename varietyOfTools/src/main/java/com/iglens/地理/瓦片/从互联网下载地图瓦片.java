package com.iglens.地理.瓦片;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/** 下载地图瓦片 */
public class 从互联网下载地图瓦片 {
  public static void main(String[] args) throws Exception {

    // 根据properties文件配置url和存储路径 TODO 后续更新为通过@Value获取
    Properties properties = new Properties();
    File file = new File("classpath:baiduMap.properties");
    InputStream inputStream = 从互联网下载地图瓦片.class.getResourceAsStream("baiduMap.properties");
    // 判断是否有此配置文件文件，如果有则更新为配置文件内配置，否则则使用类配置
    if (inputStream != null) {
      properties.load(inputStream);
      String link = properties.getProperty("link");
      if (link != null && !link.isEmpty()) {
        BDTask.link = link;
      }
      String rootDir = properties.getProperty("rootDir");
      if (rootDir != null && !rootDir.isEmpty()) {
        BDTask.rootDir = rootDir;
      }
    }

    BDTask.startDownload();
  }
}

/** 线程池下载图片 */
class BDTask implements Runnable {
  //  /** 正常百度地图 */
  // String link =
  // "http://online3.map.bdimg.com/onlinelabel/?qt=tile&x={x}&y={y}&z={z}&styles=pl&udt=20170712&scaler=1&p=1";

  /** 午夜蓝版 */
  static String link =
      "http://api0.map.bdimg.com/customimage/tile?&x={x}&y={y}&z={z}&udt=20180711&scale=1&ak=0F7691e465f5d7d161a4771f48ee38ff&styles=t%3Awater%7Ce%3Aall%7Cc%3A%23021019%2Ct%3Ahighway%7Ce%3Ag.f%7Cc%3A%23000000%2Ct%3Ahighway%7Ce%3Ag.s%7Cc%3A%23147a92%2Ct%3Aarterial%7Ce%3Ag.f%7Cc%3A%23000000%2Ct%3Aarterial%7Ce%3Ag.s%7Cc%3A%230b3d51%2Ct%3Alocal%7Ce%3Ag%7Cc%3A%23000000%2Ct%3Aland%7Ce%3Aall%7Cc%3A%2308304b%2Ct%3Arailway%7Ce%3Ag.f%7Cc%3A%23000000%2Ct%3Arailway%7Ce%3Ag.s%7Cc%3A%2308304b%2Ct%3Asubway%7Ce%3Ag%7Cl%3A-70%2Ct%3Abuilding%7Ce%3Ag.f%7Cc%3A%23000000%2Ct%3Aall%7Ce%3Al.t.f%7Cc%3A%23857f7f%2Ct%3Aall%7Ce%3Al.t.s%7Cc%3A%23000000%2Ct%3Abuilding%7Ce%3Ag%7Cc%3A%23022338%2Ct%3Agreen%7Ce%3Ag%7Cc%3A%23062032%2Ct%3Aboundary%7Ce%3Aall%7Cc%3A%231e1c1c%2Ct%3Amanmade%7Ce%3Ag%7Cc%3A%23022338%2Ct%3Apoi%7Ce%3Aall%7Cv%3Aoff%2Ct%3Aall%7Ce%3Al.i%7Cv%3Aoff%2Ct%3Aall%7Ce%3Al.t.f%7Cv%3Aon%7Cc%3A%232da0c6";
  ;
  /** 瓦片保存路径 */
  static String rootDir = "d:/mybaidumapdownload1";

  int i; // x坐标
  int j; // y坐标
  int z; // 缩放级别

  static volatile Integer c = 0; // 成功数
  static volatile Integer fail = 0; // 失败数量

  public BDTask(String link, int i, int j, int z) {
    BDTask.link = link;
    this.i = i;
    this.j = j;
    this.z = z;
  }

  public static void startDownload() {
    ThreadPoolExecutor threadPoolExecutor = null;
    long start = 0L;
    for (Level c : Level.values()) {
      int z = c.getLevel();
      int xmin = c.getX_min();
      int xmax = c.getX_max();
      int ymin = c.getY_min();
      int ymax = c.getY_max();
      start = System.currentTimeMillis(); // 开始时间
      threadPoolExecutor =
          new ThreadPoolExecutor(
              2, 4, 500, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
      for (int i = xmin; i <= xmax; i++) { // 循环X
        for (int j = ymin; j <= ymax; j++) { // 循环Y
          threadPoolExecutor.execute(new BDTask(link, i, j, z)); // 下载图片
          // new Thread(new BDTask(link,i,j,z)).start();    //此种方法会一直创建线程导致死机

          // 以下方法为单线程操作，已弃用，替换为线程操作
          /*try {
              URL url = new URL(link.replace("{x}", i + "").replace("{y}", j + "").replace("{z}", z + ""));
              HttpURLConnection conn = (HttpURLConnection) url.openConnection();
              conn.setConnectTimeout(100);
              conn.connect();
              InputStream in = conn.getInputStream();
              File dir = new File("d:/mybaidumapdownload1/tiles/" + z + "/" + i);
              if (!dir.exists()) {
                  dir.mkdirs();
              }
              File file = new File("d:/mybaidumapdownload1/tiles/" + z + "/" + i + "/" + j + ".jpg");
              if (!file.exists()) {
                  file.createNewFile();
              }
              OutputStream out = new FileOutputStream(file);
              byte[] bytes = new byte[1024 * 20];
              int len = 0;
              while ((len = in.read(bytes)) != -1) {
                  out.write(bytes, 0, len);
              }
              out.close();
              in.close();
              //System.out.println("已成功下载:" + z + "_" + i + "_" + j + ".jpg");
              c++;
          } catch (Exception e) {
              System.out.println(e.getMessage());
              fail++;
          }*/
        } // 循环Y结束
      } // 循环X结束
    }

    threadPoolExecutor.shutdown(); // 关闭线程池
    while (!threadPoolExecutor.isTerminated()) {} // 所有任务被执行完毕时继续往下执行
    System.out.println("-------用时-------:" + (System.currentTimeMillis() - start));
    System.out.println("共下载:   " + c + "   张");
    System.out.println("失败:   " + fail + "   张");
  }

  @Override
  public void run() {
    try {
      URL url = new URL(link.replace("{x}", i + "").replace("{y}", j + "").replace("{z}", z + ""));
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setConnectTimeout(100);
      conn.connect();
      InputStream in = conn.getInputStream();

      File file = new File(rootDir + "/tiles/" + z + "/" + i + "/" + j + ".jpg");
      if (!file.getParentFile().exists()) {
        file.getParentFile().mkdirs();
      }
      file.createNewFile();

      OutputStream out = new FileOutputStream(file);
      byte[] bytes = new byte[1024 * 20];
      int len = 0;
      while ((len = in.read(bytes)) != -1) {
        out.write(bytes, 0, len);
      }
      out.close();
      in.close();
      synchronized (fail) {
        c++;
      }
    } catch (Exception e) {
      System.out.println(e.getMessage());
      synchronized (c) {
        fail++;
      }
    }
  }
}

/** 所有瓦片层级枚举类型，这里所有的等级和xy轴数据为扬州的地图 level等级 x最小 x最大 y最小 y最大 */
enum Level {
  Level_3(3, 1, 1, 0, 0),
  Level_4(4, 3, 3, 0, 0),
  Level_5(5, 6, 6, 1, 3),
  Level_6(6, 12, 12, 3, 3),
  Level_7(7, 25, 25, 7, 7),
  Level_8(8, 50, 50, 14, 14),
  Level_9(9, 101, 101, 28, 29),
  Level_10(10, 202, 203, 57, 59),
  Level_11(11, 404, 407, 115, 119),
  Level_12(12, 808, 814, 230, 239),
  Level_13(13, 1617, 1629, 460, 480),
  Level_14(14, 3234, 3259, 920, 960),
  Level_15(15, 6469, 6518, 1840, 1920),
  Level_16(16, 12939, 13036, 3680, 3850),
  Level_17(17, 25878, 26073, 7360, 7670),
  Level_18(18, 51757, 52146, 14720, 15400),
  Level_19(19, 103514, 104292, 29400, 30700);

  private int level;
  private int x_min;
  private int x_max;
  private int y_min;
  private int y_max;

  Level(int level, int x_min, int x_max, int y_min, int y_max) {
    this.level = level;
    this.x_min = x_min;
    this.x_max = x_max;
    this.y_min = y_min;
    this.y_max = y_max;
  }

  public int getLevel() {
    return level;
  }

  public int getX_min() {
    return x_min;
  }

  public int getX_max() {
    return x_max;
  }

  public int getY_min() {
    return y_min;
  }

  public int getY_max() {
    return y_max;
  }
}
