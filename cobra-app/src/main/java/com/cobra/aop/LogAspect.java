package com.cobra.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Aspect
@Component
public class LogAspect {

    @Pointcut("execution(public * com.cobra.sprboot.controller.*.*(..))")
    public void logForController(){}

    @Before("logForController()")
    public void before(JoinPoint join){
        //获取方法名
        String mathName=join.getSignature().getName();
        //获取参数列表
        List<Object> args = Arrays.asList(join.getArgs());

        log.info("前置通知---->before   方法名是:"+mathName+"\t参数列表是:"+args);
    }
}
