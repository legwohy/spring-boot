package com.cobra.design.sington;

import java.util.UUID;

/**
 * @auther: admin
 * @date: 2018/12/28 16:02
 * @description:
 */
public class SingletonOne
{
    private int value;
    private static SingletonOne singletonOne;

    private SingletonOne()
    {

    }

    public static  SingletonOne getInstance(){
        if(singletonOne == null){
            singletonOne = new SingletonOne();
        }
        return singletonOne;
    }

    public void print(){
        System.out.println("=========>"+value++);
    }

}
