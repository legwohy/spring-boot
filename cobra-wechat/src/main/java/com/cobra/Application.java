package com.cobra;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * springBoot 启动类
 */
@SpringBootApplication
@MapperScan("com.cobra.dao")
public class Application
{
    public static void main(String[] args)
    {
        SpringApplication.run(Application.class,args);
    }
}
