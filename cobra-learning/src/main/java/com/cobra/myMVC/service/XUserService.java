package com.cobra.myMVC.service;

import com.cobra.myMVC.annotation.XQualifier;
import com.cobra.myMVC.annotation.XService;
import com.cobra.myMVC.dao.XUserDao;

@XService("xUserService")
public class XUserService {
    @XQualifier("userDao")
    private XUserDao userDao;

    public void query(){
        userDao.query();
    }
}
