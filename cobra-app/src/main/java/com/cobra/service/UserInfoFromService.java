package com.cobra.service;

import com.cobra.domain.entity.TUserInfo;

public interface UserInfoFromService {
    TUserInfo selectByPrimaryKey(Integer id);
}
