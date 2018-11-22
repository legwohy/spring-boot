package com.cobra.schedule;

import com.cobra.dao.SysTaskDao;
import com.cobra.pojo.SpringUtils;
import com.cobra.pojo.SysTask;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 多任务
 */
//@Component
@Slf4j
//@EnableScheduling
public class TestSchedules implements SchedulingConfigurer
{
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Resource
    private SysTaskDao sysTaskDao;



    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar)
    {
        List<SysTask> taskList = getAllTasks();
        log.info("==============定时任务启动,预计启动任务数量=" + taskList.size() + "; time=" + sdf.format(new Date()));

        //校验数据（这个步骤主要是为了打印日志，可以省略）
        //checkDataList(tasks);

        taskList.stream().parallel().forEach(task->{
            taskRegistrar.addTriggerTask(getRunnable(task), getTrigger(task));
        });

    }


    /**
     * 任务类
     * @param task
     * @return
     */
    private Runnable getRunnable(SysTask task)
    {
        return new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    // 加载类
                    Class<?> clazz = Class.forName(task.getClassName());
                    // 获取方法
                    Method method = clazz.getMethod(task.getMethodName(),null);

                    // 唤醒 实例化
                    method.invoke(clazz.newInstance(),null);
                }
                catch (InvocationTargetException e)
                {
                    log.error("定时任务启动错误，反射异常:" + task.getClassName() + ";" + task.getMethodName() + ";" + e.getMessage());
                }
                catch (Exception e)
                {
                    log.error(e.getMessage());
                }
            }
        };
    }

    private Trigger getTrigger(SysTask task)
    {
        return new Trigger()
        {
            @Override
            public Date nextExecutionTime(TriggerContext triggerContext)
            {
                String cron = task.getCron();
                List<SysTask> sysTasks = getAllTasks();
                for (int i = 0;i<sysTasks.size();i++)
                {
                    if(task.getClassName().equals(sysTasks.get(i).getClassName()))
                    {
                        cron = sysTasks.get(i).getCron();
                        break;

                    }

                }

                CronTrigger trigger = new CronTrigger(cron);
                Date nextExec = trigger.nextExecutionTime(triggerContext);

                return nextExec;
            }
        };

    }



    /**
     * 从数据库里取得所有要执行的定时任务
     * @return
     */
    private List<SysTask> getAllTasks()
    {
        return sysTaskDao.selectAll();
    }

    public static void main(String[] args)
    {
        List<Integer> datas = Arrays.asList(1,2,3,4);
        datas.stream().filter(data->{return data==1;}).forEach(data->{System.out.println("===>"+data);});



    }

}
