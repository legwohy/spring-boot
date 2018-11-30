package com.cobra.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/cobra")
public class UserInfoController
{
    @RequestMapping("/login")
    public String login(HttpServletRequest request,HttpServletResponse response)
    {
        return "wechat/login";

    }
}
