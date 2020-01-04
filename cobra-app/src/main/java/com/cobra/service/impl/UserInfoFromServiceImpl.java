package com.cobra.service.impl;


import com.cobra.domain.entity.TUserInfo;
import com.cobra.mapper.TUserInfoMapper;
import com.cobra.service.UserInfoFromService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserInfoFromServiceImpl implements UserInfoFromService{
    @Autowired
    private TUserInfoMapper userInfoMapper;

    public TUserInfo selectByPrimaryKey(Integer id){
        return userInfoMapper.selectByPrimaryKey(1);
    }
}
