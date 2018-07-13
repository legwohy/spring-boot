package com.cobra;


import com.cobra.service.CountNumber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.TimeUnit;

/**
 * tip：@SpringBootApplication = @ComponentScan+@Configuration+@EnableAutoConfiguration
 */
@Slf4j

@ComponentScan
@Configuration
@EnableAutoConfiguration
@EnableAsync
public class Application
{
    public static void main(String[] args) throws InterruptedException
    {
       // SpringApplication.run(Application.class,args);
        ConfigurableApplicationContext ctx = SpringApplication.run(Application.class,args);
        ctx.getBean(CountNumber.class).printNumber();
        for (int i = 15;i>10;i--){
            TimeUnit.MICROSECONDS.sleep(1);
            log.info("--------->main:\t"+Thread.currentThread().getName()+"\t"+i);
        }
        ctx.close();
    }
}
