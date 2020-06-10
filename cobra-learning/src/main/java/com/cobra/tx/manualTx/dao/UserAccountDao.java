package com.cobra.tx.manualTx.dao;


import com.cobra.tx.manualTx.frame.ConnectionUtils;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 每一个sql从数据源中取得连接
 */
public class UserAccountDao
{
    private ConnectionUtils connectionUtils;
    public UserAccountDao(ConnectionUtils connectionUtils){
        this.connectionUtils = connectionUtils;
    }
    public void order()throws SQLException{
        Connection conn = connectionUtils.getThreadConnection();

        // 业务操作
        System.out.println("当前【用户】线程:"+Thread.currentThread().getName()+",连接通道 hashcode="+conn.hashCode());
    }
}
