package com.cobra.thread.sleepWait.sleep;

/**
 * sleep() 方法是线程类（Thread）的静态方法，让调用线程进入睡眠状态，
 * 让出执行机会给其他线程，等到休眠时间结束后，线程进入就绪状态和其他线程一起竞争cpu的执行时间。
 *  因为sleep() 是static静态的方法，他不能改变对象的机锁，当一个synchronized块中调用了sleep() 方法，
 *  线程虽然进入休眠，但是对象的机锁没有被释放，其他线程依然无法访问这个对象

 * sleep休眠不会释放当前对象锁
 *
 * <a link='https://blog.csdn.net/xyh269/article/details/52613507'/>
 */
public class SleepMainTest {
    public static void main(String[] args){

        //
        SleepService service = new SleepService();

        Thread sleepThread = new Thread(new SSThread(service));
        Thread waitThread = new Thread(new SWThread(service));
        sleepThread.start();
        waitThread.start();

    }
}
