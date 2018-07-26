package com.cobra.service.impl;


import com.cobra.dao.BackConfigParamsMapper;
import com.cobra.pojo.BackConfigParams;
import com.cobra.service.BackConfigParamsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BackConfigParamsServiceImpl implements BackConfigParamsService
{
    @Autowired private BackConfigParamsMapper sysConfigMapper;


    public List<BackConfigParams> select(BackConfigParams sysConfig){
        return sysConfigMapper.select(sysConfig);
    }


    @Cacheable(value = "sysConfigs")
    public BackConfigParams selectByPrimaryKey(BackConfigParams sysConfig){
        return sysConfigMapper.selectByPrimaryKey(sysConfig);
    }

}
