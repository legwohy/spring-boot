package com.cobra.tx.manualTx.frame;

import com.alibaba.druid.pool.DruidDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 连接工具类 从数据源中获取一个连接 并与当前线程绑定韩国
 */
public class ConnectionUtils
{
    private static ThreadLocal<Connection> threadLocal = new ThreadLocal<>();
    /**
     * 数据源 可以自动注入
     */
    private static DruidDataSource dataSource = new DruidDataSource();
    static String driver = "com.mysql.jdbc.Driver";
    static String userName ="cis_credit";
    static String pwd = "cis_credit@M4dev";
    static String url = "jdbc:mysql://172.17.27.30:3306/cis_credit?";
    //静态代码块,设置连接数据库的参数
    static{
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(url);
        dataSource.setUsername(userName);
        dataSource.setPassword(pwd);
    }


    /**
     * 获取当前线程上的连接
     * @return
     * @throws SQLException
     */
    public Connection getThreadConnection()throws SQLException
    {
        Connection conn = threadLocal.get();
        if(null == conn){
            conn = dataSource.getConnection();
            threadLocal.set(conn);
        }

        return conn;
    }

    /**
     * 将连接和线程解绑
     */
    public void removeConnection(){
        threadLocal.remove();
    }

    /**
     * 数据源
     * @return
     */
    public DataSource getDataSource(){
        return dataSource;
    }

}
