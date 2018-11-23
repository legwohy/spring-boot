package com.cobra.tmp;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.time.LocalDateTime;

@Slf4j
public class MyJob implements Job
{
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException
    {
        JobDataMap dataMap = jobExecutionContext.getMergedJobDataMap();
        log.info("================>taskName={}",dataMap.getString("taskName"));
        log.info("================>taskGroup={}",dataMap.getString("taskGroup"));
        log.info("================>cron={}",dataMap.getString("cron"));
        log.info("================>description={}",dataMap.getString("description"));

        System.out.println("MyJob=============>> "+ LocalDateTime.now().toLocalTime());


    }
}
