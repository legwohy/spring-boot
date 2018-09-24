package com.cobra.service;


import com.cobra.pojo.UserInfo;

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

    int deleteByPrimaryKey(Integer id);

    int insert(UserInfo record);

    int insertSelective(UserInfo record);

    UserInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserInfo record);

    int updateByPrimaryKey(UserInfo record);

}
