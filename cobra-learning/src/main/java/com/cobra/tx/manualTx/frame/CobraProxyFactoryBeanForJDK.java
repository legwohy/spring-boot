package com.cobra.tx.manualTx.frame;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Bean 工厂代理类 主要代理事务
 * JDK动态代理 代理类
 */
public class CobraProxyFactoryBeanForJDK {
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
        Object proxy = Proxy.newProxyInstance(
                target.getClass().getClassLoader(),/*1.类加载器*/
                target.getClass().getInterfaces(), /*2.目标对象实现的接口*/
                new InvocationHandler() {/*3.InvocationHandler*/
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        try {
                            //1.开启事务
                            txManager.beginTransaction();
                            //2.执行操作
                            Object retVal = method.invoke(target, args);
                            //3.提交事务
                            txManager.commit();
                            //4.返回结果
                            return retVal;
                        } catch (Exception e) {
                            //5.回滚事务
                            txManager.rollback();
                            throw new RuntimeException(e);
                        } finally {
                            //6.释放连接
                            txManager.release();
                        }

                    }
                }
        );
        return proxy;
    }

}
