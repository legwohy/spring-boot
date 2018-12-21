package com.cobra.callback;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by user on 2018/10/23.
 */
public class Student
{
    public static void main(String[] args){
        AtomicInteger atomicInteger = new AtomicInteger(0);
        int i = atomicInteger.get();
        for (int j = 0;j<10;j++)
        {
           atomicInteger.compareAndSet(i,i++);

        }

        System.out.println("========"+i);
    }
}
