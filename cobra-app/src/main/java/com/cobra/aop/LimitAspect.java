package com.cobra.aop;

import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.Semaphore;

/**
 *
 * 接口限流拦截
 *
 * 测试之前 记得先注销日志拦截器，以免干扰数据判断
 */
@Slf4j
@Aspect
@Component
public class LimitAspect {
    /**
     * 每秒产生三个令牌
     * 刚开始的时候由于产生的令牌比较多，大部分请求会成功，随着请求大量过来，最后只能保证三个链接可用
     *
     * SmoothBursty 模式
     */
    private RateLimiter limiter = RateLimiter.create(3);
    /**
     * 定义切点
     * 只拦截 ApiLimitController中的方法
     */
    @Pointcut("execution(public * com.cobra.controller.ApiLimitController.*(..))")
    public void apiLimitController(){}


    @Around("apiLimitController()")
    public Object around(ProceedingJoinPoint jp)throws Throwable{
        boolean flag = limiter.tryAcquire();
        Object obj = null;
        if(flag){
            obj = jp.proceed();
        }else {
            // TODO 超过的链接可用放在队列里面
            HashMap<String,Object> map = new HashMap<>();
            map.put("success",false);
            map.put("message","失败");
            obj = map;
        }
        System.out.println(new Date()+"\t flag:"+flag+"\t obj:"+obj);

        return obj;

    }

    /**
     * Guava
     * 令牌桶
     * 控制速率
     */
    private void rateLimiterTest(){
        // 每秒生产2个令牌
        RateLimiter permit = RateLimiter.create(3);
        for (int i = 0;i<10;i++){
            new Thread(()->{
                // 阻塞方式获取令牌 一秒最多使用两个
                permit.acquire(2);
                System.out.println(new Date());
                try {Thread.sleep(1000);}catch (InterruptedException e) {e.printStackTrace();}

            }).start();
        }
    }

    /**
     * jdk自带
     * Semaphore 控制并发量
     */
    private void semaphoreTest(){
        // 同一时刻只能三个并发
        Semaphore permit = new Semaphore(3);
        for (int i = 0;i<10;i++){
            new Thread(()->{
                try {permit.acquire();}catch (InterruptedException e) {e.printStackTrace();}
                System.out.println(new Date());
                try {Thread.sleep(1000);}catch (InterruptedException e) {e.printStackTrace();}
                // 必须释放
                permit.release();
            }).start();
        }
    }

    public static void main(String[] args) throws Exception{
        LimitAspect controller = new LimitAspect();
        controller.semaphoreTest();
        Thread.sleep(2000);
        System.out.println("main end");
    }
}
