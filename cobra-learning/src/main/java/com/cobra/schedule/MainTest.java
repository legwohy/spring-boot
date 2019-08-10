package com.cobra.schedule;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;

/**
 * @auther: leigang
 * @date: 2019/1/30 14:05
 * @description:
 */
public class MainTest
{
    private static ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
    private Map<String,Future> futureMap = new HashMap<>();

    public static void main(String[] args) throws Exception
    {
        MainTest mainTest = new MainTest();
        mainTest.test("t1");
        mainTest.test("t6");
        Thread.sleep(1000);
        mainTest.test("t2");
        Thread.sleep(1000);
        mainTest.test("t3");
        mainTest.test("t4");
        mainTest.test("t5");

    }
    public  void test(String param) throws Exception
    {
        final String jobId = "task1";
        deleteAllTask();
        Future future = service.schedule(new Runnable()
                                         {
                                             @Override
                                             public void run()
                                             {
                                                 Future future = futureMap.get(jobId);
                                                 if(null != future)
                                                 {
                                                     future.cancel(true);
                                                 }

                                                 System.out.println("========>"+UUID.randomUUID().toString()+"\t参数="+param);

                                             }
                                         }
                        , 2, TimeUnit.SECONDS);
        futureMap.put(jobId,future);




    }

    private void deleteAllTask()
    {
        futureMap.forEach((jobId,future)->{
            if(!future.isDone())
            {
                future.cancel(true);
            }
        });
        futureMap.clear();

    }
}
