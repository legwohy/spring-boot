package com.cobra.test;



public class TestThread
{
    public static void main(String[] args)
    {
        new Thread(new Plus()).start();
        new Thread(new Plus()).start();
        new Thread(new Plus()).start();
        new Thread(new Plus()).start();
        new Thread(new Plus()).start();
        new Thread(new Plus()).start();
        new Thread(new Plus()).start();
        new Thread(new Plus()).start();
        new Thread(new Plus()).start();
        new Thread(new Plus()).start();
        new Thread(new Plus()).start();
        new Thread(new Plus()).start();

    }
}
class  Plus implements Runnable
{

    @Override
    public void run()
    {
        int i = 0;
        try
        {
            Thread.sleep(100);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        i = i+1;
        System.out.println(Thread.currentThread().getName()+","+i);

    }
}
