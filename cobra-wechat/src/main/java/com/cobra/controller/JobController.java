package com.cobra.controller;


import com.cobra.task.JobService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//@Controller
@Slf4j
public class JobController
{
    @Autowired private JobService jobService;

    @PostConstruct
    public void init(){
       jobService.startAll();
        log.info("初始化启动所有=========================");
    }



    @RequestMapping("/refresh/{id}")
    @ResponseBody
    public String refresh(HttpServletRequest request,HttpServletResponse response,@PathVariable Integer id)
    {

        return jobService.refresh(id);
    }

}
