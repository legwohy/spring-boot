package com.cobra.tx.manualTx.frame;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Bean 工厂代理类 主要代理事务
 * cglib 代码生成器 代理类继承被代理的类
 * cglib 是继承 所以 final类是不能继承的
 */
public class CobraProxyFactoryBeanForCGL {
    //通知
    private CobraTransactionManager txManager;
    //目标对象
    private Object target;

    public void setTxManager(CobraTransactionManager txManager) {
        this.txManager = txManager;
    }

    public void setTarget(Object target) {
        this.target = target;
    }



    /**
     * JDK动态代理
     * 传入目标对象target，为它装配好通知，返回代理对象
     * @return
     */
    public Object getProxy() {
       // 代理类
        Enhancer enhancer = new Enhancer();
        // 目标类 cglib是继承被代理的类 因此 这里必须是采用父类
        enhancer.setSuperclass(target.getClass());
        enhancer.setCallback(new MethodInterceptor() {
           @Override
           public Object intercept(Object target, Method method, Object[] objects, MethodProxy proxy) throws Throwable {
               try {
                   //1.开启事务
                   txManager.beginTransaction();
                   // 父类的方法
                   Object retVal = proxy.invokeSuper(target,objects);
                   //3.提交事务
                   txManager.commit();
                   return retVal;
               }catch (Exception e){
                   txManager.rollback();
                   throw new RuntimeException("混滚");
               }finally {
                   // 关闭连接
                   txManager.release();
               }

           }
       });
        Object bean = enhancer.create();
        return bean;
    }

}
