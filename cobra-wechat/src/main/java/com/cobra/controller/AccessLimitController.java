package com.cobra.controller;

import com.cobra.pojo.RequestLimitDO;
import com.cobra.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 限流 redis
 */
@RestController
@Slf4j
public class AccessLimitController
{
    @Autowired
    private RedisUtil redisUtil;
    @RequestMapping("/apiLimit")
    public Map<String, Object> apiLimit(){
        HashMap<String,Object> map = new HashMap<>();
        map.put("success",true);
        map.put("message","成功");

        return map;
    }

    @PostConstruct
    public void init(){
        String key = redisUtil.API_LIMIT_VALUE_PREFIX+"apiLimit";
        // 1秒内三次
        RequestLimitDO requestLimit = new RequestLimitDO();
        requestLimit.setLimit(3);
        requestLimit.setTime(1);
        requestLimit.setUnit(TimeUnit.SECONDS);
        redisUtil.set(key,requestLimit,redisUtil.API_LIMIT_EXPIRE);
        log.info("初始化时设置接口 限流缓存 key:{},value:{}",key,requestLimit);
    }

}
