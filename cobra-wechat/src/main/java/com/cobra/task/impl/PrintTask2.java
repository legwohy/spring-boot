package com.cobra.task.impl;

import com.cobra.task.MyTask;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class PrintTask2 implements MyTask
{
    @Override
    public void excute()
    {
        log.info("执行定时任务2:"+ LocalDateTime.now().toLocalTime());

    }
}
