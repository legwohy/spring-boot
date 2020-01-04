package com.cobra.tx.manualTx.frame;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 一个线程中的一个事务的多个操作，使用的是同一个Connection
 */
public class CobraTransactionManager
{
    private DataSource dataSource;
    public CobraTransactionManager(DataSource dataSource){
        this.dataSource = dataSource;
    }
    private Connection getConnection()throws SQLException{
        return SingleConnectionHolder.getConnection(dataSource);
    }

    // 开启 一个事务必须是同一个连接
    public void start()throws SQLException{
       Connection conn =  getConnection();
       conn.setAutoCommit(false);// 手动提交
    }
    // 回滚
    public void rollBack(){
        Connection conn = null;
        try
        {
            conn = getConnection();
            conn.rollback();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

    }
    // 关闭
    public void close()throws SQLException{
        Connection conn =  getConnection();
        conn.setAutoCommit(false);// 手动提交
        conn.close();
    }

    public DataSource getDataSource(){
        return dataSource;
    }
}
