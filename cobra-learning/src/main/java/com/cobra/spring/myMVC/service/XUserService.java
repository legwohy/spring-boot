package com.cobra.spring.myMVC.service;

import com.cobra.spring.myMVC.annotation.XQualifier;
import com.cobra.spring.myMVC.annotation.XService;
import com.cobra.spring.myMVC.dao.XUserDao;

@XService("xUserService")
public class XUserService {
    @XQualifier("xUserDao")
    private XUserDao userDao;

    public void query(){
        userDao.query();
    }
}
