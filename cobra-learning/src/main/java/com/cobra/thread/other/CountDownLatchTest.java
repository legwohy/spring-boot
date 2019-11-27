package com.cobra.thread.other;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *  CountDownLatch最重要的方法是countDown()和await()，
 * 前者主要是倒数一次，后者是等待倒数到0，如果没有到达0，就只有阻塞等待了。
 * 倒数到0将会释放所有等待的线程
 */
public class CountDownLatchTest
{
    // 模拟了100米赛跑，10名选手已经准备就绪，只等裁判一声令下。当所有人都到达终点时，比赛结束。
    public static void main(String[] args) throws InterruptedException
    {
        // 开始的倒数锁
        final CountDownLatch begin = new CountDownLatch(1);

        // 结束的倒数锁
        final CountDownLatch end = new CountDownLatch(10);

        // 十名选手
        final ExecutorService exec = Executors.newFixedThreadPool(10);

        for (int index = 0; index < 10; index++)
        {
            final int NO = index + 1;
            Runnable run = new Runnable()
            {
                public void run()
                {
                    try
                    {
                        // 如果当前计数为零，则此方法立即返回。
                        // 等待
                        begin.await();
                        Thread.sleep((long)(Math.random() * 10000));
                        System.out.println("No." + NO + " arrived");
                    }
                    catch (InterruptedException e) {}
                    finally
                    {
                        // 每个选手到达终点时，end就减一
                        end.countDown();
                    }
                }
            };
            exec.submit(run);
        }
        System.out.println("Game Start");
        // begin减一，开始游戏
        begin.countDown();
        // 等待end变为0，即所有选手到达终点
        end.await();
        System.out.println("Game Over");
        exec.shutdown();
    }
}