package com.iglens.生成代码器.jpa生成器;


public class TestMain {
    public static void main(String[] args) {
        CoreConfig coreConfig = new CoreConfig();
        //指定java相对项目下那个地方。我的是idea它的路径是这样
        coreConfig.setJavaShortPath("src//main//java");
        //制定实体类包路径
        coreConfig.setDomainPage("com.example.demo.domain");
        //是否生成controller层默认生成service,dao
        coreConfig.setOpenController(true);
//        coreConfig.setDaoPage("com.example.demo.test.dao");
//        coreConfig.setControllerPage("com.example.demo.test.web");
        //如果你的service,dao,controller的父包相同只需指定父包
        coreConfig.setParentPacke("com.example.demo.test");
        //制定baseDao那些类放的包路径
        coreConfig.setCorePage("com.example.demo.core");
        FacedeBuilder faceBuidler = new FacedeBuilder(coreConfig);
        faceBuidler.create();
    }
}