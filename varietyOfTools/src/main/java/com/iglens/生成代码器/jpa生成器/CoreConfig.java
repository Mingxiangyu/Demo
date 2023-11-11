package com.iglens.生成代码器.jpa生成器;

import java.util.LinkedList;
import java.util.List;
import lombok.Data;

/**
 * 核心数据结构
 */
@Data
public class CoreConfig {
    //父路径
    private String parentPacke;
    //service接口包路径
    private String servicePage;
    //dao接口包路径
    private String daoPage;
    //controller接口包路径
    private String controllerPage;
    //实体类包路径
    private String domainPage;
    //核心工具类路径
    private String corePage;
 
    //存放类名称的数据
    private List<String> classNames= new LinkedList<>();
 
    private String javaShortPath;
 
    private boolean openController =false;
    private boolean openService = true;
    private boolean openDao = true;
 
    public void addClassName(String className){
        classNames.add(className);
    }
 
    public List<String> lisAllClassName(){
        return classNames;
    }
 
    public String getDomainRealPath(){
        String path = PathConfig.getProjectPath()+"//"+javaShortPath;
        String domainPath = domainPage.replace(".","//");
        return path+"//"+domainPath;
    }
 
    public String getServiceRealPath(){
        String path = PathConfig.getProjectPath()+"//"+javaShortPath;
        String servicePath = "";
        if(servicePage!=null) {
             servicePath = servicePage.replace(".", "//");
        }else{
            servicePath = parentPacke.replace(".","//")+"//service";
        }
        return path+"//"+servicePath;
    }
 
    public String getDaoRealPath(){
        String path = PathConfig.getProjectPath()+"//"+javaShortPath;
        String daoPath = "";
        if(daoPage!=null) {
             daoPath = daoPage.replace(".", "//");
        }else{
            daoPath = parentPacke.replace(".","//")+"//dao";
        }
        return path+"//"+daoPath;
    }
 
    public String getControllerRealPath(){
        String path = PathConfig.getProjectPath()+"//"+javaShortPath;
        String controllerPath = "";
        if(controllerPage!=null) {
             controllerPath = controllerPage.replace(".", "//");
        }else{
            controllerPath = parentPacke.replace(".","//")+"//web";
        }
        return path+"//"+controllerPath;
    }
}