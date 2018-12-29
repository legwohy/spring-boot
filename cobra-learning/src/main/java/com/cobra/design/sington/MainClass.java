package com.cobra.design.sington;

import java.util.UUID;

/**
 * Double Check Lock 赋值和初始化错位（无先后）
 */
public class MainClass
{

    public static void main(String[] args) throws InterruptedException
    {

        new Thread(() -> System.out.println(DCL.getInstance().getName().toString())).start();
        new Thread(() -> System.out.println(DCL.getInstance().getName().toString())).start();
        new Thread(() -> System.out.println(DCL.getInstance().getName().toString())).start();
        new Thread(() -> System.out.println(DCL.getInstance().getName().toString())).start();
        new Thread(() -> System.out.println(DCL.getInstance().getName().toString())).start();

    }
}

class DCL
{

    private String name;
    private volatile static DCL dcl;

    private DCL(){}

    public  static DCL getInstance()
    {
        if (null == dcl)
        {
            synchronized (DCL.class)
            {
                // 分三步 1 分配内存 2.1 实例化对象 2.2 指向内存地址
                if (null == dcl)
                {
                    // 赋值和初始化是错位的
                    dcl = new DCL();
                    try {Thread.sleep(100);}catch (InterruptedException e) {e.printStackTrace();}
                }
            }

            // 唯一
            dcl.setName( ",\t" + UUID.randomUUID().toString());
            System.out.println( "...end\t" + dcl.getName());

        }

        return dcl;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

}
