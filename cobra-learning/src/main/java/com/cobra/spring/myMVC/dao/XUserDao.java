package com.cobra.spring.myMVC.dao;

import com.cobra.spring.myMVC.annotation.XRepository;

@XRepository("xUserDao")
public class XUserDao {
    public void query(){
        System.out.println("查询");
    }
}
