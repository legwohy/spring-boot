package com.cobra.tx.manualTx.frame;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * 一个线程中的一个事务的多个操作，使用的是同一个Connection
 */
public class CobraTransactionManager {


    private ConnectionUtils connectionUtils;

    /**
     * 注入连接
     */
    public void setConnectionUtils(ConnectionUtils connectionUtils) throws SQLException {
        this.connectionUtils = connectionUtils;
    }

    /**
     * 构造注入数据源
     */
    public CobraTransactionManager() {
    }


    /**
     * 开启 一个事务必须是同一个连接
     *
     * @throws SQLException
     */
    public void beginTransaction() throws SQLException {
        // 手动提交
        connectionUtils.getThreadConnection().setAutoCommit(false);
    }

    /**
     * 提交
     *
     * @throws SQLException
     */
    public void commit() throws SQLException {
        connectionUtils.getThreadConnection().commit();
    }

    // 回滚
    public void rollback() {
        try {
            connectionUtils.getThreadConnection().rollback();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * 关闭
     *
     * @throws SQLException
     */
    public void release() throws SQLException {
        connectionUtils.getThreadConnection().close();
        connectionUtils.removeConnection();
    }


    public DataSource getDataSource() {
        return connectionUtils.getDataSource();
    }

    public ConnectionUtils getConnectionUtils() {
        return connectionUtils;
    }
}
