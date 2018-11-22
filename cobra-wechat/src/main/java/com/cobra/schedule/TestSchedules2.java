package com.cobra.schedule;

import com.cobra.dao.SysTaskDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;

import java.time.LocalDateTime;

/**
 * 单任务 多时间
 */
//@Configuration
//@EnableScheduling
@Slf4j
public class TestSchedules2 implements SchedulingConfigurer
{
     @Autowired
     private SysTaskDao sysTaskDao;

    /**
     * 执行定时任务.
     */
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {

        taskRegistrar.addTriggerTask(
                        //1.添加任务内容(Runnable)
                        () -> log.info("执行定时任务2: " + LocalDateTime.now().toLocalTime()),
                        //2.设置执行周期(Trigger)
                        triggerContext -> {
                            //2.1 从数据库获取执行周期
                            String cron = sysTaskDao.selectByPrimaryKey(1).getCron();
                            log.info("=========cron======"+cron);
                            //2.2 合法性校验.

                            //2.3 返回执行周期(Date)
                            return new CronTrigger(cron).nextExecutionTime(triggerContext);
                        }
        );
    }

}


