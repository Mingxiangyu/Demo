package com.iglens.生成代码器.jpa生成器;

import java.io.File;
import java.util.List;
 
public class ControllerBuilder {
 
    private CoreConfig coreConfig;
    private String controllerPage;
 
    public ControllerBuilder(CoreConfig config){
        coreConfig = config;
        if(coreConfig.getControllerPage()!=null){
            controllerPage = coreConfig.getControllerPage();
        }else{
            controllerPage = coreConfig.getParentPacke()+".web";
        }
    }
 
    public void create(){
        List<String> classNames = coreConfig.lisAllClassName();
        if(classNames!=null){
            for(String className:classNames){
                String controllerPath =  coreConfig.getControllerRealPath();
                String controllerName = className+"Controller";
                File path = new File(controllerPath);
                if(!path.exists()) {
                    path.mkdirs();
                }
                File file = new File(controllerPath+"//"+controllerName+".java");
              if (file.exists()) {
                continue;
              }
                StringBuilder sb = new StringBuilder("package "+controllerPage+";").append(PathConfig.newLine())//
                        .append("import "+coreConfig.getDomainPage()+"."+className+";").append(PathConfig.newLine())//
                        .append("import org.springframework.web.bind.annotation.RequestMapping;").append(PathConfig.newLine())//
                        .append("import org.springframework.web.bind.annotation.ResponseBody;").append(PathConfig.newLine())//
                        .append("import org.springframework.stereotype.Controller;").append(PathConfig.newLine())//
                        .append("@Controller").append(PathConfig.newLine())//
                        .append("@RequestMapping(\""+className+"\")").append(PathConfig.newLine())//
                        .append("public class "+controllerName+"{").append(PathConfig.newLine())//
                        .append(PathConfig.newLine())//
                        .append("\t@RequestMapping(\"test\")").append(PathConfig.newLine())//
                        .append("\t@ResponseBody").append(PathConfig.newLine())//
                        .append("\tpublic Object test(){").append(PathConfig.newLine())//
                        .append("\t\treturn \"success\";").append(PathConfig.newLine())//
                        .append("\t}").append(PathConfig.newLine())//
                        .append("}");// ;
                PathConfig.copyToFile(file,sb.toString());
 
                System.out.println(">>>>>>>>>>>>>>>>>>>>>>生成 "+controllerName+" 完成");
            }
        }
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>web层代码生成完成>>>>>>>>>>>>>>>>>>>");
    }
}