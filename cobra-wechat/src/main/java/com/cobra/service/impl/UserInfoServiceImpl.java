package com.cobra.service.impl;

import com.cobra.dao.UserInfoDao;
import com.cobra.pojo.UserInfo;
import com.cobra.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Autowired
    private UserInfoDao userInfoDao;

    @Override
    public boolean login(String userPhone, String password) {
        UserInfo user = userInfoDao.selectByUserPhone(userPhone);
        if(null != user)
        {
            if("123456".equals(password)){
                return true;
            }

        }
        return false;
    }

    @Override
    public int deleteByPrimaryKey(Integer id) {
        return userInfoDao.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(UserInfo record) {
        return userInfoDao.insert(record);
    }

    @Override
    public int insertSelective(UserInfo record) {
        return userInfoDao.insertSelective(record);
    }

    @Override
    public UserInfo selectByPrimaryKey(Integer id) {
        return userInfoDao.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(UserInfo record) {
        return userInfoDao.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(UserInfo record) {
        return userInfoDao.updateByPrimaryKey(record);
    }
}
