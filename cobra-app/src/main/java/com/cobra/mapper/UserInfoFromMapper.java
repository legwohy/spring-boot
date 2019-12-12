package com.cobra.mapper;

import com.cobra.domain.entirty.UserInfoFrom;
import org.apache.ibatis.annotations.Mapper;

//@Mapper
public interface UserInfoFromMapper{
    int deleteByPrimaryKey(Integer id);

    int insert(UserInfoFrom record);

    int insertSelective(UserInfoFrom record);

    UserInfoFrom selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserInfoFrom record);

    int updateByPrimaryKey(UserInfoFrom record);
}