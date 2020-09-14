package com.cobra.design.proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * 引入jar包
 * Cglib动态代理 代理接口
 * 代理类
 */
public class CommonCglibProxy {
    private Object target;// 代理类

    public CommonCglibProxy(Object target)
    {
        this.target = target;
    }

    /**
     * JDK动态代理
     * 传入目标对象target，为它装配好通知，返回代理对象
     * @return 创建代理对象
     */
    public Object getInstance()
    {
        // 代理类
        Enhancer enhancer = new Enhancer();
        // 目标类 cglib是继承被代理的类 因此 这里必须是采用父类
        enhancer.setSuperclass(target.getClass());
        enhancer.setCallback(new MethodInterceptor()
        {
            @Override
            public Object intercept(Object target, Method method, Object[] args, MethodProxy proxy) throws Throwable
            {
                System.out.println("=============CGLIB代理执行开始============");
                System.out.println("CGLIB代理 className:"+proxy.getClass().getName()
                                + ",methodName:["+method.getName()+"] "
                                + ",args：["+ Arrays.toString(args)+"]");

                // 父类的方法
                Object retVal = proxy.invokeSuper(target, args);

                System.out.println("=============CGLIB代理执行结束============");

                return retVal;

            }
        });
        Object bean = enhancer.create();
        return bean;
    }


}
