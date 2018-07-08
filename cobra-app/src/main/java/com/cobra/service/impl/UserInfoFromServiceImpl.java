package com.cobra.service.impl;


import com.cobra.entirty.UserInfoFrom;
import com.cobra.mapper.UserInfoFromMapper;
import com.cobra.service.UserInfoFromService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserInfoFromServiceImpl implements UserInfoFromService{
    @Autowired private UserInfoFromMapper userInfoFromMapper;

    public UserInfoFrom selectByPrimaryKey(Integer id){
        return userInfoFromMapper.selectByPrimaryKey(1);
    }
}
