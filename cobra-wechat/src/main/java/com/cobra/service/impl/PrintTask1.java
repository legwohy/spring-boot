package com.cobra.service.impl;

import com.cobra.service.SysTaskService;
import com.cobra.service.MyTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class PrintTask1 implements MyTask
{
    @Autowired private SysTaskService sysTaskService;
    @Override
    public void excute()
    {
        log.info("执行定时任务1:"+ LocalDateTime.now().toLocalTime()+","+sysTaskService.selectByPrimaryKey(1).toString());

    }
}
