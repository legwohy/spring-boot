package com.cobra.tx.manualTx.service.impl;

import com.cobra.tx.manualTx.dao.UserAccountDao;
import com.cobra.tx.manualTx.dao.UserOderDao;
import com.cobra.tx.manualTx.frame.CobraTransactionManager;
import com.cobra.tx.manualTx.frame.ConnectionUtils;
import com.cobra.tx.manualTx.frame.MyTransactional;
import com.cobra.tx.manualTx.service.UserService;


/**
 * MyTransactional 会生成一个代理类
 * cglib 代理类继承 UserServiceImpl
 * jdk动态代理拷贝一份 UserServiceImpl
 */
@MyTransactional
public class UserServiceImpl implements UserService {

    public void action(){
        System.out.println("被代理类:"+UserServiceImpl.class);
    }



}
