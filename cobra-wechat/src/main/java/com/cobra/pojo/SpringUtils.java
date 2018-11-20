package com.cobra.pojo;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


/**
 * 获取springContext
 * @param <T>
 */
@Slf4j
public class SpringUtils<T> implements ApplicationContextAware
{
    private static  ApplicationContext ctx;
    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException
    {
        log.info("===================上下文初始化开始============================");
        if(SpringUtils.ctx == null)
        {
            SpringUtils.ctx = applicationContext;

        }
        log.info("===========上下文 ctx id="+ctx.getId());
        log.info("===========上下文 applicationName="+ctx.getApplicationName());
        log.info("===========上下文 displayName="+ctx.getDisplayName());
        log.info("===========上下文 environment="+ctx.getEnvironment());
        log.info("===========上下文 environment="+ctx.getStartupDate());

        log.info("===============上下文初始化完成======================");

    }

    public static ApplicationContext getApplication(){
        return ctx;
    }

    public static Object getBean(String beanName)
    {
        return ctx.getBean(beanName);

    }

    public static<T> T getBean(Class<T> clazz)
    {
        return ctx.getBean(clazz);

    }

}
