package com.iglens;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

public class 获取注册表值 {

  private static final String REGQUERY_UTIL = "reg query ";
  private static final String REGSTR_TOKEN = "REG_SZ";
  private static final String REGDWORD_TOKEN = "REG_DWORD";

  private static final String PERSONAL_FOLDER_CMD =
      REGQUERY_UTIL
          + "\"HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\"
          + "Explorer\\Shell Folders\" /v Personal";
  private static final String CPU_SPEED_CMD =
      REGQUERY_UTIL + "\"HKLM\\HARDWARE\\DESCRIPTION\\System\\CentralProcessor\\0\"" + " /v ~MHz";
  private static final String CPU_NAME_CMD =
      REGQUERY_UTIL
          + "\"HKLM\\HARDWARE\\DESCRIPTION\\System\\CentralProcessor\\0\""
          + " /v ProcessorNameString";
  private static final String HTTP_DEFAULT_BROWSERURI =
      REGQUERY_UTIL
          + "\"HKEY_CLASSES_ROOT\\http\\shell\\open\\command\""
          + ""; // http\\shell\\open\\ddeexec\\Topic
  private static final String HTTP_DEFAULT_BROWSERNAME =
      REGQUERY_UTIL + "\"HkEY_CLASSES_ROOT\\http\\shell\\open\\ddeexec\\Topic\"" + "";

  public static void main(String[] args) {
    System.out.println("Personal directory : " + getCurrentUserPersonalFolderPath());
    System.out.println("CPU Name : " + getCPUName());
    System.out.println("CPU Speed : " + getCPUSpeed() + " Mhz");
  }

  public static String getCurrentUserPersonalFolderPath() {
    try {
      java.lang.Process process = Runtime.getRuntime().exec(PERSONAL_FOLDER_CMD);
      StreamReader reader = new StreamReader(process.getInputStream());

      reader.start();
      process.waitFor();
      reader.join();

      String result = reader.getResult();
      int p = result.indexOf(REGSTR_TOKEN);

      if (p == -1) {
        return null;
      }

      return result.substring(p + REGSTR_TOKEN.length()).trim();
    } catch (Exception e) {
      return null;
    }
  }

  public static String getCPUSpeed() {
    try {
      java.lang.Process process = Runtime.getRuntime().exec(CPU_SPEED_CMD);
      StreamReader reader = new StreamReader(process.getInputStream());

      reader.start();
      process.waitFor();
      reader.join();

      String result = reader.getResult();
      int p = result.indexOf(REGDWORD_TOKEN);

      if (p == -1) {
        return null;
      }

      // CPU speed in Mhz (minus 1) in HEX notation, convert it to DEC
      String temp = result.substring(p + REGDWORD_TOKEN.length()).trim();
      return Integer.toString((Integer.parseInt(temp.substring("0x".length()), 16) + 1));
    } catch (Exception e) {
      return null;
    }
  }

  public static String getCPUName() {
    try {

      java.lang.Process process = Runtime.getRuntime().exec(HTTP_DEFAULT_BROWSERURI);
      StreamReader reader = new StreamReader(process.getInputStream());

      reader.start();
      process.waitFor();
      reader.join();

      String result = reader.getResult();
      int p = result.indexOf(REGSTR_TOKEN);

      if (p == -1) {
        return null;
      }

      return result.substring(p + REGSTR_TOKEN.length()).trim();
    } catch (Exception e) {
      return null;
    }
  }

  static class StreamReader extends Thread {
    private InputStream is;
    private StringWriter sw;

    StreamReader(InputStream is) {
      this.is = is;
      sw = new StringWriter();
    }

    @Override
    public void run() {
      try {
        int c;
        while ((c = is.read()) != -1) {
          sw.write(c);
        }
      } catch (IOException e) {
        ;
      }
    }

    String getResult() {
      return sw.toString();
    }
  }

}
