package com.cobra.thread.other;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

/**
 * import com.sun.btrace.BTraceUtils;

 *代替future的get方法 future.get方法会阻塞
 * CompletionService.take.get 谁先执行完，谁先返回
 *
 */
public class ExecutorCompletionServiceTest
{
    public static void main(String[] args) throws Exception
    {
        long start = System.currentTimeMillis();
        ExecutorService executor = Executors.newFixedThreadPool(3);
        //testExecutorCompletionService(executor);
        testFuture(executor);
        long end = System.currentTimeMillis();
        System.out.println("总耗时："+(end-start));

    }

    /**
     * CompletionService 谁先执行完 谁先返回
     *
     * @param executor
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public static void testFuture(ExecutorService executor) throws InterruptedException, ExecutionException
    {
        List<Future<String>> list = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            list.add(executor.submit(new TaskThread("HelloWorld-" + i + "-" + Thread.currentThread().getName())));
        }

        for (int j = 0; j < 5; j++)
        {
            System.out.println("最终结果：" + list.get(j).get());
        }
    }

    /**
     * CompletionService 谁先执行完 谁先返回
     *
     * @param executor
     * @throws InterruptedException
     * @throws ExecutionException
     */
    public static void testExecutorCompletionService(Executor executor) throws InterruptedException, ExecutionException
    {
        CompletionService<String> service = new ExecutorCompletionService<>(executor);
        for (int i = 0; i < 5; i++)
        {

            service.submit(new TaskThread("HelloWorld-" + i + "-" + Thread.currentThread().getName()));
        }

        for (int j = 0; j < 5; j++)
        {
            System.out.println("最终结果：" + service.take().get());
        }
    }
}

class TaskThread implements Callable<String>
{
    private String name;

    public TaskThread(String name)
    {
        this.name = name;
    }

    @Override
    public String call() throws Exception
    {
        long start = System.currentTimeMillis();
        Thread.sleep(new Random().nextInt(10) * 1000);
        long end = System.currentTimeMillis();
        System.out.println(name + "执行时间：" + (end - start));
        return name;
    }
}
