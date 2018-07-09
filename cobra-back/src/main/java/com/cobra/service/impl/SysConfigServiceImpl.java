package com.cobra.service.impl;


import com.cobra.dao.SysConfigMapper;
import com.cobra.pojo.SysConfig;
import com.cobra.service.SysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysConfigServiceImpl implements SysConfigService{
    @Autowired private SysConfigMapper sysConfigMapper;


    public List<SysConfig> select(SysConfig sysConfig){
        return sysConfigMapper.select(sysConfig);
    }


    @Cacheable(value = "sysConfigs")
    public SysConfig selectByPrimaryKey(SysConfig sysConfig){
        return sysConfigMapper.selectByPrimaryKey(sysConfig);
    }

    public int updateByPrimaryKeySelective(SysConfig sysConfig){
        return sysConfigMapper.updateByPrimaryKeySelective(sysConfig);
    }

    public int updateByPrime(SysConfig sysConfig){
        return sysConfigMapper.updateByPrimaryKey(sysConfig);
    }


}
