package com.cobra.tx.manualTx;

import com.cobra.tx.manualTx.frame.CobraBeanFactory;
import com.cobra.tx.manualTx.service.UserService;

/**
 * https://zhuanlan.zhihu.com/p/63126398
 * 手动AOP
 * 1、如果目标对象实现了接口，默认情况下会采用JDK的动态代理实现AOP。
 * 2、如果目标对象实现了接口，可以强制使用CGLIB实现AOP。
 * 3、如果目标对象没有实现了接口，必须采用CGLIB库，Spring会自动在JDK动态代理和CGLIB之间转换。
 */
public class AOPTest {
    public static void main(String[] args) throws Exception {
        // 初始化Bean工厂
        CobraBeanFactory beanFactory = new CobraBeanFactory();
        Object bean = beanFactory.getBean("com.cobra.tx.manualTx.service.impl.UserServiceImpl");
        System.out.println("代理对象:"+bean.getClass().getName());
        UserService service = (UserService)bean;
       service.action();
    }
}
