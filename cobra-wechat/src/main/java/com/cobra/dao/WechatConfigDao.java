package com.cobra.dao;

import com.cobra.pojo.WechatConfig;

public interface WechatConfigDao
{
    int deleteByPrimaryKey(Integer id);

    int insert(WechatConfig record);

    int insertSelective(WechatConfig record);

    WechatConfig selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WechatConfig record);

    int updateByPrimaryKey(WechatConfig record);
}