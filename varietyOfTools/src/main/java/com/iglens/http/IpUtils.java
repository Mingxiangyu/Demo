package com.iglens.http;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class IpUtils {
    
    /**
     * 获取本机所有网卡信息
     */
    public static List<String> getLocalIPs() {
        List<String> ipList = new ArrayList<>();
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                // 排除回环接口、虚拟接口等
                if (!networkInterface.isLoopback() && networkInterface.isUp()) {
                    Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                    while (inetAddresses.hasMoreElements()) {
                        InetAddress inetAddress = inetAddresses.nextElement();
                        if (inetAddress instanceof Inet4Address) { // 只获取IPv4地址
                            ipList.add(inetAddress.getHostAddress());
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ipList;
    }

    /**
     * 获取本机首选IP地址
     */
    public static String getLocalPreferredIP() {
        try {
            // 获取本地主机名
            String hostName = InetAddress.getLocalHost().getHostName();
            
            // 获取所有网络接口
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                // 排除回环接口、虚拟接口等
                if (!networkInterface.isLoopback() && networkInterface.isUp()) {
                    for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
                        InetAddress inetAddress = interfaceAddress.getAddress();
                        if (inetAddress instanceof Inet4Address) {
                            return inetAddress.getHostAddress();
                        }
                    }
                }
            }
            
            // 如果上面都没获取到，则使用InetAddress获取本地IP
            return InetAddress.getLocalHost().getHostAddress();
            
        } catch (Exception e) {
            e.printStackTrace();
            return "127.0.0.1";
        }
    }
    
    /**
     * 获取本机外网IP（通过访问外部服务）
     */
    public static String getExternalIP() {
        String externalIP = "";
        try {
            URL url = new URL("http://checkip.amazonaws.com");
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            externalIP = reader.readLine();
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return externalIP;
    }

    /**
     * 检查IP地址是否可用
     */
    public static boolean isIPReachable(String ipAddress, int timeout) {
        try {
            return InetAddress.getByName(ipAddress).isReachable(timeout);
        } catch (Exception e) {
            return false;
        }
    }
    
    public static void main(String[] args) {
        // 测试获取所有IP
        System.out.println("所有本机IP地址：");
        List<String> allIPs = getLocalIPs();
        allIPs.forEach(ip -> System.out.println("IP: " + ip));
        
        // 测试获取首选IP
        System.out.println("\n首选IP地址：");
        String preferredIP = getLocalPreferredIP();
        System.out.println("Preferred IP: " + preferredIP);
        
        // 测试获取外网IP
        System.out.println("\n外网IP地址：");
        String externalIP = getExternalIP();
        System.out.println("External IP: " + externalIP);
        
        // 测试IP可达性
        System.out.println("\nIP可达性测试：");
        String testIP = "8.8.8.8"; // Google的DNS服务器
        boolean isReachable = isIPReachable(testIP, 3000);
        System.out.println(testIP + " is reachable: " + isReachable);
    }
}