package com.iglens;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author xming
 */
public class 优先级进程调度算法 {

  public static void main(String[] args) {
    Scanner s = new Scanner(System.in);
    System.out.print("请输入操作(1：开始进程调度 0:结束进程):");
    int flag = s.nextInt();
    while (flag == 1) {
      ArrayList<Process> list = new ArrayList<>();
      ArrayList<Process> readyList = new ArrayList<>();
      int zz;
      int hour;
      int min;
      System.out.println(
          "=====================================================================================");
      System.out.print("请输入进程数:");
      int num = s.nextInt();
      System.out.println("请输入进程的参数:");
      System.out.println("ID号  名字  优先级	 到达时间  执行时间(分钟):");
      //输入时用空格分隔
      for (int i = 0; i < num; i++) {
        Process p = new Process();
        p.setId(s.nextInt());
        p.setName(s.next());
        p.setGood(s.nextInt());
        time t = new time();
        t.initTime(s.next());
        p.setArrive(t);
        p.setZx(s.nextInt());
        list.add(p);
      }
      //            list.sort(Process::compareTo);
      System.out.println(" ");
      System.out.println("模拟进程优先级调度过程输出结果:");
      System.out.println("ID号  名字  优先级	 到达时间  执行时间(分钟)  开始时间  完成时间  周转时间(分钟)  带权周转时间(系数):");
      int loc = 0;
      float sumZz = 0;
      float sumZzxs = 0;
      time finish = new time();
      finish.setHour(0);
      finish.setMin(0);
      // 找出最先到达的进程
      for (int i = 1; i < list.size(); i++) {
        if (time.sub(list.get(loc).getArrive().toString(), list.get(i).getArrive().toString())
            < 0) {
          loc = i;
        }
      }
      while (list.size() != 0) {
        readyList.clear();
        // 计算时间有关的信息
        if (time.sub(list.get(loc).getArrive().toString(), finish.toString()) >= 0) {
          hour = finish.getHour();
          min = finish.getMin();
          time t1 = new time();
          t1.setHour(hour);
          t1.setMin(min);
          list.get(loc).setStart(t1);
        } else {
          hour = list.get(loc).getArrive().getHour();
          min = list.get(loc).getArrive().getMin();
          time t1 = new time();
          t1.setHour(hour);
          t1.setMin(min);
          list.get(loc).setStart(t1);
        }
        hour += (min + list.get(loc).getZx()) / 60;
        min = (min + list.get(loc).getZx()) % 60;
        time t2 = new time();
        t2.setHour(hour);
        t2.setMin(min);
        finish = t2;
        list.get(loc).setFinish(t2);

        //队头进程执行完成时就绪队列中已有的进程
        for (int i = 0; i < list.size(); i++) {
          if (i != loc
              && time.sub(list.get(i).getArrive().toString(), list.get(loc).getFinish().toString())
              >= 0) {
            readyList.add(list.get(i));
          }
        }
        zz = time.sub(
            list.get(loc).getArrive().getHour() + ":" + list.get(loc).getArrive().getMin(),
            list.get(loc).getFinish().getHour() + ":" + list.get(loc).getFinish().getMin());
        list.get(loc).setZz(zz);
        list.get(loc).setZzxs((float) list.get(loc).getZz() / list.get(loc).getZx());
        sumZz += list.get(loc).getZz();
        sumZzxs += list.get(loc).getZzxs();
        System.out.println(String.format("%-6d", list.get(loc).getId())
            + String.format("%-6s", list.get(loc).getName())
            + String.format("%-6d", list.get(loc).getGood())
            + String.format("%-10s", list.get(loc).getArrive().toString())
            + String.format("%-13s", list.get(loc).getZx() + "(分钟)")
            + String.format("%-9s", list.get(loc).getStart().toString())
            + String.format("%-10s", list.get(loc).getFinish().toString())
            + String.format("%-16s", list.get(loc).getZz() + "(分钟)")
            + String.format("%-4.2f", list.get(loc).getZzxs()));
        list.remove(loc);
        //找出就绪队列中优先级最大的进程
        int x = 0;
        if (readyList.size() != 0) {
          for (int i = 0; i < readyList.size(); i++) {
            if (readyList.get(i).getGood() > readyList.get(x).getGood()) {
              x = i;
            }
          }
          for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId() == readyList.get(x).getId()) {
              loc = i;
            }
          }
        } else {
          //如果就绪队列为空，则执行第二到达的进程
          for (int i = 1; i < list.size(); i++) {
            if (time.sub(list.get(x).getArrive().toString(), list.get(i).getArrive().toString())
                < 0) {
              x = i;
            }
          }
          loc = x;
        }
      }
      System.out.println(
          String.format("%-48s", "系统平均周转时间为:") + String.format(" %-4.2f", sumZz / num));
      System.out.println(
          String.format("%-64s", "系统带权平均周转时间为: ") + String.format("%-4.2f", sumZzxs / num));
      System.out.println(
          "=====================================================================================");
      System.out.print("是否继续进程调度实验,开始实验:1，结束实验:0 :");
      flag = s.nextInt();
    }
    System.out.println("进程调度实验结束!!!");
    s.close();
  }
}
/* 测试数据
5001	p1	1	9:40	20
5004	p4	4	10:10	10
5005	p5	3	10:05	30
5002	p2	3	9:55	15
5003	p3	2	9:45	25
* */
/* 测试数据
5001	p1	1	14:40	20
5002	p4	2	10:10	10
5003	p5	3	10:05	30
5004	p2	4	9:55	15
5005	p3	5	9:45	25
5006	p6	6	10:40	20
5007	p8	7	11:10	10
5008	p9	8	12:05	30
5009	p10	9	13:55	15
5010	p7	10	7:15	15
*/


