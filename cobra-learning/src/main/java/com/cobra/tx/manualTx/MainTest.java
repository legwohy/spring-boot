package com.cobra.tx.manualTx;

import com.alibaba.druid.pool.DruidDataSource;
import com.cobra.tx.manualTx.frame.ConnectionUtils;
import com.cobra.tx.manualTx.service.UserService;
import com.cobra.tx.manualTx.service.impl.UserServiceImpl;
import com.cobra.tx.manualTx.service.impl.UserServiceManuImpl;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.*;

/**
 *
 * <a link='https://www.jianshu.com/p/1becdc376f5d'/>
 * 同一个事务里面保证同一个连接
 */
public class MainTest
{


    public static void main(String[] args)
    {
        ConnectionUtils connectionUtil = new ConnectionUtils();
        final UserService userService = new UserServiceManuImpl(connectionUtil);

        ExecutorService executor = Executors.newCachedThreadPool();
        for (int i = 0;i<3;i++){
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    userService.action();
                }
            });
        }


    }
}
