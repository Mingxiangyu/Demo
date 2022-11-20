package com.iglens;

/**
 * 进度条工具
 *
 * <p>这个进度条打印期间，其他控制台输出会影响最终结果 所以做成单线程，阻塞的打印
 *
 * @author snx
 */
public class 进度条工具 {

  /** 当前进度 */
  private int index;
  /** 步长 */
  private int step;
  /** 进度条长度,总进度数值 */
  private final int barLength;

  /** 是否初始化 */
  private boolean hasInited = false;
  /** 是否已经结束 */
  private boolean hasFinished = false;
  /** 进度条title */
  private String title;

  private static final char PROCESS_CHAR = '█';
  private static final char WAIT_CHAR = '─';

  private 进度条工具() {
    index = 0;
    step = 1;
    barLength = 100;
    title = "Progress:";
  }

  public static 进度条工具 build() {
    return new 进度条工具();
  }

  public static 进度条工具 build(int step) {
    进度条工具 progressBar = build();
    progressBar.step = step;
    return progressBar;
  }

  public static 进度条工具 build(int index, int step) {
    进度条工具 progressBar = build(step);
    progressBar.index = index;
    return progressBar;
  }

  public static 进度条工具 build(int index, int step, String title) {
    进度条工具 progressBar = build(index, step);
    progressBar.title = title;
    return progressBar;
  }

  private String generate(int num, char ch) {
    if (num == 0) {
      return "";
    }
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < num; i++) {
      builder.append(ch);
    }
    return builder.toString();
  }

  private String genProcess(int num) {
    return generate(num, PROCESS_CHAR);
  }

  private String genWaitProcess(int num) {
    return generate(num, WAIT_CHAR);
  }

  /** 清空进度条 */
  private void cleanProcessBar() {
    System.out.print(generate(barLength / step + 6, '\b'));
  }

  /** 进度+1 */
  public void process() {
    checkStatus();
    checkInit();
    cleanProcessBar();
    index++;
    drawProgressBar();
    checkFinish();
  }

  /**
   * 进度+指定数值
   *
   * @param process 指定数值
   */
  public void process(int process) {
    checkStatus();
    checkInit();
    cleanProcessBar();
    if (index + process >= barLength) {
      index = barLength;
    } else {
      index += process;
    }
    drawProgressBar();
    checkFinish();
  }

  /** 步进 */
  public void step() {
    checkStatus();
    checkInit();
    cleanProcessBar();
    if (index + step >= barLength) {
      index = barLength;
    } else {
      index += step;
    }
    drawProgressBar();
    checkFinish();
  }

  /** 绘制进度条 */
  public void drawProgressBar() {
    System.out.printf(
        "%3d%%├%s%s┤",
        index, genProcess(index / step), genWaitProcess(barLength / step - index / step));
  }

  /** 检查进度条状态 已完成的进度条不可以继续执行 */
  private void checkStatus() {
    if (hasFinished) {
      throw new RuntimeException("进度条已经完成");
    }
  }

  /** 检查是否已经初始化 */
  private void checkInit() {
    if (!hasInited) {
      init();
    }
  }

  /** 检查是否已经完成 */
  private void checkFinish() {
    if (hasFinished() && !hasFinished) {
      finish();
    }
  }

  /** 是否已经完成进度条 */
  public boolean hasFinished() {
    return index >= barLength;
  }

  /** 初始化进度条 */
  private void init() {
    checkStatus();
    System.out.print(title);
    System.out.printf(
        "%3d%%[%s%s]",
        index, genProcess(index / step), genWaitProcess(barLength / step - index / step));
    hasInited = true;
  }

  /** 结束进度条，由 checkFinish()调用 */
  private void finish() {
    System.out.println();
    hasFinished = true;
  }

  /** 间隔50ms 自动执行进度条 */
  public void printProgress() throws InterruptedException {
    init();
    do {
      step();
      Thread.sleep(50);
      index++;
    } while (index <= barLength);
    System.out.println();
  }
}
