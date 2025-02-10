package com.iglens.动态定时任务;

import org.springframework.stereotype.Component;
 
@Component
public class c1 {
    public void test1(String y){
        System.out.println("这个是test1的bean : " + y);
    }
 
    public void test2(){
        System.out.println("这个是test1的bean中test2方法");
    }
}