package org.demo.监听本地文件夹变动;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

@Component
//public class ListenComponent implements ApplicationRunner {
public class ListenComponent  {

//  @Value("${listenerfilepath.satellite}")
  private String satellitePath;

//  @Value("${listenerfilepath.notification}")
  private String notificationPath;

//  @Value("${listenerfilepath.target}")
  private String targetPath;

//  @Value("${socket.listen}")
  private String socketPath;

  private static ListenComponent listenController;
  private ThreadPoolExecutor fixedThreadPool =
      new ThreadPoolExecutor(3, 10, 50, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(10));

//  @PostConstruct // 重新加载变量
  public void init() {
    listenController = this;
    // 初使化时将已静态化的testService实例化
  }

  // 项目启动后执行的方法
//  @Override
  public void run(ApplicationArguments applicationArguments) throws Exception {
    getFile(satellitePath);
    getFile(notificationPath);
    getFile(targetPath);
    getFile(socketPath);
    SocketServerListener socketServerListener = new SocketServerListener();
    socketServerListener.init();
  }

  public void getFile(String path) {
    ScheduledExecutorService executorService =
        new ScheduledThreadPoolExecutor(
            1,
            new BasicThreadFactory.Builder()
                .namingPattern("example-schedule-pool-%d")
                .daemon(true)
                .build());
    executorService.scheduleAtFixedRate(
        () -> {
          try {
            WatchKey key;
            WatchService watchService = FileSystems.getDefault().newWatchService();
            // do something
            Paths.get(path)
                .register(
                    watchService,
                    StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_MODIFY);
            while (true) {
              System.out.println("等待文件加载！" + path);
              File[] listFiles = new File(path).listFiles();
              int length = listFiles.length;
              if (length > 0) {
                for (File f : listFiles) {
                  if (checkFileWritingOn(f.getPath())) {
                    System.out.println(f.getPath() + "读文件夹" + System.currentTimeMillis());
                    fixedThreadPool.execute(new Task(path, f.getPath()));
                  }
                }
              }
              key = watchService.take(); // 没有文件增加时，阻塞在这里
              for (WatchEvent<?> event : key.pollEvents()) {
                String filePath = path + event.context();
                //                            System.out.println("增加文件的文件夹路径 :"+filePath);
                //                            System.out.println("增加文件的名称 :"+event.context());
                int le = new File(path).listFiles().length;
                //                listenController.automaticService.fileState(path, le); TODO
                // 判断文件是否写入完毕
                if (checkFileWritingOn(filePath)) {
                  System.out.println(filePath + "新增" + System.currentTimeMillis());
                  fixedThreadPool.execute(new Task(path, filePath));
                }
              }
              if (!key.reset()) {
                break; // 中断循环
              }
            }
          } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
        },
        1,
        30,
        TimeUnit.SECONDS);
  }

  /** 线程处理 */
  class Task extends Thread {
    /** 监听路径 */
    private final String path;
    /** 新增文件路径 */
    private final String filePath;

    public Task(String path, String filePath) {
      this.path = path;
      this.filePath = filePath;
    }

    @Override
    public void run() {
      try {
        File file1 = new File(filePath);
        System.out.println("分类");
        if (path.equals(socketPath) && file1.renameTo(file1)) {
          // 传输文件分类
          //          listenController.automaticService.classify(filePath); todo
        } else if (path.equals(satellitePath)) {
          // 文件夹处理
          if (file1.isDirectory()) {
            // 获取文件夹下所有文件
            List<File> readfile = RarFileUtil.readfile(filePath, new ArrayList<>());
            for (File fil : readfile) {
              //              listenController.automaticService.cernClass(fil.getPath()); todo
            }
          } else if (file1.renameTo(file1)) {
            //            listenController.automaticService.cernClass(filePath); todo
          }
        } else if (path.equals(targetPath)) {
          if (file1.isDirectory()) {
            //            listenController.targetStorageService.repetitive(filePath); todo
          }
        } else if (path.equals(notificationPath)) {
          //          listenController.notificationService.notificationAnalyze(filePath);todo
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * 判断文件是否还在写入
   *
   * @param filePath 文件路径
   * @return true 写入完成
   * @throws Exception
   */
  // TODO 判断文件是否写入不生效
  public static boolean checkFileWritingOn(String filePath) throws Exception {
    long oldLen = 0;
    long newLen = 0;
    long time = 0;
    File file = new File(filePath);
    if (!file.exists()) {
      return false;
    }
    while (true) {
      newLen = file.length();
      if (newLen == 0) {
        System.out.println(newLen);
        Thread.sleep(2000);
        newLen = file.length();
      }
      if ((newLen - oldLen) > 0) {
        System.out.println("newLen" + newLen);
        oldLen = newLen;
        Thread.sleep(2000);
        time += 2000;
        // 设置超时时间，不要让它无限期的等下去，避免影响总的线程执行
        if (time > 500000) {
          return false;
        }
      } else {
        return true;
      }
    }
  }
}
