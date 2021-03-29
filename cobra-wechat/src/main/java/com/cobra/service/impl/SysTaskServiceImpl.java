package com.cobra.service.impl;

import com.cobra.dao.SysTaskDao;
import com.cobra.pojo.SysTask;
import com.cobra.service.SysTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @auther: admin
 * @date: 2018/11/22 17:36
 * @description:
 */
@Service
public class SysTaskServiceImpl implements SysTaskService
{
    @Autowired private SysTaskDao sysTaskDao;


    @Override
    public int insert(SysTask record)
    {
        return sysTaskDao.insert(record);
    }

    @Override
    public int insertSelective(SysTask record)
    {
        return sysTaskDao.insertSelective(record);
    }

    @Override
    public SysTask selectByPrimaryKey(Object id)
    {
        return sysTaskDao.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(SysTask record)
    {
        return sysTaskDao.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(SysTask record)
    {
        return sysTaskDao.updateByPrimaryKey(record);
    }

    @Override
    public List<SysTask> selectAll()
    {
        return sysTaskDao.selectAll();
    }
}
