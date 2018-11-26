package com.cobra.controller;

import com.cobra.pojo.SysTask;
import com.cobra.service.SysTaskService;
import com.cobra.tmp.JobParameter;
import com.cobra.tmp.JobService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@Slf4j
public class NewJobController
{
    @Autowired private SchedulerFactoryBean factoryBean;
    @Autowired private JobService jobService;
    @Autowired private SysTaskService sysTaskService;

    @PostConstruct
    public void init(){
        startAll();
        log.info("初始化启动所有=========================");
    }


    private void startAll(){
        List<SysTask> sysTasks = sysTaskService.selectAll();
        Scheduler scheduler = factoryBean.getScheduler();
        sysTasks.forEach(sysTask -> {
            if(sysTask.getStatus().equals(1))
            {
                try
                {
                    scheduler.scheduleJob(jobService.getJobDetail(sysTask),jobService.getTrigger(sysTask));
                }catch (Exception e){
                    e.printStackTrace();
                }


            }
        });

    }


    @RequestMapping("/refresh/{id}")
    @ResponseBody
    public String refresh(HttpServletRequest request,HttpServletResponse response,@PathVariable Integer id)
    {
        SysTask sysTask = sysTaskService.selectByPrimaryKey(id);
        if(null == sysTask)
        {
            return "暂无此任务";
        }

        Scheduler scheduler = factoryBean.getScheduler();

        if(null == sysTask.getStatus() || sysTask.getStatus().equals(0))
        {
            try
            {
                scheduler.deleteJob(JobKey.jobKey(sysTask.getTaskName(),sysTask.getTaskGroup()));
                return "关闭定时任务【"+sysTask.getTaskName()+"】成功!";
            }catch (Exception e)
            {
                e.printStackTrace();
            }

        }

        if(sysTask.getStatus().equals(1))
        {

            try
            {
                // 删除任务
                scheduler.deleteJob(JobKey.jobKey(sysTask.getTaskName(),sysTask.getTaskGroup()));
                // 添加任务
                scheduler.scheduleJob(jobService.getJobDetail(sysTask),jobService.getTrigger(sysTask));

                return "开启定时任务【"+sysTask.getTaskName()+"】成功!";

            }catch (Exception e){
                e.printStackTrace();
            }

        }

        return "刷新任务失败";
    }

}
