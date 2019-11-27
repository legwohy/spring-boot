package com.cobra;


import com.cobra.util.SpringUtils;
import com.cobra.pojo.User1;
import com.cobra.pojo.User2;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * springBoot 启动类
 */
@SpringBootApplication
@MapperScan("com.cobra.dao")
@Slf4j
public class WechatApplication
{
    public static void main(String[] args)
    {
        // 注入 上下文
        SpringUtils springUtils = new SpringUtils();
        springUtils.setApplicationContext(SpringApplication.run(WechatApplication.class,args));

        // 获取bean
        User1 u1 = SpringUtils.getBean(User1.class);
        User2 u2 = SpringUtils.getBean(User2.class);


        log.info("---------Application startup--------------------------"+u1.toString()+","+u2.toString());
    }
}
