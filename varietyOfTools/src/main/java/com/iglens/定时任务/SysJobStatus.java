package com.iglens.定时任务;
/** job状态控制 */
public enum SysJobStatus {
  NORMAL("正常", 1),
  SUSPEND("暂停", 0);

  private String desc;
  private Integer index;

  private SysJobStatus(String desc, Integer index) {
    this.desc = desc;
    this.index = index;
  }

  public String desc() {
    return this.desc;
  }

  public Integer index() {
    return this.index;
  }
}
