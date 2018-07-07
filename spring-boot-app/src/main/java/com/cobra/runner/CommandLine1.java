package com.cobra.runner;


import com.cobra.constants.BackConfigParam;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Order(value = 1)
@Component
public class CommandLine1 implements CommandLineRunner{

    @Override
    public void run(String... strings) throws Exception {
        Map<String,Object> map = new HashMap<>();
        map.put("name","jack");
        map.put("gender","female");
        map.put("age",18);
        BackConfigParam.backMap = map;
        System.out.println("------------->先加载");
    }
}
