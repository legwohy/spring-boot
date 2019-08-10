package com.cobra.sprboot;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * spring bean的回调处理器
 *
 * spring IOC提供的扩展接口
 * 调用顺序
 -->  Spring IOC容器实例化Bean
 -->  调用BeanPostProcessor的postProcessBeforeInitialization方法
 -->  调用bean实例的初始化方法（如afterPropertiesSet和任意已声明的init方法）

 -->  调用BeanPostProcessor的postProcessAfterInitialization方法

 * 注意：不能标记延迟初始化 否则spring将不会注册
 * */
public class AppleAwarePostProcessor implements BeanPostProcessor
{
    private Apple apple;
    public AppleAwarePostProcessor(Apple apple){this.apple = apple;}

    /**
     * bean实例初始化方法调用之前回调本方法
     *  @see org.springframework.beans.factory.config.BeanPostProcessor
     * @throws BeansException
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException
    {
        System.out.println("bean=["+beanName+"]开始实例化=============");
        if(bean instanceof AppleAware){
            ((AppleAware)bean).setApple(apple);
        }

        return bean;  // 不能返回null
    }

    /**
     * bean实例初始化方法调用完成后回调本方法
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException
    {
        System.out.println("bean=["+beanName+"]完成实例化=============");
        return bean;  // 不能返回null
    }
}
