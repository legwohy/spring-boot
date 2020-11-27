package com.cobra.spring.myMVC.controller;

import com.cobra.spring.myMVC.annotation.XController;
import com.cobra.spring.myMVC.annotation.XQualifier;
import com.cobra.spring.myMVC.annotation.XRequestMapping;
import com.cobra.spring.myMVC.service.XUserService;

import java.util.HashMap;
import java.util.Map;

@XController
@XRequestMapping("/http/xUser")
public class XUserController {
    @XQualifier("xUserService")
    private XUserService userService;

    @XRequestMapping("/query")
    public Map<String,Object> query(){
        System.out.println("============>>>");
        Map<String,Object> map = new HashMap<>();
        userService.query();
        map.put("code","202");
        map.put("message","响应成功");

        return map;
    }
}
