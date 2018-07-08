package com.cobra.mapper;

import com.cobra.entirty.UserInfoFrom;
import com.cobra.util.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
public interface UserInfoFromMapper{
    int deleteByPrimaryKey(Integer id);

    int insert(UserInfoFrom record);

    int insertSelective(UserInfoFrom record);

    UserInfoFrom selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserInfoFrom record);

    int updateByPrimaryKey(UserInfoFrom record);
}