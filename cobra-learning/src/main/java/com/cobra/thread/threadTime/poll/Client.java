package com.cobra.thread.threadTime.poll;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <a link='https://blog.csdn.net/dailywater/article/details/19771689'/>
 */
public class Client {
    public static void main(String[] args) {
        //方式一：直接设定DefaultUncaughtExceptionHandler，然后直接t.start();task.start()启动线程即可。
        //Thread.setDefaultUncaughtExceptionHandler(new MyUncauhtExceptionHandler());

        //方式二：创建线程创建工厂，利用线程池启动线程
        ExecutorService exec = Executors.newCachedThreadPool(new MyHandlerFactory());
        CobraTimeoutThread tt = new CobraTimeoutThread(800);
        Thread t = new Thread(tt);
        CobraTaskThread task = new CobraTaskThread(tt);
        exec.execute(t);
        exec.execute(task);
        exec.shutdown();
    }

}
