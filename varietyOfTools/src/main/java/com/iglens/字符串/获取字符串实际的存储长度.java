package com.iglens.字符串;

import java.nio.charset.Charset;
import org.apache.commons.lang3.StringUtils;

public class 获取字符串实际的存储长度 {
  public static void main(String[] args) {
    String s =
        "腾讯云\n"
            + "备案控制台\n"
            + "开发者社区\n"
            + "学习\n"
            + "实践\n"
            + "活动\n"
            + "工具\n"
            + "TVP\n"
            + "文章/答案/技术大牛搜索\n"
            + "写文章\n"
            + "提问\n"
            + "登录/注册\n"
            + "Java编程小技巧：获取字符串存储字节长度\n"
            + "文章来源：企鹅号 - 雾里寻踪\n"
            + "\n"
            + "我们在存储字符串到MySQL数据库时，英文是占据一个字节，而中文等却是占据2个字节甚至更多，如何在入库前判断实际的长度呢？我们可以通过getBytes方法获取存储长度，保证不会超出存储大小。\n"
            + "\n"
            + "代码如下：\n"
            + "\n"
            + "/**\n"
            + "\n"
            + "* 获取字符串实际的存储长度\n"
            + "\n"
            + "* @param str 字符串\n"
            + "\n"
            + "* @return int\n"
            + "\n"
            + "*/\n"
            + "\n"
            + "public static int getActualLength(String str) {\n"
            + "\n"
            + "if (StringUtil.isEmptyOrNull(str)) {\n"
            + "\n"
            + "return 0;\n"
            + "\n"
            + "}\n"
            + "\n"
            + "return str.getBytes(Charset.forName(\"UTF-8\")).length;\n"
            + "\n"
            + "}\n"
            + "\n"
            + "/**\n"
            + "\n"
            + "* 获取字符串实际的存储长度\n"
            + "\n"
            + "* @param str         字符串\n"
            + "\n"
            + "* @param charsetName 编码格式\n"
            + "\n"
            + "* @return int\n"
            + "\n"
            + "*/\n"
            + "\n"
            + "public static int getActualLength(String str, String charsetName) {\n"
            + "\n"
            + "if (StringUtil.isEmptyOrNull(str)) {\n"
            + "\n"
            + "return 0;\n"
            + "\n"
            + "}\n"
            + "\n"
            + "if (StringUtil.isEmptyOrNull(charsetName)) {\n"
            + "\n"
            + "charsetName = \"UTF-8\";\n"
            + "\n"
            + "}\n"
            + "\n"
            + "return str.getBytes(Charset.forName(charsetName)).length;\n"
            + "\n"
            + "}\n"
            + "\n"
            + "发表于: 2019-08-28\n"
            + "原文链接：https://kuaibao.qq.com/s/20190826A0MG2E00?refer=cp_1026\n"
            + "腾讯「腾讯云开发者社区」是腾讯内容开放平台帐号（企鹅号）传播渠道之一，根据《腾讯内容开放平台服务协议》转载发布内容。\n"
            + "如有侵权，请联系 cloudcommunity@tencent.com 删除。\n"
            + "0\n"
            + "分享\n"
            + "\n"
            + "扫描二维码\n"
            + "\n"
            + "\n"
            + "上一篇：黑客提醒：电脑数据丢了，可以这样找回！\n"
            + "下一篇：《魔兽世界》发布紧急通知：怀旧服服务器将于凌晨4：00重启\n"
            + "同媒体快讯\n"
            + "Java编程小技巧：字符串相关操作的实用方法\n"
            + "2022-11-20\n"
            + "社区\n"
            + "专栏文章\n"
            + "阅读清单\n"
            + "互动问答\n"
            + "技术沙龙\n"
            + "技术视频\n"
            + "团队主页\n"
            + "腾讯云TI平台\n"
            + "活动\n"
            + "自媒体分享计划\n"
            + "邀请作者入驻\n"
            + "自荐上首页\n"
            + "技术竞赛\n"
            + "资源\n"
            + "技术周刊\n"
            + "社区标签\n"
            + "开发者手册\n"
            + "开发者实验室\n"
            + "关于\n"
            + "视频介绍\n"
            + "社区规范\n"
            + "免责声明\n"
            + "联系我们\n"
            + "友情链接\n"
            + "腾讯云开发者\n"
            + "\n"
            + "扫码关注腾讯云开发者\n"
            + "\n"
            + "领取腾讯云代金券\n"
            + "\n"
            + "热门产品\n"
            + "域名注册云服务器区块链服务消息队列网络加速云数据库域名解析云存储视频直播\n"
            + "热门推荐\n"
            + "人脸识别腾讯会议企业云CDN 加速视频通话图像分析MySQL 数据库SSL 证书语音识别\n"
            + "更多推荐\n"
            + "数据安全负载均衡短信文字识别云点播商标注册小程序开发网站监控数据迁移\n"
            + "Copyright © 2013 - 2022 Tencent Cloud. All Rights Reserved. 腾讯云 版权所有 京公网安备 11010802017518 粤B2-20090059-1\n"
            + "\n"
            + "扫描二维码";

    int aNull = getActualLength(s, "");
    System.out.println(aNull);
  }

  /**
   * 获取字符串实际的存储长度
   *
   * @param str 字符串
   * @param charsetName 编码格式
   * @return int
   */
  public static int getActualLength(String str, String charsetName) {
    if (StringUtils.isEmpty(str)) {
      return 0;
    }

    if (StringUtils.isEmpty(charsetName)) {
      charsetName = "UTF-8";
    }

    return str.getBytes(Charset.forName(charsetName)).length;
  }
}
