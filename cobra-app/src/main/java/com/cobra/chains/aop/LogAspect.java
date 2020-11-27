package com.cobra.chains.aop;

import com.cobra.constants.Constant;
import com.cobra.util.Serial;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.*;

@Slf4j
@Aspect
@Component
public class LogAspect {

    @Pointcut("execution(public * com.cobra.controller.*.*(..))")
    public void logForController(){}

    @Before("logForController()")
    public void before(JoinPoint join){
        //获取方法名
        String mathName=join.getSignature().getName();
        //获取参数列表
        List<Object> args = Arrays.asList(join.getArgs());

        log.info("前置通知---->before  methodName=[{}]，args=[{}]",mathName,args);
    }

    /**
     * 请求前后添加追踪日志
     * @param jp
     * @return
     * @throws Throwable
     */
    @Around("logForController()")
    public Object around(ProceedingJoinPoint jp)throws Throwable{
        log.info("环绕通知---->  methodName=[{}]，args=[{}]",jp.getSignature().getName(),jp.getArgs());
        MDC.put(Constant.LOG_TRACE_ID, Serial.genSerialNo_32(null));
        Object result = jp.proceed();
        // TODO 无论使用哪种方法添加追踪日志 必须在方法执行完毕清除
        MDC.clear();
        return result;

    }
}
