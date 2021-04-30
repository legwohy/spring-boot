package com.cobra.thread.threadTime;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 1.线程池创建多线程
 2.对创建的线程有超时检测
 3.另超时的线程退出执行，释放线程池中的线程
 4.一个线程超时退出了，需要对应的再拉起一个线程来确保任务能继续执行
 完成上面的需求实际上就是线程超时、线程退出、线程创建的结合使用

 * <a link='https://blog.csdn.net/zhangjikuan/article/details/78670288'/>
 */
public class ThreadMonitorTimeout {
    /** 维护每个线程id和时间戳的映射，用来监控哪个线程超时用的*/
    private ConcurrentMap<Integer, Long> aliveThreadRefreshTimeMap = new ConcurrentHashMap<>();

    /** 维护每个线程id和线程返回值future的映射，用来取消超时线程运行使用*/
    final Map<Integer, Future<?>> aliveThreadFutureMap = new HashMap<>();

    /** 记录着最后创建的线程的id位置，用于重新拉起新的线程时仍然使用不重复的id号用的，
     * 可以区分当前线程是由于超时重新拉起的还是启动时创建的，不要纠结那个没有3号线程的问题，只要不重复就好*/
    private int aliveThreadNum = 0;

    /** 任然在cachePool线程池中拉起新线程，之所以用newCachedThreadPool就是看好他的重复使用线程，
     * 退出的线程空闲时可以让新的线程重复使用*/
    private ExecutorService cachedThreadPool;

    private void doOtherThing(int mseconds) throws InterruptedException {

        for (int i = 0; i < 1000; i++) {
            int j = i;
        }
        Thread.sleep(mseconds);

    }

    /** 一直循环的方式不停的执行某些耗时操作*/
    private Runnable workerThread(int i, int sleepTime) {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    long currentTime = System.currentTimeMillis();
                    aliveThreadRefreshTimeMap.put(i, currentTime);
                    System.out.printf("worker thread#%s start...\n", i);
                    while (true) {
                        doOtherThing(sleepTime);
                        currentTime = System.currentTimeMillis();
                        aliveThreadRefreshTimeMap.replace(i, currentTime);
                    }
                } catch (InterruptedException e) {
                    System.out.printf("thread %d into InterruptedException, over\n", i);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    /** 监控线程
     *
     * C程序中看门狗喂狗思想，给每一个线程一个唯一标识，也就是i，每个线程中维护了一个全局变量，
     * 就是当前时间戳，每个循环更新一次最新的时间戳，监控线程中通过读这个时间戳(非阻塞)，
     * 来判断线程是否超时，如果超时调用 Future#cancel() 来取消线程运行，
     * 但是线程池中的多个线程future又该如何跟超时线程对应起来呢，仍然是自己定义的唯一的线程id(i)，
     * 如何退出超时线程呢？这里使用的中断异常，收到中断信号抛出异常从而跳出循环
     * */
    Runnable monitorWorker = new Runnable() {
        @Override
        public void run() {
            try {
                System.out.printf("monitor thread start..., aliveThreadRefreshTimeMap:%s\n", aliveThreadRefreshTimeMap);
                List<Integer> removeIdList = new ArrayList<>();
                for (int threadId : aliveThreadRefreshTimeMap.keySet()) {
                    long currentTime = System.currentTimeMillis();
                    long refreshTimes = currentTime - aliveThreadRefreshTimeMap.get(threadId);
                    System.out.printf("thread %d, refreshTimes is %d\n", threadId, refreshTimes);
                    if (refreshTimes > 10*1000) {
                        System.out.printf("alive thread %d: is %dms to refresh, will restart\n", threadId, currentTime - aliveThreadRefreshTimeMap.get(threadId));
                        aliveThreadFutureMap.get(threadId).cancel(true);

                        aliveThreadNum ++;
                        Future<?> future = cachedThreadPool.submit(workerThread(aliveThreadNum, aliveThreadNum*4000));
                        aliveThreadFutureMap.put(aliveThreadNum,future);
                        removeIdList.add(threadId);
                        System.out.printf("restart success, thread id is:%d\n", aliveThreadNum);
                    }
                }
                for (int id : removeIdList) {
                    aliveThreadFutureMap.remove(id);
                    aliveThreadRefreshTimeMap.remove(id);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };

    /**
     * 创建线程
     */
    public void createTask() {
        // 开启三个线程来执行 workerThread
        cachedThreadPool = Executors.newCachedThreadPool();
        for (int i = 0; i < 3; i++) {
            // aliveCachedThreadPool.execute(adhaAliveDetectTask);
            Future<?> future = cachedThreadPool.submit(workerThread(i, i*6000));
            aliveThreadFutureMap.put(i, future);
            aliveThreadNum++;
        }

        // 创建一个定时调度线程，来执行监控，注意这里使用单个线程来监控Excutor线程池中创建的所有线程
        ScheduledExecutorService monitorExecutor = Executors.newScheduledThreadPool(1);
        monitorExecutor.scheduleAtFixedRate(monitorWorker, 0, 1, TimeUnit.SECONDS);
        while(true);
    }
}
