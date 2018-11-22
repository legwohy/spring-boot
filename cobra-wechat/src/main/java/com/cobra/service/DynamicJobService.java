package com.cobra.service;

import com.cobra.dao.SysTaskDao;
import com.cobra.job.DynamicJob;
import com.cobra.pojo.SysTask;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EalenXie on 2018/6/4 14:25
 */
@Service
public class DynamicJobService
{

    @Autowired
    private SysTaskDao sysTaskDao;


    /**
     * 从数据库中加载获取到所有Job
     * @return
     */
    public List<SysTask> loadJobs()
    {
        List<SysTask> list = new ArrayList<>();
        sysTaskDao.selectAll().forEach(list::add);
        return list;
    }


    /**
     * 获取JobDataMap.(Job参数对象)
     * @param job
     * @return
     */
    public JobDataMap getJobDataMap(SysTask job)
    {
        JobDataMap map = new JobDataMap();
        map.put("name", job.getName());
        map.put("group", job.getGroup());
        map.put("cronExpression", job.getCron());
        map.put("parameter", job.getParameter());
        map.put("JobDescription", job.getDescription());
        map.put("parameter", job.getParameter());
        map.put("status", job.getStatus());
        map.put("className", job.getClassName());
        map.put("methodName", job.getMethodName());
        return map;
    }

    /**
     * 获取JobDetail,JobDetail是任务的定义,而Job是任务的执行逻辑,JobDetail里会引用一个Job Class来定义
     * @param jobKey
     * @param description
     * @param map
     * @return
     */
    public JobDetail geJobDetail(JobKey jobKey, String description, JobDataMap map)
    {
        return JobBuilder.newJob(DynamicJob.class)
                        .withIdentity(jobKey)
                        .withDescription(description)
                        .setJobData(map)
                        .storeDurably()
                        .build();
    }

    /**
     * 获取Trigger (Job的触发器,执行规则)
     * @param job
     * @return
     */
    public Trigger getTrigger(SysTask job)
    {
        return TriggerBuilder.newTrigger()
                        .withIdentity(job.getName(), job.getGroup())
                        .withSchedule(CronScheduleBuilder.cronSchedule(job.getCron()))
                        .build();
    }

    /**
     *  获取JobKey,包含Name和Group
     * @param job
     * @return
     */
    public JobKey getJobKey(SysTask job)
    {
        return JobKey.jobKey(job.getName(), job.getGroup());
    }
}
