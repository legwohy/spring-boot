package com.cobra.controller;

import com.cobra.pojo.User1;
import com.cobra.util.cache.CacheLoad;
import com.cobra.util.cache.CacheTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("/cache")
public class CacheController {
    @Autowired private CacheTemplateService cacheTemplateService;

    @RequestMapping("/query")
    public List<User1> query(){
        String key = "cache";
        List<User1> result = cacheTemplateService.query(key, 10, new CacheLoad< List<User1>>() {
            @Override
            public List<User1> load() {
                List<User1> list = new ArrayList<User1>();
                User1 j1 = new User1(1001,"J",new Date());
                User1 j2 = new User1(1002,"Q",new Date());
                User1 j3 = new User1(1003,"K",new Date());
                list.add(j1);
                list.add(j2);
                list.add(j3);

                return list;
            }
        });

        return result;
    }
}
