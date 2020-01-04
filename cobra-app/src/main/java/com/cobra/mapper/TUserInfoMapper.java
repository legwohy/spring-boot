package com.cobra.mapper;

import com.cobra.domain.entity.TUserInfo;

public interface TUserInfoMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(TUserInfo record);

    int insertSelective(TUserInfo record);


    TUserInfo selectByPrimaryKey(Integer id);


    int updateByPrimaryKeySelective(TUserInfo record);

    int updateByPrimaryKey(TUserInfo record);
}