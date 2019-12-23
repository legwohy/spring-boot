package com.cobra.myMVC.dao;

import com.cobra.myMVC.annotation.XRepository;

@XRepository("xUserDao")
public class XUserDao {
    public void query(){
        System.out.println("查询");
    }
}
