package com.cobra.design.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

/**
 * JDK动态代理 代理接口
 * 代理类
 */
public class CommonJdkProxy implements InvocationHandler
{
    private Object target;// 代理类

    public CommonJdkProxy(Object target)
    {
        this.target = target;
    }

    /**
     * @return 创建代理对象
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
        System.out.println("=============JDK代理执行开始============");
        System.out.println("JDK代理 className:"+proxy.getClass().getName()
                        + ",methodName:["+method.getName()+"] "
                        + ",args：["+ Arrays.toString(args)+"]");
        Object rs = method.invoke(target,args);

        System.out.println("=============JDK代理执行结束============");

        return rs;
    }
}
