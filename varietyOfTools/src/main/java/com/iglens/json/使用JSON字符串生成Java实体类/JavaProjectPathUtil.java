package com.iglens.json.使用JSON字符串生成Java实体类;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * 获取Java项目路径
 *
 * @author xunjian.xiao
 * @date 2022/11/11 14:57
 */

public class JavaProjectPathUtil {
    /**
     *  获取当前类的所在工程真实路径
     * @param clazz
     * @return {@link String}
     * @author xunjian.xiao
     * @date 2022/11/14 9:35
     */
    public static String getPath(Class clazz){
        return clazz.getResource("").getPath().replace("target/classes","src/main/java")+File.separator+clazz.getSimpleName()+".java";
    }

    /**
     * 获取类加载的根路径
     * @param
     * @return {@link String}
     * @author xunjian.xiao
     * @date 2022/11/11 14:58
     */
    public String getProjectPathUrl(){
        // 第一种：获取类加载的根路径   D:\git\daotie\daotie\target\classes
        File f = new File(this.getClass().getResource("/").getPath());
        System.out.println("获取类加载的根路径:"+this.getClass().getResource("/").getPath());
        System.out.println(f);

        // 获取当前类的所在工程路径; 如果不加“/”  获取当前类的加载目录  D:\git\daotie\daotie\target\classes\my
        File f2 = new File(this.getClass().getResource("").getPath());
        System.out.println("获取当前类的加载目录:"+this.getClass().getResource("").getPath());
        System.out.println(f2);

        // 第二种：获取项目路径    D:\git\daotie\daotie
        File directory = new File("");// 参数为空
        String courseFile = null;
        try {
            courseFile = directory.getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(courseFile);


        // 第三种：  file:/D:/git/daotie/daotie/target/classes/
        URL xmlpath = this.getClass().getClassLoader().getResource("");
        System.out.println(xmlpath);


        // 第四种： D:\git\daotie\daotie
        System.out.println(System.getProperty("user.dir"));
        /*
         * 结果： C:\Documents and Settings\Administrator\workspace\projectName
         * 获取当前工程路径
         */
        // 第五种：  获取所有的类路径 包括jar包的路径
        System.out.println(System.getProperty("java.class.path"));

        return null;
    }
}

