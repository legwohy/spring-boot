package com.cobra.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * api安全
 */
@Api(description = "API限流")
@RestController
public class ApiLimitController {

    @RequestMapping("/apiLimit")
    public Object apiLimit(){
        HashMap<String,Object> map = new HashMap<>();
        map.put("success",true);
        map.put("message","成功");

        return map;
    }



}
