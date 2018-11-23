package com.cobra.tmp;

import com.cobra.pojo.SysTask;
import org.quartz.*;
import org.springframework.stereotype.Service;

@Service
public class JobService
{

    public JobDataMap buildJobDataMap(SysTask sysTask){
        JobDataMap dataMap = new JobDataMap();
        dataMap.put("taskName",sysTask.getTaskName());
        dataMap.put("taskGroup",sysTask.getTaskGroup());
        dataMap.put("cron",sysTask.getCron());
        dataMap.put("description",sysTask.getDescription());

        return dataMap;
    }


    public Trigger getTrigger(SysTask sysTask){
        return TriggerBuilder
                        .newTrigger()
                        .withIdentity(sysTask.getTaskName(),sysTask.getTaskGroup())
                        .withSchedule(CronScheduleBuilder.cronSchedule(sysTask.getCron()))
                        .build();
    }

    public JobDetail getJobDetail(SysTask sysTask){
        return JobBuilder
                        .newJob(MyJob.class)
                        .withIdentity(sysTask.getTaskName(),sysTask.getTaskGroup())
                        .setJobData(buildJobDataMap(sysTask))
                        .build();
    }
}
