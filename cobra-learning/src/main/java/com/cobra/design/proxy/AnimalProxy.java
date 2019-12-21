package com.cobra.design.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

/**
 * JDK动态代理 代理接口
 * 代理类
 */
public class AnimalProxy implements InvocationHandler
{
    private Object target;// 代理类

    public AnimalProxy(Object target)
    {
        this.target = target;
    }

    /**
     * @return getInstance 接口
     */
    public  Object getInstance()
    {
        return Proxy.newProxyInstance(
                        this.target.getClass().getClassLoader(),// 加载代理类
                        this.target.getClass().getInterfaces(),// 代理类实现的所有接口驻足
                        this);

    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
    {
        if(proxy instanceof Animal){
            System.out.println("代理类型为 Animal");
        }else {
            System.out.println("sb=====");
        }
        System.out.println("代理方法名字："+method.getName()+",代理传入的参数值args="+ Arrays.toString(args));
        Object rs = method.invoke(target,args);

        System.out.println("方法执行完成");

        return rs;
    }
}
