package com.iglens.生成代码器.jpa生成器;

import java.io.File;
import java.util.List;
 
public class ServiceBuilder {
    private CoreConfig coreConfig;
    private String servicePage;
 
    public ServiceBuilder(CoreConfig config){
        coreConfig = config;
        if(config.getServicePage()!=null){
            servicePage = config.getServicePage();
        }else{
            servicePage = config.getParentPacke()+".service";
        }
    }
 
    public void create(){
        List<String> classNames = coreConfig.lisAllClassName();
        if(classNames!=null){
            for(String className:classNames){
                String servicePath =  coreConfig.getServiceRealPath();
                String serviceName = "I"+className+"Service";
                File path = new File(servicePath);
                File path2 = new File(servicePath+"//impl");
                if(!path.exists()) {
                    path.mkdirs();
                    path2.mkdirs();
                }
                File file = new File(servicePath+"//"+serviceName+".java");
              if (file.exists()) {
                continue;
              }
                StringBuilder sb = new StringBuilder("package "+servicePage+";").append(PathConfig.newLine())//
                        .append("import "+coreConfig.getCorePage()+".*;").append(PathConfig.newLine())//
                        .append("import "+coreConfig.getDomainPage()+"."+className+";").append(PathConfig.newLine())//
                        .append("public interface "+serviceName+" extends IService<"+className+">{").append(PathConfig.newLine())//
                        .append("}");// ;
                PathConfig.copyToFile(file,sb.toString());
                //添加dao中内容
                String serviceImplPath = servicePath+"//impl";
                String serviceImplName = className+"ServiceImpl";
                file = new File(serviceImplPath+"//"+serviceImplName+".java");
                sb = new StringBuilder("package "+servicePage+".impl;").append(PathConfig.newLine())//
                        .append("import "+coreConfig.getCorePage()+".*;").append(PathConfig.newLine())//
                        .append("import "+coreConfig.getDomainPage()+"."+className+";").append(PathConfig.newLine())//
                        .append("import "+coreConfig.getDaoPage()+"."+className+"Dao;").append(PathConfig.newLine())//
                        .append("import org.springframework.stereotype.Service;").append(PathConfig.newLine())//
                        .append("import "+servicePage+"."+serviceName+";").append(PathConfig.newLine())//
                        .append("@Service").append(PathConfig.newLine())//
                        .append("public class "+serviceImplName+" extends ServiceImpl<"+className+"Dao,"+className+"> implements "+serviceName+"{").append(PathConfig.newLine())//
                        .append("}");
                PathConfig.copyToFile(file,sb.toString());
                System.out.println(">>>>>>>>>>>>>>>>>>>>>>生成 "+serviceImplName+" 完成");
            }
        }
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>service层代码生成完成>>>>>>>>>>>>>>>>>>>");
    }
 
}