package com.cobra.myMVC.controller;

import com.cobra.myMVC.annotation.XController;
import com.cobra.myMVC.annotation.XQualifier;
import com.cobra.myMVC.annotation.XRequestMapping;
import com.cobra.myMVC.annotation.XService;
import com.cobra.myMVC.service.XUserService;

import java.util.HashMap;
import java.util.Map;

@XController
@XRequestMapping("/rest/xUser")
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
