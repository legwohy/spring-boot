package com.cobra.thread.threadTime.poll;

import java.util.concurrent.ThreadFactory;

/**
 * MyHandlerThreadFactory(可选)：实现ThreadFactory接口，线程的创建工厂，
 * 在这里主要是为线程池修改默认为线程异常捕获工厂，
 * 若在代码中设定Thread.setDefaultUncaughtExceptionHandler(new MyUncauhtExceptionHandler());，
 * 则该类可以不用，但一般写法需要用到该类。建议使用该类
 */
public class MyHandlerFactory implements ThreadFactory {
    @Override
    public Thread newThread(Runnable r) {
        // 定义线程创建工厂，这里主要设定MyUncauhtExceptionHandler
        Thread t = new Thread(r);
        t.setUncaughtExceptionHandler(new MyUncauhtExceptionHandler());
        return t;
    }

}
