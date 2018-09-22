package com.cobra.service;


public interface UserInfoService {
    /**
     * 登陆
     * @param userPhone 手机号码
     * @param password 密码
     * @return
     *             true 登陆成功
     *             false 登陆失败
     */
    boolean login(String userPhone,String password);
}
