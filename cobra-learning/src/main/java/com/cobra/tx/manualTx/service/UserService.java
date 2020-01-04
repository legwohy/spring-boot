package com.cobra.tx.manualTx.service;

import com.cobra.tx.manualTx.dao.UserAccountDao;
import com.cobra.tx.manualTx.dao.UserOderDao;
import com.cobra.tx.manualTx.frame.CobraTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;

public class UserService
{
    private UserOderDao userOderDao;
    private UserAccountDao userAccountDao;

    private CobraTransactionManager transactionManager;

    /** 同一个数据源*/
    public UserService (DataSource dataSource){
        userOderDao = new UserOderDao(dataSource);
        userAccountDao = new UserAccountDao(dataSource);
        transactionManager = new CobraTransactionManager(dataSource);
    }
    public void action(){
        try
        {
            transactionManager.start();

            // 业务
            userOderDao.buy();
            userAccountDao.order();


        }catch (SQLException e){
            e.printStackTrace();
            transactionManager.rollBack();
        }finally
        {
            try {transactionManager.close();}catch (SQLException e){}
        }
    }
}
