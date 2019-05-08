package com.cobra.trans;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionStatus;

/**
 * Created by legwo on 2019/5/8.
 */
public class TestTrans
{
    @Autowired private PlatformTransactionManager txManager;

    /**
     * 编程式事务比注解式事务更加细粒 两者不能混用 否则抛出异常
     */
    public void testService(){

        // 定义事务
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setReadOnly(true);
        // 启用事务
        TransactionStatus status = txManager.getTransaction(def);

        try
        {
            //TODO 业务代码
            txManager.commit(status); // 事务提交
        }catch (Exception e)
        {
            txManager.rollback(status);// 回滚

        }

    }
}

