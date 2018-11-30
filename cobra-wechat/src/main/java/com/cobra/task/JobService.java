package com.cobra.task;

import com.cobra.pojo.SysTask;
import com.cobra.service.SysTaskService;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobService
{
    @Autowired private SchedulerFactoryBean factoryBean;

    @Autowired private SysTaskService sysTaskService;

    /**
     * 启动所有
     */
    public void startAll(){
        List<SysTask> sysTasks = sysTaskService.selectAll();
        Scheduler scheduler = factoryBean.getScheduler();
        sysTasks.forEach(sysTask -> {
            if(sysTask.getStatus().equals(1))
            {
                try
                {
                    scheduler.scheduleJob(getJobDetail(sysTask),getTrigger(sysTask));
                }catch (Exception e){
                    e.printStackTrace();
                }


            }
        });

    }

    /**
     * 刷新定时任务
     * @param id
     * @return
     */
    public String refresh( Integer id)
    {
        SysTask sysTask = sysTaskService.selectByPrimaryKey(id);
        if(null == sysTask)
        {
            return "暂无此任务";
        }

        try
        {

            Scheduler scheduler = factoryBean.getScheduler();

            // 删除
            if(null == sysTask.getStatus() || sysTask.getStatus().equals(0))
            {
                scheduler.deleteJob(JobKey.jobKey(sysTask.getTaskName(),sysTask.getTaskGroup()));

                return "关闭定时任务【"+sysTask.getTaskName()+"】!";
            }

            // 增加
            if(sysTask.getStatus().equals(1))
            {

                // 删除任务
                scheduler.deleteJob(JobKey.jobKey(String.valueOf(sysTask.getId()),sysTask.getTaskGroup()));
                // 添加任务
                scheduler.scheduleJob(getJobDetail(sysTask),getTrigger(sysTask));

                return "开启定时任务【"+sysTask.getTaskName()+"】!";
            }

        }catch (Exception e){
            e.printStackTrace();
        }


        return "刷新任务失败";
    }

    public Trigger getTrigger(SysTask sysTask){
        return TriggerBuilder
                        .newTrigger()
                        .withIdentity(getTriggerKey(sysTask))
                        .withSchedule(CronScheduleBuilder.cronSchedule(sysTask.getCron()))
                        .build();
    }

    public JobDetail getJobDetail(SysTask sysTask){
        return JobBuilder
                        .newJob(DynamicJob.class)
                        .withIdentity(getJobKey(sysTask))
                        .setJobData(buildJobDataMap(sysTask))
                        .build();
    }

    public TriggerKey getTriggerKey(SysTask sysTask)
    {
        return new TriggerKey(String.valueOf(sysTask.getId()),sysTask.getTaskGroup());
    }

    public JobKey getJobKey(SysTask sysTask)
    {
        return new JobKey(String.valueOf(sysTask.getId()),sysTask.getTaskGroup());
    }

    public JobDataMap buildJobDataMap(SysTask sysTask){
        JobDataMap dataMap = new JobDataMap();
        dataMap.put("taskName",sysTask.getTaskName());
        dataMap.put("taskGroup",sysTask.getTaskGroup());
        dataMap.put("cron",sysTask.getCron());
        dataMap.put("className",sysTask.getClassName());
        dataMap.put("methodName",sysTask.getMethodName());
        dataMap.put("parameter",sysTask.getParameter());
        dataMap.put("description",sysTask.getDescription());

        return dataMap;
    }
}
