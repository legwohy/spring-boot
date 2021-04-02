package com.cobra.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * 线程池工具类，工程所有多线程均调用此类，控制整个工程允许的最大线程数
 *
 *
 */
public class ThreadPoolUtils
{
    Logger logger = LoggerFactory.getLogger(this.getClass());
    public static ThreadPoolUtils threadPoolUtils;

    /**
     * 1、核心线程是否满
     * 2、队列是否满
     * 3、线程池是否满
     * 4、(饱和)拒绝策略
     * @see Executors#newSingleThreadExecutor() 核心线程和最大线程均为1 链表(可以当无界队列) LinkedBlockingQueue
     * @see Executors#newFixedThreadPool(int) ()   固定线程池 最大线程数和核心线程数相同，链表 LinkedBlockingQueue
     * @see Executors#newScheduledThreadPool(int) () 固定线程池，最大线程无界 堆队列 DelayedWorkQueue
     * @see Executors#newCachedThreadPool()  缓存线程池 核心为0 最大线程无界 阻塞队列 SynchronousQueue
     */
    ExecutorService executorService = new ThreadPoolExecutor(
                    2,// 核心线程
                    3,// 线程池最大容量 队列满时才会创建该线程
                    0,// 最大线程空闲存活时间 当线程大于核心线程数时，会回收空闲线程
                    TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<Runnable>(),// 队列
                    new CobraRejectHandler()// 拒绝策略 只有线程池和队列均满才会执行

    );

    public static ThreadPoolUtils getInstance()
    {
        if (threadPoolUtils == null)
        {
            threadPoolUtils = new ThreadPoolUtils();
        }
        return threadPoolUtils;
    }

    public void run(Runnable r)
    {
        executorService.execute(r);
    }

    /**
     * 静态内部类
     * @see java.util.concurrent.ThreadPoolExecutor.AbortPolicy 默认 拒绝策略  直接抛出异常
     * @see java.util.concurrent.ThreadPoolExecutor.DiscardPolicy 丢弃任务 不抛出异常
     * @see java.util.concurrent.ThreadPoolExecutor.DiscardOldestPolicy 丢弃前面任务 重新提交新任务
     * @see java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy 调用线程执行该任务
     */
    public static class CobraRejectHandler implements RejectedExecutionHandler
    {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor)
        {
            System.out.println("执行拒绝策略。。。");

        }
    }

}