class Process implements Comparable {

  //编号
  private int id;
  // 进程名
  private String name;
  /**
   * 优先级
   */
  int good;
  /**
   * 到达就绪队列的时间
   */
  private time arrive;
  //执行时间
  private int zx;
  //进入CPU运行开始的时间
  private time start;
  //完成时间
  private time finish;
  /**
   * 周转时间 = 完成时间 - 到达就绪时间
   */
  private int zz;
  // 带权周转系数 = 周转时间/执行时间
  private float zzxs;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getGood() {
    return good;
  }

  public void setGood(int good) {
    this.good = good;
  }

  public time getArrive() {
    return arrive;
  }

  public void setArrive(time arrive) {
    this.arrive = arrive;
  }

  public int getZx() {
    return zx;
  }

  public void setZx(int zx) {
    this.zx = zx;
  }

  public time getStart() {
    return start;
  }

  public void setStart(time start) {
    this.start = start;
  }

  public time getFinish() {
    return finish;
  }

  public void setFinish(time finish) {
    this.finish = finish;
  }

  public int getZz() {
    return zz;
  }

  public void setZz(int zz) {
    this.zz = zz;
  }

  public float getZzxs() {
    return zzxs;
  }

  public void setZzxs(float zzxs) {
    this.zzxs = zzxs;
  }

  @Override
  public int compareTo(@NotNull Object o) {
    if (o instanceof Process) {
      Process t = (Process) o;
      return (this.arrive.getHour() * 60 + this.arrive.getMin()) - (t.arrive.getHour() * 60
          + t.arrive.getMin());
    }
    return 0;
  }

  @Override
  public String toString() {
    return "Process{" +
        "id=" + id +
        ", name='" + name + '\'' +
        ", good=" + good +
        ", arrive=" + arrive +
        ", zx=" + zx +
        ", start=" + start +
        ", finish=" + finish +
        ", zz=" + zz +
        ", zzxs=" + zzxs +
        '}';
  }

}

class time {

  private int hour;
  private int min;

  public int getHour() {
    return hour;
  }

  public void setHour(int hour) {
    this.hour = hour;
  }

  public int getMin() {
    return min;
  }

  public void setMin(int min) {
    this.min = min;
  }

  public void initTime(String t) {
    this.hour = Integer.parseInt(t.split(":")[0]);
    this.min = Integer.parseInt(t.split(":")[1]);
  }

  public static int sub(String s, String f) {
    return Integer.parseInt(f.split(":")[0]) * 60 + Integer.parseInt(f.split(":")[1])
        - Integer.parseInt(s.split(":")[0]) * 60 - Integer.parseInt(s.split(":")[1]);
  }

  @Override
  public String toString() {
    if (min < 10) {
      return hour + ":0" + min;
    } else {
      return hour + ":" + min;
    }
  }
}


