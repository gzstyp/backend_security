package com.fwtai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 Api启动器,模块化开发,本前端模块api打包步骤:
 １.先注释掉web模块里的pom.xml里的build节点;
 ２.双击父模块ace_admin里maven快捷键的install,不推荐双击package打包,因为可能打包后jar会很大;
 ３.另外,web 、api等模块需要在单独的模块里打包jar才不被叠加jar文件才小;
 ４.只打包web或api即可实现前后端分离;
 ５.如果不想前后端分离则打包模块frontend,但务必要注释模块web模块里的pom.xml里的build节点;
 ６.每次修改代码都要父模块install一下
*/
@EnableTransactionManagement
@SpringBootApplication
@EnableAsync
public class LaunchApi{

    public static void main(String[] args){
        SpringApplication.run(LaunchApi.class,args);
    }
}