package com.cobra.service;

import com.cobra.domain.entity.TUserInfoDO;

public interface UserInfoFromService {
    TUserInfoDO selectByPrimaryKey(Integer id);
}
