package com.iglens.统计代码;

import com.sun.istack.internal.Nullable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

public class TraceWatch {

  /**
   * Start time of the current task.
   */
  private long startMs;

  /**
   * Name of the current task.
   */
  @Nullable
  private String currentTaskName;

  @Getter
  private final Map<String, List<TraceWatch.TaskInfo>> taskMap = new HashMap<>();

  /**
   * 开始时间差类型指标记录，如果需要终止，请调用 {@link #stop()}
   *
   * @param taskName 指标名
   */
  public void start(String taskName) throws IllegalStateException {
    if (this.currentTaskName != null) {
      throw new IllegalStateException("Can't start TraceWatch: it's already running");
    }
    this.currentTaskName = taskName;
    this.startMs = System.currentTimeMillis();
  }

  /**
   * 终止时间差类型指标记录，调用前请确保已经调用
   */
  public void stop() throws IllegalStateException {
    if (this.currentTaskName == null) {
      throw new IllegalStateException("Can't stop TraceWatch: it's not running");
    }
    long lastTime = System.currentTimeMillis() - this.startMs;

    TraceWatch.TaskInfo info = new TraceWatch.TaskInfo(this.currentTaskName, lastTime);

    this.taskMap.computeIfAbsent(this.currentTaskName, e -> new LinkedList<>()).add(info);

    this.currentTaskName = null;
  }

  /**
   * 直接记录指标数据，不局限于时间差类型
   *
   * @param taskName 指标名
   * @param data 指标数据
   */
  public void record(String taskName, Object data) {
    TraceWatch.TaskInfo info = new TraceWatch.TaskInfo(taskName, data);

    this.taskMap.computeIfAbsent(taskName, e -> new LinkedList<>()).add(info);
  }

  @Data
  @AllArgsConstructor
  public static final class TaskInfo {

    private final String taskName;

    private final Object data;
  }
}
