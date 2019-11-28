package com.cobra;

import com.cobra.constants.BackConfigParam;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;

import java.util.HashMap;
import java.util.Map;

/**
 * 启动SpringBoot 默认扫 @SpringBootApplication下的所有文件
 */
@SpringBootApplication //必须标注该注解才能扫描程序里面的注解组件
@EnableCaching // 支持缓存
@ServletComponentScan // 支持@WebFilter注解 代替写注册bean
public class AppApplication implements CommandLineRunner{

    public static void main(String[] args){
        SpringApplication.run(AppApplication.class,args);
    }

    /**
     * 加载静态文件
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
        Map<String,Object> map = new HashMap<>();
        map.put("name","jack");
        map.put("gender","female");
        map.put("age",18);
        BackConfigParam.backMap = map;
        System.out.println("------------->先加载");
    }
}
