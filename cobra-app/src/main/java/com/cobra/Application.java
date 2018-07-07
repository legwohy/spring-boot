package com.cobra;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * 启动SpringBoot 默认扫 @SpringBootApplication下的所有文件
 */
@SpringBootApplication //必须标注该注解才能扫描程序里面的注解组件
@EnableCaching // 支持缓存
public class Application {

    public static void main(String[] args){
        SpringApplication.run(Application.class,args);
    }
}
