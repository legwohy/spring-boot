package com.cobra.task;

import com.cobra.util.SpringContextHolder;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * :@DisallowConcurrentExecution : 此标记用在实现Job的类上面,意思是不允许并发执行.
 * :注意org.quartz.threadPool.threadCount线程池中线程的数量至少要多个,否则@DisallowConcurrentExecution不生效
 * :假如Job的设置时间间隔为3秒,但Job执行时间是5秒,设置@DisallowConcurrentExecution以后程序会等任务执行完毕以后再去执行,否则会在3秒时再启用新的线程执行
 */
@DisallowConcurrentExecution
@Component
public class DynamicJob implements Job
{
    private Logger logger = LoggerFactory.getLogger(DynamicJob.class);

    /**
     * 核心方法,Quartz Job真正的执行逻辑.
     * @param executorContext executorContext JobExecutionContext中封装有Quartz运行所需要的所有信息
     * @throws JobExecutionException execute()方法只允许抛出JobExecutionException异常
     */
    @Override
    public void execute(JobExecutionContext executorContext) throws JobExecutionException
    {
        JobDataMap map = executorContext.getMergedJobDataMap();
        String parameter = map.getString("parameter");
        String className = map.getString("className");
        String methodName = map.getString("methodName");
        logger.info("Running Job taskName : {} ", map.getString("taskName"));
        logger.info("Running Job taskGroup: {} ", map.getString("taskGroup"));
        logger.info("Running Job cron {}: " , map.getString("cron"));
        logger.info("Running Job parameter : {} ", parameter);
        logger.info("Running Job className : {} ", className);
        logger.info("Running Job methodName : {} ", methodName);
        long startTime = System.currentTimeMillis();

        try
        {
            // 执行定时任务
            Object bean = SpringContextHolder.getBean(toLowerCaseFirstOne(map.getString("className")));
            Class clazz = bean.getClass();
            Method method = clazz.getMethod(map.getString("methodName"), null);
            method.invoke(bean, null);
        }
        catch (Exception e)
        {
            logger.error("程序执行错误：className={},methodName={}", map.getString("className"), map.getString("methodName"));
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis();
        logger.info(">>>>>>>>>>>>> Running Job has been completed , cost time :  " + (endTime - startTime) + "ms\n");
    }

    private static String toLowerCaseFirstOne(String s){
        if(Character.isLowerCase(s.charAt(0)))
        {
            return s;
        }else
        {
            return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();
        }
    }

}
