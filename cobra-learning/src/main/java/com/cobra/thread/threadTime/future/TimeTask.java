package com.cobra.thread.threadTime.future;

import java.util.concurrent.*;


public class TimeTask implements Callable<String> {

    @Override
    public String call() {
        //执行任务主体,简单示例
        try {
            //Thread.sleep(1000);
            if (true) {
                for (; ; ) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();

        }


        return "hehe";
    }

    public static void main(String[] args) {
        TimeTask timeTask = new TimeTask();
        ExecutorService exec = Executors.newCachedThreadPool();
        Future<String> f = exec.submit(timeTask);
        try {
            f.get(200, TimeUnit.MILLISECONDS);

        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("线程中断异常");
        } catch (ExecutionException e) {
            e.printStackTrace();
            System.out.println("Execution 异常");
        } catch (TimeoutException e) {
            //定义超时后的状态修改

            System.out.println("thread time out");
            e.printStackTrace();

        }
    }

}
