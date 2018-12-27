package com.cobra.controller;

import com.cobra.design.strategy.QueryContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
//@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
public class HelloController
{
    @Autowired private QueryContext queryContext;
    @RequestMapping("/hello/{type}")
    public void hello(HttpServletRequest request, HttpServletResponse response,@PathVariable String type) throws IOException
    {
        queryContext.query(type);
        response.getWriter().print("hello"+",");
    }
}
