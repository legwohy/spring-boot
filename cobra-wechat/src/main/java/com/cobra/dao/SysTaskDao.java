package com.cobra.dao;

import com.cobra.pojo.SysTask;
import com.cobra.util.MyMapper;

import java.util.List;

public interface SysTaskDao
{

    int deleteByPrimaryKey(Integer id);

    int insert(SysTask record);

    int insertSelective(SysTask record);

    SysTask selectByPrimaryKey(Object id);

    int updateByPrimaryKeySelective(SysTask record);

    int updateByPrimaryKey(SysTask record);

    List<SysTask> selectAll();


}
