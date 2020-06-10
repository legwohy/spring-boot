package com.cobra.tx.manualTx.dao;

import com.cobra.tx.manualTx.frame.ConnectionUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

/**
 * 每一个sql都是一个连接
 */
public class UserOderDao
{
    private ConnectionUtils connectionUtils;
    public UserOderDao(ConnectionUtils connectionUtils){
        this.connectionUtils = connectionUtils;
    }
    public void buy()throws SQLException{
        Connection conn = connectionUtils.getThreadConnection();
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 业务操作
        System.out.println("当前【订单】线程:"+Thread.currentThread().getName()+",连接通道 hashcode="+conn.hashCode());
    }
}
