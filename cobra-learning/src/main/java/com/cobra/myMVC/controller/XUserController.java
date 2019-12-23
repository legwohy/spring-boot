package com.cobra.myMVC.controller;

import com.cobra.myMVC.annotation.XController;
import com.cobra.myMVC.annotation.XQualifier;
import com.cobra.myMVC.annotation.XRequestMapping;
import com.cobra.myMVC.annotation.XService;
import com.cobra.myMVC.service.XUserService;

import java.util.HashMap;
import java.util.Map;

@XController("xUserController")
@XRequestMapping("/xUser")
public class XUserController {
    @XQualifier("userService")
    private XUserService userService;

    @XRequestMapping("/query")
    public Map<String,Object> query(){
        Map<String,Object> map = new HashMap<>();
        userService.query();
        map.put("code","202");
        map.put("message","响应成功");

        return map;
    }
}
