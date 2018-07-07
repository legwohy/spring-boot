package com.cobra.runner;

import com.cobra.constants.BackConfigParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(value = 2)
@Component
public class CommandLine2 implements CommandLineRunner,ApplicationRunner{
    //@Autowired private IitialzingService service;
    @Override
    public void run(String... strings) throws Exception {
        //service.afterPropertiesSet();
        System.out.println("------------>后加载");
    }

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        BackConfigParam.backMap.toString();
        System.out.println("--------->后"+BackConfigParam.backMap.toString());
    }
}
