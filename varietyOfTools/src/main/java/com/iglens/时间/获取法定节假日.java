package com.iglens.时间;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class 获取法定节假日 {
    /**
     * 请求第三方接口的方法
     *
     * @param url 请求的url
     * @return
     */
    public static String get(String url, int i) {
        // 请求url
        URL getUrl = null;
        // 连接
        HttpURLConnection connection = null;
        // 输入流
        BufferedReader reader = null;
        // 返回结果
        StringBuilder lines = new StringBuilder();
        try {
            // 初始化url
            getUrl = new URL(url);
            // 获取url的连接
            connection = (HttpURLConnection) getUrl.openConnection();
            // 发起连接
            connection.connect();
            // 获取输入流
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
            // 读取返回结果
            String line = "";
            // 读取每一行
            while ((line = reader.readLine()) != null) {
                // 拼接返回结果
                lines.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            // 如果请求失败, 尝试重新请求
            if (i < 2) {
                i++;
                try {
                    System.out.println("第" + i + "次获取失败, 尝试重新请求");
                    Thread.sleep(3000);
                    return get(url, i);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            } else {
                System.out.println("获取失败, 请检查网络或稍后重试");
            }
        } finally {
            // 在finally中关闭资源
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
        return lines.toString();
    }

    /**
     * 设置尝试次数
     *
     * @param url
     * @return
     */
    public static String get(String url) {
        return get(url, 0);
    }

    /**
     * 获取指定年份的url
     *
     * @param year 年份
     * @return
     */
    private static String getPath(String year) {
        // 源json地址
        // return "https://raw.githubusercontent.com/NateScarlet/holiday-cn/master/" + year + ".json";
        // 国内镜像地址
        // return "https://natescarlet.coding.net/p/github/d/holiday-cn/git/raw/master/" + year + ".json";
        // cdn地址
        return "https://cdn.jsdelivr.net/gh/NateScarlet/holiday-cn@master/" + year + ".json";
    }

    /**
     * 获取指定年份的节假日信息
     *
     * @param year 年份 如："2025"
     * @return
     */
    public static JSONArray getHolidayOfYear(String year) {
        // 获取指定年份的url
        String url = getPath(year);
        // 获取返回结果
        String json = get(url);
        // 解析返回结果
        JSONObject jsonObject = JSONObject.parseObject(json);
        JSONArray jsonArray = jsonObject.getJSONArray("days");
        return jsonArray;
    }

    /**
     * 获取指定日期的节假日信息
     *
     * @param date 日期 如："2025-04-05"
     * @return 如果是节假日, 则isOffDay为true, 否则为false
     */
    public static JSONObject getHolidayOfDate(String date) {
        // 获取该年份的节假日信息
        JSONArray jsonArray = getHolidayOfYear(date.substring(0, 4));
        JSONObject result = new JSONObject();
        // 遍历节假日信息
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            // 如果节假日信息中的日期与传入的日期相同，则返回该节假日信息
            if (jsonObject.getString("date").equals(date)) {
                result = jsonObject;
            }
        }
        if (result.isEmpty()) {
            // 如果没有找到该日期的节假日信息，则将该日期的"isOffDay"设置为false
            result.put("date", date);
            result.put("isOffDay", false);
        }
        return result;
    }

    /**
     * 判断日期是否节假日的方法
     *
     * @param date 日期 如："2025-04-05"
     */
    public static boolean isHoliday(String date) {
        // 获取该年份的节假日信息
        JSONArray jsonArray = getHolidayOfYear(date.substring(0, 4));
        // 遍历节假日信息
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            // 如果传入的日期存在于该年的节假日中，则返回对应的节假日信息(true为节假日, false为调休工作日)
            if (jsonObject.getString("date").equals(date)) {
                return jsonObject.getBoolean("isOffDay");
            }
        }
        // 如果不存在, 则直接返回false(非节假日)
        return false;
    }

    /**
     * 获取指定Date的节假日信息
     *
     * @param date
     * @return
     */
    public static JSONObject getHolidayOfDate(Date date) {
        // 将Date格式化为String
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return getHolidayOfDate(sdf.format(date));
    }

    /**
     * 判断指定Date是否节假日
     *
     * @param date
     * @return
     */
    public static boolean isHoliday(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return isHoliday(sdf.format(date));
    }

    /**
     * 获取从指定日期开始的节假日天数
     *
     * @param date
     * @return
     */
    public static int holidayAfterDate(String date, SimpleDateFormat sdf) {
        // 日历类
        Calendar calendar = Calendar.getInstance();
        JSONArray holidayOfYear = getHolidayOfYear(date.substring(0, 4));
        int days = 0;
        try {
            calendar.setTime(sdf.parse(date));
            for (int i = 0; i < holidayOfYear.size(); i++) {
                JSONObject jsonObject = holidayOfYear.getJSONObject(i);
                if (jsonObject.getString("date").equals(date) && jsonObject.getBoolean("isOffDay")) {
                    days++;
                    calendar.add(Calendar.DATE, 1);
                    date = sdf.format(calendar.getTime());
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return days;
    }

    public static int holidayAfterDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return holidayAfterDate(date, sdf);
    }

    public static int holidayAfterDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return holidayAfterDate(sdf.format(date), sdf);
    }

    // 测试
    public static void main(String[] args) {
        System.out.println("2025年的节假日: " + getHolidayOfYear("2025"));
        System.out.println("节假日的信息: " + getHolidayOfDate("2025-04-05"));
        System.out.println("节假日调休工作日: " + getHolidayOfDate("2025-04-02"));
        System.out.println("非节假日的信息: " + getHolidayOfDate("2025-04-07"));
        System.out.println("判断一个节假日: " + isHoliday("2025-04-05"));
        System.out.println("判断一个非节假日: " + isHoliday("2025-04-07"));
        System.out.println("自一个节假日起的节假日天数: " + holidayAfterDate("2025-04-04"));
        System.out.println("自非节假日起的节假日天数: " + holidayAfterDate("2025-04-06"));
    }
}
