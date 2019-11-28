package com.cobra.thread.threadTime.poll;

/**
 * 自定义的线程异常捕获类
 * 在守护进程由于超时被迫销毁时，能够执行这个异常里的代码，一般用于任务执行主体超时后的状态改变，
 * 如将任务标记为超时状态。各位请注意：线程中抛出的异常，是不能够被直接捕获的
 */
public class MyUncauhtExceptionHandler implements Thread.UncaughtExceptionHandler{
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        // 简单起见，只打印一句话
        System.out.println("捕获到异常:\tthreadName="+t.getName()+"\t,errorMsg="+e.getMessage());
    }
}
