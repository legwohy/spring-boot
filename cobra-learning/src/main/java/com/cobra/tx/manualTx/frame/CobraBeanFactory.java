package com.cobra.tx.manualTx.frame;



/**
 * 实例工厂
 */
public class CobraBeanFactory {
    public Object getBean(String name) throws Exception {
        //得到目标类的Class对象
        Class<?> clazz = Class.forName(name);
        //得到目标对象
        Object bean = clazz.newInstance();
        //得到目标类上的@MyTransactional注解

        MyTransactional myTransactional = clazz.getAnnotation(MyTransactional.class);
        //如果打了@MyTransactional注解，返回代理对象，否则返回目标对象
        if (null != myTransactional) {
            // JDK代理
            //CobraProxyFactoryBeanForJDK proxyFactoryBean = new CobraProxyFactoryBeanForJDK();
            CobraProxyFactoryBeanForCGL proxyFactoryBean = new CobraProxyFactoryBeanForCGL();
            CobraTransactionManager txManager = new CobraTransactionManager();
            txManager.setConnectionUtils(new ConnectionUtils());
            //装配通知和目标对象
            proxyFactoryBean.setTxManager(txManager);
            proxyFactoryBean.setTarget(bean);
            Object proxyBean = proxyFactoryBean.getProxy();
            //返回代理对象
            return proxyBean;
        }


        //返回目标对象
        return bean;
    }
}
