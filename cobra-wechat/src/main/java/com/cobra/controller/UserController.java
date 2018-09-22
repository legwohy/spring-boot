package com.cobra.controller;

import com.cobra.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class UserController {
    @Autowired private UserInfoService userInfoService;

    @RequestMapping("/login")
    public String login(HttpServletRequest request, HttpServletResponse response)
    {

        String userPhone = request.getParameter("userPhone");
        String password = request.getParameter("password");
        boolean flag = userInfoService.login(userPhone,password);
        if(flag){
            // 跳转页面
        }

        return "tg/index1";




    }

}
