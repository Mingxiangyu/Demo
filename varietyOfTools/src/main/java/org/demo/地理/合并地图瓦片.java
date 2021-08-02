//package org.demo.地理;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.ExecutionException;
//import org.springframework.scheduling.support.TaskUtils;
//
//public class 合并地图瓦片 {
//
//  /** 合并图片 */
//  public void mergeTileImage(TileMergeMatWrap mat, TaskAllInfoEntity taskAllInfo, int zoom, LogCallback logBack,
//      LogCallback firshFinishBack) {
//    if (zoom == 0) {
//      return;
//    }
//    try {
//      logBack.execute("正在合并第" + zoom + "级地图，请勿关闭程序 implemented by OpenCV");
//      // 声明变量
//      TaskInstEntity taskInst = taskAllInfo.getEachLayerTask().get(zoom);
//      int z = taskInst.getZ();
//      int xStart = taskInst.getXStart();
//      int xEnd = taskInst.getXEnd();
//      int yStart = taskInst.getYStart();
//      int yEnd = taskInst.getYEnd();
//      int mergeImageWidth = StaticVar.TILE_WIDTH * (xEnd - xStart + 1);
//      int mergeImageHeight = StaticVar.TILE_HEIGHT * (yEnd - yStart + 1);
//      long xiangsudaxiao = (long) mergeImageWidth * (long) mergeImageHeight;
//      logBack.execute("合并后的图片width：" + mergeImageWidth + "，height：" + mergeImageHeight + "，像素大小："
//          + String.valueOf(xiangsudaxiao));
//      if (xiangsudaxiao > (long) Integer.MAX_VALUE) {
//        logBack.execute("该" + zoom + "级地图合并后像素大小大于int最大值" + Integer.MAX_VALUE + "，合并时间可能会稍长，建议低配置电脑不要执行超大尺寸合并");
//      }
//      String outPath = taskAllInfo.getSavePath() + "/tile-merge";
//      File outDir = new File(outPath);
//      if (!outDir.exists() || !outDir.isDirectory()) {
//        outDir.mkdirs();
//      }
//      // 文件类型
//      String suffix = TaskUtils.getSuffix(taskAllInfo.getImgType());
//      String outputFile = outPath + "/z=" + z + "." + suffix;
//      // 开启线程
//      mat.init(mergeImageWidth, mergeImageHeight);
//      int cpuCoreCount = Runtime.getRuntime().availableProcessors();
//      double d = Math.floor(Math.sqrt(cpuCoreCount));
//      TaskBlockDivide divide = TaskUtils.blockDivide(xStart, xEnd, yStart, yEnd, d);
//      ArrayList<Integer[]> divideX = divide.getDividX();
//      ArrayList<Integer[]> divideY = divide.getDividY();
//      List<Future<ImageMergeAsyncTaskResult>> futures = new ArrayList<>();
//      for (int i = 0; i < divideX.size(); i++) {
//        for (int j = 0; j < divideY.size(); j++) {
//          Future<ImageMergeAsyncTaskResult> future = tileMergeTask.exec(mat, z, xStart, yStart,
//              xStart + divideX.get(i)[0], xStart + divideX.get(i)[1], yStart + divideY.get(j)[0],
//              yStart + divideY.get(j)[1], taskInst.getPolygons(), taskAllInfo.getImgType(),
//              taskAllInfo.getSavePath(), taskAllInfo.getPathStyle(), i, j);
//          futures.add(future);
//        }
//      }
//      for (Future<ImageMergeAsyncTaskResult> future : futures) {
//        try {
//          future.get();
//        } catch (InterruptedException e) {
//          e.printStackTrace();
//        } catch (ExecutionException e) {
//          e.printStackTrace();
//        }
//      }
//      firshFinishBack.execute("true");
//      logBack.execute("正在写入至硬盘...");
//      mat.output(outputFile);
//      mat.destroy();
//      logBack.execute("第" + zoom + "级地图合并完成");
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }
//}
