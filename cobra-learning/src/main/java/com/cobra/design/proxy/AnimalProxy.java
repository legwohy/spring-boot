package com.cobra.design.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
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
        System.out.println("调入前========");
        Object rs = method.invoke(target,args);

        System.out.println("调入==========后");

        return rs;
    }
}
