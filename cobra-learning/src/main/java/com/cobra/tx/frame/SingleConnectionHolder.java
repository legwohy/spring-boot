package com.cobra.tx.frame;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;


public class SingleConnectionHolder
{
    private static ThreadLocal<ConnectionHolder> threadLocal = new ThreadLocal<>();

    private static ConnectionHolder getConnectionHolder()
    {
        ConnectionHolder holder = threadLocal.get();
        if (null == holder)
        {
            // 实例化一个
             holder = new ConnectionHolder();
            threadLocal.set(holder);
        }

        return holder;
    }

    public static Connection getConnection(DataSource dataSource)throws SQLException
    {
        return getConnectionHolder().getConnection(dataSource);
    }

}
