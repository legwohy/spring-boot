package com.cobra.thread.other;

/**
 * join 异步线程并在main线程中执行 main结束 join也结束 几个异步线程的join不能保证顺序
 * 比如 t1.join与t2.join不能保证t1就在t2之前执行但是保证 main线程结束之前t1 t2线程必然结束
 *
 * sleep 休眠会一直拿着锁
 * yeild 让步是释放锁
 */
public class ThreadJoin
{
    public static void main(String[] args)
    {
        System.out.println(Thread.currentThread().getName() + "\t启动");
        Thread t1 = new Thread(new PrintThread("t1"));
        Thread t2 = new Thread(new PrintThread("t2"));
        Thread t3 = new Thread(new PrintThread("t3"));
        Thread t4 = new Thread(new PrintThread("t4"));
        Thread t5 = new Thread(new PrintThread("t5"));

        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
        try
        {
            t2.join();
            t1.join();

        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        System.out.println(Thread.currentThread().getName() + "\t结束");

    }
}

class PrintThread implements Runnable
{
    private String name;

    public PrintThread(String name)
    {
        this.name = name;
    }

    @Override
    public void run()
    {
        System.out.println(name + "\t执行了\t");
       /* for (int i = 0;i<3;i++){
            System.out.println(name + "\t执行了\t"+i+"次");
        }*/

    }
}
