package com.cobra.tx.dao;

import com.cobra.tx.frame.SingleConnectionHolder;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 每一个sql都是一个连接
 */
public class UserOderDao
{
    private DataSource dataSource;
    public UserOderDao(DataSource dataSource){
        this.dataSource = dataSource;
    }
    public void buy()throws SQLException{
        Connection conn = SingleConnectionHolder.getConnection(dataSource);

        // 业务操作
        System.out.println("当前【订单】线程:"+Thread.currentThread().getName()+",连接通道 hashcode="+conn.hashCode());
    }
}
