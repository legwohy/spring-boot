package com.cobra.dao;

import com.cobra.pojo.UserInfo;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInfoDao {

    UserInfo selectByUserPhone(String userPhone);

    int deleteByPrimaryKey(Integer id);

    int insert(UserInfo record);

    int insertSelective(UserInfo record);

    UserInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserInfo record);

    int updateByPrimaryKey(UserInfo record);
}