package com.cobra.threadLocal;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @description: threadLocal 一个线程只保留一个变量 并且只保存一个 在最后一个get完之后需要remove
 */
public class ThreadLocalMainClass
{
    public static void main(String[] args)
    {
        ProductUtils.set(new Product("JJJ"));
        ProductUtils.set(new Product("QQQ"));
        ProductUtils.set(new Product("KKK"));
        ProductThread t1 = new ProductThread("1",new Product("A"));
        ProductThread t2 = new ProductThread("2",new Product("B"));
        ProductThread t3 = new ProductThread("3",new Product("C"));
        ProductThread t4 = new ProductThread("4",new Product("D"));
        ProductThread t5 = new ProductThread("5",new Product("E"));
        ProductThread t6 = new ProductThread("6",new Product("F"));
        ProductThread t7 = new ProductThread("7",new Product("G"));

        new Thread(t1).start();
        new Thread(t2).start();
        new Thread(t3).start();
        new Thread(t4).start();
        new Thread(t5).start();
        new Thread(t6).start();
        new Thread(t7).start();



        System.out.println("主线程1:"+ProductUtils.get().getName());
        System.out.println("main线程:"+Thread.currentThread());



    }

    public void concurrent(String name){

        String cureentName = ProductUtils.get().getName();
       System.out.println("次线程二级:"+cureentName+"\t"+name+",当前线程名:"+ Thread.currentThread().getName());



    }
}

class CenterClass{
    public void concurrent(String name){

        new ThreadLocalMainClass().concurrent(name);
        System.out.println("次线程一级:"+ProductUtils.get().getName()+"\t"+name+",当前线程名:"+ Thread.currentThread().getName());
    }
}

class ProductThread implements Runnable{
    private String name;
    private Product product;
    public ProductThread(String name,Product product)
    {
        this.name = name;
        this.product = product;
    }

    @Override
    public void run()
    {
        System.out.println("当前线程名:"+name+"\t,产品名:"+product.getName());

        ProductUtils.set(product);
        try
        {
            Thread.sleep(1000);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        new CenterClass().concurrent(name);

    }




}
