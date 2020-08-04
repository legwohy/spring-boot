package com.cobra.tx.springTx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * https://www.jianshu.com/p/3665e5802cd8?from=groupmessage
 * 自定义事务 DefaultTransactionDefinition 这里是每次均建立一起连接
 * 如果 commit or rollback 未执行或执行失败，将会导致该事务持有的数据库连接无法正常归还到连接池中
 *
 * 由于存在问题 1、建议代码模拟多线程测试看是否打挂
 * 2、还是建议spring自身的事务
 */
public class TestTrans
{
    @Autowired private PlatformTransactionManager txManager;

    /**
     * 编程式事务比注解式事务更加细粒 两者不能混用 否则抛出异常
     *
     */
    public void testService(){

        // 定义事务
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setReadOnly(true);
        // 启用事务
        TransactionStatus status = txManager.getTransaction(def);

        try
        {
            // 连接池被打挂的场景
            // 1、若抛出 error 异常 exception抓不到
            // 2、业务未执行到commit就return
            // 3、事务开关循环
            // 开启事务1（requires_new）-> 然后开事务2(requires_new) -> 之后提交事务1（commit） -> 在提交事务2(commit)
            txManager.commit(status); // 事务提交
        }catch (Throwable e)
        {
            // 建议抓的范围扩大一些 Exception->Throwable
            txManager.rollback(status);// 回滚

        }

    }
}

