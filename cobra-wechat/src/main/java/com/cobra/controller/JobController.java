package com.cobra.controller;

import com.cobra.pojo.SysTask;
import com.cobra.service.DynamicJobService;
import com.cobra.service.SysTaskService;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.Set;

/**
 * Created by EalenXie on 2018/6/4 16:12
 */
@Controller
public class JobController
{

    private static Logger logger = LoggerFactory.getLogger(JobController.class);
    @Autowired
    private SchedulerFactoryBean factory;

    @Autowired
    private DynamicJobService jobService;

    @Autowired
    private SysTaskService sysTaskService;


    /**
     * 初始化启动所有的Job
     */
    @PostConstruct
    public void initialize()
    {
        try
        {
            reStartAllJobs();
            logger.info("INIT SUCCESS");
        }
        catch (SchedulerException e)
        {
            logger.info("INIT EXCEPTION : " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 根据ID重启某个Job
     * @param id
     * @return
     * @throws SchedulerException
     */
    @RequestMapping("/refresh/{id}")
    @ResponseBody
    public String refresh(@PathVariable Integer id) throws SchedulerException
    {
        String result;
        SysTask entity = sysTaskService.selectByPrimaryKey(id);
        if (entity == null)
        {
            return "error: id is not exist ";
        }
        TriggerKey triggerKey = new TriggerKey(entity.getName(), entity.getGroup());
        JobKey jobKey = jobService.getJobKey(entity);
        Scheduler scheduler = factory.getScheduler();
        try
        {
            scheduler.unscheduleJob(triggerKey);
            scheduler.deleteJob(jobKey);
            JobDataMap map = jobService.getJobDataMap(entity);
            JobDetail jobDetail = jobService.geJobDetail(jobKey, entity.getDescription(), map);
            if (entity.getStatus().equals("OPEN"))
            {
                scheduler.scheduleJob(jobDetail, jobService.getTrigger(entity));
                result = "Refresh Job : " + entity.getName() + " success !";
            }
            else
            {
                result = "Refresh Job : " + entity.getName() + " failed ! , " +
                                "Because the Job status is " + entity.getStatus();
            }
        }
        catch (SchedulerException e)
        {
            result = "Error while Refresh " + e.getMessage();
        }
        return result;
    }

    /**
     * 重启数据库中所有的Job
     * @return
     */
    @RequestMapping("/refresh/all")
    @ResponseBody
    public String refreshAll()
    {
        String result;
        try
        {
            reStartAllJobs();
            result = "SUCCESS";
        }
        catch (SchedulerException e)
        {
            result = "EXCEPTION : " + e.getMessage();
        }
        return "refresh all jobs : " + result;
    }

    /**
     * 重新启动所有的job
     */
    private void reStartAllJobs() throws SchedulerException
    {
        Scheduler scheduler = factory.getScheduler();
        Set<JobKey> set = scheduler.getJobKeys(GroupMatcher.anyGroup());
        for (JobKey jobKey : set)
        {
            scheduler.deleteJob(jobKey);
        }
        for (SysTask job : jobService.loadJobs())
        {
            logger.info("Job register name : {} , group : {} , cron : {}", job.getName(), job.getGroup(), job.getCron());
            JobDataMap map = jobService.getJobDataMap(job);
            JobKey jobKey = jobService.getJobKey(job);
            JobDetail jobDetail = jobService.geJobDetail(jobKey, job.getDescription(), map);
            if (job.getStatus().equals("OPEN"))
            {
                scheduler.scheduleJob(jobDetail, jobService.getTrigger(job));
            }
            else
            {
                logger.info("Job jump name : {} , Because {} status is {}", job.getName(), job.getName(), job.getStatus());
            }
        }
    }

}
