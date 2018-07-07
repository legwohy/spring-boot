package com.ihome.service.impl;


import com.ihome.dao.SysConfigMapper;
import com.ihome.pojo.SysConfig;
import com.ihome.service.SysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SysConfigServiceImpl implements SysConfigService{
    @Autowired private SysConfigMapper sysConfigMapper;

    @Cacheable(value = "sysConfigs")
    public List<SysConfig> select(SysConfig sysConfig){
        return sysConfigMapper.select(sysConfig);
    }



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
