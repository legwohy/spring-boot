package com.cobra.tx.manualTx.service.impl;

import com.alibaba.druid.pool.DruidDataSource;
import com.cobra.tx.manualTx.dao.UserAccountDao;
import com.cobra.tx.manualTx.dao.UserOderDao;
import com.cobra.tx.manualTx.frame.CobraTransactionManager;
import com.cobra.tx.manualTx.frame.ConnectionUtils;
import com.cobra.tx.manualTx.service.UserService;

import java.sql.SQLException;

/**
 * MyTransactional 会生成一个代理类
 */

public class UserServiceManuImpl implements UserService
{

    private UserOderDao userOderDao;
    private UserAccountDao userAccountDao;

    private CobraTransactionManager transactionManager;

    /**
     * 无参构造
     */
    public UserServiceManuImpl(){

    }
    /**
     * 同一个数据源
     * */
    public UserServiceManuImpl(ConnectionUtils connectionUtils){
        userOderDao = new UserOderDao(connectionUtils);
        userAccountDao = new UserAccountDao(connectionUtils);
        transactionManager = new CobraTransactionManager();
        try {
            transactionManager.setConnectionUtils(connectionUtils);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void action(){
        DruidDataSource druid = (DruidDataSource) transactionManager.getDataSource();
        try
        {
            // 这里 必须保证是同一连接
            transactionManager.beginTransaction();

            // 业务
            userOderDao.buy();
            userAccountDao.order();

            transactionManager.commit();

            return;

        }catch (SQLException e){
            e.printStackTrace();
            transactionManager.rollback();
            throw new RuntimeException("异常");
        }finally
        {

            try {transactionManager.release();}catch (SQLException e){}
            System.out.println("剩余数量:"+druid.getActiveCount());

        }
    }



}
