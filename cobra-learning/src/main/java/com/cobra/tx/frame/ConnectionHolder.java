package com.cobra.tx.frame;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


public class ConnectionHolder
{
    private Map<DataSource,Connection> map = new HashMap<>();

    public Connection getConnection(DataSource dataSource)throws SQLException{

        // 先从缓存里面查
       Connection conn =  map.get(dataSource);
       if(null == conn){
           conn = dataSource.getConnection();
           map.put(dataSource,conn);
       }
       return conn;
    }
}
