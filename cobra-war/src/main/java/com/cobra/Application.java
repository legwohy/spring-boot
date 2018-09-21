package com.cobra;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



/**
 * SpringBoot的启动类
 * war包是外置容器 可以添加SCI接口组件
 *
 */
@SpringBootApplication
public class Application
{
    public static void main(String[] args)
    {
        SpringApplication.run(Application.class,args);
    }



}
