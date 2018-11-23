package com.cobra.controller;

import com.cobra.pojo.SysTask;
import com.cobra.service.SysTaskService;
import com.cobra.tmp.JobParameter;
import com.cobra.tmp.JobService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
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

    @RequestMapping("/add/{id}")
    @ResponseBody
    public String start(@PathVariable Integer id){
        SysTask sysTask = sysTaskService.selectByPrimaryKey(id);

        //JobParameter jobParameter = new JobParameter("task1","task","*/10 * * * * ?","每10秒钟执行一次");
        Scheduler scheduler = factoryBean.getScheduler();
        try
        {
            log.info("====================添加{}任务",sysTask.getTaskName());
            if(sysTask.getStatus().equals(1))
            {
                // 删除
                scheduler.deleteJob(JobKey.jobKey(sysTask.getTaskName(),sysTask.getTaskGroup()));

                // 增加
                scheduler.scheduleJob(jobService.getJobDetail(sysTask),jobService.getTrigger(sysTask));

            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return "开始执行";
    }

    @RequestMapping("/delete/{id}")
    @ResponseBody
    public String stop(@PathVariable Integer id){
        SysTask sysTask = sysTaskService.selectByPrimaryKey(id);
        //JobParameter jobParameter = new JobParameter("task1","task","*/10 * * * * ?","每10秒钟执行一次");

        Scheduler scheduler = factoryBean.getScheduler();
        try
        {
            log.info("====================删除{}任务",sysTask.getTaskName());

            // 删除任务
            scheduler.deleteJob(JobKey.jobKey(sysTask.getTaskName(),sysTask.getTaskGroup()));


        }catch (Exception e){
            e.printStackTrace();
        }
        return "任务停止";
    }

    @RequestMapping("/delete/all")
    public String stopAll()
    {
        deleteAll();

        return "停止所有";

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

    private void deleteAll(){
        List<SysTask> sysTasks = sysTaskService.selectAll();
        Scheduler scheduler = factoryBean.getScheduler();
        sysTasks.forEach(sysTask -> {
            if(sysTask.getStatus().equals(1))
            {
                try
                {
                    if (sysTask.getStatus().equals(1))
                    {
                        scheduler.deleteJob(JobKey.jobKey(sysTask.getTaskName(),sysTask.getTaskGroup()));
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }


            }
        });
    }
}
