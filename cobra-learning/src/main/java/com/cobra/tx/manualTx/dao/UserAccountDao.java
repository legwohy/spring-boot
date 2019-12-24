package com.cobra.tx.manualTx.dao;


import com.cobra.tx.manualTx.frame.SingleConnectionHolder;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 每一个sql从数据源中取得连接
 */
public class UserAccountDao
{
    private DataSource dataSource;
    public UserAccountDao(DataSource dataSource){
        this.dataSource = dataSource;
    }
    public void order()throws SQLException{
        Connection conn = SingleConnectionHolder.getConnection(dataSource);

        // 业务操作
        System.out.println("当前【用户】线程:"+Thread.currentThread().getName()+",连接通道 hashcode="+conn.hashCode());
    }
}
