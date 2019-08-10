package com.cobra.design.structor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * 适配器模式
 *  1) 适配对象和被适配对象之间没有层级关系
 *  2) 将一个接口转化为客户端的一个接口
 *  3）java AWT IO SpringMVC(WebMvcConfigurerAdapter 适配器 拦截器映射)
 */
@SpringBootApplication
public class AdaptorDemo extends WebMvcConfigurerAdapter
{
    /**
     * 字节流(二进制) --> 字符流(文本)
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception
    {
        InputStream is = new FileInputStream("");
        // InputStreamReader是一个适配器关系 且 reader与is没有层级关系
        Reader reader = new InputStreamReader(is,"UTF-8");// 将is转化为reader

        SpringApplication.run(AdaptorDemo.class,args);
    }
}
