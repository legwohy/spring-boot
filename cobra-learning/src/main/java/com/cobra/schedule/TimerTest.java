package com.cobra.schedule;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @auther: leigang
 * @date: 2019/1/23 10:30
 * @description:
 */
public class TimerTest
{
    /**
     * 三个任务同时启动了 同时main方法不会关闭
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception
    {
        System.out.println("main start=======");
        Timer timer = new Timer();
        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
        Date d1 = sdf.parse("2019-01-30 10:36:15");
        Date d2 = sdf.parse("2019-01-30 10:36:30");
        Date d3 = sdf.parse("2019-01-30 10:36:45");
        timer.schedule(new MyTimeTask(),d1);
        timer.schedule(new MyTimeTask(),d2);
        timer.schedule(new MyTimeTask(),d3);

        System.out.println("main end=======");
    }
}

class MyTimeTask extends TimerTask{

    @Override
    public void run()
    {
        System.out.println("===========定时任务启动了");
    }
}