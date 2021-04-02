package com.cobra.thread.other;

import java.util.concurrent.CyclicBarrier;

/**
 * https://mp.weixin.qq.com/s/5y6sqVUlh88-ax6kzVZ3gQ
 */
public class CyclicBarrierTest
{
    public static void main(String[] args){
        final CyclicBarrier barrier = new CyclicBarrier(2, myThread);
        new Thread(new Runnable() {
            @Override
            public void run(){
                try {
                    System.out.println(Thread.currentThread().getName());
                    barrier.await();
                    System.out.println(Thread.currentThread().getName());
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "thread1").start();

        new Thread(new Runnable() {
            @Override
            public void run(){
                try {
                    System.out.println(Thread.currentThread().getName());
                    barrier.await();
                    System.out.println(Thread.currentThread().getName());
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "thread2").start();
    }



    static Thread myThread = new Thread(()-> System.out.println("myThread"),"thread3");
}
