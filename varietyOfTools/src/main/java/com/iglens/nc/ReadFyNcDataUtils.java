package com.iglens.nc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import ucar.ma2.Array;
import ucar.nc2.Attribute;
import ucar.nc2.NetcdfFile;
import ucar.nc2.Variable;

/** https://blog.csdn.net/python113/article/details/125410208 */
public class ReadFyNcDataUtils {

  /**
   * 该工具类针对FY-3C卫星的GNOS掩星数据进行数据提取 提示：getVariablesLists()； getGlobalVariablesLists(),在这两个方法中，String
   * vbName和int position 是我根据目前（2020-8-13）的数据结构给定的，后续有可能国家卫星气象中心提供的数据可能会不一样，可以自行修改 提取数据包括：Global
   * Attributes:掩星点坐标（经纬度），时间（年，月，日） Attributes:海拔，温度，压强 也可以根据自己需要提取其他相关参数，请自行配置
   * TODO：涉及到了文件读，应该要有关闭操作,请在使用后添加.close（）进行关闭
   */
  private static List<String> mLat = new ArrayList<String>();

  private static List<String> mLon = new ArrayList<String>();
  private static List<String> mYear = new ArrayList<String>();
  private static List<String> mMonth = new ArrayList<String>();
  private static List<String> mDay = new ArrayList<String>();
  private static List<String> mHour = new ArrayList<String>();
  private static List<String> mMinute = new ArrayList<String>();
  private static List<String> mSecond = new ArrayList<String>();
  private static List<Double> mAlts = new ArrayList<Double>();
  private static List<List> mAltsList = new ArrayList<List>();

  private static List<Double> mTemps = new ArrayList<Double>();
  private static List<List> mTempsList = new ArrayList<List>();

  private static List<Double> mPress = new ArrayList<Double>();
  private static List<List> mPressList = new ArrayList<List>();
  private static List<NetcdfFile> mNc = new ArrayList<NetcdfFile>();
  private static List<File> fileList;
  private static NetcdfFile ncData;

  public static void main(String[] args) {
    String path = "C:\\Users\\zhouhuilin\\Desktop\\testNc";
    getNcDataList(path);
  }

  /**
   * 根据用户提供的文件夹路径，对数据进行读取，并返回路径名 TODO：在这里忽略了对文件夹路径，以及文件夹内容的判断 但是，不会真的有人这么无聊给错误的文件夹或者路径吧，不会吧，不会吧
   * 注意！！！这个方法是必须要实现的，因为这会根据你提供的路径进行读取
   *
   * @param filepath 这个参数必须给
   * @return
   */
  public static List<NetcdfFile> getNcDataList(String filepath) {
    File file = new File(filepath);
    File[] filesNames = file.listFiles();
    fileList = Arrays.asList(filesNames);
    // TODO:将读出的文件列表进行数据来源判断：①GNOS②COSMIC
    for (File itemFile : fileList) {
      try {
        // 根据代码，将这个二进制文件里面的内容读取出来，我们看看使用代码读取到的东西是什么。把读取到的东西进行控制台输出，和读取TXT文件一样，
        ncData = NetcdfFile.open(itemFile.getPath());
        // 控制台输出
        System.out.println(ncData);
        mNc.add(ncData);

      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return mNc;
  }

  /**
   * 获取掩星点纬度,11
   *
   * @return
   */
  public static List<String> getRoLat() {
    return getGlobalVariablesLists(11, mLat);
  }

  /**
   * 获取掩星点经度,12
   *
   * @return
   */
  public static List<String> getRoLon() {
    return getGlobalVariablesLists(12, mLon);
  }

  /**
   * 获取掩星事件发生年份
   *
   * @return
   */
  public static List<String> getRoYear() {
    return getGlobalVariablesLists(0, mYear);
  }

  /**
   * 获取掩星事件发生月份
   *
   * @return
   */
  public static List<String> getRoMonth() {
    return getGlobalVariablesLists(1, mMonth);
  }

  /**
   * 获取掩星事件发生天
   *
   * @return
   */
  public static List<String> getRoDay() {
    return getGlobalVariablesLists(2, mDay);
  }

  /**
   * 获取掩星事件发生小时
   *
   * @return
   */
  public static List<String> getRoHour() {
    return getGlobalVariablesLists(3, mHour);
  }

  /**
   * 获取掩星事件发生分钟
   *
   * @return
   */
  public static List<String> getRoMinute() {
    return getGlobalVariablesLists(4, mMinute);
  }

  /**
   * 获取掩星事件发生秒数
   *
   * @return
   */
  public static List<String> getRoSecond() {
    return getGlobalVariablesLists(5, mSecond);
  }

  /**
   * 获得海拔
   *
   * @param
   * @return
   */
  public static List<List> getAltList() {
    return getVariablesLists("MSL_alt", mAlts, mAltsList);
  }

  /**
   * 获得温度
   *
   * @param
   * @return
   */
  public static List<List> getTempList() {
    return getVariablesLists("Temp", mTemps, mTempsList);
  }

  /**
   * 获得压强
   *
   * @param
   * @return
   */
  public static List<List> getPressList() {
    return getVariablesLists("Pres", mPress, mPressList);
  }

  private static List<String> getGlobalVariablesLists(int position, List<String> mGlobalVb) {
    for (NetcdfFile netcdfFile : mNc) {
      List<Attribute> globalAttributes = netcdfFile.getGlobalAttributes();
      Array values = globalAttributes.get(position).getValues();
      String s = values.toString();
      mGlobalVb.add(s);
    }
    return mGlobalVb;
  }

  private static List<List> getVariablesLists(
      String vbName, List<Double> mVrs, List<List> mVrsList) {
    for (NetcdfFile netcdfFile : mNc) {
      Variable pres = netcdfFile.findVariable(vbName);
      Array read = null;
      try {
        read = pres.read();
      } catch (IOException e) {
        e.printStackTrace();
      }
      for (long i = 0; i < read.getSize(); i++) {
        double aDouble = read.getDouble((int) i);
        mVrs.add(aDouble);
      }
      mVrsList.add(mVrs);
    }
    return mVrsList;
  }
}
