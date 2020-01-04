package com.cobra.sprboot.controller;

import com.cobra.sprboot.strategy.QueryContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class HelloController
{
    // 策略模式
    @Autowired private QueryContext queryContext;
    @RequestMapping("/hello/{type}")
    public void hello(HttpServletRequest request, HttpServletResponse response,@PathVariable String type) throws IOException
    {
        queryContext.query(type);
        response.getWriter().print("hello"+",");
    }
}
