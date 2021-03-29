package com.cobra.service;

import com.cobra.pojo.SysTask;

import java.util.List;

/**
 * @auther: admin
 * @date: 2018/11/22 17:36
 * @description:
 */
public interface SysTaskService
{

    int insert(SysTask record);

    int insertSelective(SysTask record);

    SysTask selectByPrimaryKey(Object id);

    int updateByPrimaryKeySelective(SysTask record);

    int updateByPrimaryKey(SysTask record);

    List<SysTask> selectAll();
}
