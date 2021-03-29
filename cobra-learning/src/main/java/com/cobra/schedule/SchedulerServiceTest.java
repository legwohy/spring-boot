package com.cobra.schedule;

import org.springframework.scheduling.concurrent.DefaultManagedTaskScheduler;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @auther: admin
 * @date: 2019/1/30 09:59
 * @description:
 */
public class SchedulerServiceTest
{
    public static void main(String[] args) throws Exception
    {
        System.out.println("==========>main start");
        SchedulerServiceTest test = new SchedulerServiceTest();
        test.testSchedulerExecutorService();
        System.out.println("==========>main end");

    }

    /**
     * 每一个分别执行 不能关闭
     * @throws Exception
     */
    public void testDefaultManagedTaskScheduler() throws Exception
    {
        System.out.println("==========>testDefaultManagedTaskScheduler start");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d1 = sdf.parse("2019-01-30 10:17:15");
        Date d2 = sdf.parse("2019-01-30 10:17:30");
        Date d3 = sdf.parse("2019-01-30 10:17:45");
        DefaultManagedTaskScheduler scheduler = new DefaultManagedTaskScheduler();
        scheduler.schedule(() ->
        {
            System.out.println("==========>定时任务启动类1\t" + d1);
        }, d1);
        scheduler.schedule(() ->
        {
            System.out.println("==========>定时任务启动类2\t" + d2);
        }, d2);
        scheduler.schedule(() ->
        {
            System.out.println("==========>定时任务启动类3\t" + d3);
        }, d3);

        System.out.println("==========>testDefaultManagedTaskScheduler end");
    }

    public void testSchedulerExecutorService()
    {
        System.out.println("==========>testSchedulerExecutorService start");
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        Future f1 = service.schedule(() ->
        {
            System.out.println("10秒后执行一次");
        }, 10, TimeUnit.SECONDS);
        if (!f1.isDone())
        {
            f1.cancel(true);

        }
        Future f2 = service.schedule(() ->
        {
            System.out.println("5秒后执行一次");
        }, 5, TimeUnit.SECONDS);
        if (!f2.isDone())
        {
            f2.cancel(true);

        }
        Future f3 = service.schedule(() ->
        {
            System.out.println("15秒后执行一次");
        }, 15, TimeUnit.SECONDS);
        if (!service.isShutdown())
        {

        }

        System.out.println("==========>testSchedulerExecutorService end");

    }
}

/**
 * 自定义定时任务工具类
 */
class MyScheduledExecutorService
{
    private static ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private  ConcurrentMap<String, Future> futures = new ConcurrentHashMap<>();
    public void test(){
        System.out.println("========>test start ");
        final String jobID = "my_job_1";

        // 保证原子操作
        final AtomicInteger count = new AtomicInteger(0);

        /**
         * 等待其它线程执行完毕后方可执行  当countDown值为0时主线程才会忘下走
         * 指定初始线程的数量 每当一个线程完成自己任务时计数器减一
         * 每添加一个线程 值加一
         * 初始化同步计数器 当计数器的值为0时 方可往下走
         */
        final CountDownLatch countDownLatch = new CountDownLatch(1);

        // 每隔一秒执行一次
        Future future = scheduler.scheduleWithFixedDelay(new Runnable()
        {
            @Override
            public void run()
            {
                System.out.println("原子操作 自增："+count.getAndIncrement());

                if (count.get() > 10)
                {
                    Future future = futures.get(jobID);
                    if (future != null)
                    {
                        future.cancel(true);
                    }

                    // 计数器值减一
                    countDownLatch.countDown();
                }

                System.out.println("==========>run 线程计数值："+countDownLatch.getCount());
            }
        }, 0, 1, TimeUnit.SECONDS);

        futures.put(jobID, future);

        // 主线程必须等待
        try
        {
            countDownLatch.await();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        /**
         * 若任务不关闭将会一直占用线程
         */
        scheduler.shutdown();

        System.out.println("========>test end ");
    }

    public static void main(String[] args)
    {
        MyScheduledExecutorService service = new MyScheduledExecutorService();
        service.test();

    }


}
