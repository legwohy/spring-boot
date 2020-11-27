package com.cobra.lock;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 可重入锁
 */
public class ReetrantLockDemo extends Thread {
    /**
     *  true 公平锁 false 非公平锁
     *  先启动先持有锁
     */
    private ReentrantLock lock = new ReentrantLock(true);

    private void fairLock(){
        try {
            lock.lock();
            System.out.println(Thread.currentThread().getName() + "正在持有锁");
        } finally {
            System.out.println(Thread.currentThread().getName() + "释放了锁");
            lock.unlock();
        }
    }

    public static void main(String[] args){
        ReetrantLockDemo myFairLock = new ReetrantLockDemo();
        Runnable runnable = () -> {
            System.out.println(Thread.currentThread().getName() + "启动");
            myFairLock.fairLock();
        };
        Thread[] thread = new Thread[10];
        for (int i = 0; i < 10; i++) {
            thread[i] = new Thread(runnable);
        }
        for (int i = 0; i < 10; i++) {
            thread[i].start();
        }
    }

    /**
     * 可重入锁
     */
    private synchronized void doSomething(){ System.out.println("doSomething..."); doSomethingElse();}
    private synchronized void doSomethingElse(){ System.out.println("doSomethingElse...");}

}

